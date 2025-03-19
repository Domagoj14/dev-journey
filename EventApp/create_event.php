<?php
session_start();
include('db_connect.php');

// Check if the user is an admin
if (!isset($_SESSION['user_id']) || $_SESSION['username'] !== 'admin') {
    header('Location: login.php');
    exit();
}

// Variable to store success/error message
$message = '';

if (isset($_POST['submit'])) {
    $title = mysqli_real_escape_string($conn, $_POST['title']);
    $description = mysqli_real_escape_string($conn, $_POST['description']);
    $date = mysqli_real_escape_string($conn, $_POST['date']);
    $city = mysqli_real_escape_string($conn, $_POST['city']);

    // Handle image upload
    if (isset($_FILES['image']) && $_FILES['image']['error'] == 0) {
        $image_name = $_FILES['image']['name'];
        $image_tmp_name = $_FILES['image']['tmp_name'];
        $image_path = 'uploads/' . basename($image_name);

        // Move uploaded image to the uploads folder
        if (move_uploaded_file($image_tmp_name, $image_path)) {
            // Save event with image path to the database
            $sql = "INSERT INTO events (title, description, date, city, image, interested_count) VALUES ('$title', '$description', '$date', '$city', '$image_path', 0)";
        } else {
            $message = "Pogreška u učitavanju fotografije.";
        }
    } else {
        // Save event without image if no image uploaded
        $sql = "INSERT INTO events (title, description, date, city, interested_count) VALUES ('$title', '$description', '$date', '$city', 0)";
    }

    if (mysqli_query($conn, $sql)) {
        $message = "Događaj kreiran uspješno!";
    } else {
        $message = "Error: " . mysqli_error($conn);
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kreiraj događaj</title>
    <link rel="stylesheet" href="create_event.css">
    <style>
        .message {
            margin-top: 20px;
            text-align: center;
            color: #fff;
            background-color: #4CAF50; /* Green background for success */
            padding: 10px;
            border-radius: 5px;
        }

        .error {
            background-color: #f44336; /* Red background for errors */
        }
    </style>
</head>
<body>
    <div class="container">
        <form action="create_event.php" method="POST" enctype="multipart/form-data">
            <h1>Kreiraj novi događaj</h1>

            <label for="title">Naslov događaja:</label>
            <input type="text" name="title" required><br>

            <label for="description">Opis događaja:</label>
            <textarea name="description" required></textarea><br>

            <label for="date">Datum:</label>
            <input type="date" name="date" required><br>

            <label for="city">Grad:</label>
            <input type="text" name="city" required><br>

            <label for="image">Fotografija:</label>
            <input type="file" name="image" accept="image/*"><br>

            <button type="submit" name="submit">Kreiraj događaj</button>
            <br><a href="events.php">Pogledaj sve događaje</a>
        </form>

        <!-- Display the message (success or error) here -->
        <?php if (!empty($message)) { ?>
            <div class="message <?php echo strpos($message, 'Error') !== false ? 'error' : ''; ?>">
                <?php echo $message; ?>
            </div>
        <?php } ?>
    </div>
</body>
</html>