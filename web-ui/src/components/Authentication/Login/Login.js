import { useState } from "react";
import "./Login.css";
import "../auth_image.css";
import "../logo-for-auth.css";
import "../../background.css";
import logo from "../.././background-pictures/logo-with-name.svg";
import { Link, useNavigate } from "react-router-dom";
import { updateContactList, getUserByName } from "../../GetRequests";
import { getToken } from "../../PostRequests";

const Login = ({ setCurrentUser, setToken, setContacts }) => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();

    const handleUsernameChange = (event) => {
        const newUsername = event.target.value;
        setUsername(newUsername);

        // Check validation
        if (newUsername.length < 2) {
            document.getElementById("username-mistake").innerHTML =
                "*Username must be at least more than 2 characters";
        } else {
            document.getElementById("username-mistake").innerHTML = "";
        }
    };

    const handlePasswordChange = (event) => {
        const newPassword = event.target.value;
        setPassword(newPassword);

        // Check if password is at least 6 characters long
        if (newPassword.length < 6) {
            document.getElementById("password-mistake").innerHTML =
                "Password must be at least 6 characters";
        }
        // Check if the password contains one lowercase char
        else if (!/[a-z]/.test(newPassword)) {
            document.getElementById("password-mistake").innerHTML =
                "Password must contain at least one lowercase character";
        }
        // Check if the password contains at least one number
        else if (!/[0-9]/.test(newPassword)) {
            document.getElementById("password-mistake").innerHTML =
                "Password must contain at least one number";
        }
        // Check if the password contains at least one uppercase char
        else if (!/[A-Z]/.test(newPassword)) {
            document.getElementById("password-mistake").innerHTML =
                "Password must contain at least one uppercase character";
        }
        // Otherwise, clear the text
        else {
            document.getElementById("password-mistake").innerHTML = "";
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        // Get the user token
        const token = await getToken(username, password);
        if (token === false) {
            document.getElementById("login-mistake").innerHTML =
                "*Invalid username or password. Please try again";
            return 0;
        } else {
            setToken(token);
            const user = await getUserByName(username, token);
            if (user === false) {
                document.getElementById("login-mistake").innerHTML =
                    "Error, please try again";
                return 0;
            }
            setCurrentUser(user);
            updateContactList(token, setContacts);
            navigate("/");
        }
    };

    return (
        <>
            <div id="logo">
                <img src={logo} alt="logo"></img>
            </div>
            <div className="trapezoid-yellow"></div>
            <div className="trapezoid-blue"></div>
            <div className="img-container">
                <div className="container-fluid">
                    <div className="row main-content text-center">
                        <div className="col-md-8 col-xs-12 col-sm-12 login_form">
                            <div className="container-fluid">
                                <div className="row">
                                    <h2>Log In</h2>
                                </div>
                                <div className="row">
                                    <form
                                        control=""
                                        className="form-group"
                                        onSubmit={handleSubmit}>
                                        <div id="login-mistake"></div>
                                        <div className="row input-container">
                                            <span className="person-key-icons">
                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    width="16"
                                                    height="16"
                                                    fill="currentColor"
                                                    className="bi bi-person"
                                                    viewBox="0 0 16 16">
                                                    <path d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6Zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0Zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4Zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10Z" />
                                                </svg>
                                            </span>
                                            <input
                                                type="text"
                                                name="username"
                                                id="login-username"
                                                className="form__input"
                                                placeholder="Username"
                                                onChange={handleUsernameChange}
                                                maxLength="20"></input>
                                        </div>
                                        <div
                                            className="error-message"
                                            id="username-mistake"></div>
                                        <div className="row input-container">
                                            <span className="person-key-icons">
                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    width="16"
                                                    height="16"
                                                    fill="currentColor"
                                                    className="bi bi-key"
                                                    viewBox="0 0 16 16">
                                                    <path d="M0 8a4 4 0 0 1 7.465-2H14a.5.5 0 0 1 .354.146l1.5 1.5a.5.5 0 0 1 0 .708l-1.5 1.5a.5.5 0 0 1-.708 0L13 9.207l-.646.647a.5.5 0 0 1-.708 0L11 9.207l-.646.647a.5.5 0 0 1-.708 0L9 9.207l-.646.647A.5.5 0 0 1 8 10h-.535A4 4 0 0 1 0 8zm4-3a3 3 0 1 0 2.712 4.285A.5.5 0 0 1 7.163 9h.63l.853-.854a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.646-.647a.5.5 0 0 1 .708 0l.646.647.793-.793-1-1h-6.63a.5.5 0 0 1-.451-.285A3 3 0 0 0 4 5z" />
                                                    <path d="M4 8a1 1 0 1 1-2 0 1 1 0 0 1 2 0z" />
                                                </svg>
                                            </span>
                                            <input
                                                type="password"
                                                name="password"
                                                id="password"
                                                className="form__input"
                                                placeholder="Password"
                                                onChange={handlePasswordChange}
                                                maxLength="20"></input>

                                            {/* <span className="eye-icon">
                                                <svg
                                                    xmlns="http://www.w3.org/2000/svg"
                                                    width="16"
                                                    height="16"
                                                    fill="currentColor"
                                                    className="bi bi-eye"
                                                    viewBox="0 0 16 16">
                                                    <path d="M16 8s-3-5.5-8-5.5S0 8 0 8s3 5.5 8 5.5S16 8 16 8zM1.173 8a13.133 13.133 0 0 1 1.66-2.043C4.12 4.668 5.88 3.5 8 3.5c2.12 0 3.879 1.168 5.168 2.457A13.133 13.133 0 0 1 14.828 8c-.058.087-.122.183-.195.288-.335.48-.83 1.12-1.465 1.755C11.879 11.332 10.119 12.5 8 12.5c-2.12 0-3.879-1.168-5.168-2.457A13.134 13.134 0 0 1 1.172 8z" />
                                                    <path d="M8 5.5a2.5 2.5 0 1 0 0 5 2.5 2.5 0 0 0 0-5zM4.5 8a3.5 3.5 0 1 1 7 0 3.5 3.5 0 0 1-7 0z" />
                                                </svg>
                                            </span> */}
                                        </div>
                                        <div
                                            className="error-message"
                                            id="password-mistake"></div>

                                        <div
                                            className="row"
                                            style={{ marginTop: "1.5em" }}>
                                            <input
                                                type="checkbox"
                                                name="remember_me"
                                                id="remember_me"
                                                className=""></input>
                                            <label htmlFor="remember_me">
                                                Remember Me!
                                            </label>
                                        </div>
                                        <div className="row">
                                            <input
                                                type="submit"
                                                value="Login"
                                                className="btn login-btn"></input>
                                        </div>
                                    </form>
                                </div>
                                <div className="row">
                                    <p>
                                        Don't have an account?{" "}
                                        <Link to={"/register"}>
                                            Register Here
                                        </Link>
                                    </p>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="platform-icons">
                    <a href="#">
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            width="32"
                            height="32"
                            fill="currentColor"
                            className="bi bi-android2"
                            viewBox="0 0 16 16"
                            color="white">
                            <path d="m10.213 1.471.691-1.26c.046-.083.03-.147-.048-.192-.085-.038-.15-.019-.195.058l-.7 1.27A4.832 4.832 0 0 0 8.005.941c-.688 0-1.34.135-1.956.404l-.7-1.27C5.303 0 5.239-.018 5.154.02c-.078.046-.094.11-.049.193l.691 1.259a4.25 4.25 0 0 0-1.673 1.476A3.697 3.697 0 0 0 3.5 5.02h9c0-.75-.208-1.44-.623-2.072a4.266 4.266 0 0 0-1.664-1.476ZM6.22 3.303a.367.367 0 0 1-.267.11.35.35 0 0 1-.263-.11.366.366 0 0 1-.107-.264.37.37 0 0 1 .107-.265.351.351 0 0 1 .263-.11c.103 0 .193.037.267.11a.36.36 0 0 1 .112.265.36.36 0 0 1-.112.264Zm4.101 0a.351.351 0 0 1-.262.11.366.366 0 0 1-.268-.11.358.358 0 0 1-.112-.264c0-.103.037-.191.112-.265a.367.367 0 0 1 .268-.11c.104 0 .19.037.262.11a.367.367 0 0 1 .107.265c0 .102-.035.19-.107.264ZM3.5 11.77c0 .294.104.544.311.75.208.204.46.307.76.307h.758l.01 2.182c0 .276.097.51.292.703a.961.961 0 0 0 .7.288.973.973 0 0 0 .71-.288.95.95 0 0 0 .292-.703v-2.182h1.343v2.182c0 .276.097.51.292.703a.972.972 0 0 0 .71.288.973.973 0 0 0 .71-.288.95.95 0 0 0 .292-.703v-2.182h.76c.291 0 .54-.103.749-.308.207-.205.311-.455.311-.75V5.365h-9v6.404Zm10.495-6.587a.983.983 0 0 0-.702.278.91.91 0 0 0-.293.685v4.063c0 .271.098.501.293.69a.97.97 0 0 0 .702.284c.28 0 .517-.095.712-.284a.924.924 0 0 0 .293-.69V6.146a.91.91 0 0 0-.293-.685.995.995 0 0 0-.712-.278Zm-12.702.283a.985.985 0 0 1 .712-.283c.273 0 .507.094.702.283a.913.913 0 0 1 .293.68v4.063a.932.932 0 0 1-.288.69.97.97 0 0 1-.707.284.986.986 0 0 1-.712-.284.924.924 0 0 1-.293-.69V6.146c0-.264.098-.491.293-.68Z" />
                        </svg>
                    </a>

                    <a href="#">
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            color="white"
                            width="32"
                            height="32"
                            fill="currentColor"
                            className="bi bi-windows"
                            viewBox="0 0 16 16">
                            <path d="M6.555 1.375 0 2.237v5.45h6.555V1.375zM0 13.795l6.555.933V8.313H0v5.482zm7.278-5.4.026 6.378L16 16V8.395H7.278zM16 0 7.33 1.244v6.414H16V0z" />
                        </svg>
                    </a>
                    <a href="#">
                        <svg
                            xmlns="http://www.w3.org/2000/svg"
                            color="white"
                            width="32"
                            height="32"
                            fill="currentColor"
                            className="bi bi-globe"
                            viewBox="0 0 16 16">
                            <path d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm7.5-6.923c-.67.204-1.335.82-1.887 1.855A7.97 7.97 0 0 0 5.145 4H7.5V1.077zM4.09 4a9.267 9.267 0 0 1 .64-1.539 6.7 6.7 0 0 1 .597-.933A7.025 7.025 0 0 0 2.255 4H4.09zm-.582 3.5c.03-.877.138-1.718.312-2.5H1.674a6.958 6.958 0 0 0-.656 2.5h2.49zM4.847 5a12.5 12.5 0 0 0-.338 2.5H7.5V5H4.847zM8.5 5v2.5h2.99a12.495 12.495 0 0 0-.337-2.5H8.5zM4.51 8.5a12.5 12.5 0 0 0 .337 2.5H7.5V8.5H4.51zm3.99 0V11h2.653c.187-.765.306-1.608.338-2.5H8.5zM5.145 12c.138.386.295.744.468 1.068.552 1.035 1.218 1.65 1.887 1.855V12H5.145zm.182 2.472a6.696 6.696 0 0 1-.597-.933A9.268 9.268 0 0 1 4.09 12H2.255a7.024 7.024 0 0 0 3.072 2.472zM3.82 11a13.652 13.652 0 0 1-.312-2.5h-2.49c.062.89.291 1.733.656 2.5H3.82zm6.853 3.472A7.024 7.024 0 0 0 13.745 12H11.91a9.27 9.27 0 0 1-.64 1.539 6.688 6.688 0 0 1-.597.933zM8.5 12v2.923c.67-.204 1.335-.82 1.887-1.855.173-.324.33-.682.468-1.068H8.5zm3.68-1h2.146c.365-.767.594-1.61.656-2.5h-2.49a13.65 13.65 0 0 1-.312 2.5zm2.802-3.5a6.959 6.959 0 0 0-.656-2.5H12.18c.174.782.282 1.623.312 2.5h2.49zM11.27 2.461c.247.464.462.98.64 1.539h1.835a7.024 7.024 0 0 0-3.072-2.472c.218.284.418.598.597.933zM10.855 4a7.966 7.966 0 0 0-.468-1.068C9.835 1.897 9.17 1.282 8.5 1.077V4h2.355z" />
                        </svg>
                    </a>
                </div>
                <p id="available-on">Available on</p>
            </div>

            <div className="trapezoid"></div>
        </>
    );
};

export default Login;
