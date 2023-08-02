import "./App.css";
import { useState } from "react";
import Register from "./components/Authentication/Register/Register";
import Login from "./components/Authentication/Login/Login";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import Chat from "./components/Chat/Chat";

function App() {
    const [contacts, setContacts] = useState([]);
    const [currentUser, setCurrentUser] = useState(null);
    const [token, setToken] = useState("");

    return (
        <Router>
            <Routes>
                <Route
                    path="/"
                    element={
                        // Check if user is registered or not, if it is send him to the chat page (a feature for next exercises)
                        currentUser ? (
                            <Chat
                                currentUser={currentUser}
                                contacts={contacts}
                                setContacts={setContacts}
                                setCurrentUser={setCurrentUser}
                                token={token}
                            />
                        ) : (
                            <Register />
                        )
                    }
                />
                <Route
                    path="/login"
                    element={
                        <Login
                            setCurrentUser={setCurrentUser}
                            setToken={setToken}
                            setContacts={setContacts}
                        />
                    }
                />
                <Route path="/register" element={<Register />} />
            </Routes>
        </Router>
    );
}

export default App;
