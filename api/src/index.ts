import 'dotenv/config';
import express from 'express';
import cors from 'cors';
import messageRoutes from './routes/messages.js';

const app = express();

app.use(cors());
app.use(express.json());
app.use('/', messageRoutes);

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`API escuchando en http://localhost:${PORT}`);
});

