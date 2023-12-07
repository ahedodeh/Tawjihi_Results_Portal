<?php
// Database configuration
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "network";

try {
    // Create a new PDO instance
    $conn = new PDO("mysql:host=$servername;dbname=$dbname", $username, $password);

    // Set the PDO error mode to exception
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // SQL query to fetch the highest 3 grades along with names and cities
    $sql = "SELECT name, grade, city FROM students ORDER BY grade DESC LIMIT 3";

    // Prepare the SQL statement
    $stmt = $conn->prepare($sql);

    // Execute the query
    $stmt->execute();

    // Fetch the results as an associative array
    $results = $stmt->fetchAll(PDO::FETCH_ASSOC);

    // Check if there are any results
    if (count($results) > 0) {
        // Output the data as a JSON array
        echo json_encode($results);
    } else {
        echo "No data found";
    }
} catch (PDOException $e) {
    echo "Connection failed: " . $e->getMessage();
}
?>
