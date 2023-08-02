import "./TopBarInfo.css";
import AddContact from "./AddContact";

function UserInfo({ currentUser, setContacts, token, socket }) {
    return (
        <div className=" mb-4 mb-md-0 d-flex flex-row align-items-center user-info">
            <div className="justify-content-start">
                <img
                    src={currentUser.profilePic}
                    alt="pic"
                    className="d-flex align-self-center me-3 rounded-circle"
                    style={{ width: "50px", height: "50px" }}></img>
            </div>

            <div className="flex-grow-1">
                <div className="d-flex justify-content-between align-items-center">
                    <div>{currentUser.displayName}</div>
                    <AddContact
                        setContacts={setContacts}
                        token={token}
                        socket={socket}
                    />
                </div>
            </div>
        </div>
    );
}

export default UserInfo;
