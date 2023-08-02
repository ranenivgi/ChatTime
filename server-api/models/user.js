const mongoose = require("mongoose");

const Schema = mongoose.Schema;
const User = new Schema({
    username: {
        type: String,
        required: false,
    },
    password: {
        type: String,
        required: false,
    },
    displayName: {
        type: String,
        required: false,
    },
    profilePic: {
        type: String,
        required: false,
    },
});
module.exports = mongoose.model("User", User);
