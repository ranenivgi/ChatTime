import { useRef } from "react";
import IncomingMessage from "./IncomingMessage";
import "./Message.css";
import OutgoingMessage from "./OutgoingMessage";

const DisplayMessages = ({
    currentUser,
    currentContact,
    currentChatMessages,
}) => {
    const scrollBarRef = useRef(null);
    // Sort the messages be date created time
    const sortedMessages = [...currentChatMessages].sort(
        (a, b) => new Date(a.created) - new Date(b.created)
    );

    return (
        <div
            ref={scrollBarRef}
            className="pt-3 pe-3"
            style={{ height: "60vh", overflow: "auto" }}>
            {sortedMessages.length > 0 ? (
                sortedMessages.map((message, index) => {
                    if (message.sender.username === currentUser.username) {
                        return (
                            <OutgoingMessage
                                key={message.id}
                                message={message}
                                currentUser={currentUser}
                                scrollBarRef={scrollBarRef}
                            />
                        );
                    } else {
                        return (
                            <IncomingMessage
                                key={message.id}
                                message={message}
                                currentContact={currentContact}
                            />
                        );
                    }
                })
            ) : (
                <></>
            )}
        </div>
    );
};

export default DisplayMessages;
