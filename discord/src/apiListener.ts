import express from 'express';
import { bot } from './bot.js';
import fs from 'fs';
import path from 'path';
import { TextChannel, NewsChannel, ThreadChannel } from 'discord.js';
import { NextFunction } from 'express';
import { EmbedBuilder } from 'discord.js';

import { 
  blockQuote, 
  bold,
  italic, 
  quote, 
  spoiler, 
  strikethrough, 
  underline, 
  subtext, 
  inlineCode, 
  codeBlock } from 'discord.js';
import axios from 'axios';
import bodyParser from "body-parser";

const app = express();
app.use(express.json());
app.use(cors());
app.use(bodyParser.json());

const serversFilePath = path.join(process.cwd(), 'src/database/servers.json');

app.post('/sendMessage', (req, res, next: NextFunction) => {
  (async () => {
    try {
      const { server_uuid, username, message, type } = req.body;

      if (!server_uuid || !username || !message || !type) {
        return res.status(400).json({ error: 'Faltan datos' });
      }

      let db: Record<string, { discordChannelId: string }> = {};
      if (fs.existsSync(serversFilePath)) {
        db = JSON.parse(fs.readFileSync(serversFilePath, 'utf8'));
      }

      const channelId = db[server_uuid]?.discordChannelId;
      if (!channelId) {
        return res.status(403).json({ error: 'Servidor no vinculado con Discord' });
      }

      const channel = await bot.channels.fetch(channelId);
      if (
        channel instanceof TextChannel ||
        channel instanceof NewsChannel ||
        channel instanceof ThreadChannel
      ) {

        const embed = new EmbedBuilder()
        .setDescription(blockQuote(bold(inlineCode(message))))
        .setTimestamp()
        .setFooter({ text: `Usuario: ${username}` })
        .setThumbnail(`https://minotar.net/avatar/${username}`);


        switch (type) {
          case 'chat':
            embed.setTitle(type)
            embed.setColor('#00b0f4');
            break;
          case 'status':
            embed.setTitle(type)
            embed.setColor('#43b581');
            break;
          case 'warning':
            embed.setTitle(type)
            embed.setColor('#faa61a');
            break;
          default:
            embed.setTitle(type)
            embed.setColor('#5865f2');
            break;
        }

        await channel.send({ embeds: [embed] });
        return res.status(200).json({ status: 'ok' });
      } else {
        return res.status(500).json({ error: 'El canal no es de texto' });
      }
    } catch (error) {
      console.error('Error al enviar mensaje a Discord:', error);
      return res.status(500).json({ error: 'Error interno al enviar el mensaje' });
    }
  })().catch(next);
});

app.post('/fromdiscord', async (req: any, res: any) => {
  try {
    const { server_uuid, username, message, type } = req.body;

    if (!server_uuid || !username || !message || !type) {
      return res.status(400).json({ error: 'Faltan datos' });
    }

    let db: Record<string, { discordChannelId: string }> = {};
    if (fs.existsSync(serversFilePath)) {
      db = JSON.parse(fs.readFileSync(serversFilePath, 'utf8'));
    }

    const channelId = db[server_uuid]?.discordChannelId;
    if (!channelId) {
      return res.status(403).json({ error: 'Servidor no vinculado con Discord' });
    }

    const channel = await bot.channels.fetch(channelId);

  } catch (error) {
    console.error('Error en /fromdiscord:', error);
    return res.status(500).json({ error: 'Error interno' });
  }
});




const PORT = process.env.PORT || 4000;
app.listen(PORT, () => console.log(`Bot API escuchando en http://localhost:${PORT}`));
import cors from "cors";
