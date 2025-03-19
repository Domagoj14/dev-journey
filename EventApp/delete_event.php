<?php
session_start();
include('db_connect.php');

// Only allow admin users to delete events
if (isset($_SESSION['username']) && $_SESSION['username'] === 'admin') {
    if (isset($_POST['event_id'])) {
        $event_id = $_POST['event_id'];

        // Delete event from the database
        $sql = "DELETE FROM events WHERE id = '$event_id'";
        if (mysqli_query($conn, $sql)) {
            // Also, delete related entries from interested_users
            $delete_interested_sql = "DELETE FROM interested_users WHERE event_id = '$event_id'";
            mysqli_query($conn, $delete_interested_sql);

            echo json_encode(['success' => true]);
        } else {
            echo json_encode(['success' => false, 'message' => 'Error deleting event']);
        }
    }
} else {
    echo json_encode(['success' => false, 'message' => 'Unauthorized']);
}
?>