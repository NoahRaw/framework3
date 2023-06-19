/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import etu1874.framework.ClassIdentifier;
import etu1874.framework.ModelView;
import etu1874.framework.Param;
import etu1874.framework.Scop;

/**
 *
 * @author ITU
 */
@Scop("NonSingleton")
public class Emp2{
    String idEmployer;
    String nom;
    Integer identifiant;

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

    public Emp2() {
    }

    public Emp2(String idEmployer, String nom) {
        this.idEmployer = idEmployer;
        this.nom = nom;
    }

    public Integer getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(Integer identifiant) {
        this.identifiant = identifiant;
    }
    
    @ClassIdentifier(id="test3")
    public ModelView test()
    {
        ModelView m=new ModelView("index.jsp");
        m.ajouterElement("test", "test");
        m.ajouterElement("emp", this);
        return m;
    }
    
    @ClassIdentifier(id="test4")
    public ModelView test2(@Param("test") String test)
    {
        ModelView m=new ModelView("index.jsp");
        m.ajouterElement("emp", this);
        m.ajouterElement("test", test);
        return m;
    }
}
