FROM python:3.9-slim-buster

# create & activate virtualenv
ENV VIRTUAL_ENV=/opt/venv
RUN python3 -m venv $VIRTUAL_ENV
ENV PATH="$VIRTUAL_ENV/bin:$PATH"
WORKDIR /usr/src/flask_uuid

# install system deps
RUN apt-get update \
 && apt-get install -y --no-install-recommends \
      build-essential \
      python3-dev \
      curl \
      default-libmysqlclient-dev \
      pkg-config \
 && rm -rf /var/lib/apt/lists/*

# copy and install Python deps
COPY requirements.txt .
RUN pip install --upgrade pip \
 && pip install grpcio --only-binary=:all: \
 && pip install mysqlclient \
 && pip install -r requirements.txt \
 && pip install \
      opentelemetry-distro \
      opentelemetry-exporter-otlp \
      opentelemetry-instrumentation-flask

# copy app code
COPY . .

# OpenTelemetry settings
ENV OTEL_SERVICE_NAME=library \
    OTEL_TRACES_EXPORTER=otlp \
    OTEL_METRICS_EXPORTER=otlp \
    OTEL_EXPORTER_OTLP_ENDPOINT=http://otel-collector:4318 \
    OTEL_EXPORTER_OTLP_PROTOCOL=http/protobuf

EXPOSE 80
ENTRYPOINT ["opentelemetry-instrument", "python", "waitress-server.py"]
