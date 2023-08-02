const tokenService = require("../services/token");
const user = require("../services/user");
const dataMap = require("../tokenMap");

const generateToken = async (req, res) => {
    const { username, password } = req.body;

    try {
        // Perform authentication logic here to verify the username and password
        const isValid = await user.getUser(username, password);

        if (!isValid) {
            return res.status(404).json({ errors: ["User not exists"] });
        }

        if (req.body.hasOwnProperty("deviceToken")) {
            const deviceToken = req.body.firebaseToken;
            const userName = req.body.username;
            // Adding a new key-value pair to the map
            dataMap.set(userName, deviceToken);
        }

        // Generate a token using the token service
        const token = tokenService.generateToken(username);

        // Return the token in the response
        res.status(200).json({ token });
    } catch (error) {
        return res
            .status(500)
            .json({ errors: error.message });
    }
};

function index(req, res) {
    res.json({ data: "secret data" });
}

module.exports = { generateToken, index };
