<?php
include('db_connect.php'); 

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $username = $_POST['username'];
    $email = $_POST['email'];
    $password = password_hash($_POST['password'], PASSWORD_BCRYPT);

    $sql = "INSERT INTO users (username, email, password) VALUES ('$username', '$email', '$password')";
    if (mysqli_query($conn, $sql)) {
        echo "Registration successful!";
    } else {
        echo "Error: " . mysqli_error($conn);
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registracija</title>
    <link rel="stylesheet" href="registration_login.css">
</head>
<body>
    <form method="POST" action="">
        <h1>Registracija</h1>
        <label for="username">Korisničko ime:</label>
        <input type="text" name="username" required><br>
        
        <label for="email">Email:</label>
        <input type="email" name="email" required><br>
        
        <label for="password">Lozinka:</label>
        <input type="password" name="password" required><br>
        
        <button type="submit">Registracija</button>
    </form>
    <footer class="footer">
        <p>&copy; <?php echo date('Y'); ?> EventApp. All rights reserved.</p>
        <p><a href="privacy_policy.php">Privacy Policy</a> | <a href="terms_of_service.php">Terms of Service</a></p>
    </footer>
</body>
</html>