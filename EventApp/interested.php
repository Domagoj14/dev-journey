<?php
session_start();
include('db_connect.php');

if (isset($_SESSION['user_id']) && isset($_POST['event_id']) && isset($_POST['action'])) {
    $user_id = $_SESSION['user_id'];
    $event_id = $_POST['event_id'];
    $action = $_POST['action'];

    if ($action === 'add') {
        // Check if the user is already interested in the event
        $check_sql = "SELECT * FROM interested_users WHERE event_id = '$event_id' AND user_id = '$user_id'";
        $check_result = mysqli_query($conn, $check_sql);

        if (mysqli_num_rows($check_result) == 0) {
            // Insert into interested_users
            $insert_sql = "INSERT INTO interested_users (event_id, user_id) VALUES ('$event_id', '$user_id')";
            mysqli_query($conn, $insert_sql);

            // Increment the interested count for the event
            $update_sql = "UPDATE events SET interested_count = interested_count + 1 WHERE id = '$event_id'";
            mysqli_query($conn, $update_sql);
        }
    } elseif ($action === 'remove') {
        // Remove from interested_users
        $delete_sql = "DELETE FROM interested_users WHERE event_id = '$event_id' AND user_id = '$user_id'";
        mysqli_query($conn, $delete_sql);

        // Decrement the interested count for the event
        $update_sql = "UPDATE events SET interested_count = interested_count - 1 WHERE id = '$event_id'";
        mysqli_query($conn, $update_sql);
    }

    // Get the updated interested count
    $new_count_sql = "SELECT interested_count FROM events WHERE id = '$event_id'";
    $new_count_result = mysqli_query($conn, $new_count_sql);
    $new_count_row = mysqli_fetch_assoc($new_count_result);

    // Get the updated list of users interested
    $users_sql = "SELECT users.username FROM interested_users 
                  JOIN users ON interested_users.user_id = users.id 
                  WHERE interested_users.event_id = '$event_id'";
    $users_result = mysqli_query($conn, $users_sql);

    $usernames = [];
    while ($user_row = mysqli_fetch_assoc($users_result)) {
        $usernames[] = $user_row['username'];
    }
    $updated_users_list = implode('<br>', $usernames); // Generate HTML list of usernames

    // Return a success response with the new interested count and updated list of users
    echo json_encode([
        'success' => true,
        'new_count' => $new_count_row['interested_count'],
        'updated_users' => $updated_users_list
    ]);
} else {
    echo json_encode(['success' => false]);
}
?>