# MedHealth: AI-Powered Health Assistant

![MedHealth App Banner](https://placehold.co/1200x300/0061A4/FFFFFF/png?text=MedHealth&font=raleway)

**MedHealth** is a modern, feature-rich Android application designed to serve as a personal health assistant. Built entirely with Kotlin and following the latest Android development best practices, it leverages on-device AI for preliminary diagnostics and integrates with powerful cloud services to provide a comprehensive and professional user experience.

This project is intended as a portfolio piece and a demonstration of advanced Android development skills, including AI/ML integration, modern UI/UX with Jetpack Compose, and a robust, scalable architecture using Hilt, Coroutines, and a full suite of Firebase services.

**Disclaimer:** This application is for educational and demonstration purposes only. The diagnostic, acuity, and calculation features are not a substitute for professional medical advice. Always consult a qualified healthcare provider for any medical concerns.

---

## ✨ Features

MedHealth is packed with advanced, fully functional features:

*   **👨‍⚕️ AI-Powered Diagnostics:** Capture an image of an eye, and an on-device TensorFlow Lite model provides a preliminary analysis for conditions like Cataracts and Diabetic Retinopathy.
*   **🤖 AI Health Agent:** A conversational chatbot powered by the **Google Gemini API**. It can answer general health questions and provide detailed explanations on complex medical AI research topics.
*   **📄 Secure Document Uploader:** Users can upload medical reports (PDFs, images) from their device, which are securely stored in Firebase Storage and linked to their profile.
*   **📝 Diagnosis History & Notes:** All diagnostic results are saved locally (Room) and synced to the cloud (Firestore). Users can add and edit personal notes for each entry.
*   **👁️ Snellen Chart Generator:** Dynamically generates a standard Snellen eye chart for basic visual acuity self-assessment.
*   **🔢 IOL Calculator:** A specialized tool implementing the SRK/T formula to calculate intraocular lens power for cataract surgery (for educational purposes).
*   **☁️ Full Firebase Integration:**
    *   **Authentication:** Secure email/password sign-in and registration.
    *   **Firestore:** Cloud-based, real-time database for syncing user data and history.
    *   **Storage:** Securely stores all user-uploaded images and documents.
    *   **Crashlytics:** Essential for real-time crash reporting and stability monitoring.
    *   **Cloud Messaging (FCM):** Set up to receive push notifications.
    *   **Remote Config:** The AI Health Agent's behavior can be updated remotely without an app update.
    *   **Analytics:** Logs custom events for key user actions.
*   **🎨 Professional UI/UX:**
    *   Built entirely with **Jetpack Compose** following **Material 3** design guidelines.
    *   Supports **Dynamic Color Theming** (UI adapts to user's wallpaper on Android 12+).
    *   Clean, dashboard-style home screen for easy navigation.
    *   Professional touches like loading indicators and empty state screens.

---

## 🛠️ Tech Stack & Architecture

This project showcases a modern, scalable, and maintainable Android architecture.

*   **Language:** 100% **Kotlin**
*   **UI:** **Jetpack Compose** with Material 3
*   **Architecture:** **MVVM** (Model-View-ViewModel)
*   **Asynchronous Programming:** **Kotlin Coroutines & Flows** for all async operations.
*   **Dependency Injection:** **Hilt** for managing dependencies across the app.
*   **Networking:** **Retrofit** for API communication (PubMed, etc.).
*   **Local Database:** **Room** for offline data persistence.
*   **AI/ML:** **TensorFlow Lite** for on-device inference.
*   **Cloud Backend:** **Firebase** (Auth, Firestore, Storage, Crashlytics, FCM, Remote Config, Analytics)
*   **Build System:** **Gradle with Kotlin DSL** and **Version Catalogs**.
*   **Annotation Processing:** **KSP** (Kotlin Symbol Processing) for improved build performance.

---

## 🚀 Setup and Configuration

To build and run this project yourself, you will need to configure your own Firebase project and obtain a Gemini API key.

### Step 1: Clone the Repository

```bash
git clone <your-repository-url>
cd medhealth
Use code with caution.
Markdown
Step 2: Firebase Setup
Go to the Firebase Console and create a new project.
Add an Android app to your Firebase project with the package name com.jmr.medhealth.
Follow the setup steps to download the google-services.json file.
Place the downloaded google-services.json file in the MedHealth/app/ directory.
In the Firebase Console, enable the following services:
Authentication: Enable the "Email/Password" sign-in provider.
Firestore Database: Create a new database.
Storage: Create a new storage bucket.
Remote Config: Navigate to Remote Config and add a new parameter with the key ai_agent_prompt. You can paste the default prompt from RemoteConfigRepository.kt as its value.
Step 3: Get API Keys
Gemini API Key:
Go to Google AI Studio and create an API key.
Open the local.properties file at the root of the project (create it if it doesn't exist).
Add the following line, replacing YOUR_KEY_HERE with your actual key:
Generated properties
GOOGLE_API_KEY="YOUR_KEY_HERE"
Use code with caution.
Properties
TensorFlow Lite Model:
This project is configured to use a TFLite model for eye disease detection. You must provide your own trained model.
Place your model file in app/src/main/assets/ and name it disease_model.tflite.
Create a corresponding labels file named disease_labels.txt in the same directory.
Step 4: Build and Run
Open the project in the latest stable version of Android Studio, wait for Gradle to sync, and click the "Run" button. The project should build and install on your emulator or physical device.
📸 Screenshots
(This is where you would add screenshots of your app's main screens)
Home Dashboard	AI Health Agent
![alt text](https://placehold.co/400x800.png)
![alt text](https://placehold.co/400x800.png)
Diagnosis Details	Snellen Chart
![alt text](https://placehold.co/400x800.png)
![alt text](https://placehold.co/400x800.png)
🤝 Contributing
This is a personal portfolio project, but suggestions and feedback are always welcome. Please feel free to open an issue to discuss any ideas or improvements.
