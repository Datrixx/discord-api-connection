import express from 'express';
import { bot } from './bot.js';
import fs from 'fs';
import path from 'path';
import { TextChannel, NewsChannel, ThreadChannel } from 'discord.js';
import { NextFunction } from 'express';

const app = express();
app.use(express.json());

const serversFilePath = path.join(process.cwd(), 'src/database/servers.json');

app.post('/sendMessage', (req, res, next: NextFunction) => {
  (async () => {
    try {
      const { server_uuid, username, message } = req.body;

      if (!server_uuid || !username || !message) {
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
        await channel.send(`📢 **${username}**: ${message}`);
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


const PORT = process.env.PORT || 4000;
app.listen(PORT, () => console.log(`Bot API escuchando en http://localhost:${PORT}`));
