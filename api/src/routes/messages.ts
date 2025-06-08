import { Router } from 'express';
import { handleIncomingMessage, registerServer } from '../controllers/messageController.js';

const router = Router();

router.post('/message', handleIncomingMessage as any);
router.post('/register', registerServer as any);

export default router;
