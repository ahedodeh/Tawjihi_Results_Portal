import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class distribution extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database configuration
    private static final String DB_URL = "jdbc:mysql://localhost/network";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(response);
    }

    private void processRequest(HttpServletResponse response) throws IOException {
        // Set response content type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Establish database connection
            conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // Fetch the total number of students
            String totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students";
            stmt = conn.prepareStatement(totalStudentsQuery);
            ResultSet result = stmt.executeQuery();
            int totalStudents = 0;
            if (result.next()) {
                totalStudents = result.getInt("total_students");
            }

            // Fetch the average grade
            String averageGradeQuery = "SELECT AVG(grade) AS average_grade FROM students";
            stmt = conn.prepareStatement(averageGradeQuery);
            result = stmt.executeQuery();
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
            stmt = conn.prepareStatement(distributionQuery);
            result = stmt.executeQuery();
            double excellent = 0, veryGood = 0, good = 0, acceptable = 0, failed = 0;
            if (result.next()) {
                excellent = result.getDouble("excellent");
                veryGood = result.getDouble("very_good");
                good = result.getDouble("good");
                acceptable = result.getDouble("acceptable");
                failed = result.getDouble("failed");
            }

            // Create a JSON response
            String jsonResponse = "{\"total_students\": " + totalStudents + ", " +
                    "\"average_grade\": " + averageGrade + ", " +
                    "\"distribution\": { " +
                    "\"excellent\": " + excellent + ", " +
                    "\"very_good\": " + veryGood + ", " +
                    "\"good\": " + good + ", " +
                    "\"acceptable\": " + acceptable + ", " +
                    "\"failed\": " + failed +
                    " } }";

            // Send the JSON response
            out.print(jsonResponse);
        } catch (SQLException e) {
            // Handle any database errors
            e.printStackTrace();
        } finally {
            // Close resources
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
            if (out != null) {
                out.close();
            }
        }
    }
}