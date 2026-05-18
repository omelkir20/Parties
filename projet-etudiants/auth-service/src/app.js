const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const morgan = require('morgan');
const authRoutes = require('./routes/auth');

const app = express();
const PORT = process.env.PORT || 3001;
const MONGO_URI = process.env.MONGO_URI || 'mongodb://localhost:27017/authdb';

app.use(cors());
app.use(express.json());
app.use(morgan('combined'));

app.use('/auth', authRoutes);

app.get('/health', (req, res) => {
  res.json({ status: 'UP', service: 'auth-service' });
});

app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ message: 'Erreur interne du serveur' });
});

mongoose
  .connect(MONGO_URI)
  .then(() => {
    console.log('Connecte a MongoDB');
    app.listen(PORT, () => console.log(`auth-service demarre sur le port ${PORT}`));
  })
  .catch((err) => {
    console.error('Erreur de connexion MongoDB :', err.message);
    process.exit(1);
  });

module.exports = app;
