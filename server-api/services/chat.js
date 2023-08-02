const Chat = require("../models/chat");
const User = require("../services/user");
const jwt = require("jsonwebtoken");
const tokenMap = require("../tokenMap");
const admin = require("firebase-admin");
const socketMap = require("../socketMap")

const createChat = async (contactName, token) => {
    try {
        const data = jwt.verify(token, process.env.JWT_SECRET);
        if (!data) {
            throw new Error("Invalid token");
        }
        const currentUser = await User.getUserByUsername(data.username);
        const contact = await User.getUserByUsername(contactName);
        if (!contact) {
            throw new Error("Contact not found");
        }

        if (currentUser.username === contact.username) {
            throw new Error("Thou shalt not talk with thy self");
        }

        const chat = new Chat({
            users: [contact, currentUser],
        });
        await chat.save();
        const response = {
            username: contact.username,
            displayName: contact.displayName,
            profilePic: contact.profilePic,
        };
        
        if (tokenMap.get(contact.username)) {
            const registrationToken = await tokenMap.get(contact.username);
            const msgNotific = {
                notification: {
                    title: "new chat",
                    body: contact.username,
                },
                data: {
                    action: "add-contact",
                    receiver: contact.username
                },
                token: registrationToken,
            };
            await admin
                .messaging()
                .send(msgNotific)
                .then((response) => {})
                .catch((error) => {
                    throw new Error(error);
                });
        } else if (socketMap.get(contact.username)) {
            await socketMap.get(contact.username).emit("add-contact");
        }
        return { id: chat._id, user: response };
    } catch (error) {
        throw error;
    }
};

const getChats = async (token) => {
    try {
        const data = jwt.verify(token, process.env.JWT_SECRET);
        if (!data) {
            throw new Error("Invalid token");
        }

        const chats = await Chat.find({
            users: { $elemMatch: { username: { $eq: data.username } } },
        }).exec();

        const chatsWithLastMessage = chats.map((chat) => {
            const lastMessage =
                chat.messages && chat.messages.length > 0
                    ? chat.messages[chat.messages.length - 1]
                    : null;
            const otherUser = chat.users.find(
                (user) => user.username !== data.username
            );

            return {
                id: chat._id,
                user: {
                    username: otherUser.username,
                    displayName: otherUser.displayName,
                    profilePic: otherUser.profilePic,
                },
                lastMessage: lastMessage
                    ? {
                          id: lastMessage._id,
                          created: lastMessage.created,
                          content: lastMessage.content,
                      }
                    : null,
            };
        });

        return chatsWithLastMessage;
    } catch (error) {
        throw error;
    }
};

const getChatByID = async (token, chatID) => {
    try {
        const data = jwt.verify(token, process.env.JWT_SECRET);
        if (!data) {
            throw new Error("Invalid token");
        }

        const chat = await Chat.findOne({ _id: chatID });
        if (!chat) return null;
        return chat;
    } catch (error) {
        throw error;
    }
};

const deleteChatByID = async (token, chatID) => {
    try {
        const data = jwt.verify(token, process.env.JWT_SECRET);
        if (!data) {
            throw new Error("Invalid token");
        }
        const result = await Chat.deleteOne({ _id: chatID });
        if (result.deletedCount > 0) {
            return true;
        }
        return false;
    } catch (error) {
        throw error;
    }
};

module.exports = { createChat, getChats, getChatByID, deleteChatByID };
