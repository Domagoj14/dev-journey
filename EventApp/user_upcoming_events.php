<?php
session_start();
include('db_connect.php');

// Check if the user is logged in
if (!isset($_SESSION['user_id'])) {
    header('Location: login.php');
    exit();
}

$user_id = $_SESSION['user_id'];

// Fetch the user's interested events, sorted by date
$sql = "SELECT events.*
        FROM events
        JOIN interested_users ON events.id = interested_users.event_id
        WHERE interested_users.user_id = '$user_id'
        ORDER BY events.date ASC";
$result = mysqli_query($conn, $sql);
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tvoji nadolazeći događaji</title>
    <link rel="stylesheet" href="style.css"> <!-- Existing CSS file -->
    <link rel="stylesheet" href="user_upcoming_events.css"> <!-- New CSS file -->
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <!-- Navigation Bar -->
    <nav class="navbar">
        <a href="events.php">Naslovna</a>
        <ul class="auth">
            <?php if (isset($_SESSION['user_id'])) { ?>
                <li><a href="logout.php">Odjava (<?php echo $_SESSION['username']; ?>)</a></li>
            <?php } else { ?>
                <li><a href="login.php">Prijava</a></li>
            <?php } ?>
        </ul>
    </nav>

    <!-- Main Content -->
    <div class="container">
        <h1>Tvoji nadolazeći događaji</h1>
        <div class="events-container">
            <?php while ($event = mysqli_fetch_assoc($result)) { ?>
                <div class="event-item">
                    <?php if (!empty($event['image'])) { ?>
                        <img src="<?php echo $event['image']; ?>" alt="Event Image" class="event-image">
                    <?php } ?>
                    <h2><?php echo $event['title']; ?></h2>
                    <p>Datum: <?php echo $event['date']; ?></p>
                    <p><?php echo $event['description']; ?></p>
                    <p>Grad: <?php echo $event['city']; ?></p>
                </div>
            <?php } ?>
        </div>
    </div>
    <footer class="footer">
        <p>&copy; <?php echo date('Y'); ?> EventApp. All rights reserved.</p>
        <p><a href="privacy_policy.php">Privacy Policy</a> | <a href="terms_of_service.php">Terms of Service</a></p>
    </footer>
</body>
</html>