# Azure Deployment Guide — GAREEBI Server

## Prerequisites
- Azure CLI installed and logged in (`az login`)
- Docker Desktop running
- Your Azure subscription active

---

## Step 1: Create Azure Container Registry (ACR)

```bash
# Create a resource group
az group create --name gareebi-rg --location eastus

# Create ACR (Basic tier is fine for hackathon)
az acr create \
  --resource-group gareebi-rg \
  --name gareebiregistry \
  --sku Basic \
  --admin-enabled true

# Get ACR credentials
az acr credential show --name gareebiregistry
```

---

## Step 2: Build & Push Docker Image to ACR

```bash
# Login to ACR
az acr login --name gareebiregistry

# Build the image
docker build -t gareebi-server:latest .

# Tag for ACR
docker tag gareebi-server:latest gareebiregistry.azurecr.io/gareebi-server:latest

# Push to ACR
docker push gareebiregistry.azurecr.io/gareebi-server:latest
```

---

## Step 3: Set up PostgreSQL (TimescaleDB)

**Option A: Azure Database for PostgreSQL Flexible Server**
> Note: TimescaleDB extension is supported on Azure PostgreSQL Flexible Server.

```bash
az postgres flexible-server create \
  --resource-group gareebi-rg \
  --name gareebi-pg \
  --admin-user gareebi_user \
  --admin-password "YourStrongPassword!" \
  --sku-name Standard_B1ms \
  --tier Burstable \
  --version 15

# Enable TimescaleDB extension
az postgres flexible-server parameter set \
  --resource-group gareebi-rg \
  --server-name gareebi-pg \
  --name azure.extensions \
  --value TIMESCALEDB
```

**Option B: Keep TimescaleDB in Docker Compose** (simpler for hackathon, less production-ready)

---

## Step 4: Create Azure Cache for Redis

```bash
az redis create \
  --resource-group gareebi-rg \
  --name gareebi-redis \
  --sku Basic \
  --vm-size c0 \
  --location eastus
```

---

## Step 5: Deploy to Azure App Service (Docker Compose)

```bash
# Create App Service Plan (Linux)
az appservice plan create \
  --name gareebi-plan \
  --resource-group gareebi-rg \
  --is-linux \
  --sku B2

# Create Web App with Docker Compose support
az webapp create \
  --resource-group gareebi-rg \
  --plan gareebi-plan \
  --name gareebi-server-app \
  --multicontainer-config-type compose \
  --multicontainer-config-file docker-compose.yml

# Set environment variables
az webapp config appsettings set \
  --resource-group gareebi-rg \
  --name gareebi-server-app \
  --settings \
    DB_HOST="gareebi-pg.postgres.database.azure.com" \
    DB_NAME="gareebi_db" \
    DB_USER="gareebi_user" \
    DB_PASS="YourStrongPassword!" \
    REDIS_HOST="gareebi-redis.redis.cache.windows.net" \
    REDIS_PORT="6380" \
    JWT_SECRET="your-256-bit-secret-here"
```

---

## Step 6: Map Custom Domain (jaych.me)

1. **Get the App Service URL:**
   ```bash
   az webapp show \
     --resource-group gareebi-rg \
     --name gareebi-server-app \
     --query defaultHostName
   # e.g. gareebi-server-app.azurewebsites.net
   ```

2. **Add a CNAME record in your DNS provider (jaych.me):**
   | Type | Host | Value |
   |------|------|-------|
   | CNAME | `api` | `gareebi-server-app.azurewebsites.net` |

   This maps `api.jaych.me` → Azure App Service.

3. **Bind the domain in Azure:**
   ```bash
   az webapp config hostname add \
     --resource-group gareebi-rg \
     --webapp-name gareebi-server-app \
     --hostname api.jaych.me
   ```

4. **Enable HTTPS (free managed certificate):**
   ```bash
   az webapp config ssl bind \
     --certificate-type SNISslCertificate \
     --name gareebi-server-app \
     --resource-group gareebi-rg \
     --hostname api.jaych.me
   ```

---

## Final URLs

| Endpoint | URL |
|----------|-----|
| Register | `POST https://api.jaych.me/api/v1/auth/register` |
| Login | `POST https://api.jaych.me/api/v1/auth/login` |
| Telemetry | `POST https://api.jaych.me/api/v1/telemetry` |
| Trade | `POST https://api.jaych.me/api/v1/trade` |
| Marketplace | `GET https://api.jaych.me/api/v1/marketplace` |
| Profile | `GET https://api.jaych.me/api/v1/users/me` |
| Live Stream | `WSS wss://api.jaych.me/ws/energy-stream` |
