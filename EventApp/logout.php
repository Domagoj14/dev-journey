<?php
session_start();
session_destroy(); // Destroy the session
header('Location: events.php'); // Redirect to events page after logout
exit();