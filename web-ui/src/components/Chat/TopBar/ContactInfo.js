import Logout from "./Logout";
import "./TopBarInfo.css";

function ContactInfo({ currentContact, setContacts, setCurrentUser }) {
    return (
        <div className="mb-4 mb-md-0 d-flex flex-row align-items-center current-chat-info">
            <div className="justify-content-start">
                {currentContact.id !== null ? (
                    <img
                        src={currentContact.user.profilePic}
                        className="d-flex align-self-center me-3 rounded-circle"
                        style={{ width: "50px", height: "50px" }}
                        alt=""></img>
                ) : (
                    <img className="d-flex align-self-center me-3" alt=""></img>
                )}
            </div>
            <div className="flex-grow-1">
                <div className="d-flex justify-content-between align-items-center">
                    <div>{currentContact.user.displayName}</div>
                    <Logout
                        setContacts={setContacts}
                        setCurrentUser={setCurrentUser}
                    />
                </div>
            </div>
        </div>
    );
}

export default ContactInfo;
