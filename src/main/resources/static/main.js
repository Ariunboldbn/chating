'use strict';

var chatPage = document.querySelector('#chat-page');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#messageInput');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var userNameInput = document.querySelector('#userNameInput');
var registryForm = document.querySelector('#registry');
var loginButton = document.querySelector('#loginButton');
var header = document.querySelector("#header");
var fileInput = document.querySelector('#fileInput');

var stompClient = null;

function connect() {
    var socket = new SockJS('/websocket');
    stompClient = Stomp.over(socket); 
    stompClient.connect({}, onConnected, onError);
}

function onConnected() {
    stompClient.subscribe('/topic/public', onMessageReceived);
}

function onError() {
    connectingElement.textContent = 'connection error';
}

function send(event) {
    event.preventDefault();

    var messageContent = messageInput.value.trim();
    var userName = userNameInput.value.trim();
    var file = fileInput.files[0];

    if (messageContent || file) {
        var chatMessage = {
            content: messageContent,
            sender: userName
        };

        if (file) {
            var reader = new FileReader();
            reader.onloadend = function () {
                chatMessage.fileContent = reader.result.split(',')[1];
                chatMessage.fileName = file.name;

                stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
                fileInput.value = ''; 
            };
            reader.readAsDataURL(file);
        } else {
            stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
        }

        messageInput.value = '';
    }
}


function handler(event) { 
    registryForm.classList.add("hidden");
    messageForm.classList.remove('hidden');
    event.preventDefault();
}

function onMessageReceived(payload) {
    var message = JSON.parse(payload.body);

    var container = document.createElement('div');
    var userElement = document.createElement('h4');
    var textElement = document.createElement('p');
    var userName = document.createTextNode(message.sender);

    userElement.appendChild(userName);

    if (message.content) {
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
    }

    if (message.fileContent) {
        var image = document.createElement('img');
        image.src = 'data:image/jpeg;base64,' + message.fileContent;
        image.style.maxWidth = '100px';
        container.appendChild(image);
    }

    container.appendChild(userElement);
    container.appendChild(textElement);
    container.classList.add('flex');
    messageArea.appendChild(container);
}


loginButton.addEventListener('click', handler)
messageForm.addEventListener('submit', send, true)
window.onload = connect;