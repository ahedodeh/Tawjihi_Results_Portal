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

    // Fetch the average grade
    $averageGradeQuery = "SELECT AVG(grade) AS average_grade FROM students";
    $stmt = $pdo->query($averageGradeQuery);
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $average_grade = $result['average_grade'];

    // Fetch the distribution of grades with percentages
    $distributionQuery = "SELECT
        SUM(CASE WHEN grade >= 90 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS excellent,
        SUM(CASE WHEN grade >= 75 AND grade < 90 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS very_good,
        SUM(CASE WHEN grade >= 65 AND grade < 75 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS good,
        SUM(CASE WHEN grade >= 50 AND grade < 65 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS acceptable,
        SUM(CASE WHEN grade < 50 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS failed
        FROM students";
    $stmt = $pdo->query($distributionQuery);
    $result = $stmt->fetch(PDO::FETCH_ASSOC);
    $excellent = $result['excellent'];
    $very_good = $result['very_good'];
    $good = $result['good'];
    $acceptable = $result['acceptable'];
    $failed = $result['failed'];

    // Create an associative array with the results
    $data = array(
        'total_students' => $totalStudents,
        'average_grade' => $average_grade,
        'distribution' => array(
            'excellent' => $excellent,
            'very_good' => $very_good,
            'good' => $good,
            'acceptable' => $acceptable,
            'failed' => $failed
        )
    );

    // Convert the array to JSON and send it as the response
    header('Content-Type: application/json');
    echo json_encode($data);
} catch (PDOException $e) {
    echo "Connection failed: " . $e->getMessage();
}
?>
