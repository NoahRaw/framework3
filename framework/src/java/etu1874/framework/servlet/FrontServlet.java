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
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
//            out.print(Utilitaire.infoUrl2(request.getPathInfo()));
            Mapping m=Utilitaire.findInHashMap(mappingUrls, Utilitaire.infoUrl2(request.getPathInfo()));
            
            //set Object from formulaire
            Object o=Class.forName(m.getClassName()).newInstance();;
            for(int i=1;i<getClass().getDeclaredFields().length;i++)
            {
                String me="set".concat(String.valueOf(o.getClass().getDeclaredFields()[i].getName().charAt(0)).toUpperCase().concat(o.getClass().getDeclaredFields()[i].getName().substring(1)));
                Method method=Utilitaire.searchMethod(o.getClass().getMethods(),me);
                
                String type=o.getClass().getDeclaredFields()[i].getType().getSimpleName();
                String value="";
                if(request.getParameter(String.valueOf(o.getClass().getDeclaredFields()[i].getName()))!=null){
                    value=(String)request.getParameter(String.valueOf(o.getClass().getDeclaredFields()[i].getName()));
                    method.invoke(o,Utilitaire.getValue(type,value));    
                }
//                out.print(me+"="+value);
            }
            
            //recuperation des noms de l'argument du fonction
            Method method=Utilitaire.searchMethod(o.getClass().getMethods(),m.getMethod());
                Parameter[] parameters = method.getParameters();
                String[] parameterNames = Utilitaire.get_parameters_name(method);
                
//                tableau d'objet pour la liste des parametres
                Object[] listParametre=new Object[parameterNames.length]; 
                
                for (int j = 0; j < parameterNames.length; j++) {
                    listParametre[j]=request.getParameter(parameterNames[j]);
                    out.println(parameterNames[j]);
                }
            
            ModelView mv=Utilitaire.getAssociatedView(m,o,listParametre);
            RequestDispatcher dispat = request.getRequestDispatcher("/"+mv.getVue());
            HashMap<String, Object> data=mv.getData();
            
            // Parcours de la HashMap
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                // Récupération de la clé et de la valeur correspondante
                String key = entry.getKey();
                Object value = entry.getValue();

                // Initialisation de la variable de requête correspondante
                request.setAttribute(key, value);
            }
            out.print("vita:"+mv.getVue());
            
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
