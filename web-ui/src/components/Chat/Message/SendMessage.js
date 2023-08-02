import "./SendMessage.css";
import { useRef } from "react";
import { getMessages, updateContactList } from "../../GetRequests";
import { addMessageToChat } from "../../PostRequests";

// Use getMessages in your components

function SendMessage({
    selectedChat,
    token,
    setCurrentChatMessages,
    setContacts,
    currentContact,
    socket
}) {
    const messageInput = useRef("");
    // Create new message object, add the content and send it
    const sendTextMessage = async () => {
        const message = messageInput.current.value.trim();
        if (message.length > 0 && selectedChat !== null) {
            await addMessageToChat(message, token, selectedChat);
            await getMessages(selectedChat, token, setCurrentChatMessages);
            await updateContactList(token, setContacts);

            const data = await {
                chatID: selectedChat,
                contactUsername: currentContact.user.username,
            };
            // Emit the message to the WebSocket server
            socket.current.emit("send_message", data);
        }
        messageInput.current.value = "";
    };

    return selectedChat !== null ? (
        <div className="text-muted d-flex justify-content-start align-items-center pe-3 pt-3 mt-2">
            <input
                type="text"
                ref={messageInput}
                className="form-control form-control-lg"
                placeholder="Type message"
                id="message-input"
                onKeyDown={(event) => {
                    if (event.key === "Enter") {
                        sendTextMessage();
                    }
                }}></input>
            <a className="ms-3" href="#!" onClick={sendTextMessage}>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="20"
                    height="20"
                    fill="currentColor"
                    className="bi bi-send"
                    viewBox="0 0 16 16">
                    <path d="M15.854.146a.5.5 0 0 1 .11.54l-5.819 14.547a.75.75 0 0 1-1.329.124l-3.178-4.995L.643 7.184a.75.75 0 0 1 .124-1.33L15.314.037a.5.5 0 0 1 .54.11ZM6.636 10.07l2.761 4.338L14.13 2.576 6.636 10.07Zm6.787-8.201L1.591 6.602l4.339 2.76 7.494-7.493Z" />
                </svg>
            </a>
        </div>
    ) : (
        <div></div>
    );
}

export default SendMessage;
