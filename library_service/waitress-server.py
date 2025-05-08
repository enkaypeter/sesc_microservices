from waitress import serve
from helpers import register_with_consul
import application

register_with_consul()
serve(application.app, host='0.0.0.0', port=80)
