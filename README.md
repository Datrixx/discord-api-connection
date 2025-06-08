# Discord-Minecraft Chat Bridge (discord-api-connection)

• This is a backend small project that involves learning topics such as TypeScript, Java, and API management. It's a simple project that enables connectivity between a **Discord bot (TypeScript with DiscordX)** and a **Minecraft server (plugin created with Java)**, _using Express + TypeScript as a connecting bridge_.

> Everything currently runs locally, but the project is designed to scale in the future.

---

## 🌐 Project Structure

```
project-root/
├── api/           # Express + TypeScript API (bridge)
├── discord/       # Discord bot using DiscordX
└── java/plugin/   # Minecraft plugin using PaperMC
```

---

## 💡 Features

- 🔁 **Real-time two-way chat relay**  
  Messages sent in Minecraft appear in Discord, and vice versa.

- 🔒 **Basic authentication system**  
  Includes `/registerapi` and `/loginapi` commands for security.

- 🧩 **Modular architecture**  
  Each part (bot, plugin, API) is independently deployable and testable.

- 🗂️ **Simple local JSON-based storage**  
  Uses `servers.json` for storing registered server information.

---

## 🚀 Tech Stack

| Component        | Technology                  |
|------------------|-----------------------------|
| Discord Bot      | TypeScript, DiscordX, Node.js |
| API Server       | Express, TypeScript         |
| Minecraft Plugin | Java, PaperMC               |
| Build Tools      | `ts-node-dev`, `tsx`, Maven |
| Database         | Local JSON file (`servers.json`) |

---

## 📦 Setup Instructions

### Prerequisites

- Node.js (v18+)
- Java 17+
- Maven
- Discord Bot Token
- Minecraft Server (Paper 1.20.4)

---

### 1. API Setup (Express + TypeScript)

```bash
cd api
npm install
npm run dev
```

> Main file: `src/index.ts`  
> Starts an Express server to handle communication between Minecraft and Discord.

---

### 2. Discord Bot Setup

Create a `.env` file inside the `discord/` folder with the following:

```env
DISCORD_TOKEN=your-bot-token-here
GUILD_ID=your-server-id
```

Start the bot using:

```bash
cd discord
npm install
npm run dev
```

> The bot listens to messages and handles commands like `/registerapi` and `/loginapi`.

---

### 3. Minecraft Plugin Setup

- Navigate to `java/plugin/`
- Build the plugin with Maven:

```bash
mvn clean package
```

- Copy the resulting `.jar` from `target/` to your Minecraft server's `plugins/` folder.
- Start the Minecraft server (ensure it is using **Paper 1.20.4**).

---

## 🔐 Authentication Flow

The system supports a simple security mechanism:

- `/registerapi` – Registers a new connection between a Discord server and a Minecraft server.
- `/loginapi` – Authenticates and enables real-time message syncing.

All server entries are stored in `servers.json`.

---

## 📁 Key Files

- `api/package.json` – Express app using `axios`, `uuid`, `dotenv`, etc.
- `discord/package.json` – Discord bot (uses Discord.js v14).
- `plugin/pom.xml` – Java Minecraft plugin using PaperMC 1.20.4.

---

## 🔮 Future Improvements

- 🌍 Move from local JSON storage to a cloud database (e.g., MongoDB, PostgreSQL).
- ☁️ Deploy the Express API on a cloud platform.
- 🛡️ Improve authentication and add token-based access.
- 📌 Support multi-server connections and dynamic configuration.
- 🧪 Add unit tests and CI workflows.

---

## 📜 License

MIT License

---

## 🤝 Contributions

Contributions are welcome! If you plan to extend this project, feel free to open issues or pull requests.
