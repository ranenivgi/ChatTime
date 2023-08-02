const express = require("express");
var app = express();
app.use(express.json());

const bodyParser = require("body-parser");
//app.use(bodyParser.json({ limit: "10mb" }));
app.use(bodyParser.urlencoded({ extended: true }));

const cors = require("cors");
app.use(cors());

const customEnv = require("custom-env");
customEnv.env(process.env.NODE_ENV, "./config");

const mongoose = require("mongoose");
mongoose.connect(process.env.CONNECTION_STRING, {
    useNewUrlParser: true,
    useUnifiedTopology: true,
});

app.use(express.static("public"));
app.use("/", express.static("public"));
app.use("/register", express.static("public"));
app.use("/login", express.static("public"));

const users = require("./routes/user");
const tokens = require("./routes/token");
const chats = require("./routes/chat");

app.use("/api/Users", users);
app.use("/api/Tokens", tokens);
app.use("/api/Chats", chats);

const socketMap = require("./socketMap");
const tokenMap = require("./tokenMap");
const admin = require("firebase-admin");
const serviceAccount = require("./firebase-service.json");

admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});

const http = require("http");
const { Server } = require("socket.io");
const server = http.createServer(app);
const io = new Server(server, {
    cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST", "DELETE"],
    },
});

io.on("connection", (socket) => {
    socket.on("join_chat", (data) => {
        socket.join(data);
        socketMap.set(data, socket);
        delete tokenMap.get(data);
    });

    socket.on("add-contact", (username) => {
        if (!socket.in(username)) {
            return;
        }
        socket.in(username).emit("add-contact");
    });

    socket.on("send_message", (data) => {
        if (!socket.in(data.contactUsername)) {
            return;
        }
        socket.in(data.contactUsername).emit("receive_message", data);
    });
});

server.listen(process.env.PORT);
