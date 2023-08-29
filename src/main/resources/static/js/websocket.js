let stompClient = null;

$(document).ready(function() {
  console.log("Page is loaded");
  connect();
  $("#send").click(function(event) {
  event.preventDefault();
    sendMessage();
  });

});


function connect() {
    const socket = new SockJS("/new-order");
    try {
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function(frame) {
            console.log("Connected " + frame);
            stompClient.subscribe("/topic/new-order", function(message) {
                const data = JSON.parse(message.body);
                appendNotification(data);
                 // Show the badge and add the shake animation class
                 showNotificationBadge();
                 addShakeAnimation();
                 showNotificationDot()
                document.getElementById("notificationSound").play();

                //showMessage(data);
            });
        });
    } catch (err) {
        console.error(err.message);
    }
}

function showMessage(message) {
    const row = $("#messages");
    row.append(`
     <tr>
         <td> ${message.orderId} </td>
         <td> ${message.targetURL} </td>
         <td> ${message.status} </td>
     </tr>
    `);


}

function sendMessage() {
  console.log("Sending message ...");
  stompClient.send("/ws/new-order", {}, JSON.stringify({"message" : $("#messageContent").val()}));
}

function appendNotification(data) {
    const listGroup = document.getElementById("notificationList");

    const listItem = document.createElement("a");
    listItem.classList.add("list-group-item", "list-group-item-action");
    listItem.href = data.targetURL; // Set the target URL from the data


    const titleDiv = document.createElement("div");
    titleDiv.classList.add("d-flex", "flex-column", "w-100", "mb-0");

    const title = document.createElement("h5");
    title.classList.add("mb-0");
    title.style.fontSize = "1rem";
    title.style.fontWeight = "600";
    title.style.textTransform = "uppercase";
    title.innerText = "Order ID: " + data?.orderId; // Replace with actual data

    titleDiv.appendChild(title);

    const message = document.createElement("p");
    message.classList.add("mb-1");
    message.innerText = "Message: " + data?.message; // Replace with actual data

    const author = document.createElement("small");
    author.innerText = "- " + data?.adminId; // Replace with actual data

    listItem.appendChild(titleDiv);
    listItem.appendChild(message);
    listItem.appendChild(author);

    listGroup.insertBefore(listItem, listGroup.firstChild); // Append at the beginning
}

// Function to add the shake animation class
function addShakeAnimation() {
    const badgeElement = document.getElementById("notificationBell");
    badgeElement.classList.add("shake");
}

// Function to remove the shake animation class
function removeShakeAnimation() {
    const badgeElement = document.getElementById("notificationBell");
    badgeElement.classList.remove("shake");
}

// Function to show the notification badge
function showNotificationBadge() {
    const badgeElement = document.getElementById("notificationBell");
    badgeElement.style.display = "block"; // Show the badge
}

// Function to hide the notification badge
function hideNotificationBadge() {
    const badgeElement = document.getElementById("notificationBell");
    badgeElement.style.display = "none"; // Hide the badge
}

// Function to show the notification dot
function showNotificationDot() {
    const dotElement = document.querySelector(".badge-dot");
    dotElement.style.display = "block"; // Show the dot
}


// Add an event listener to the modal close event
const notifModal = document.getElementById("closeNotif");
notifModal.addEventListener("click", function () {
    hideNotificationDot();
});

// Function to hide the notification dot
function hideNotificationDot() {
    const dotElement = document.querySelector(".badge-dot");
    dotElement.style.display = "none"; // Hide the dot
    console.log("Hooorrah")
}