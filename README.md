# NeoJackaroo

A strategic single-player adaptation of Jackaroo with custom rules, unique cards, and computer-controlled opponents.

## 📝 Overview

NeoJackaroo was developed as a part of the project based CSEN401 course at the German University in Cairo. But for us, it wasn’t just about submitting an assignment, it was about simulating a real-world software development experience.

Our team, "الجيم في الجيب," approached this project like a startup: we used Git for collaboration, documented every task in GitHub Projects, and held ourselves to professional standards for coding, documentation, design, and testing.

> 🎥 [Watch the Gameplay on YouTube](https://www.youtube.com/watch?v=7XBjpjv66m4&t=13s)

> 🎮 [Download & Play: You can download the fully packaged executable version of NeoJackaroo here](https://ziadelnaggar.itch.io/neojackaroo).

## 🎮 Features

* 💻 **Computer-controlled opponents**: Face off against intelligent CPU players.
* 🃏 **Custom card mechanics**: Includes both standard and wild cards with special actions.
* 🧠 **OOP-compliant design**: Clean architecture using enums, interfaces, and design patterns.
* 🎨 **Custom-designed assets**: Handcrafted visuals created using Adobe Illustrator and Photoshop.
* 🧪 **Robust exception handling**: Covers all edge cases and invalid actions gracefully.
* 🎵 **Sound effects and music**: Engaging audio environment to enhance gameplay.
* 🎬 **Smooth animations and transitions**: Responsive GUI built using JavaFX and FXML.

## 📂 Project Structure

```
NeoJackaroo/
├── src/                         # Java source code
│   ├── controller/             # App entry point and global managers
│   ├── engine/                 # Game flow logic
│   │   └── board/              # Board, cells, movement rules
│   ├── exception/              # Custom exceptions
│   ├── model/                  # Cards, players, marbles
│   │   ├── card/               # Card logic
│   │   │   ├── standard/       # Standard cards (Ace, King, etc.)
│   │   │   └── wild/           # Custom wild cards (Burner, Saver)
│   │   └── player/             # Player and CPU logic
│   └── view/                   # JavaFX controllers and FXML scenes
│       └── assets/             # Assets: images, sounds, gifs, CSS
├── buildsystem/                # Gradle setup
│   ├── build.gradle            
│   ├── gradlew / gradlew.bat   # Gradle wrappers
│   └── build-installer.ps1     # Script to build a Windows installer
└── README.md                   # This file
```
## 🎮 Screenshots from the Game  

### 🎬 Intro  
<img width="2559" height="1420" alt="Intro Screen" src="https://github.com/user-attachments/assets/ab9c756b-9271-46c5-b7e2-9d7e650f5613" />

---
### 🕹️ Gameplay  
<img width="2559" height="1411" alt="Gameplay" src="https://github.com/user-attachments/assets/16339e22-304e-4dbd-b4b6-258bc3595bc4" />

---
### ⚠️ Handling Errors  
<img width="1814" height="1069" alt="Error Handling" src="https://github.com/user-attachments/assets/7bce5cef-ece4-4e9d-8228-c74e573676d9"/>

---

### 🏆 Winning Situation  
<img width="1821" height="1005" alt="Winning Screen" src="https://github.com/user-attachments/assets/c8e9ac41-f218-4dfe-ab6e-56bf5f51b2fb" />

---

✨ ...and much more when you try the game!


## 🏃‍➡️ Getting Started

### 1. Install Dependencies

Before running or building NeoJackaroo, make sure to install:

* ✅ [**Liberica JDK 21 Full**](https://bell-sw.com/pages/downloads/#/java-21-lts)

  * Includes both JDK and JavaFX (essential for running the game and building the installer).

* ✅ [**WiX Toolset v3.11.1**](https://github.com/wixtoolset/wix3/releases)

  * When you find correct version under "Assets," download `wix311-binaries.zip`
  * Unzip and place in a folder (e.g., `C:\wix`)
  * Add full path C:\wix\bin to your system environment variables:

      1. Press Win + S and type Environment Variables
      
      2. Click on Edit the system environment variables
      
      3. In System Properties, click Environment Variables
      
      4. Under System Variables, select Path and click Edit
      
      5. Click New and add: C:\wix\bin
      
      6. Click OK on all dialogs to save
         
 ### Configure Eclipse to Use It
 
  * Open Eclipse
  
  * Go to Window → Preferences → Java → Installed JREs
  
  * Click Add → Standard VM
  
  * In the JRE home, browse to your Liberica JDK path (e.g., C:\Programs\LibericaJDK-21)
  
  * Click Finish, and check the box next to it to set it as default

### 2. Clone the Repository

```bash
git clone https://github.com/ziadelnaggar01/NeoJackaroo.git
```

### 3. Import into Eclipse

* Open Eclipse.
* Go to **File → Import... → Existing Projects into Workspace**.
* Click **Select root directory**, then browse and select the cloned repository folder.
* Ensure the project appears in the list, then click **Finish**.

### 4. Verify Build Path

You can run the game directly from Eclipse or from terminal using Gradle:
After importing, verify that Eclipse recognizes the project correctly:

* Right-click the project → **Build Path → Configure Build Path**.
*  Check the following:
   - The **Source** tab includes the `src` folder.
   - The **Libraries** tab contains the **JRE System Library**.

### 5. Build the Windows Installer
   Open terminal in project directory and run
```powershell
powershell -ExecutionPolicy Bypass -File .\build-installer.ps1
```

This generates a `.msi` file with bundled JRE and JavaFX—ready to share!

## ⚠️ Recommended Setup

* 💻 Resolution: **1920×1080**
* 🔍 Display scaling: **100%**

## 🚀 Enjoy the Game

NeoJackaroo is a love letter to teamwork, clean code, and creative gameplay. Whether you're playing or exploring the codebase, we hope you enjoy the experience as much as we did building it.

**Happy coding and game on! 🎮✨**
