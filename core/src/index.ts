
import express from "express";
import cors from "cors";
import { WebSocketServer } from "ws";

const app = express();
app.use(cors());
app.use(express.json());

const wss = new WebSocketServer({ port: 5001 });
const connections = new Map<string, WebSocket>();

wss.on("connection", (ws) => {
  ws.on("message", (message) => {
    try {
      const data = JSON.parse(message.toString());

      if (data.type === "register") {
        const { server_uuid } = data;
        if (!server_uuid) return;

        connections.set(server_uuid, ws as any);
        console.log(`🔗 Plugin conectado: ${server_uuid}`);
      }

    } catch (err) {
      console.error("❌ Error parsing message:", err);
    }
  });

  ws.on("close", () => {
    for (const [uuid, socket] of connections) {
      if (socket === ws as any) {
        connections.delete(uuid);
        console.log(`❌ Plugin desconectado: ${uuid}`);
        break;
      }
    }
  });
});


app.post("/fromDiscord", (req: any, res: any) => {
  const { server_uuid, message, username } = req.body;

  const ws = connections.get(server_uuid);
  if (!ws || ws.readyState !== ws.OPEN) {
    console.log("Servidor no conectado o WS cerrado para UUID:", server_uuid);
    return res.status(404).json({ error: "Servidor no conectado" });
  }

  ws.send(JSON.stringify({ username, message, type: "chat" }));
  res.json({ status: "ok" });
});

app.listen(5000, () => console.log("🚀 API+WS corriendo en http://localhost:5000"));
