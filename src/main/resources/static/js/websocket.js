let stompClient = null;
let userStompClient = null;
var $j = jQuery.noConflict();
$j(document).ready(function () {
    console.log("Page is loaded");
    connect();
    $j("#send").click(function (event) {
        event.preventDefault();
        sendMessage();
    });

    const notifModal = document.getElementById("closeNotif");
    notifModal.addEventListener("click", function () {
        hideNotificationDot();
    });

});


function connect() {
    const socket = new SockJS("/new-order");
    const userSocket = new SockJS("/notify-user")
    try {
        stompClient = Stomp.over(socket);
        userStompClient = Stomp.over(userSocket);
        stompClient.connect({}, function (frame) {
            console.log("Connected " + frame);
            stompClient.subscribe("/topic/new-order", function (message) {
                  displayNotification(message);

            });
        });
        userStompClient.connect({}, function (frame) {
                    console.log("User Socket Connected " + frame);
                    userStompClient.subscribe("/user/topic/notify-user", function (message) {
                       displayNotification(message);
                    });
                });
    } catch (err) {
        console.error(err.message);
    }
}

function displayNotification(message) {
    const data = JSON.parse(message.body);
    appendNotification(data);
    // Show the badge and add the shake animation class
    showNotificationBadge();
    addShakeAnimation();
    showNotificationDot()
    document.getElementById("notificationSound").play();

}

function showMessage(message) {
    const row = $j("#messages");
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
    stompClient.send("/ws/new-order", {}, JSON.stringify({"message": $j("#messageContent").val()}));
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
    message.innerText = data?.message; // Replace with actual data

    const author = document.createElement("small");
    let adminId = data?.adminId ? data.adminId : "";
    if (adminId.length !== 0) {
        author.innerText = "- " + adminId; // Replace with actual data
    }

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


// Function to show the notification badge
function showNotificationBadge() {
    const badgeElement = document.getElementById("notificationBell");
    badgeElement.style.display = "block"; // Show the badge
}

// Function to show the notification dot
function showNotificationDot() {
    const dotElement = document.querySelector(".badge-dot");
    dotElement.style.display = "block"; // Show the dot
}





// Function to hide the notification dot
function hideNotificationDot() {
    const dotElement = document.querySelector(".badge-dot");
    dotElement.style.display = "none"; // Hide the dot
    console.log("Hooorrah")
}