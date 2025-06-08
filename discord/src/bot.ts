import "reflect-metadata";
import { dirname, importx } from "@discordx/importer";
import type { Interaction, Message } from "discord.js";
import { IntentsBitField } from "discord.js";
import { Client } from "discordx";
import "dotenv/config.js";
import { loadCommands } from "./utils/loadCommands.js";


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

bot.once("ready", async () => {
  void bot.initApplicationCommands();
  console.log("✅ Bot iniciado");
});
bot.on("interactionCreate", async (interaction: Interaction) => {
  bot.executeInteraction(interaction);
});




async function run() {
  await loadCommands();

  if (!process.env.BOT_TOKEN) {
    throw new Error("❌ BOT_TOKEN no definido en el archivo .env");
  }

  await bot.login(process.env.BOT_TOKEN);
}

void run();

