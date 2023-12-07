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
    PreparedStatement stmtPassed = null;
    PreparedStatement stmtTotal = null;
    ResultSet resultPassed = null;
    ResultSet resultTotal = null;

    try {
        // Establish database connection
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://" + hostname + "/" + database, username, password);

        if (request.getParameter("city") != null) {
            String city = request.getParameter("city");

            // Fetch the number of students who passed
            String passedStudentsQuery = "SELECT COUNT(*) AS passed_students FROM students WHERE grade >= 50 AND city = ?";
            stmtPassed = conn.prepareStatement(passedStudentsQuery);
            stmtPassed.setString(1, city);
            resultPassed = stmtPassed.executeQuery();
            int passedStudents = 0;
            if (resultPassed.next()) {
                passedStudents = resultPassed.getInt("passed_students");
            }

            // Fetch the total number of students
            String totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students WHERE city = ?";
            stmtTotal = conn.prepareStatement(totalStudentsQuery);
            stmtTotal.setString(1, city);
            resultTotal = stmtTotal.executeQuery();
            int totalStudents = 0;
            if (resultTotal.next()) {
                totalStudents = resultTotal.getInt("total_students");
            }

            // Calculate the number of students who failed
            int failedStudents = totalStudents - passedStudents;

            // Calculate the pass and fail percentages
            double passPercentage = (totalStudents > 0) ? Math.round((double) passedStudents / totalStudents * 100 * 100) / 100.0 : 0;
            double failPercentage = (totalStudents > 0) ? Math.round((double) failedStudents / totalStudents * 100 * 100) / 100.0 : 0;

            // Create a JSON object with the results
            JSONObject data = new JSONObject();
            data.put("pass_percentage", passPercentage);
            data.put("fail_percentage", failPercentage);

            // Send the JSON response
            out.println(data.toString());
        }
    } catch (ClassNotFoundException e) {
        e.printStackTrace();
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        // Close resources
        if (resultPassed != null) {
            try {
                resultPassed.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmtPassed != null) {
            try {
                stmtPassed.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (resultTotal != null) {
            try {
                resultTotal.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (stmtTotal != null) {
            try {
                stmtTotal.close();
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