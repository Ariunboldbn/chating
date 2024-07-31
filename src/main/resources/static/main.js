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
var imageInput = document.querySelector('#imageInput');
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

function handler(event) { 
    registryForm.classList.add("hidden");
    messageForm.classList.remove('hidden');
    event.preventDefault();
}

function send(event) {
    event.preventDefault();

    var messageContent = messageInput.value.trim();
    var userName = userNameInput.value.trim();
    var image = imageInput.files[0];
    var input = fileInput.files[0];
    
    if (!messageContent && !image && !input) {
        return;
    }

    var chatMessage = {
        content: messageContent,
        sender: userName
    };

    var filesToProcess = 0;
    var processedFiles = 0;

    function onFileProcessed() {
        processedFiles++;
        if (processedFiles === filesToProcess) {
            stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
            fileInput.value = ''; 
            imageInput.value = ''; 
            messageInput.value = '';
        }
    }

    if (input) {
        filesToProcess++;
        var formData = new FormData();
        formData.append("file", input);

        fetch("/upload", {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.ok) {
                chatMessage.fileContent = './uploads/' + input.name;
                chatMessage.fileName = input.name;
                onFileProcessed();
            } else {
                console.error("File upload failed.");
            }
        });
    }

    if (image) {
        filesToProcess++;
        var formData = new FormData();
        formData.append("file", image);

        fetch("/upload", {
            method: "POST",
            body: formData
        }).then(response => {
            if (response.ok) {
                chatMessage.imageUrl = './uploads/' + image.name;
                chatMessage.imageName = image.name;
                onFileProcessed();
            } else {
                console.error("Image upload failed.");
            }
        });
    }

    if (filesToProcess === 0) {
        stompClient.send("/app/chat.send", {}, JSON.stringify(chatMessage));
        fileInput.value = ''; 
        imageInput.value = ''; 
        messageInput.value = '';
    }
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

    if (message.imageUrl) {
        var image = document.createElement('img');
        image.src = message.imageUrl;
        image.style.maxWidth = '100px';
        container.appendChild(image);
    }

    if(message.fileContent) {
        var content = document.createElement('a');
        content.href = message.fileContent;
        content.download = message.fileName;
        content.textContent = message.fileName;
        content.style.display = 'block';
        container.appendChild(content)   
    }
    

    container.appendChild(userElement);
    container.appendChild(textElement);
    container.classList.add('flex');
    messageArea.appendChild(container);
}



loginButton.addEventListener('click', handler)
messageForm.addEventListener('submit', send, true)
window.onload = connect;