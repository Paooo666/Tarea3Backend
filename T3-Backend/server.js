const express = require('express');
const bcrypt = require('bcryptjs');
const app = express();
app.use(express.json());

const users = []; // Base de datos temporal en memoria

// Verificar API 
app.get('/', (req, res) => {
    res.send("API de Node.js activa y funcionando desde Docker");
});

// Registro con Bcrypt (metodo de seguridad para proteger contraseñas)
app.post('/register', async (req, res) => {
    const { username, password } = req.body;
    if (users.find(u => u.username === username)) {
        return res.status(400).json({ message: "Usuario ya existe" });
    }
    const hashedPassword = await bcrypt.hash(password, 10);
    users.push({ username, password: hashedPassword });
    res.status(201).json({ message: "Registro exitoso" });
});

//Login 
app.post('/login', async (req, res) => {
    
    const { username, password } = req.body;
    if (!username || !password) {
        return res.status(400).json({ message: "Faltan datos en el JSON" });
    }


    const user = users.find(u => u.username === username);
    if (user && await bcrypt.compare(password, user.password)) {
        res.json({ message: `Bienvenido ${username}` });
    } else {
        res.status(401).json({ message: "Credenciales incorrectas" });
    }
});

// Escuchar en el puerto 5000 
app.listen(5000, '0.0.0.0', () => console.log("Servidor en puerto 5000"));