# 🚲 E-Charging System for E-Bikes and E-Scooters

A smart charging system for electric bicycles and scooters, combining mobile Android development, IoT hardware control, and Firebase cloud backend.

## 📌 Overview

This project addresses **range anxiety** for e-bike and e-scooter users by building a shared smart charging platform. The system is designed for **both rental companies** and **personal users**, featuring:

- 📱 **Mobile app** for users to locate stations, reserve charging slots, and manage accounts  
- 🔐 **User authentication and payments** via Firebase, JWT, OAuth 2.0  
- 📡 **RFID-based Arduino module** for smart slot control and company billing  
- ☁️ **Cloud-based database** for real-time updates and status management  

## 📱 Android App Features

- Built in **Kotlin** using Android Studio
- User-friendly interface for:
  - 🔍 Station & slot listing
  - 🏷️ QR code scanning for slot selection
  - ⏱️ Charging duration selection
  - 💳 Payment method choice (Wallet / Google Pay / Credit Card)
  - 🔋 Real-time charging status
- Connected to **Firebase Realtime Database** for authentication, bookings, and state sync

## ⚙️ Arduino + RFID Integration

- Built with **Arduino UNO R4**, **RC522 RFID**, and **custom electric circuit system**
- NFC cards used by company users for automatic authentication
- Simulates real charging behavior with variable current models
- Communicates with cloud via serial protocol

## 🔗 System Architecture

| Module           | Tools                        | Work                                                             |
|------------------|------------------------------|------------------------------------------------------------------|
| Arduino Module   | Arduino IDE, RC522, UNO R4   | NFC reading, charging simulation, LED feedback                   |
| Firebase         | Firestore                    | User auth, slot states, payment info, session logging            |
| Android App      | Android Studio, Kotlin       | UI/UX, QR code integration, slot management, payments            |

## 🧠 Key Technologies

- **Languages**: Kotlin, Arduino C++, Firebase Security Rules
- **Platforms**: Android Studio, Arduino IDE, Firebase (Firestore DB, Auth)
- **IoT Integration**: NFC module (RC522), simulated charging flow logic
- **UI/UX**: Custom Android screens + QR Code interactions

## 📸 Screenshots

### Android App Layout
![Home](./docs/Android%20App%20Layout.png)

## 🚀 Future Work

- Add 2FA and BLE integration  
- Expand payment options and billing logic (e.g., Stripe, PayPal integration)
- Refactor the system into a full-stack architecture:
  - Build a dedicated backend using FastAPI or Node.js (Express) to manage user sessions, reservations, and hardware communication
	- Migrate from Firebase to a PostgreSQL database for scalable relational data management
	- Separate frontend (mobile app) and backend responsibilities, following REST API best practices
	- Add a web-based admin dashboard for station owners to monitor charging usage and user activity

## 📁 Repository Structure

```
eCharging/
├── app/
│   └── src/
│       └── main/
│           ├── AndroidManifest.xml
│           ├── java/com/example/echarging/
│           │   ├── .kt                  # Kotlin source files (Activities, ViewModels, Adapters)
│           └── res/
│               ├── layout/              # XML layouts (UI structure)
│               ├── drawable/            # Icons and custom graphics
│               ├── mipmap-/            # App launcher icons for different resolutions
│               ├── values/              # Strings, themes, styles
│               ├── values-night/        # Dark mode theming
│               ├── font/                # Custom fonts
│               └── xml/                 # Navigation, preferences, etc.
├── build.gradle.kts                    # Gradle config
├── docs/                               # UI screenshots & flowcharts
├── generate_qr_codes.py               # QR code generator (Python)
└── README.md                           
```
