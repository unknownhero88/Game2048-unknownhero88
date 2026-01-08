# 🎮 2048 Game – Android (Jetpack Compose)

A modern **2048 puzzle game** built using **Kotlin** and **Jetpack Compose**.  
This project focuses on **clean UI**, **smooth animations**, and **beginner-friendly architecture**.

---

## ✨ Features

- 🧩 Classic **2048 gameplay**
- 🎨 Modern & clean UI (Jetpack Compose)
- 👉 Swipe gestures (Up / Down / Left / Right)
- 🎞️ Smooth **slide animations**
- 💥 **Merge bounce animation**
- 🌱 **New tile pop animation**
- 🏆 **High score saved** (persistent using SharedPreferences)
- ☠️ **Game Over dialog** with restart option
- 📱 Responsive design (works on different screen sizes)

---

## 📸 Screenshots

| Gameplay | Game Over |
|--------|-----------|
| ![gameplay](app/src/main/res/drawable-ss/gplay.jpg) | ![gameover](app/src/main/res/drawable-ss/gover.jpg) |

---

## APK Download

| [Game.apk](https://drive.google.com/drive/folders/130M2bu_y4l6ol38n1ciDKU3PLoYMRWl1?usp=sharing) |
---

## 🛠 Tech Stack

- **Language:** Kotlin  
- **UI:** Jetpack Compose  
- **Architecture:** MVVM  
- **State Management:** ViewModel + Compose State  
- **Animations:** Animatable, animateDpAsState  
- **Persistence:** SharedPreferences  

---

## 🧠 Game Logic Overview

- Tiles move based on swipe direction
- Same numbers merge into one
- Score increases on every merge
- New tiles (2 or 4) appear after each move
- Game ends when no moves are possible
- High score is saved locally

---

## 🚀 How to Run the Project

1. Clone the repository  
   ```bash
   git clone https://github.com/unknownhero88/Game2048-unknownhero88.git
2. Open the project in Android Studio
3. Sync Gradle
4. Run on emulator or physical device 📱

---

## 📂 Project Structure
  ```bash
    app/
       ├── MainActivity.kt        # UI & animations
       ├── GameViewModel.kt       # Game logic & state
       ├── ui/                    # UI components (tiles, dialogs)
```
---

## 🎯 Future Improvements

- 🔊 Sound effects
- 📳 Haptic feedback
- 🏆 Google Play leaderboard
- 🔄 Undo last move
- 🌗 Dark mode
- 🥇 Win (2048) celebration animation

---

## 🤝 Contributing

Contributions are welcome!
Feel free to fork the project and submit a pull request.

---

## 📄 License

This project is for learning & educational purposes.

---

## 🙌 Author

### Rishi Sahu

Learning Android Development with Kotlin & Jetpack Compose 🚀


---
