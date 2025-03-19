<?php
session_start();
include('db_connect.php');

// Check if a city is selected
$selected_city = isset($_GET['city']) ? mysqli_real_escape_string($conn, $_GET['city']) : null;

// Fetch events from the database, optionally filtered by city
if ($selected_city) {
    $sql = "SELECT * FROM events WHERE city = '$selected_city'";
} else {
    $sql = "SELECT * FROM events";
}
$result = mysqli_query($conn, $sql);

// Check if user is logged in and if they're an admin
$user_id = isset($_SESSION['user_id']) ? $_SESSION['user_id'] : null;
$is_admin = isset($_SESSION['username']) && $_SESSION['username'] === 'admin'; // Check if admin
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Događaji</title>
    
    <link rel="stylesheet" href="style.css"> <!-- Link to CSS file -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script> <!-- Add jQuery for AJAX -->
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <ul>
            <li>
                <a class="image-logo" href="events.php">
                    <img src="images/logo.PNG" alt="Logo" class="navbar-logo">
                </a>
            </li>
            <?php if ($is_admin) { ?>
                <li class="create-event"><a href="create_event.php">Kreiraj događaj</a></li>
            <?php } ?>
            
            <!-- Dropdown for City Selection -->
            <li class="dropdown">
                <a href="#" class="dropbtn">▼ Filtriraj po gradu</a>
                <div class="dropdown-content">
                    <a href="events.php">Svi gradovi</a>
                    <a href="events.php?city=Zagreb">Zagreb</a>
                    <a href="events.php?city=Osijek">Osijek</a>
                    <a href="events.php?city=Split">Split</a>
                    <a href="events.php?city=Rijeka">Rijeka</a>
                    <a href="events.php?city=Zadar">Zadar</a>
                    <a href="events.php?city=Dubrovnik">Dubrovnik</a>
                    <a href="events.php?city=Vukovar">Vukovar</a>
                    <a href="events.php?city=Slavonski brod">Slavonski Brod</a>
                    <a href="events.php?city=Varaždin">Varaždin</a>
                </div>
            </li>
            <?php if (isset($_SESSION['user_id'])) { ?>
                <li class="upcoming-events"><a href="user_upcoming_events.php">Tvoji nadolazeći događaji</a></li>
            <?php } ?>
        </ul>
        
        <ul class="auth">
            <?php if (isset($_SESSION['user_id'])) { ?>
                <li><a href="logout.php">Odjava (<?php echo $_SESSION['username']; ?>)</a></li>
            <?php } else { ?>
                <li><a href="register.php">Registracija</a></li>
                <li><a href="login.php">Prijava</a></li>
            <?php } ?>
        </ul>
    </nav>

    <!-- Main Content -->
    <div class="container">
        <h1>Svi događaji <?php echo $selected_city ? "in $selected_city" : ""; ?></h1>
        <ul class="events-list">
            <?php while ($event = mysqli_fetch_assoc($result)) { ?>
                <li class="event-item" id="event-<?php echo $event['id']; ?>">

                    <?php if (!empty($event['image'])) { ?>
                        <img src="<?php echo $event['image']; ?>" alt="Event Image" class="event-image">
                    <?php } ?>
                    <h2><?php echo $event['title']; ?></h2>
                    <p><?php echo $event['description']; ?></p>
                    <p>Datum: <?php echo date('d/m/Y', strtotime($event['date'])); ?></p>
                    <p>Grad: <?php echo $event['city']; ?></p>
                    
                    <!-- Interested count with hover effect -->
                    <div class="tooltip" id="tooltip-<?php echo $event['id']; ?>">
                    <p><u>Zainteresiranih</u>: <span id="interested-count-<?php echo $event['id']; ?>"><?php echo $event['interested_count']; ?></span></p>
                    <?php if (isset($_SESSION['user_id'])) { ?>
                        <span class="tooltiptext">
                            <?php
                            // Display interested users
                            $event_id = $event['id'];
                            $interested_sql = "SELECT users.username FROM interested_users 
                                                JOIN users ON interested_users.user_id = users.id 
                                                WHERE interested_users.event_id = '$event_id'";
                            $interested_result = mysqli_query($conn, $interested_sql);

                            if (mysqli_num_rows($interested_result) > 0) {
                                while ($user = mysqli_fetch_assoc($interested_result)) {
                                    echo $user['username'] . "<br>";
                                }
                            } else {
                                echo "No users interested yet.";
                            }
                            ?>
                        </span>
                    <?php } else { ?>
                        <span class="tooltiptext">Prijavi se da vidiš tko je zainteresiran.</span>
                    <?php } ?>
                </div>

                    <!-- Check if user is already interested in this event -->
                    <?php
                    $user_interested = false;
                    if (isset($_SESSION['user_id'])) {
                        $check_interested_sql = "SELECT * FROM interested_users WHERE event_id = '$event_id' AND user_id = '$user_id'";
                        $check_interested_result = mysqli_query($conn, $check_interested_sql);
                        if (mysqli_num_rows($check_interested_result) > 0) {
                            $user_interested = true; // User already clicked "I'm Interested"
                        }
                    }
                    ?>

                    <!-- "I'm Interested" button -->
                    <form method="POST" action="interested.php">
                        <input type="hidden" name="event_id" value="<?php echo $event['id']; ?>">
                        <?php if (isset($_SESSION['user_id'])) { ?>
                            <button 
                                type="button" 
                                class="btn-interested"
                                style="background-color: <?php echo $user_interested ? '#4CAF50' : '#efc576'; ?>;"
                                id="interested-btn-<?php echo $event['id']; ?>"
                                onclick="<?php echo $user_interested ? 'confirmUnmarkInterest(' . $event['id'] . ')' : 'markInterested(' . $event['id'] . ')'; ?>">
                                <?php echo $user_interested ? 'Zainteresiran sam ✔' : "Zainteresiran/a?"; ?>
                            </button>
                        <?php } else { ?>
                            <button type="button" class="btn-interested" onclick="window.location.href='login.php';">Zainteresiran/a?</button>
                        <?php } ?>
                    </form>

                    <!-- Admin Delete Button (only visible to admin) -->
                    <?php if ($is_admin) { ?>
                        <button class="btn-delete" onclick="deleteEvent(<?php echo $event['id']; ?>)">Obriši događaj</button>
                    <?php } ?>
                </li>
            <?php } ?>
        </ul>
    </div>

        <!-- Footer -->
    <footer class="footer">
        <p>&copy; <?php echo date('Y'); ?> EventApp. All rights reserved.</p>
        <p><a href="privacy_policy.php">Privacy Policy</a> | <a href="terms_of_service.php">Terms of Service</a></p>
    </footer>

    <script>
        // Function to mark the user as interested using AJAX
        function markInterested(eventId) {
            $.ajax({
                url: 'interested.php',
                type: 'POST',
                data: {
                    event_id: eventId,
                    action: 'add'
                },
                success: function(response) {
                    var data = JSON.parse(response);
                    if (data.success) {
                        // Update interested count
                        $('#interested-count-' + eventId).text(data.new_count);

                        // Update interested button to reflect interest
                        $('#interested-btn-' + eventId)
                            .text("Zainteresiran si ✔")
                            .css('background-color', '#4CAF50')
                            .attr('onclick', 'confirmUnmarkInterest(' + eventId + ')');
                        
                        // Update tooltip with updated list of interested users
                        $('#tooltip-' + eventId + ' .tooltiptext').html(data.updated_users);
                    }
                }
            });
        }

        function confirmUnmarkInterest(eventId) {
            if (confirm("Are you sure you want to remove your interest?")) {
                $.ajax({
                    url: 'interested.php',
                    type: 'POST',
                    data: {
                        event_id: eventId,
                        action: 'remove'
                    },
                    success: function(response) {
                        var data = JSON.parse(response);
                        if (data.success) {
                            // Update interested count
                            $('#interested-count-' + eventId).text(data.new_count);

                            // Update interested button to reflect that the user is no longer interested
                            $('#interested-btn-' + eventId)
                                .text("Zainteresiran/a?")
                                .css('background-color', '#efc576')
                                .attr('onclick', 'markInterested(' + eventId + ')');
                            
                            // Update tooltip with updated list of interested users
                            $('#tooltip-' + eventId + ' .tooltiptext').html(data.updated_users);
                        }
                    }
                });
            }
        }

        // Function to delete event using AJAX
        function deleteEvent(eventId) {
            if (confirm("Are you sure you want to delete this event?")) {
                $.ajax({
                    url: 'delete_event.php',
                    type: 'POST',
                    data: { event_id: eventId },
                    success: function(response) {
                        var data = JSON.parse(response);
                        if (data.success) {
                            $('#event-' + eventId).remove(); // Remove the event from the list
                        } else {
                            alert("Error deleting event.");
                        }
                    }
                });
            }
        }
    </script>
</body>
</html>