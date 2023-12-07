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
import java.util.ArrayList;
import java.util.List;


public class HighestGradesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    // JDBC database URL, username, and password
    private static final String DB_URL = "jdbc:mysql://localhost:3306/network";
    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.jdbc.Driver");

            // Create a connection to the database
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);

            // SQL query to fetch the highest 3 grades along with names and cities
            String sql = "SELECT name, grade, city FROM students ORDER BY grade DESC LIMIT 3";

            // Prepare the SQL statement
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Execute the query
            ResultSet rs = stmt.executeQuery();

            // Create a list to hold the results
            List<Student> students = new ArrayList<>();

            // Iterate through the result set and add data to the list
            while (rs.next()) {
                String name = rs.getString("name");
                int grade = rs.getInt("grade");
                String city = rs.getString("city");
                Student student = new Student(name, grade, city);
                students.add(student);
            }

            // Close the resources
            rs.close();
            stmt.close();
            conn.close();

            // Output the data as a JSON array
            out.println(jsonify(students));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("Error: " + e.getMessage());
        }
    }

    private String jsonify(List<Student> students) {
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            json.append("{");
            json.append("\"name\":\"").append(student.getName()).append("\",");
            json.append("\"grade\":").append(student.getGrade()).append(",");
            json.append("\"city\":\"").append(student.getCity()).append("\"");
            json.append("}");
            if (i < students.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    private class Student {
        private String name;
        private int grade;
        private String city;

        public Student(String name, int grade, String city) {
            this.name = name;
            this.grade = grade;
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public int getGrade() {
            return grade;
        }

        public String getCity() {
            return city;
        }
    }
}
