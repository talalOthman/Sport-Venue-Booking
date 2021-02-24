/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc;

import bean.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import jdbc.JDBCUtility;

/**
 *
 * @author MSI
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/LoginServlet"})
public class LoginServlet extends HttpServlet {

    private JDBCUtility jdbcUtility;
    private Connection con;
    
    public void init() throws ServletException
    {
        String driver = "com.mysql.jdbc.Driver";

        String dbName = "sportCenter";
        String url = "jdbc:mysql://localhost/" + dbName + "?";
        String userName = "root";
        String password = "";

        jdbcUtility = new JDBCUtility(driver,
                                      url,
                                      userName,
                                      password);

        jdbcUtility.jdbcConnect();
        con = jdbcUtility.jdbcGetConnection();
        
        
        
        
    }    

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        
        
        User user = new User();
        
        //Get the session object
	HttpSession session = request.getSession();
        
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String userType = "", fullName = "";
        
        String sqlQuery = "SELECT * FROM user WHERE login = ? AND password = ?";
        
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sqlQuery);
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
                rs.next();
                userType = rs.getString("usertype");
                fullName = rs.getString("fullname");
                
                
                
                user.setLogin(login);
                user.setFullName(fullName);
                user.setUserType(userType);
        }
        catch (SQLException ex) {  
            user = null;
            throw new ServletException("Register failed", ex);
        }
        
        if (user != null) {
            if(user.getUserType()=="admin"){
                session.setAttribute("adminloggedin", user);
                response.sendRedirect(request.getContextPath() + "/client.html");
            }
            else{
                session.setAttribute("clientloggedin", user);
                response.sendRedirect(request.getContextPath() + "/client.html");
            }
        }
        else {
            response.sendRedirect(request.getContextPath() + "/not-exist.html");
        }            
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
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
     * Handles the HTTP
     * <code>POST</code> method.
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