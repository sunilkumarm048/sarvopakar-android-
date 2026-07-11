# Sarvopakar Android App (Capacitor)

Native Android shell that loads the live site (https://www.sarvopakar.com)
inside a WebView, with:
- Real GPS permissions (fine + coarse location) for live provider location
- A high-importance "order_alerts" notification channel with the custom
  order ring (`shop_new_order.mp3`) baked into the APK
- Notification permission prompt on first launch (Android 13+)

## Build the APK (no Android Studio needed)
1. Create a new GitHub repository (e.g. `sarvopakar-android`).
2. Upload ALL files in this folder to the repo (keep the folder structure,
   including the hidden `.github` folder).
3. Go to the repo's **Actions** tab → "Build Android APK" → **Run workflow**
   (it also runs automatically on every push).
4. When the run finishes (~5 min), open it and download the
   **sarvopakar-app-debug** artifact — the APK is inside.
5. Copy `app-debug.apk` to an Android phone and install it
   (allow "install from unknown sources" when asked).

## Phase 2 (next step): call-style background ring
Requires a free Firebase project for FCM push:
1. https://console.firebase.google.com → Add project → add an Android app
   with package name `com.sarvopakar.app`.
2. Download `google-services.json`.
3. Bring that file back to the chat — the FCM plugin, backend sender, and
   full-screen ringing alert get wired from there. The custom-ring channel
   is already in place in MainActivity.
