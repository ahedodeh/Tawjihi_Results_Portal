<%@ page import="java.sql.*" %>
<%@ page import="org.json.JSONObject" %>
<%@ page contentType="application/json" %>
<%
    // Database configuration
    String hostname = "localhost";
    String username = "root";
    String password = "";
    String database = "network";

    Connection conn = null;
    Statement stmt = null;
    ResultSet result = null;

    try {
        // Establish database connection
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + database, username, password);

        // Fetch the total number of students
        String totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students";
        stmt = conn.createStatement();
        result = stmt.executeQuery(totalStudentsQuery);
        int totalStudents = 0;
        if (result.next()) {
            totalStudents = result.getInt("total_students");
        }

        // Fetch the average grade
        String averageGradeQuery = "SELECT AVG(grade) AS average_grade FROM students";
        result = stmt.executeQuery(averageGradeQuery);
        double averageGrade = 0;
        if (result.next()) {
            averageGrade = result.getDouble("average_grade");
        }

        // Fetch the distribution of grades with percentages
        String distributionQuery = "SELECT " +
                "SUM(CASE WHEN grade >= 90 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS excellent, " +
                "SUM(CASE WHEN grade >= 75 AND grade < 90 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS very_good, " +
                "SUM(CASE WHEN grade >= 65 AND grade < 75 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS good, " +
                "SUM(CASE WHEN grade >= 50 AND grade < 65 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS acceptable, " +
                "SUM(CASE WHEN grade < 50 THEN 1 ELSE 0 END) / COUNT(*) * 100 AS failed " +
                "FROM students";
        result = stmt.executeQuery(distributionQuery);
        double excellent = 0;
        double veryGood = 0;
        double good = 0;
        double acceptable = 0;
        double failed = 0;
        if (result.next()) {
            excellent = result.getDouble("excellent");
            veryGood = result.getDouble("very_good");
            good = result.getDouble("good");
            acceptable = result.getDouble("acceptable");
            failed = result.getDouble("failed");
        }

        // Create a JSON object with the results
        JSONObject data = new JSONObject();
        data.put("total_students", totalStudents);
        data.put("average_grade", averageGrade);
        JSONObject distribution = new JSONObject();
        distribution.put("excellent", excellent);
        distribution.put("very_good", veryGood);
        distribution.put("good", good);
        distribution.put("acceptable", acceptable);
        distribution.put("failed", failed);
        data.put("distribution", distribution);

        // Send the JSON response
        out.println(data.toString());
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Close resources
        if (result != null) {
            try {
                result.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
%>