import { useRef, useState } from "react";
import "./AddContact.css";
import { updateContactList } from "../../GetRequests";
import { addContact } from "../../PostRequests";

function AddContact({ setContacts, token, socket }) {
    const newContactName = useRef("");
    const [inputValue, setInputValue] = useState("");
    const [showModal, setShowModal] = useState(false);
    const [modalErrorMessage, setModalErrorMessage] = useState("");

    // Create new contact and add it
    const createContact = async () => {
        const isValid = await addContact(
            newContactName.current.value,
            token,
            setModalErrorMessage
        );
        if (isValid === false) {
            return 0;
        }
        updateContactList(token, setContacts);

        // Add myself in the contact's contact list
        socket.current.emit("add-contact", newContactName.current.value);

        // Clear the input after adding new contact
        newContactName.current.value = "";
        // Clear the input flag
        setInputValue("");
        // Clear wrong modal username text
        setModalErrorMessage("");
        // Close the modal
        setShowModal(false);
    };

    // Set the flag to prevent adding invalid contact
    const handleInvalidContact = (event) => {
        setModalErrorMessage("");
        if (event.target.value.trim() !== "") {
            setInputValue(event.target.value);
        } else {
            setInputValue("");
        }
    };

    return (
        <div style={{ marginRight: "4%" }}>
            {/* Button trigger modal */}
            <button
                type="button"
                className="popup-icon"
                onClick={() => setShowModal(true)}>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    width="25"
                    height="25"
                    fill="currentColor"
                    className="bi bi-person-plus"
                    viewBox="0 0 16 16">
                    <path d="M6 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H1s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C9.516 10.68 8.289 10 6 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z" />
                    <path
                        fillRule="evenodd"
                        d="M13.5 5a.5.5 0 0 1 .5.5V7h1.5a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0V8h-1.5a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5z"
                    />
                </svg>
            </button>

            {/* Modal */}
            {showModal && (
                <div
                    className="modal fade show"
                    style={{ display: "block" }}
                    tabIndex="-1"
                    aria-labelledby="modalLabel"
                    aria-hidden="true">
                    <div className="modal-dialog">
                        <div className="modal-content">
                            <div className="modal-header">
                                <h1
                                    className="modal-title fs-5"
                                    id="modalLabel">
                                    Add Contact
                                </h1>
                                <button
                                    type="button"
                                    className="btn-close"
                                    aria-label="Close"
                                    onClick={() =>
                                        setShowModal(false)
                                    }></button>
                            </div>
                            <div className="modal-body">
                                <input
                                    ref={newContactName}
                                    className="input-group rounded mb-3 modal-input"
                                    type="text"
                                    placeholder="Contact's user name"
                                    maxLength="20"
                                    onChange={handleInvalidContact}
                                    onKeyDown={(event) => {
                                        if (event.key === "Enter") {
                                            createContact();
                                        }
                                    }}
                                />
                                <p id="wrong-modal-username">
                                    {modalErrorMessage}
                                </p>
                            </div>
                            <div className="modal-footer">
                                <button
                                    type="button"
                                    disabled={!inputValue}
                                    className="btn btn-primary"
                                    onClick={createContact}>
                                    Confirm
                                </button>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );
}
export default AddContact;
