const Message = require("../models/message");
const Chat = require("../models/chat");
const User = require("../models/user");
const userService = require("../services/user");
const jwt = require("jsonwebtoken");
const tokenMap = require("../tokenMap");
const socketMap = require("../socketMap");
const admin = require("firebase-admin");

const createMessage = async (token, chatID, message) => {
    try {
        const data = jwt.verify(token, process.env.JWT_SECRET);
        if (!data) {
            throw new Error("Invalid token");
        }

        const user = await userService.getUserByUsername(data.username);
        if (!user) return null;
        const newMessage = new Message({
            created: new Date(),
            sender: {
                username: user.username,
                displayName: user.displayName,
                profilePic: user.profilePic,
            },
            content: message,
        });
        await newMessage.save();

        const chat = await Chat.findById(chatID);
        chat.messages.push(newMessage);
        await chat.save();

        const response = {
            id: newMessage._id,
            created: newMessage.created,
            sender: {
                username: user.username,
                displayName: user.displayName,
                profilePic: user.profilePic,
            },
            content: newMessage.content,
        };
        if (chat.users[0].username === user.username) {
            var talkingTo = chat.users[1];
        } else {
            var talkingTo = chat.users[0];
        }

        if (tokenMap.get(talkingTo.username)) {
            delete socketMap.get(talkingTo.username);
            const registrationToken = tokenMap.get(talkingTo.username);
            const name = await User.findOne({
                username: response.sender.username,
            });
            const msgNotific = {
                notification: {
                    title: name.displayName,
                    body: response.content,
                },
                data: {
                    action: "send_message",
                    receiver: talkingTo.username,
                },
                token: registrationToken,
            };
            admin
                .messaging()
                .send(msgNotific)
                .then((response) => {
                })
                .catch((error) => {
                    throw new Error(error);
                });
        } else if (socketMap.get(talkingTo.username)) {
            const newMsg = {
                chatID: chatID,
                contactUsername: talkingTo.username,
            };
            await socketMap
                .get(talkingTo.username)
                .emit("receive_message", newMsg);
        }

        return response;
    } catch (error) {
        throw error;
    }
};

const getMessages = async (token, chatID) => {
    try {
        const data = jwt.verify(token, process.env.JWT_SECRET);
        if (!data) {
            throw new Error("Invalid token");
        }

        const chat = await Chat.findById(chatID);

        const messages = chat.messages
            .slice()
            .reverse()
            .map((message) => ({
                id: message._id,
                created: message.created,
                sender: {
                    username: message.sender.username,
                },
                content: message.content,
            }));

        return messages;
    } catch (error) {
        throw error;
    }
};

module.exports = { createMessage, getMessages };
