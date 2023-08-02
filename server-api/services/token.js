const jwt = require("jsonwebtoken");

const generateToken = (username) => {
    try {
        const token = jwt.sign({ username }, process.env.JWT_SECRET);
        return token;
    } catch (error) {
        throw error;
    }
};

module.exports = { generateToken };
