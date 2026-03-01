# ⚡ GAREEBI  - (Green And Renewable Energy Exchange By IoT)
### Peer-to-Peer Microgrid Energy Trading Platform  

<div align="center">

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)
![Backend](https://img.shields.io/badge/Backend-Flask-000000?logo=flask)
![Cloud](https://img.shields.io/badge/Cloud-Dockerized-2496ED?logo=docker&logoColor=white)
![IoT](https://img.shields.io/badge/Hardware-ESP32-E7352C?logo=espressif&logoColor=white)

**Decentralized. Sustainable. Physical.**

demonstration video and presentation link - https://drive.google.com/drive/folders/1iNJECqqoQDzwcl8ufGNy_SwZ3OrLAkdM?usp=sharing

</div>

---

## 🌍 Problem

Rural and semi-urban communities generate renewable energy (solar/biogas),  
but lack a transparent system to trade excess power locally.

Traditional grids:
- ❌ Centralized  
- ❌ Opaque billing  
- ❌ No carbon awareness  
- ❌ No micro-level energy routing  

---

## 💡 Solution

**GAREEBI** enables real-time peer-to-peer electricity trading  
with actual physical power routing via IoT hardware.

A user buys energy → Cloud verifies → Relay switches → Power flows.

Not simulated.  
Not theoretical.  
**Physically executed.**

---

# 🏗 System Architecture
📱 ANDROID APP (Kotlin + Compose)
          │
POST /api/purchase
          ▼
☁️ CLOUD BROKER (Dockerized Flask on Render)
          │
  GET /api/status
          ▼
🔌 ESP32 MICROGRID NODE
          │
          ▼
💡 RELAY SWITCHES → ELECTRICITY FLOWS



### Flow

1. User purchases energy (kWh).
2. Backend updates trade state.
3. ESP32 polls cloud status.
4. Relay activates.
5. Power is physically routed.

---

# ⚡ Core Features

### 🔁 Real-Time Energy Trading
Instant P2P electricity exchange via mobile interface.

### 🌱 Carbon Footprint Tracking
Dynamic CO₂ calculation: Carbon = kWh × 0.71 kg CO₂


Users see real environmental impact per trade.

### 🔌 Physical Relay Control
5V hardware relay triggered by cloud transaction.

### 📱 Modern Android Architecture
- Kotlin
- Jetpack Compose
- MVVM
- StateFlow
- Material 3

### ☁️ Cloud-Native Backend
- Python 3.11
- Flask API
- Gunicorn
- Docker container
- CI/CD deployed on Render

---

# 🛠 Tech Stack

## Hardware (Microgrid Layer)

- ESP32 (WiFi enabled)
- 5V Active-Low Relay Module
- Basic Load Node (LED / ESP8266)
- Arduino Framework (C++)

---

## Backend (Control Layer)

- Flask REST API
- Gunicorn
- Docker
- Render Cloud Hosting

Endpoints:
POST /api/purchase
Body: { "units": 5 }

GET /api/status
Response: { "relay_status": "ON" | "OFF" }


---

## Mobile (User Layer)

- Android Studio
- Kotlin
- Jetpack Compose
- OkHttp3 Networking
- Reactive StateFlow Architecture

---

# 🚀 Quick Start

### 1️⃣ Deploy Backend
Auto-deploy via GitHub → Render webhook, link - https://esp-hosting-vtlb.onrender.com

---

### 2️⃣ Flash ESP32

- Open `SellerNode.ino`
- Update:
  - WiFi SSID
  - WiFi Password
  - `https://esp-hosting-vtlb.onrender.com` (Render URL)
- Connect Relay IN → Pin D2
- Upload to ESP32

---

### 3️⃣ Run Android App

- Open project in Android Studio
- Set cloud base URL
- Install on physical device
- Tap **Buy Electricity**
- Hear the relay click ⚡

---

### Built for NMIMS Hackathon ⚡  
**Energy should be tradable. Carbon should be visible. Power should be local.**
