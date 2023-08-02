import "./Chat.css";
import "../background.css";
import "./logo-for-chat.css";
import logo from ".././background-pictures/logo-with-name.svg";
import DisplayMessages from "./Message/DisplayMessages";
import { useState, useRef, useEffect } from "react";
import ContactsList from "./Contacts/ContactsList";
import Userinfo from "./TopBar/UserInfo";
import SearchContact from "./Contacts/SearchContact";
import ContactInfo from "./TopBar/ContactInfo";
import SendMessage from "./Message/SendMessage";
import { getMessages, updateContactList } from "../GetRequests";
import io from "socket.io-client";

const Chat = ({
    currentUser,
    contacts,
    setContacts,
    setCurrentUser,
    token,
}) => {
    const [selectedChat, setSelectedChat] = useState(null);
    const [currentContact, setCurrentContact] = useState({
        user: {
            username: null,
            displayName: null,
            profilePic: null,
        },
        id: null,
        lastMessage: null,
    });
    const [currentChatMessages, setCurrentChatMessages] = useState([]);
    const [searchQuery, setSearchQuery] = useState("");

    const socket = useRef(null);

    // Connect new user to the socket io and update its listeners
    useEffect(() => {
        const fetchData = async () => {
            socket.current = io.connect(process.env.REACT_APP_BASE_URL);
            socket.current.emit("join_chat", currentUser.username);
            socket.current.on("add-contact", async () => {
                await updateContactList(token, setContacts);
            });
        };

        fetchData();

        return () => {
            if (socket.current) {
                socket.current.disconnect();
            }
        };
    }, [currentUser]);

    useEffect(() => {
        const handleReceivedMessage = async (data) => {
            if (data.chatID === selectedChat) {
                await getMessages(data.chatID, token, setCurrentChatMessages);
            }
            await updateContactList(token, setContacts);
        };

        if (socket.current) {
            socket.current.on("receive_message", handleReceivedMessage);
        }

        return () => {
            if (socket.current) {
                socket.current.off("receive_message", handleReceivedMessage);
            }
        };
    }, [selectedChat]);


    return (
        <>
            <div id="logo">
                <img src={logo} alt="logo"></img>
            </div>
            <section id="screen-background">
                <div className="container py-5">
                    <div className="row block">
                        <div className="col-md-12">
                            <div
                                className="card"
                                id="chat"
                                style={{ borderRadius: "20px" }}>
                                <div>
                                    <div className="row block">
                                        <div
                                            className="col-md-6 col-lg-5 col-xl-4 mb-4 mb-md-0"
                                            style={{
                                                borderRadius: "20px !important",
                                            }}>
                                            <Userinfo
                                                currentUser={currentUser}
                                                setContacts={setContacts}
                                                token={token}
                                                socket={socket}
                                            />

                                            <div className="p-3">
                                                <SearchContact
                                                    setSearchQuery={
                                                        setSearchQuery
                                                    }
                                                />
                                                <ContactsList
                                                    contacts={contacts}
                                                    setCurrentContact={
                                                        setCurrentContact
                                                    }
                                                    setSelectedChat={
                                                        setSelectedChat
                                                    }
                                                    searchQuery={searchQuery}
                                                    setCurrentChatMessages={
                                                        setCurrentChatMessages
                                                    }
                                                    token={token}
                                                />
                                            </div>
                                        </div>

                                        <div
                                            className="col-md-6 col-lg-7 col-xl-8 bac"
                                            style={{
                                                borderTopRightRadius: "20px",
                                                borderBottomRightRadius: "20px",
                                            }}>
                                            <ContactInfo
                                                currentContact={currentContact}
                                                setContacts={setContacts}
                                                setCurrentUser={setCurrentUser}
                                            />
                                            <DisplayMessages
                                                currentUser={currentUser}
                                                currentContact={currentContact}
                                                currentChatMessages={
                                                    currentChatMessages
                                                }
                                            />

                                            <SendMessage
                                                selectedChat={selectedChat}
                                                token={token}
                                                setCurrentChatMessages={
                                                    setCurrentChatMessages
                                                }
                                                setContacts={setContacts}
                                                currentContact={currentContact}
                                                socket={socket}
                                            />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
};

export default Chat;
