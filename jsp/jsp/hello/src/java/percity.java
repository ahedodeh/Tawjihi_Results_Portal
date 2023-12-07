import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

public class percity extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost/network";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String city = request.getParameter("city");
        processRequest(city, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String city = request.getParameter("city");
        processRequest(city, response);
    }

    private void processRequest(String city, HttpServletResponse response) throws IOException {
        // Set response content type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement stmtPassed = null;
        PreparedStatement stmtTotal = null;

        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Fetch the number of students who passed
            String passedStudentsQuery = "SELECT COUNT(*) AS passed_students FROM students WHERE grade >= 50 AND city = ?";
            stmtPassed = conn.prepareStatement(passedStudentsQuery);
            stmtPassed.setString(1, city);
            ResultSet resultPassed = stmtPassed.executeQuery();
            int passedStudents = 0;
            if (resultPassed.next()) {
                passedStudents = resultPassed.getInt("passed_students");
            }

            // Fetch the total number of students
            String totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students WHERE city = ?";
            stmtTotal = conn.prepareStatement(totalStudentsQuery);
            stmtTotal.setString(1, city);
            ResultSet resultTotal = stmtTotal.executeQuery();
            int totalStudents = 0;
            if (resultTotal.next()) {
                totalStudents = resultTotal.getInt("total_students");
            }

            // Calculate the number of students who failed
            int failedStudents = totalStudents - passedStudents;

            // Calculate the pass and fail percentages
            double passPercentage = (totalStudents > 0) ? ((double) passedStudents / totalStudents) * 100 : 0;
            double failPercentage = (totalStudents > 0) ? ((double) failedStudents / totalStudents) * 100 : 0;

            // Create a JSON response
            String jsonResponse = "{\"pass_percentage\": " + passPercentage + ", \"fail_percentage\": " + failPercentage + "}";

            // Send the JSON response
            out.print(jsonResponse);
        } catch (SQLException e) {
            // Handle any database errors
            e.printStackTrace();
        } finally {
            // Close resources
            if (stmtPassed != null) {
                try {
                    stmtPassed.close();
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
            if (out != null) {
                out.close();
            }
        }
    }
}