<?php
// Database configuration
$hostname = 'localhost';
$username = 'root';
$password = '';
$database = 'network';

try {
    // Create a new PDO instance
    $pdo = new PDO("mysql:host=$hostname;dbname=$database", $username, $password);

    // Set the PDO error mode to exception
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

    // Fetch the total number of students
    $totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students";
    $stmt = $pdo->query($totalStudentsQuery);
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $totalStudents = $result['total_students'];

    // Fetch the number of students who passed
    $passedStudentsQuery = "SELECT COUNT(*) AS passed_students FROM students WHERE grade >= 50";
    $stmt = $pdo->query($passedStudentsQuery);
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $passedStudents = $result['passed_students'];

    // Calculate the number of students who failed
    $failedStudents = $totalStudents - $passedStudents;

    // Calculate the pass and fail percentages
    $passPercentage = ($passedStudents / $totalStudents) * 100;
    $failPercentage = ($failedStudents / $totalStudents) * 100;

    // Create an associative array with the results
    $data = array(
        'total_students' => $totalStudents,
        'passed_students' => $passedStudents,
        'failed_students' => $failedStudents,
        'pass_percentage' => $passPercentage,
        'fail_percentage' => $failPercentage
    );

    // Convert the array to JSON and send it as the response
    header('Content-Type: application/json');
    echo json_encode($data);
} catch (PDOException $e) {
    echo "Connection failed: " . $e->getMessage();
}
?>
