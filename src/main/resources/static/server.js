const apiBaseUrl = 'http://localhost:8080/api';
let chatRoomId = null;
let userId = null;
let ws;
let userMap = new Map();

async function login() {
    const username = document.getElementById('loginUsername').value;

    try {
        const response = await fetch(`${apiBaseUrl}/auth/login?username=${username}`, {
            method: 'POST'
        });

        const result = await response.json();
        if (response.ok) {
            console.log('Login successful!');
            document.getElementById('loginForm').reset();
            document.getElementById('loginForm').style.display = 'none';
            document.getElementById('chatContainer').style.display = 'block';
            userId = await getLoggedInUserId(username);
            initializeWebSocket();
            await populateUserDropdown();
        } else {
            alert(`Error: ${result.message}`);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
}

async function getLoggedInUserId(username) {
    try {
        const response = await fetch(`${apiBaseUrl}/users/me?username=${username}`);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const user = await response.json();
        console.log("User data:", user);

        if (user && user.id) {
            userMap.set(user.id, user.username);
            return user.id;
        } else {
            throw new Error('User ID not found in response');
        }
    } catch (error) {
        console.error('Error fetching user ID:', error);
        return null;
    }
}

async function populateUserDropdown() {
    try {
        const response = await fetch(`${apiBaseUrl}/users`);
        const users = await response.json();

        const userSelect = document.getElementById('userSelect');
        userSelect.innerHTML = '';

        users.forEach(user => {
            if (user.id !== userId) {
                const button = document.createElement('button');
                button.value = user.id;
                button.textContent = user.username;
                button.addEventListener('click', () => startChat(user.id));
                button.style = "width: 100px;"
                userSelect.appendChild(button);
                userMap.set(user.id, user.username);
            }
        });
    } catch (error) {
        console.error('Error fetching users:', error);
        alert('An error occurred while fetching users. Please try again.');
    }
}

async function startChat(user2Id) {
    document.getElementById('messageArea').style.display = 'block';
    try {
        const response = await fetch(`${apiBaseUrl}/chatrooms?user1Id=${userId}&user2Id=${user2Id}`, {
            method: 'POST'
        });

        const result = await response.json();
        if (response.ok) {
            chatRoomId = result.id;
            loadMessages(chatRoomId);
        } else {
            alert(`Error: ${result.message}`);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
}

async function loadMessages(chatRoomId) {
    try {
        const response = await fetch(`${apiBaseUrl}/messages/room/${chatRoomId}`);
        const messages = await response.json();

        const messagesContainer = document.getElementById('messages');
        messagesContainer.innerHTML = '';

        messages.forEach(msg => {
            displayMessage(msg);
        });
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
}

function displayMessage(message) {
    const messagesContainer = document.getElementById('messages');
    const div = document.createElement('div');
    const username = userMap.get(message.senderId) || 'Unknown';
    div.textContent = `${username}: ${message.content}`;
    messagesContainer.appendChild(div);
}

function initializeWebSocket() {
    ws = new WebSocket(`ws://localhost:8080/chat?userId=${userId}`);

    ws.onopen = function (event) {
        console.log('WebSocket connection opened');
    };

    ws.onmessage = function (event) {
        console.log('WebSocket message received', event.data);
        const message = JSON.parse(event.data);
        displayMessage(message);
    };

    ws.onclose = function (event) {
        console.log('WebSocket connection closed');
    };

    ws.onerror = function (error) {
        console.error('WebSocket error:', error);
    };
}

function sendMessage() {
    if (!chatRoomId) {
        console.error('Chat room not selected.');
        return;
    }

    const content = document.getElementById('messageInput').value;

    const message = {
        chatRoomId: chatRoomId,
        senderId: userId,
        content: content
    };

    ws.send(JSON.stringify(message));

    fetch(`${apiBaseUrl}/messages`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(message)
    })
        .then(response => response.json())
        .then(() => {
            loadMessages(chatRoomId);
        })
        .catch(error => console.error('Error:', error));

    document.getElementById('messageInput').value = "";
}


document.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('sendMessageBtn').addEventListener('click', sendMessage);
    document.getElementById('startChatBtn').addEventListener('click', startChat);
});
