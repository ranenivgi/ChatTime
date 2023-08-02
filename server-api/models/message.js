const mongoose = require("mongoose");
const User = require("../models/user");

const Schema = mongoose.Schema;
const Message = new Schema({
    created: {
        type: String,
        required: true,
    },
    sender: {type: User.schema},
    content: {
        type: String,
        required: false,
    }
});

module.exports = mongoose.model("Message", Message);
