const chatController = require("../controllers/chat");
const messageController = require("../controllers/message");

const express = require("express");
var router = express.Router();

router
    .route("/")
    .post(chatController.createChat)
    .get(chatController.getChats);
router
    .route("/:id")
    .get(chatController.getChatByID)
    .delete(chatController.deleteChatByID);
router
    .route("/:id/Messages")
    .post(messageController.createMessage)
    .get(messageController.getMessages);

module.exports = router;
