<?php
// Database configuration
$hostname = 'localhost';
$username = 'root';
$password = '';
$database = 'network';

$pdo = new PDO("mysql:host=$hostname;dbname=$database", $username, $password);

if (isset($_GET['city'])) {
    $city = $_GET['city'];

    // Fetch the number of students who passed
    $passedStudentsQuery = "SELECT COUNT(*) AS passed_students FROM students WHERE grade >= 50 AND city = ?";
    $stmtPassed = $pdo->prepare($passedStudentsQuery);
    $stmtPassed->execute([$city]);
    $resultPassed = $stmtPassed->fetch(PDO::FETCH_ASSOC);
    $passedStudents = $resultPassed['passed_students'];

    // Fetch the total number of students
    $totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students WHERE city = ?";
    $stmtTotal = $pdo->prepare($totalStudentsQuery);
    $stmtTotal->execute([$city]);
    $resultTotal = $stmtTotal->fetch(PDO::FETCH_ASSOC);
    $totalStudents = $resultTotal['total_students'];

    // Calculate the number of students who failed
    $failedStudents = $totalStudents - $passedStudents;

    // Calculate the pass and fail percentages
    $passPercentage = ($totalStudents > 0) ? round(($passedStudents / $totalStudents) * 100, 2) : 0;
    $failPercentage = ($totalStudents > 0) ? round(($failedStudents / $totalStudents) * 100, 2) : 0;

    // Create an associative array with the results
    $data = array(
        'pass_percentage' => $passPercentage,
        'fail_percentage' => $failPercentage
    );

    // Convert the array to JSON and send it as the response
    header('Content-Type: application/json');
    echo json_encode($data);
}
?>
