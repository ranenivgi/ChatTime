export const getMessages = async (selectedChat, token, setCurrentChatMessages) => {
    const response = await fetch(
        `${process.env.REACT_APP_API_URL}/Chats/${selectedChat}/Messages`,
        {
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token,
            },
        }
    );

    if (!response.ok) {
        return 0;
    }

    const data = await response.json();
    setCurrentChatMessages(data);
    return data;
};

export const updateContactList = async (token, setContacts) => {
    const response = await fetch(`${process.env.REACT_APP_API_URL}/Chats`, {
        headers: {
            "Content-Type": "application/json",
            Authorization: "Bearer " + token,
        },
    });

    if (!response.ok) {
        return 0;
    }

    const data = await response.json();
    setContacts(data);
};

export const getUserByName = async (username, token) => {
    const response = await fetch(
        `${process.env.REACT_APP_API_URL}/Users/${username}`,
        {
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + token,
            },
        }
    );

    if (!response.ok) {
        return false; // return false if the response is not okay
    }

    const data = await response.json();
    return data; // return the token
};
