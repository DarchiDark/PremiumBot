<div align="left" style="position: relative;">
<h1>Telegram Premium Bot Template</h1>
<img src="https://i.imgur.com/UybektI.png/?size=512&id=55494&format=png" align="right" width="30%" style="margin: -20px 0 0 20px;">
<p align="left">
  <em><code>❯ Telegram bot for selling and managing Premium subscriptions</code></em>
</p>
<p align="left">
  <img src="https://img.shields.io/github/last-commit/DarchiDark/PremiumBot?style=default&logo=git&logoColor=orange&color=faad07" alt="last-commit">
  <img src="https://img.shields.io/github/languages/top/DarchiDark/PremiumBot?style=default&color=faad07" alt="repo-top-language">
  <img src="https://img.shields.io/github/languages/count/DarchiDark/PremiumBot?style=default&color=faad07" alt="repo-language-count">
</p>
</div>
<br clear="right">

## 🔗 Table of Contents

- [📍 Overview](#-overview)
- [🌍 Localization](#-localization)
- [👾 Features](#-features)
- [🚀 Getting Started](#-getting-started)
  - [☑️ Requirements](#️-requirements)
  - [⚙️ Installation](#️-installation)
  - [🤖 Usage](#-usage)
- [📌 Roadmap](#-roadmap)
- [🔰 Contributing](#-contributing)
- [🙌 Acknowledgments](#-acknowledgments)

---

## 📍 Overview

**Telegram bot template** for managing Premium subscriptions directly in a Telegram chat.  
It serves as a foundation for future projects and is designed to be scalable and easily extendable.

---

## 🌍 Localization

Localization is supported. The current bot language is **Russian**.  
In the future, localization will be available via a `lang.yml` file.  
Currently, you can manually translate any string by replacing Russian text in the code with your preferred language.

---

## 👾 Features

- **Client profile management**
- **User-friendly interface** for both users and admins, all inside Telegram (no need for CLI commands)
- **Code system** to grant permissions or features to users
- **Referral system** to track user sources and display stats and leaderboards

---

## 🚀 Getting Started

### ☑️ Requirements

- **Java 21**
- **Telegram bot** created via BotFather

### ⚙️ Installation

1. Clone the repository via terminal:
```sh
git clone https://github.com/DarchiDark/PremiumBot.git
cd PremiumBot
```

Or open it in an IDE such as **IntelliJ IDEA**:
![IDE Screenshot](https://github.com/user-attachments/assets/87bdc173-7adf-4580-aec9-05f0af12b324)

2. Edit the private authentication code in `TelegramBot.java` on **line 57**.  
Replace the default code with your own secret phrase to prevent unauthorized admin access.

**Security Tip:** After deploying, you can remove the `/auth` command entirely for 100% protection.

3. Build the project using Gradle:
```sh
./gradlew build
```
The output `.jar` file will be in `build/libs/` named `PremiumBot-1.0-SNAPSHOT-all.jar`.  
**⚠️ Do NOT use `PremiumBot-1.0-SNAPSHOT.jar` – it lacks necessary dependencies.**

### 🤖 Usage

Start the bot using:
```sh
java -jar PremiumBot-1.0-SNAPSHOT-all.jar
```

On first launch, the bot generates a folder and a `config.yml` file.  
It will crash until configured correctly — **this is expected behavior**.

Configure the following in `config.yml`:
- Telegram Bot **HTTP Token** from BotFather
- **MySQL database URL**, username, and password

To gain admin access:
```text
/auth YOUR_SECRET_CODE
```
Once authenticated, re-send `/start` to unlock the **"Admin Panel 👨‍💻"** button.

Now you're ready to create referral codes and manage users.

---

## 📌 Roadmap

- [x] Basic functionality for subscriptions, groups, and referrals
- [ ] Content management and distribution system for users
- [ ] Migration to Spring Boot for modularity and DI
- [ ] Web dashboard for advanced analytics

---

## 🔰 Contributing

- Fork the repository
- Create a new branch (`feature/your-feature-name`)
- Make your changes and test them
- Submit a pull request with a detailed explanation

---

## 🙌 Acknowledgments

- **Project author and visionary**: [DarchiDark](https://github.com/DarchiDark)
- **Telegram Java API**: [Ruben Bermudez (rubenlagus)](https://github.com/rubenlagus/TelegramBots)
