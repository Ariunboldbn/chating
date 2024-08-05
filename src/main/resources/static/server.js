const apiBaseUrl = 'http://localhost:8080/api';

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
            userId = await getLoggedInUserId();
        } else {
            alert(`Error: ${result.message}`);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
}

async function getLoggedInUserId() {
    try {
        const response = await fetch(`${apiBaseUrl}/users/me`);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const user = await response.json();
        console.log("User data:", user);

        if (user && user.id) {
            return user.id;
        } else {
            throw new Error('User ID not found in response');
        }
    } catch (error) {
        console.error('Error fetching user ID:', error);
        return null;
    }
}

async function createChatRoom(user2Id) {
    const user1Id = await getLoggedInUserId();
    console.log("here is id " + user1Id)
    try {
        const response = await fetch(`${apiBaseUrl}/chatrooms?user1Id=${encodeURIComponent(user1Id)}&user2Id=${encodeURIComponent(user2Id)}`, {
            method: 'POST'
        });

        const result = await response.json();
        if (response.ok) {
            alert('Chat room created!');
            loadChatRoom(result.id);
        } else {
            alert(`Error: ${result.message}`);
        }
    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred. Please try again.');
    }
}

async function loadChatRoom(chatRoomId) {
    try {
        const response = await fetch(`${apiBaseUrl}/messages/${chatRoomId}`);

        if (!response.ok) {
            throw new Error(`HTTP error! Status: ${response.status}`);
        }

        const text = await response.text();
        if (!text) {
            throw new Error('No data received');
        }

        let messages;
        try {
            messages = JSON.parse(text);
        } catch (jsonError) {
            throw new Error('Failed to parse JSON: ' + jsonError.message);
        }

        if (!Array.isArray(messages)) {
            throw new Error('Unexpected response format. Expected an array of messages.');
        }

        const messagesContainer = document.getElementById('messages');
        messagesContainer.innerHTML = '';

        messages.forEach(message => {
            const div = document.createElement('div');
            div.textContent = `${message.senderId}: ${message.content}`;
            messagesContainer.appendChild(div);
        });

    } catch (error) {
        console.error('Error:', error);
        alert('An error occurred while loading messages. Please try again.');
    }
}

let chatRoomId = null;
let userId = null;

document.addEventListener('DOMContentLoaded', (event) => {
    const ws = new WebSocket('ws://localhost:8080/chat');

    ws.onopen = function (event) {
        console.log('WebSocket connection opened');
    };

    ws.onmessage = function (event) {
        const message = JSON.parse(event.data);
        displayMessage(message);
    };

    ws.onclose = function (event) {
        console.log('WebSocket connection closed');
    };

    ws.onerror = function (error) {
        console.error('WebSocket error:', error);
    };

    document.getElementById('sendMessageBtn').addEventListener('click', function () {
        sendMessage();
    });

    document.getElementById('startChatBtn').addEventListener('click', function () {
        startChat();
    });

    async function startChat() {
        const user2Id = document.getElementById('user2').value;

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

    function sendMessage() {
        if (!chatRoomId) {
            console.error('Chat room not selected.');
            return;
        }

        const content = document.getElementById('messageInput').value;

        const message = {
            senderId: userId,
            content: content
        };

        ws.send(JSON.stringify(message));

        fetch(`${apiBaseUrl}/messages`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ chatRoomId: chatRoomId, senderId: userId, content: content })
        })
            .then(response => response.json())
            .then(() => {
                loadMessages(chatRoomId);
            })
            .catch(error => console.error('Error:', error));
    }

    async function loadMessages(chatRoomId) {
        try {
            const response = await fetch(`${apiBaseUrl}/messages/room/${chatRoomId}`);
            const messages = await response.json();

            const messagesContainer = document.getElementById('messages');
            messagesContainer.innerHTML = '';

            messages.forEach(msg => {
                const div = document.createElement('div');
                div.textContent = `${msg.senderId}: ${msg.content}`;
                messagesContainer.appendChild(div);
            });
        } catch (error) {
            console.error('Error:', error);
            alert('An error occurred. Please try again.');
        }
    }

    function displayMessage(message) {
        const messagesContainer = document.getElementById('messages');
        const div = document.createElement('div');
        div.textContent = `${message.senderId}: ${message.content}`;
        messagesContainer.appendChild(div);
    }
});