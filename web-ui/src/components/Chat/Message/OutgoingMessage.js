import "./Message.css";

const OutgoingMessage = ({ message, currentUser, scrollBarRef }) => {
    // Scroll down with the last message sent
    if (scrollBarRef.current) {
        requestAnimationFrame(() => {
            scrollBarRef.current.scrollTop = scrollBarRef.current.scrollHeight;
        });
    }
    // Format the message time
    const date = new Date(message.created);
    const options = {
        hour: "2-digit",
        minute: "2-digit",
    };
    const formattedTime = date.toLocaleTimeString([], options).toUpperCase();

    return (
        <div className="d-flex flex-row justify-content-start">
            <img
                src={currentUser.profilePic}
                alt="profile"
                className="rounded-circle chat-img"
                style={{ width: "40px", height: "40px" }}
            />
            <div>
                <p className="small p-2 ms-3 mb-1 rounded-3 send">
                    {message.content}
                </p>
                <p className="small ms-3 mb-3 rounded-3 text-muted float-end">
                    {formattedTime}
                </p>
            </div>
        </div>
    );
};

export default OutgoingMessage;
