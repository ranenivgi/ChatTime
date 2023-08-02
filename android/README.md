# Advanced Programming 2

# ChatTime - Exercise 3:

In this exercise we operated self designed android Chat App named <b>ChatTime</b>. <br />

The current app's milestone is presenting the `Login screen`, `Register screen` and `Chat Screen` pages, built as an android app. It enables a user to register to the app, login with the registered user and then start chatting with other contacts. In addition, it supports settings activity which provides editing the app's local settings.<br />

# Running
Make sure you have mongodb service opened!

To run follow these commands: <br />

-   Clone the repository: <br />

```shell
git clone https://github.com/ranenivgi/AP2_ex3.git
cd AP2_ex3
```

-   Install dependencies: <br />

```shell
cd server-api
npm i
```

-   Run the app: <br />

```shell
npm start
```

<br />

-   The android client is in the android-client folder: <br />

<br />

# About

## Registration Screen

The registration screen allows users to create a new account by entering their username, password, confirm the password, display name, and picture.<br>
You must follow these rules to register:

-   All input fields are limited with 20 characters.
-   Username and Display Name input fields must contain more than 2 characters.
-   Password input field must contain at least 6 characters, with at least one lowercase, uppercase and a number.
-   Confirm password input field must match the password input field.
-   You must upload an <b>image</b> using the upload button, which will be used as your profile image.

Screenshot:

![register-screen](https://github.com/ranenivgi/AP2_ex1B/assets/118674164/8d33b747-4a8a-4f1a-9e87-38128c92f440)

## Login Screen

The login screen enables registered users to enter the chat using their username and password. In addition, it allows to click a link to the registration screen for unregistered users.

Screenshot:

![login-screen](https://github.com/ranenivgi/AP2_ex1B/assets/118674164/663758e6-e3c7-427d-a78d-9413b28375a5)

## Chat Screen

The chat screen divided into two parts. The first part is the contacts column, where you can add contacts and start chatting with them, and the second part is the actual chat.

Screenshot:

![chat-screen](https://github.com/ranenivgi/AP2_ex1B/assets/118674164/0660f025-bb8d-41f2-9f7d-ad5532dd8ac6)

## Settings Screen
In the settings screen you are able to edit the app's theme, the server's ip and there is a logout option.
