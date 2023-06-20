/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1874.framework.servlet;

import etu1874.framework.Auth;
import etu1874.framework.ClassIdentifier;
import etu1874.framework.FileUpload;
import etu1874.framework.Mapping;
import etu1874.framework.ModelView;
import etu1874.framework.Scop;
import etu1874.framework.Utilitaire;
import static etu1874.framework.Utilitaire.getClasses2;
import javax.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.Part;

/**
 *
 * @author ITU
 */
@WebServlet(name = "FrontServlet", urlPatterns = {"/*"})
@MultipartConfig(maxFileSize = 2000000)
public class FrontServlet extends HttpServlet {
    HashMap<String, Mapping> mappingUrls;
    HashMap<String, Object> singletonObject;
    HashMap<String, Object> mappingSession;
    String packageName;
    String profil;
    String isConnected;

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
        //recuperation du fichier    
        FileUpload fileUpload=null;
        try{
                Part filePart= request.getPart("file");
                if(filePart!=null){
                    String fileName = filePart.getSubmittedFileName();

                    // Lire les données du fichier et les stocker dans un tableau de bytes
                    byte[] fileBytes = new byte[(int) filePart.getSize()];
                    filePart.getInputStream().read(fileBytes);

                    fileUpload=new FileUpload(fileName,"",fileBytes);
                }
            }
            catch(Exception ei)
            {
                
            }
        
        
        //initialiser l'objet qui appelle la classe
        try (PrintWriter out = response.getWriter()) {
            Mapping m=Utilitaire.findInHashMap(mappingUrls, Utilitaire.infoUrl2(request.getPathInfo()));
                
            Object o=new Object();
            //set Object from formulaire
            if(Utilitaire.isObjectSingleton(singletonObject,m.getClassName())) {
                o=Utilitaire.getObjectSingleton(singletonObject,m.getClassName());
                //reinitialiser l'objet
                for(int i=0;i<o.getClass().getDeclaredFields().length;i++)
                {
                    String me="set".concat(String.valueOf(o.getClass().getDeclaredFields()[i].getName().charAt(0)).toUpperCase().concat(o.getClass().getDeclaredFields()[i].getName().substring(1)));
                    Method method=Utilitaire.searchMethod(o.getClass().getMethods(),me);

                    String type="null";
                    String value="";
                    if(request.getParameter(String.valueOf(o.getClass().getDeclaredFields()[i].getName()))!=null){
                        value=null;
                        method.invoke(o,Utilitaire.getValue(type,value));    
                    }
                }
            } else {
                o=Class.forName(m.getClassName()).newInstance();
            }
            
            
//            attribution des valeurs des attributs de l'objet ou se trouve la fonction appelez via le lien
        if(isFonctionAccessible(Utilitaire.searchMethod(o.getClass().getMethods(),m.getMethod()))==true){
            for(int i=0;i<o.getClass().getDeclaredFields().length;i++)
            {
                String me="set".concat(String.valueOf(o.getClass().getDeclaredFields()[i].getName().charAt(0)).toUpperCase().concat(o.getClass().getDeclaredFields()[i].getName().substring(1)));
                Method method=Utilitaire.searchMethod(o.getClass().getMethods(),me);
                
                String type=o.getClass().getDeclaredFields()[i].getType().getSimpleName();
                String value="";
                
                if(type.equalsIgnoreCase("FileUpload") && fileUpload!=null)
                {
                    Object[] listArgument=new Object[1];
                    listArgument[0]=fileUpload;
                    method.invoke(o,listArgument);    
                }
                else if(request.getParameter(String.valueOf(o.getClass().getDeclaredFields()[i].getName()))!=null){
                    value=(String)request.getParameter(String.valueOf(o.getClass().getDeclaredFields()[i].getName()));
                    method.invoke(o,Utilitaire.getValue(type,value));    
                }
            }
            
            //recuperation des noms de l'argument du fonction
            Method method=Utilitaire.searchMethod(o.getClass().getMethods(),m.getMethod());
                String[] parameterNames = Utilitaire.get_parameters_name(method);
                
//                tableau d'objet pour la liste des parametres
                Object[] listParametre=new Object[parameterNames.length]; 
                
                for (int j = 0; j < parameterNames.length; j++) {
                    listParametre[j]=request.getParameter(parameterNames[j]);
                }
            
            
                ModelView mv=Utilitaire.getAssociatedView(m,o,listParametre);
                
                if(mv.getSession()!=null)
                    mappingSession=mv.getSession();
                
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
                dispat.forward(request,response);
            }
        else
        {
            out.print("vous n'avez pas acces a ce methode");
        }
        }
    }
    
    //fonction pour tester si on a acces a une fontion
    public boolean isFonctionAccessible(Method method) throws Exception
    {
        Annotation annotation = method.getAnnotation(Auth.class);
                if (annotation == null) {
                    return true;
                }
                
                if(mappingSession!=null)
                {
                    if((boolean)mappingSession.get(isConnected)==true)
                    {
                        String annotationValue=((Auth)annotation).profil();
                        String profilValue=(String)mappingSession.get(profil);
                        if(annotationValue.equalsIgnoreCase(profilValue))
                            return true;
                    }
                }
        return false;
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
        isConnected = this.getInitParameter("isConnected");  
        profil = this.getInitParameter("profil");  
        
        try {
            mappingUrls=Utilitaire.getAnnotatedMethods(packageName, ClassIdentifier.class);
            singletonObject=Utilitaire.getSingletonClasses(packageName, Scop.class);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(FrontServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
