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
    PreparedStatement stmt = null;
    ResultSet result = null;

    try {
        // Establish database connection
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + database, username, password);

        // Fetch the total number of students
        String totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students";
        stmt = conn.prepareStatement(totalStudentsQuery);
        result = stmt.executeQuery();
        int totalStudents = 0;
        if (result.next()) {
            totalStudents = result.getInt("total_students");
        }

        // Fetch the number of students who passed
        String passedStudentsQuery = "SELECT COUNT(*) AS passed_students FROM students WHERE grade >= 50";
        stmt = conn.prepareStatement(passedStudentsQuery);
        result = stmt.executeQuery();
        int passedStudents = 0;
        if (result.next()) {
            passedStudents = result.getInt("passed_students");
        }

        // Calculate the number of students who failed
        int failedStudents = totalStudents - passedStudents;

        // Calculate the pass and fail percentages
        double passPercentage = (double) passedStudents / totalStudents * 100;
        double failPercentage = (double) failedStudents / totalStudents * 100;

        // Create a JSON object with the results
        JSONObject data = new JSONObject();
        data.put("total_students", totalStudents);
        data.put("passed_students", passedStudents);
        data.put("failed_students", failedStudents);
        data.put("pass_percentage", passPercentage);
        data.put("fail_percentage", failPercentage);

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