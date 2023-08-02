import "./SingleContact.css";
import { getMessages } from "../../GetRequests";

const SingleContact = ({
    currentContact,
    setSelectedChat,
    setCurrentContact,
    setCurrentChatMessages,
    token,
}) => {
    // Set the contact when opening the chat
    const openChat = async () => {
        setCurrentContact(currentContact);
        setSelectedChat(currentContact.id);
        await getMessages(currentContact.id, token, setCurrentChatMessages);
    };

    let lastMessageContent = "";

    // Check last message validation and cut it if its too long
    if (
        currentContact.lastMessage &&
        currentContact.lastMessage.content.length > 12
    ) {
        lastMessageContent =
            currentContact.lastMessage.content.slice(0, 12) + "...";
    } else if (currentContact.lastMessage) {
        lastMessageContent = currentContact.lastMessage.content;
    }

    let formattedTime = "";

    // Format the message time
    if (currentContact && currentContact.lastMessage) {
        const date = new Date(currentContact.lastMessage.created);
        const options = {
            hour: "2-digit",
            minute: "2-digit",
        };
        formattedTime = date.toLocaleTimeString([], options).toUpperCase();
    }

    return (
        <li className="p-2 border-bottom" onClick={openChat}>
            <a href="#!" className="d-flex justify-content-between">
                <div className="d-flex flex-row">
                    <div>
                        <img
                            src={currentContact.user.profilePic}
                            alt="avatar"
                            className="d-flex align-self-center me-3 rounded-circle contact-img"
                            style={{ width: "60px", height: "60px" }}></img>
                        <span className="badge bg-success badge-dot"></span>
                    </div>
                    <div className="pt-1">
                        <p className="fw-bold mb-0">
                            {currentContact.user.displayName}
                        </p>
                        {currentContact.lastMessage != null ? (
                            <p className="small text-muted">
                                {lastMessageContent}
                            </p>
                        ) : (
                            <p></p>
                        )}
                    </div>
                </div>
                <div className="pt-1">
                    {currentContact.lastMessage != null ? (
                        <p className="small text-muted mb-1">{formattedTime}</p>
                    ) : (
                        <p></p>
                    )}
                    {currentContact.unreadMessages > 0 ? (
                        <span className="badge bg-danger rounded-pill float-end">
                            {/* {currentContact.unreadMessages} */}
                        </span>
                    ) : (
                        <span></span>
                    )}
                </div>
            </a>
        </li>
    );
};

export default SingleContact;
