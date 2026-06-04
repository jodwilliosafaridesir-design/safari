🎤 **SAFARI - Voice Assistant for Android Tablets**

A powerful voice-controlled assistant for Android tablets that can manage applications, automate tasks, control SMS/calls, manage contacts & calendar, and handle file operations.

## 🚀 Features

- **🎤 Voice Control**: Speech recognition with natural language processing
- **⌨️ Text Commands**: Execute commands via text input
- **📱 App Management**: Launch and close applications
- **💬 SMS Automation**: Send SMS messages
- **☎️ Phone Control**: Make calls
- **👥 Contacts Management**: Access and manage contacts
- **📅 Calendar Integration**: View calendar events
- **📁 File Management**: Open, delete, and list files

## 📋 Requirements

- Android 7.0+ (API 24)
- Target Android 15 (API 35)
- Microphone permission
- Contacts permission
- SMS/Call permissions
- File access permissions
- Calendar permission

## 🏗️ Project Structure

```
safari/
├── app/src/main/
│   ├── kotlin/com/safari/voiceassistant/
│   │   ├── ui/
│   │   │   ├── MainActivity.kt
│   │   │   ├── CommandActivity.kt
│   │   │   └── SettingsActivity.kt
│   │   ├── service/
│   │   │   ├── VoiceAssistantService.kt
│   │   │   ├── CommandExecutorService.kt
│   │   │   └── AutomationService.kt
│   │   ├── commands/
│   │   │   ├── CommandParser.kt
│   │   │   └── CommandExecutor.kt
│   │   ├── database/
│   │   │   └── CommandDatabase.kt
│   │   └── utils/
│   │       └── PermissionManager.kt
│   ├── AndroidManifest.xml
│   └── res/
│       ├── layout/
│       ├── values/
│       └── drawable/
├── build.gradle.kts
└── README.md
```

## 📦 Dependencies

- **androidx.core:core-ktx** - Core Android utilities
- **androidx.lifecycle** - ViewModel and LiveData
- **androidx.room** - Local database
- **androidx.work** - Background task scheduling
- **kotlinx.coroutines** - Async programming
- **Google Play Services** - Location and other services

## 🎯 Quick Start

### 1. Clone the repository
```bash
git clone https://github.com/jodwilliosafaridesir-design/safari.git
cd safari
```

### 2. Build the project
```bash
./gradlew build
```

### 3. Install on your device
```bash
./gradlew installDebug
```

### 4. Grant permissions
- Allow microphone, contacts, SMS, and file access when prompted

## 🗣️ Voice Commands Examples

### App Control
- "Ouvre Chrome" (Open Chrome)
- "Lance Gmail" (Launch Gmail)
- "Ferme WhatsApp" (Close WhatsApp)

### SMS
- "Envoie SMS à 06XXXXXXXX : Message" (Send SMS)

### Calls
- "Appelle Maman" (Call Mom)

### Contacts
- "Affiche les contacts" (Show contacts)

### Calendar
- "Affiche le calendrier" (Show calendar)

### Files
- "Ouvre fichier /sdcard/document.pdf" (Open file)
- "Supprime fichier /sdcard/temp.txt" (Delete file)
- "Liste les fichiers" (List files)

## 🔐 Permissions

The app requires the following permissions:

```xml
RECORD_AUDIO - For voice input
READ_CONTACTS - To access contacts
SEND_SMS - To send SMS messages
CALL_PHONE - To make phone calls
READ_CALENDAR - To access calendar events
READ/WRITE_EXTERNAL_STORAGE - For file management
INTERNET - For cloud features (future)
```

## 🛠️ Development

### Adding New Commands

1. Add the command pattern to `CommandParser.kt`
2. Add execution logic to `CommandExecutor.kt`
3. Update voice command examples in `README.md`

### Example: Add a new command
```kotlin
// In CommandParser.kt
lowerCommand.startsWith("mon_commande") -> {
    val param = extractParam(lowerCommand, listOf("mon_commande"))
    ParsedCommand("MY_COMMAND", param)
}

// In CommandExecutor.kt
fun myCommand(param: String) {
    // Implementation
    speakResponse("Commande exécutée")
}
```

## 📱 Testing

The app has been tested on:
- Android 15 (API 35)
- Xiaomi Pad Pro (2.0.206.0.VMUEUXM)
- Tablets with 7-10 inch displays

## 🔄 Future Enhancements

- [ ] Cloud sync for commands
- [ ] Custom command creation
- [ ] AI-powered response generation
- [ ] Smart home integration
- [ ] Multi-language support
- [ ] Advanced scheduling
- [ ] Voice feedback customization

## 📝 License

MIT License - See LICENSE file for details

## 🤝 Contributing

Contributions are welcome! Please:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Open a Pull Request

## 📧 Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Contact: jodwilliosafaridesir-design

---

**Made with ❤️ for Android Tablet Users**
