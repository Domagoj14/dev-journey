<?php
session_start();
include('db_connect.php');

if ($_SERVER['REQUEST_METHOD'] == 'POST') {
    $email = $_POST['email'];
    $password = $_POST['password'];

    $sql = "SELECT * FROM users WHERE email = '$email'";
    $result = mysqli_query($conn, $sql);

    if ($result && mysqli_num_rows($result) > 0) {
        $user = mysqli_fetch_assoc($result);
        if (password_verify($password, $user['password'])) {
            $_SESSION['user_id'] = $user['id'];
            $_SESSION['username'] = $user['username'];
            echo "Login successful!";
            header('Location: events.php');
        } else {
            echo "Invalid password.";
        }
    } else {
        echo "No user found with that email.";
    }
}
?>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Prijava</title>
    <link rel="stylesheet" href="registration_login.css">
</head>
<body>
    <form method="POST" action="">
    <h1>Prijava</h1>
        <label for="email">Email:</label>
        <input type="email" name="email" required><br>
        
        <label for="password">Lozinka:</label>
        <input type="password" name="password" required><br>
        
        <button type="submit">Prijava</button>
    </form>
    <footer class="footer">
        <p>&copy; <?php echo date('Y'); ?> EventApp. All rights reserved.</p>
        <p><a href="privacy_policy.php">Privacy Policy</a> | <a href="terms_of_service.php">Terms of Service</a></p>
    </footer>
</body>
</html>