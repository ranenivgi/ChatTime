const User = require("../models/user");
const jwt = require("jsonwebtoken");

const createUser = async (username, password, displayName, profilePic) => {
    try {
        const isExists = await getUserByUsername(username);
        if (isExists) {
            throw new Error("User exists");
        }
        const user = new User({
            username,
            password,
            displayName,
            profilePic,
        });
        //if (profilePic) userPassName.profilePic = profilePic;
        await user.save();

        return { username, displayName, profilePic };
    } catch (error) {
        throw new Error("User exists");
    }
};

const getUser = async (username, password) => {
    try {
        const user = await User.findOne({ username, password });
        if (!user) return null;
        return {
            username: user.username,
            displayName: user.displayName,
            profilePic: user.profilePic,
        };
    } catch (error) {
        throw new Error(
            "An error occurred while fetching user by username and password."
        );
    }
};

const getUserByUsername = async (username) => {
    try {
        const user = await User.findOne({ username });
        if (!user) return null;
        return {
            username: user.username,
            displayName: user.displayName,
            profilePic: user.profilePic,
        };
    } catch (error) {
        throw new Error("An error occurred while fetching user by username.");
    }
};

const getCurrentUser = async (token, username) => {
    try {
        const data = jwt.verify(token, process.env.JWT_SECRET);
        if (!data) {
            throw new Error("Invalid token");
        }
        if (data.username !== username) {
            throw new Error("Unauthorized");
        }
        const result = await getUserByUsername(data.username);
        if (!result) {
            throw new Error("Unauthorized");
        }
        return result;
    } catch (error) {
        throw error;
    }
};

module.exports = { createUser, getUser, getUserByUsername, getCurrentUser };
