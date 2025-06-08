import { Request, Response } from 'express';
import axios from 'axios';

export const handleIncomingMessage = async (req: Request, res: Response) => {
  const { server_uuid, username, message } = req.body;
  if (!server_uuid || !username || !message) {
    return res.status(400).json({ error: 'Faltan datos' });
  }
  try {
    await axios.post('http://localhost:4000/sendMessage', {
      server_uuid,
      username,
      message,
    });
    return res.status(200).json({ status: 'ok' });
  } catch (error) {
    console.error('Error enviando mensaje al bot:', error);
    return res.status(500).json({ error: 'No se pudo enviar el mensaje a Discord' });
  }
};


export const registerServer = (req: Request, res: Response) => {
  const { id, serverName } = req.body;

  if (!id) return res.status(400).json({ error: 'Falta el ID' });

  console.log(`Servidor registrado: ${id} (${serverName || 'Sin nombre'})`);
  return res.status(200).json({ message: 'Registrado correctamente' });
};
