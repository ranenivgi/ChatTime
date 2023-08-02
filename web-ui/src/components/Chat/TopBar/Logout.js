import { useNavigate } from "react-router-dom";
import "./Logout.css";

function Logout({ setContacts, setCurrentUser }) {
    const navigate = useNavigate();

    // Reset what should be reset on logout
    const handleLogout = () => {
        setContacts([]);
        setCurrentUser(null);
        navigate("/login");
    };

    return (
        <div>
            <button
                id="logout-button"
                type="button"
                className="btn btn-danger"
                onClick={handleLogout}>
                Logout
            </button>
        </div>
    );
}

export default Logout;
