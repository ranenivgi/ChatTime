export const getToken = async (username, password) => {
    const details = {
        username: username,
        password: password,
    };
    const response = await fetch(`${process.env.REACT_APP_API_URL}/Tokens`, {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(details),
    });

    if (!response.ok) {
        return false; // return false if the response is not okay
    }

    const data = await response.text();
    return data; // return the token
};

export const addUser = async (user) => {
    const details = {
        username: user.username,
        password: user.password,
        displayName: user.displayName,
        profilePic: user.profilePic,
    };
    const response = await fetch(`${process.env.REACT_APP_API_URL}/Users`, {
        method: "post",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify(details),
    });

    if (!response.ok) {
        return false;
    }
    return true;
};

export const addMessageToChat = async (message, token, selectedChat) => {
    const response = await fetch(
        `${process.env.REACT_APP_API_URL}/Chats/${selectedChat}/Messages`,
        {
            method: "post",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token,
            },
            body: JSON.stringify({ msg: message }),
        }
    );

    if (!response.ok) {
        return 0;
    }
};

export const addContact = async (contactName, token, setModalErrorMessage) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/Chats`, {
        method: "post",
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
        },
        body: JSON.stringify({ username: contactName }),
    });

    if (!response.ok) {
        setModalErrorMessage("*Wrong username");
        return false;
    }
    return true;
};
