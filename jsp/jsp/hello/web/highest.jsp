<%@ page import="java.sql.*" %>
<%@ page import="org.json.JSONArray" %>
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

        // SQL query to fetch the highest 3 grades along with names and cities
        String sql = "SELECT name, grade, city FROM students ORDER BY grade DESC LIMIT 3";

        // Prepare the SQL statement
        stmt = conn.prepareStatement(sql);

        // Execute the query
        result = stmt.executeQuery();

        // Create a JSON array to store the results
        JSONArray jsonArray = new JSONArray();

        // Process the result set
        while (result.next()) {
            // Create a JSON object for each row
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", result.getString("name"));
            jsonObject.put("grade", result.getInt("grade"));
            jsonObject.put("city", result.getString("city"));

            // Add the JSON object to the array
            jsonArray.put(jsonObject);
        }

        // Send the JSON response
        out.println(jsonArray.toString());
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