/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
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
import org.json.simple.JSONObject;

/**
 *
 * @author PC
 */
@WebServlet(urlPatterns = {"/per"})
public class per extends HttpServlet {
private static final String DB_URL = "jdbc:mysql://localhost/network";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
              throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Fetch the total number of students
            String totalStudentsQuery = "SELECT COUNT(*) AS total_students FROM students";
            PreparedStatement totalStudentsStmt = conn.prepareStatement(totalStudentsQuery);
            ResultSet totalStudentsResult = totalStudentsStmt.executeQuery();
            totalStudentsResult.next();
            int totalStudents = totalStudentsResult.getInt("total_students");

            // Fetch the number of students who passed
            String passedStudentsQuery = "SELECT COUNT(*) AS passed_students FROM students WHERE grade >= 50";
            PreparedStatement passedStudentsStmt = conn.prepareStatement(passedStudentsQuery);
            ResultSet passedStudentsResult = passedStudentsStmt.executeQuery();
            passedStudentsResult.next();
            int passedStudents = passedStudentsResult.getInt("passed_students");

            // Calculate the number of students who failed
            int failedStudents = totalStudents - passedStudents;

            // Calculate the pass and fail percentages
            double passPercentage = ((double) passedStudents / totalStudents) * 100;
            double failPercentage = ((double) failedStudents / totalStudents) * 100;

            // Create a JSON object with the results
            JSONObject responseData = new JSONObject();
            responseData.put("total_students", totalStudents);
            responseData.put("passed_students", passedStudents);
            responseData.put("failed_students", failedStudents);
            responseData.put("pass_percentage", passPercentage);
            responseData.put("fail_percentage", failPercentage);

            // Send the JSON response
            out.println(responseData.toJSONString());

            // Clean up resources
            totalStudentsResult.close();
            totalStudentsStmt.close();
            passedStudentsResult.close();
            passedStudentsStmt.close();
            conn.close();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            out.println("Error processing request.");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
