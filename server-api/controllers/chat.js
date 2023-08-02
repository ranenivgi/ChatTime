const chatService = require("../services/chat");

const createChat = async (req, res) => {
    try {
        const { username } = req.body;
        if (req.headers.authorization) {
            const tokenObject = JSON.parse(
                req.headers.authorization.split(" ")[1]
            );
            const token = tokenObject.token;
            const result = await chatService.createChat(username, token);
            res.status(200).json(result);
        }
    } catch (error) {
        if (error.message === "Invalid token") {
            return res.status(401).json({ errors: ["Invalid token"] });
        } else if (error.message === "Thou shalt not talk with thy self") {
            return res
                .status(404)
                .json({ errors: ["Thou shalt not talk with thy self"] });
        } else if (error.message === "Contact not found") {
            return res.status(404).json({ errors: ["Contact not found"] });
        } else {
            res.status(500).json({
                errors: ["An error occurred during chat creation"],
            });
        }
    }
};

const getChats = async (req, res) => {
    try {
        if (req.headers.authorization) {
            const tokenObject = JSON.parse(
                req.headers.authorization.split(" ")[1]
            );
            const token = tokenObject.token;
            const result = await chatService.getChats(token);
            res.status(200).json(result);
        }
    } catch (error) {
        res.status(404).json({
            errors: error.message,
        });
    }
};

const getChatByID = async (req, res) => {
    try {
        if (req.headers.authorization && req.params.id) {
            const tokenObject = JSON.parse(
                req.headers.authorization.split(" ")[1]
            );
            const token = tokenObject.token;
            const result = await chatService.getChatByID(token, req.params.id);
            if (!result) {
                throw new Error("Invalid id");
            }
            res.status(200).json(result);
        }
    } catch (error) {
        res.status(404).json({
            errors: error.message,
        });
    }
};

const deleteChatByID = async (req, res) => {
    try {
        if (req.headers.authorization && req.params.id) {
            const tokenObject = JSON.parse(
                req.headers.authorization.split(" ")[1]
            );
            const token = tokenObject.token;
            const result = await chatService.deleteChatByID(
                token,
                req.params.id
            );
            if (!result) {
                throw new Error("Invalid id");
            }
            res.status(200);
        }
    } catch (error) {
        res.status(404).json({
            errors: error.message,
        });
    }
};

module.exports = { createChat, getChats, getChatByID, deleteChatByID };
