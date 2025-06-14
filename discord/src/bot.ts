import "reflect-metadata";
import { dirname, importx } from "@discordx/importer";
import type { Interaction, Message } from "discord.js";
import { IntentsBitField } from "discord.js";
import { Client } from "discordx";
import "dotenv/config.js";
import { loadCommands } from "./utils/loadCommands.js";
import axios from "axios";
import { fileURLToPath } from "url";
import path from "path";
import fs from "fs";
const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);
const serversFilePath = path.join(__dirname, "./database/servers.json");
import fetch from "node-fetch";


export const bot = new Client({
  intents: [
    IntentsBitField.Flags.Guilds,
    IntentsBitField.Flags.GuildMembers,
    IntentsBitField.Flags.GuildMessages,
    IntentsBitField.Flags.GuildMessageReactions,
    IntentsBitField.Flags.GuildVoiceStates,
    IntentsBitField.Flags.MessageContent
  ],
  silent: false,
  botGuilds: [
    (client) => ["1374561104688058438"]]
});


import WebSocket from 'ws';

const ws = new WebSocket('ws://localhost:5001');

ws.on('open', () => {
  console.log('Bot conectado al servidor WS');
});

export function sendToMinecraft(server_uuid: string, username: string, message: string) {
  ws.send(JSON.stringify({
    type: "chat",
    server_uuid,
    username,
    message
  }));
}
ws.on('error', (error) => {
  console.error('Error WS:', error);
});




bot.once("ready", async () => {
  void bot.initApplicationCommands();
  console.log("✅ Bot iniciado");
});
bot.on("interactionCreate", async (interaction: Interaction) => {
  bot.executeInteraction(interaction);
});

let serversDB: Record<string, { discordChannelId: string }> = {};

try {
  if (fs.existsSync(serversFilePath)) {
    const data = fs.readFileSync(serversFilePath, "utf8");
    serversDB = JSON.parse(data);
    console.log("serversDB cargado:", serversDB);
  } else {
    console.log("No se encontró el archivo servers.json, iniciando vacío.");
  }
} catch (error) {
  console.error("Error leyendo servers.json:", error);
}

bot.on("messageCreate", async (message) => {
  if (message.author.bot) return;

  const server_uuid_database = Object.entries(serversDB).find(
    ([uuid, data]) => data.discordChannelId === message.channel.id
  )?.[0];

  if (!server_uuid_database) return;

  try {
    const response = await fetch("http://localhost:5000/fromdiscord", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        server_uuid: server_uuid_database,
        username: message.author.username,
        message: message.content,
        type: "chat"
      }),
    });

    if (!response.ok) {
      const text = await response.text();
      console.error("❌ Error al reenviar al API /fromdiscord:", text);
    }
  } catch (error) {
    console.error("❌ Error haciendo POST a /fromdiscord:", error);
  }
});



async function run() {
  await loadCommands();

  if (!process.env.BOT_TOKEN) {
    throw new Error("❌ BOT_TOKEN no definido en el archivo .env");
  }

  await bot.login(process.env.BOT_TOKEN);
}

void run();

