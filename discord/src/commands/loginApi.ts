import { Discord, Slash, SlashOption } from "discordx";
import { ApplicationCommandOptionType, CommandInteraction } from "discord.js";
import * as fs from "fs";
import path from "path";
import { fileURLToPath } from "url";

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

const serversFilePath = path.join(__dirname, "../database/servers.json");

@Discord()
export class LoginApiCommand {
  @Slash({
    name: "loginapi",
    description: "Vincula tu servidor de Minecraft con este canal de Discord",
  })
  async loginApi(
    @SlashOption({
      name: "uuid",
      description: "UUID generado por el servidor Minecraft (/registerapi)",
      required: true,
      type: ApplicationCommandOptionType.String,
    })
    uuid: string,
    interaction: CommandInteraction
  ) {
    let db: Record<string, { discordChannelId: string }> = {};

    if (fs.existsSync(serversFilePath)) {
      db = JSON.parse(fs.readFileSync(serversFilePath, "utf8"));
    }

    if (db[uuid]) {
      return interaction.reply({
        content: "❌ Este servidor ya está vinculado con un canal.",
        ephemeral: true,
      });
    }

    db[uuid] = {
      discordChannelId: interaction.channelId,
    };

    fs.writeFileSync(serversFilePath, JSON.stringify(db, null, 2), "utf8");

    return interaction.reply({
      content: `✅ Servidor vinculado correctamente a este canal.`,
      ephemeral: true,
    });
  }
}
