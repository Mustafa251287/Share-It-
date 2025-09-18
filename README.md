# ShareIt - Android File Sharing App

A lightweight, local Android application for fast and secure peer-to-peer file sharing between devices without using the internet or mobile data.

> **Note:** This is a functional prototype. The APK can be downloaded and installed below to test all features.

## 📸 Screenshots

<div align="center">
  <img src="screenshots/screenshot_1.jpg" alt="Main Screen" width="30%"/>
  <img src="screenshots/screenshot_2.jpg" alt="Sending a file" width="30%"/>
  <img src="screenshots/screenshot_3.jpg" alt="Receiving a file" width="30%"/>
</div>

## 🎥 Video Demo

See the app in action! A full walkthrough of the sending and receiving process.

[![Watch the video demo](https://img.youtube.com/vi/[YOUR_VIDEO_ID_HERE]/0.jpg)](https://www.youtube.com/watch?v=[YOUR_VIDEO_ID_HERE])

## ✨ Key Features

-   **Offline, Local Transfer:** Creates a direct Wi-Fi hotspot or uses Wi-Fi Direct to establish a connection between two devices. No internet required.
-   **High-Speed Transfer:** Optimized for sharing large files quickly over a local network.
-   **Easy-to-Use Interface:** Simple UI to select files and see transfer progress.
-   **Security:** Files are transferred directly between devices without being stored on a third-party server.

## 🛠️ Technical Implementation

This project demonstrates advanced Android concepts:

-   **Android Networking:** Using Wi-Fi Direct and/or Socket Programming for peer-to-peer communication.
-   **Progress Updates:** Using `Handler` or `LiveData` to update the UI with transfer progress in real-time.

## 📦 Installation & Testing

### Option 1: Download the APK
1.  **Download the signed APK file:** [**Download ShareIt.apk from Google Drive**]([YOUR_GOOGLE_DRIVE_LINK_HERE])
2.  On your Android phone, enable "Install from unknown sources" when prompted.
3.  Install the APK and test the app! You need two Android devices to test the full functionality.

### Option 2: Build from Source
1.  Clone this repository:
    ```bash
    git clone https://github.com/[YOUR_USERNAME]/ShareIt.git
    ```
2.  Open the project in **Android Studio**.
3.  Build and run the project on a connected Android device or emulator.

## 🔧 Code Structure
```
ShareIt/
├── .gitignore
├── LICENSE
├── README.md
└── src/
    └── main/
        └── java/
            └── com/
                └── sharing/
                    └── files/
                        ├── MainActivity.java      # Entry point of the application
                        ├── ReceiveActivity.java   # Handles the file receiving logic & UI
                        ├── SendActivity.java      # Handles the file sending logic & UI
                        ├── SendingActivity.java   # (Likely) Manages the ongoing sending process
                        └── Capture.java           # (Likely) Utility for taking photos/videos to share
```

## 🤝 Contributing

This is a personal project for my portfolio, but suggestions and ideas are always welcome. Feel free to fork the repo and submit a pull request.

---

<div align="center">

**Built with ❤️ using Java and Android SDK**

</div>
