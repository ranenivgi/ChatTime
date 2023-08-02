const mongoose = require("mongoose");
const User = require("../models/user");
const Message = require("../models/message");

const Schema = mongoose.Schema;
const Chat = new Schema({
    users: [
        {
            type: User.schema,
            required: true,
        },
    ],
    messages: [
        {
            type: Message.schema,
            ref: "Message",
            default: [],
        },
    ],
});
module.exports = mongoose.model("Chat", Chat);
