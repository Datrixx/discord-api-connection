# Discord-Minecraft Chat Bridge (discord-api-connection)

This backend project is a bridge between a **Discord bot (TypeScript with DiscordX)** and a **Minecraft server (Java plugin)**, using **Express + Cors + WS + TypeScript** as an intermediate communication layer, now including WebSocket connection for real-time communication and dynamic plugin control.

> Currently everything runs locally, but it is designed to scale to production.

---

## 🌐 Project Structure

```
project-root/
├── core/           # WebSocket + Express + Cors
├── discord/        # Discord Bot with DiscordX and WebSocket
└── java/plugin/    # Minecraft Plugin with Spigot and WebSocket
```

---

## 💡 Features

- 🔁 **Bidirectional real-time chat**  
  Messages (both chat and join/quit server events) sent in Minecraft are reflected on Discord and vice versa.

- 🔄 **WebSocket connection with API**  
  The plugin connects via WS to the local server to receive and send events in real time.

- 🔒 **Basic authentication system**  
  Commands `/registerapi` to register and `/loginapi <UUID>` to authenticate the connection.

- ⚙️ **Dynamic WebSocket connection control**  
  Command `/toggleplugin` to enable/disable the connection with the API without restarting the server.

- 🗂️ **Simple local JSON storage**  
  Server logs and configurations saved in `servers.json` and `api-rest.yml`.

---

## 🚀 Technology Stack

| Component        | Technology                     |
|------------------|--------------------------------|
| Discord Bot      | TypeScript, DiscordX, Node.js  |
| API Server       | Express, TypeScript            |
| Minecraft Plugin | Java, Spigot 1.8.8, WebSocket (ws) |
| Tools            | `ts-node-dev`, `tsx`, Maven    |
| Database         | Local JSON (`servers.json`)    |

---

## 📦 Installation Instructions

### Requirements

- Node.js v18+
- Java 17+
- Maven
- Discord bot token
- Minecraft server (Spigot 1.8.8)

---

### 1. Setup Core (Express + TypeScript)

```bash
cd api
npm install
npm run dev
```

> Main file: `core/src/index.ts`  
> Starts Express and WebSocket server for communication with plugin and bot.

---

### 2. Setup Discord Bot

Create a `.env` file in `discord/` with:

```env
BOT_TOKEN=your-token-here
```

Start the bot with:

```bash
cd discord
npm install
npm run dev
```

> The bot handles `/loginapi <UUID>` commands and listens to messages for synchronization.

---

### 3. Setup Minecraft Plugin

- Go to `java/plugin/`
- Build the plugin with Maven:

```bash
mvn clean package
```

- Copy the resulting `.jar` from `target/` into the `plugins/` folder of your Minecraft server (ONLY the `discordmcapi-1.0-SNAPSHOT.jar`).
- Start the Spigot 1.8.8 server.

---

## 🔐 Authentication & Control Flow

- `/registerapi` returns a random UUID in the Minecraft chat and saves it in `config.yml`.
- `/loginapi <UUID>` registers, authenticates, and activates the WebSocket bridge connection.
- `/toggleplugin` enables or disables the WebSocket connection and message synchronization without restarting the server.

The `enabled` state is saved in `api-rest.yml` for persistence.

---

## 📁 Key Files

- `core/package.json` – WebSocket with dependencies like `axios`, `ws`, `uuid`, `dotenv`.
- `discord/package.json` – Discord bot using Discord.js v14 and DiscordX.
- `plugin/pom.xml` – Java plugin with Spigot 1.8.8 and WebSocket.
- `plugin/src/main/java/com/discordconnection/plugin/Main.java` – Main plugin class with WS logic and state control.

---

## 🔮 Future Improvements

- Migrate local JSON storage to cloud database (MongoDB, PostgreSQL).
- Deploy API on cloud platform (AWS, Heroku, etc.).
- Improve authentication with tokens and roles.
- Support multiple servers and dynamic configuration.
- Implement unit testing and CI/CD.

---

## 📜 License

MIT License

Copyright (c) 2025 Daniel Flores Viera

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

More info: https://github.com/danielvflores

---

## 🤝 Contributions

Contributions welcome! If you want to extend or improve the project, open issues or pull requests.

---

## ⚒️ MINECRAFT PLUGIN SNAPSHOT:
[discordmcapi-1.0-SNAPSHOT.jar](https://github.com/danielvflores/discord-api-connection/raw/refs/heads/main/java/discordmcapi/target/discordmcapi-1.0-SNAPSHOT.jar)

---

## 📦 Releases

[![Última release](https://img.shields.io/badge/release-minecraft--discord--api-blue?style=flat-square)](https://github.com/danielvflores/discord-api-connection/releases/tag/minecraft-discord-api)

Conexión entre Minecraft y Discord usando API personalizada.