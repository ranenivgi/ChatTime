import SingleContact from "./SingleContact";

function ContactsList({
    contacts,
    setCurrentContact,
    setSelectedChat,
    searchQuery,
    token,
    setCurrentChatMessages,
}) {
    // Filter the contacts by last message time, following the search input
    const sortedContacts = [...contacts].sort((a, b) => {
        const dateA =
            a.lastMessage && a.lastMessage.created
                ? new Date(a.lastMessage.created)
                : null;
        const dateB =
            b.lastMessage && b.lastMessage.created
                ? new Date(b.lastMessage.created)
                : null;

        if (dateA && dateB) {
            return dateB - dateA;
        } else if (!dateA && dateB) {
            return 1;
        } else if (dateA && !dateB) {
            return -1;
        } else {
            return 0;
        }
    });

    const filteredContacts = sortedContacts.filter((currentContact) =>
        currentContact.user.displayName
            .toLowerCase()
            .includes(searchQuery.toLowerCase())
    );

    return (
        <div style={{ height: "60vh", overflow: "auto" }}>
            <ul className="list-unstyled mb-0">
                {filteredContacts.length > 0 ? (
                    filteredContacts.map((currentContact) => (
                        <SingleContact
                            key={currentContact.id}
                            currentContact={currentContact}
                            setSelectedChat={setSelectedChat}
                            setCurrentContact={setCurrentContact}
                            token={token}
                            setCurrentChatMessages={setCurrentChatMessages}
                        />
                    ))
                ) : (
                    <></>
                )}
            </ul>
        </div>
    );
}

export default ContactsList;
