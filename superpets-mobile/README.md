# Compose Multiplatform MVP Template

A modern, production-ready template for Compose Multiplatform projects.

## Features

- 🎯 MVVM Architecture
- 📱 Multiplatform (Android + iOS)
- 🎨 Material 3 Design
- 🔄 Kotlin Coroutines & Flow
- 🗄️ Room Database
- 🌐 Ktor Client
- 💉 Koin Dependency Injection
- 🧭 Navigation Compose
- 🖼️ Coil Image Loading
- 📝 Kotlinx Serialization

## Quick Start

1. Clone this template
2. Update `template.properties` with your app details
3. Run the setup script:
   ```bash
   ./setup.sh
   ```
4. Open the project in Android Studio
5. Run on your target platform

## Project Structure

```
composeApp/
├── src/
│   ├── commonMain/
│   │   ├── kotlin/
│   │   │   ├── di/           # Dependency injection
│   │   │   ├── domain/       # Business logic
│   │   │   ├── data/         # Data layer
│   │   │   ├── ui/           # UI components
│   │   │   └── utils/        # Utilities
│   │   └── resources/        # Shared resources
│   ├── androidMain/          # Android-specific code
│   └── iosMain/             # iOS-specific code
```

## Architecture

- **Domain Layer**: Business logic and use cases
- **Data Layer**: Repository implementations and data sources
- **UI Layer**: ViewModels and UI components
- **DI**: Koin modules for dependency injection

## Configuration

Edit `template.properties` to customize:
- Application name and package
- SDK versions
- Feature flags

## Requirements

- Android Studio Hedgehog or newer
- Xcode 15+ (for iOS development)
- JDK 11+
- Kotlin 1.9.0+
- Gradle 8.0+

## License

MIT License - See LICENSE file for details
