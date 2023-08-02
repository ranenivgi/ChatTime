const userService = require("../services/user");

const createUser = async (req, res) => {
    try {
        const { username, password, displayName, profilePic } = req.body;
        const newUser = await userService.createUser(
            username,
            password,
            displayName,
            profilePic
        );
        res.status(200).json(newUser);
    } catch (error) {
        res.status(404).json({ errors: error.message });
    }
};

const getCurrentUser = async (req, res) => {
    try {
        if (req.headers.authorization && req.params.username) {
            const tokenObject = JSON.parse(
                req.headers.authorization.split(" ")[1]
            );
            const token = tokenObject.token;

            const result = await userService.getCurrentUser(
                token,
                req.params.username
            );
            if (!result) {
                throw new Error("Unauthorized");
            }
            res.status(200).json(result);
        }
    } catch (error) {
        res.status(404).json({
            errors: error.message,
        });
    }
};

module.exports = { createUser, getCurrentUser };
