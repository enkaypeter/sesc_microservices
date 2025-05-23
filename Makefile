INFRA_COMPOSE=./docker-compose/docker-compose.infra.yml
APP_COMPOSE=./docker-compose/docker-compose.app.yml
OBS_COMPOSE=./docker-compose/docker-compose.obs.yml

.PHONY: up down build logs infra obs app restart help

help:
	@echo "Usage:"
	@echo "  make infra      Start only infrastructure services"
	@echo "  make app        Start only application services"
	@echo "  make up         Start all services"
	@echo "  make down-v   	 Stop all services and remove volumes"
	@echo "  make down       Stop all services"
	@echo "  make build      Build all services"
	@echo "  make restart    Rebuild and restart everything"
	@echo "  make logs       Follow logs from all services"

infra:
	docker compose -f $(INFRA_COMPOSE) up -d

obs:
	docker compose -f $(OBS_COMPOSE) up -d

app:
	docker compose -f $(APP_COMPOSE) up -d
app-build:
	docker compose -f $(APP_COMPOSE) build

up:
	docker compose -f $(INFRA_COMPOSE) -f $(APP_COMPOSE) up -d
down:
	docker compose -f $(INFRA_COMPOSE) -f $(APP_COMPOSE) -f $(OBS_COMPOSE)  down
down-app:
	docker compose -f $(APP_COMPOSE) down --volumes --remove-orphans
down-v:
	docker compose -f $(INFRA_COMPOSE) -f $(APP_COMPOSE) down --volumes --remove-orphans


build:
	docker compose -f $(INFRA_COMPOSE) -f $(APP_COMPOSE) -f $(OBS_COMPOSE) build

restart:
	make down
	make build
	make up

logs:
	docker compose -f $(INFRA_COMPOSE) -f $(APP_COMPOSE) logs -f
