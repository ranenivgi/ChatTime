import "./Message.css";

const IncomingMessage = ({ message, currentContact }) => {
    // Format the message time
    const date = new Date(message.created);
    const options = {
        hour: "2-digit",
        minute: "2-digit",
    };
    const formattedTime = date.toLocaleTimeString([], options).toUpperCase();

    return (
        <div key={message.id} className="d-flex flex-row justify-content-end">
            <div>
                <p className="small p-2 me-3 mb-1 text-white rounded-3 bg-primary receive">
                    {message.content}
                </p>
                <p className="small me-3 mb-3 rounded-3 text-muted">
                    {formattedTime}
                </p>
            </div>
            <img
                src={currentContact.user.profilePic}
                alt="profile"
                className="rounded-circle chat-img"
                style={{ width: "40px", height: "40px" }}
            />
        </div>
    );
};

export default IncomingMessage;
