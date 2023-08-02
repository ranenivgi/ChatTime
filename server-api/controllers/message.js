const messageService = require("../services/message");

const createMessage = async (req, res) => {
    try {
        if (req.headers.authorization && req.params.id) {
            const tokenObject = JSON.parse(
                req.headers.authorization.split(" ")[1]
            );
            const token = tokenObject.token;

            const { msg } = req.body;
            const result = await messageService.createMessage(
                token,
                req.params.id,
                msg
            );
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

const getMessages = async (req, res) => {
    try {
        if (req.headers.authorization && req.params.id) {
            const tokenObject = JSON.parse(
                req.headers.authorization.split(" ")[1]
            );
            const token = tokenObject.token;

            const result = await messageService.getMessages(
                token,
                req.params.id
            );
            if (!result) {
                throw new Error("Invalid chat");
            }
            res.status(200).json(result);
        }
    } catch (error) {
        res.status(404).json({
            errors: error.message,
        });
    }
};

module.exports = { createMessage, getMessages };
