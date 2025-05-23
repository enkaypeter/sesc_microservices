import http          from 'k6/http';
import { check, sleep }   from 'k6';
import { Trend, Rate, Counter, Gauge } from 'k6/metrics';
import { randomItem }     from 'https://jslib.k6.io/k6-utils/1.4.0/index.js';

/*────────────────────────────────────────────
  1.  EXTRA METRICS
  ────────────────────────────────────────────*/
export const rps        = new Rate   ('successful_rps');        // #1
export const httpErrors = new Rate   ('http_error_rate');       // #2
export const apdex      = new Gauge  ('apdex_score');           // #3
export const p99Trend   = new Trend  ('http_req_duration_p99'); // #4
export const bytesSent  = new Counter('bytes_sent');            // #5
export const enrolments = new Counter('enrolments_created');    // #7

export const studentRT = new Trend('student_resp_time');

/*────────────────────────────────────────────
  2.  TEST OPTIONS
  ────────────────────────────────────────────*/
export const options = {
  vus:  10,
  duration: '3m',
  thresholds: {
    // 95th latency + long-tail
    'http_req_duration{service:student-service}': ['p(95)<400','p(99)<600'],
    // overall reliability
    'http_error_rate':  ['rate<0.02'],
    // business success
    'enrolments_created': ['count>100'],
    // apdex (≥0.85 considered “Good”)
    'apdex_score':       ['value>0.85'],
  },
  tags: { test: 'sesc-load' }
};

/*────────────────────────────────────────────
  3.  UTILS & CONSTANTS
  ────────────────────────────────────────────*/
const STUDENT_BASE = 'http://localhost:8090';
const PAUSE = 1;

function vuEmail() {
  return `user_${__VU}_${Date.now()}@test.local`;
}

/*────────────────────────────────────────────
  4.  MAIN USER FLOW
  ────────────────────────────────────────────*/
export default function () {
  // 1. Register
  const regRes = http.post(`${STUDENT_BASE}/api/auth/register`, JSON.stringify({
    email: vuEmail(),
    password: 'Passw0rd!'
  }), { headers:{'Content-Type':'application/json'}, tags:{service:'student-service'} });

  check(regRes, { 'registered': (r)=>r.status===200 });
  trackResult(regRes);

  // 2. Login
  const loginRes = http.post(`${STUDENT_BASE}/api/auth/login`, JSON.stringify({
    email: regRes.json('user.email'),
    password: 'Passw0rd!'
  }), { headers:{'Content-Type':'application/json'}, tags:{service:'student-service'} });

  check(loginRes, { 'login ok': (r)=>r.status===200 });
  trackResult(loginRes);
  const token = loginRes.json('token.accessToken');
  const authHdr = { headers: { Authorization: `Bearer ${token}`, 'Content-Type':'application/json'} };

  // 3. View courses
  const coursesRes = http.get(`${STUDENT_BASE}/api/courses`, { tags:{service:'student-service'} });
  check(coursesRes, {'courses ok': (r)=>r.status===200 });
  trackResult(coursesRes);
  const courseId = randomItem(coursesRes.json('data')).id;

  // 4. Enrol
  const enrolRes = http.post(`${STUDENT_BASE}/api/enrolments`, JSON.stringify({courseId}), { ...authHdr, tags:{service:'student-service'} });
  check(enrolRes, { 'enrolled': (r)=>r.status===201 });
  trackResult(enrolRes);
  enrolments.add(1);                       // business counter

  // 5. Graduation check (quick)
  const gradRes = http.get(`${STUDENT_BASE}/api/graduation/eligibility`, { ...authHdr, tags:{service:'student-service'} });
  trackResult(gradRes);


  sleep(PAUSE);
}

/*────────────────────────────────────────────
  5.  Helper to feed every response into metrics
  ────────────────────────────────────────────*/
function trackResult(res) {
  const ok = res.status < 400;
  rps.add(ok);                 // Rate counts true as “1”, false as “0”
  httpErrors.add(!ok);
  bytesSent.add(res.request.body?.length || 0);

  // Apdex : satisfied (t ≤300 ms) =1, tolerating (≤1200 ms)=0.5, else 0
  const t = res.timings.duration;
  const score = (t <= 300) ? 1 : (t <= 1200 ? 0.5 : 0);
  apdex.add(score);

  // feed custom P99 trend (k6 will still compute p99 for built-in metric,
  // but this keeps it explicit and tag-aware)
  p99Trend.add(t, res.tags);
  studentRT.add(t);
}
