const express = require('express');
const jwt = require('jsonwebtoken');
const User = require('../models/User');
const authMiddleware = require('../middleware/authMiddleware');

const router = express.Router();
const JWT_SECRET = process.env.JWT_SECRET || 'secret_jwt_tres_long_et_securise';
const JWT_EXPIRES_IN = process.env.JWT_EXPIRES_IN || '24h';

const signToken = (userId) =>
  jwt.sign({ id: userId }, JWT_SECRET, { expiresIn: JWT_EXPIRES_IN });

router.post('/register', async (req, res) => {
  try {
    const { username, email, password, role } = req.body;
    if (!username || !email || !password) {
      return res.status(400).json({ message: 'Tous les champs sont obligatoires' });
    }
    const existing = await User.findOne({ $or: [{ email }, { username }] });
    if (existing) {
      return res.status(409).json({ message: 'Email ou nom d\'utilisateur deja utilise' });
    }
    const user = await User.create({ username, email, password, role });
    const token = signToken(user._id);
    res.status(201).json({ message: 'Inscription reussie', token, user });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;
    if (!email || !password) {
      return res.status(400).json({ message: 'Email et mot de passe requis' });
    }
    const user = await User.findOne({ email }).select('+password');
    if (!user || !(await user.comparePassword(password))) {
      return res.status(401).json({ message: 'Identifiants invalides' });
    }
    const token = signToken(user._id);
    res.json({ message: 'Connexion reussie', token, user });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

router.get('/me', authMiddleware, async (req, res) => {
  try {
    const user = await User.findById(req.userId);
    if (!user) return res.status(404).json({ message: 'Utilisateur non trouve' });
    res.json({ user });
  } catch (err) {
    res.status(500).json({ message: err.message });
  }
});

router.post('/verify', async (req, res) => {
  try {
    const { token } = req.body;
    if (!token) return res.status(400).json({ message: 'Token requis' });
    const decoded = jwt.verify(token, JWT_SECRET);
    const user = await User.findById(decoded.id);
    if (!user) return res.status(401).json({ message: 'Utilisateur introuvable' });
    res.json({ valid: true, userId: decoded.id, role: user.role });
  } catch (err) {
    res.status(401).json({ valid: false, message: 'Token invalide ou expire' });
  }
});

module.exports = router;
