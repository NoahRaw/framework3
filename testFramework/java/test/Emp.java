/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import etu1874.framework.Auth;
import etu1874.framework.ClassIdentifier;
import etu1874.framework.FileUpload;
import etu1874.framework.JsonAnnotation;
import etu1874.framework.ModelView;
import etu1874.framework.Param;
import etu1874.framework.Scop;
import etu1874.framework.Utilitaire;
import java.io.IOException;
import java.util.HashMap;

/**
 *
 * @author ITU
 */
@Scop("Singleton")
public class Emp{
    String idEmployer;
    String nom;
    FileUpload file;
    Integer identifiant;

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }

    public FileUpload getFile() {
        return file;
    }

    public void setFile(FileUpload file) {
        this.file = file;
    }
    
    public String getIdEmployer() {
        return idEmployer;
    }

    public void setIdEmployer(String idEmployer) {
        this.idEmployer = idEmployer;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Emp() {
    }

    public Emp(String idEmployer, String nom) {
        this.idEmployer = idEmployer;
        this.nom = nom;
    }
    
    @ClassIdentifier(id="/test.do")
    public ModelView test()
    {
        ModelView m=new ModelView("index.jsp");
        m.ajouterElement("test", "test");
        m.ajouterElement("emp", this);
        return m;
    }
    
    @ClassIdentifier(id="/login.do")
    public ModelView login()
    {
        ModelView m=new ModelView("connecte.jsp");
        
        HashMap<String, Object> session = new HashMap<String, Object>();
        session.put("isConnected",true);
        session.put("profil","admin");
        m.setSession(session);
        
        return m;
    }
    
    @ClassIdentifier(id="/test2.do")
    @Auth(profil="admin")
    public ModelView test2(@Param("test") String test) throws IOException
    {
        getFile().setPath("E:\\s4\\webdynamique\\framework3\\"+getFile().getFileName());
        Utilitaire.saveToFile(getFile().getFileByte(), getFile().getPath());
        ModelView m=new ModelView("resutat.jsp");
        m.ajouterElement("emp", this);
        m.ajouterElement("emp", this);
        m.ajouterElement("test", test);
//        m.setIsJson(true);
        return m;
    }
    
    @ClassIdentifier(id="/json.do")
    @Auth(profil="admin")
    public ModelView json(@Param("test") String test) throws IOException
    {
        ModelView m=new ModelView("resutat.jsp");
        m.ajouterElement("emp", this);
        m.ajouterElement("emp", this);
        m.ajouterElement("test", test);
        m.setIsJson(true);
        return m;
    }
    
    @JsonAnnotation(isJson = true)
    @ClassIdentifier(id="/json2.do")
    @Auth(profil="admin")
    public ModelView json2(@Param("test") String test) throws IOException
    {
        ModelView m=new ModelView("resutat.jsp");
        m.ajouterElement("emp", this);
        m.ajouterElement("emp", this);
        m.ajouterElement("test", test);
        return m;
    }
}
