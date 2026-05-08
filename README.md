# IntelliQueue 🚀

**IntelliQueue** is a modern, real-time Queue Management Simulator built using Java Swing. It provides a thread-safe, interactive environment to manage and visualize customer flow with priority handling.

## ✨ Features

- **Real-time Simulation**: Watch customers being served with live countdowns and progress bars.
- **Dual-Counter System**: 
  - **Counter 1**: Dedicated to the Normal queue.
  - **Counter 2**: Dedicated to the Senior/Priority queue.
- **Priority Logic**: Intelligent handling of Senior vs. Normal customers.
- **Live Statistics**: Track total served, average wait times, and current queue lengths in real-time.
- **Modern UI**: Clean, card-based design with progress bars and smooth updates.
- **Graceful Shutdown**: Thread-safe cleanup when closing the application.
- **Audio Feedback**: Sound notifications upon service completion.

## 🛠️ Technology Stack

- **Language**: Java 17+
- **UI Framework**: Java Swing & AWT
- **Architecture**: MVC (Model-View-Controller)
- **Concurrency**: Multi-threaded processing with dedicated counter threads.

## 🚀 Getting Started

### Prerequisites
- Java Development Kit (JDK) 17 or higher installed.

### How to Run
1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   ```
2. Navigate to the project folder:
   ```bash
   cd IntelliQueue
   ```
3. Compile the project:
   ```bash
   javac -d bin -sourcepath src src/Main.java
   ```
4. Run the application:
   ```bash
   java -cp bin Main
   ```

## 📂 Project Structure

```text
IntelliQueue/
├── src/
│   ├── controller/   # Handles business logic and thread management
│   ├── model/        # Data structures for customers and queues
│   ├── view/         # UI components and layout
│   └── Main.java     # Application entry point
└── .gitignore        # Standard Java ignore rules
```

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
