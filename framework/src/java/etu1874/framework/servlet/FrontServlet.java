/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1874.framework.servlet;

import etu1874.framework.ClassIdentifier;
import etu1874.framework.Mapping;
import etu1874.framework.ModelView;
import etu1874.framework.Utilitaire;
import static etu1874.framework.Utilitaire.getClasses2;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ITU
 */
@WebServlet(name = "FrontServlet", urlPatterns = {"/*"})
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    String packageName;
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
            throws ServletException, IOException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet FrontServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet FrontServlet at " + request.getContextPath() + "</h1>");
//            out.println("<h1>url " + Utilitaire.infoUrl(request.getRequestURL().toString(), "http://localhost:8080/Framework/")+ "</h1>");
//            out.println("<p>url: " + Utilitaire.infoUrl2(request.getPathInfo())+ "</p>");
//            out.println("<p>class Name: " +Utilitaire.findMethodsAnnotatedWith(lc, Utilitaire.infoUrl2(request.getPathInfo()))[0] + "</p>");
//            out.println("<p>foncttion Name: " +Utilitaire.findMethodsAnnotatedWith(lc, Utilitaire.infoUrl2(request.getPathInfo()))[1] + "</p>");
//            out.println("</body>");
//            out.println("</html>");
                     
//            for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
//                String key = entry.getKey();
//                Mapping value = entry.getValue();
//                out.println("annotation = " + key);
//                out.println("class Name = " + value.getClassName());
//                out.println("foncttion Name = " + value.getMethod());
//            }
            Mapping m=Utilitaire.findInHashMap(mappingUrls, Utilitaire.infoUrl2(request.getPathInfo()));
            ModelView mv=Utilitaire.getAssociatedView(m);
            out.println("vue Name = " + mv.getVue());
            RequestDispatcher dispat = request.getRequestDispatcher("/"+mv.getVue()); 
            dispat.forward(request,response); 
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    public void init() throws ServletException 
    {
        packageName = this.getInitParameter("packageName"); 
        try {
            mappingUrls=Utilitaire.getAnnotatedMethods(packageName, ClassIdentifier.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}