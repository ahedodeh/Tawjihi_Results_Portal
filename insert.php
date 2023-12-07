<?php
// Set up the database connection
$servername = "localhost";
$username = "root";
$password = "";
$dbname = "network";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Check if the request method is POST
if ($_SERVER["REQUEST_METHOD"] === "POST") {
    // Get the data from the Java client
    $name = $_POST["name"];
    $grade = $_POST["grade"];
    $city = $_POST["city"];

    // Insert data into the "students" table
    $sql = "INSERT INTO students (name, grade, city) VALUES ('$name', '$grade', '$city')";

    if ($conn->query($sql) === TRUE) {
        echo "Data inserted successfully";
    } else {
        echo "Error: " . $sql . "<br>" . $conn->error;
    }
}

// Close the database connection
$conn->close();
?>
