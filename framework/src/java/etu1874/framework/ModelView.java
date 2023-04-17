/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1874.framework;

import java.util.HashMap;

/**
 *
 * @author ITU
 */
public class ModelView {
    String vue;
    HashMap<String, Object> data;

    public HashMap<String, Object> getData() {
        return data;
    }

    public void setData(HashMap<String, Object> data) {
        this.data = data;
    }

    public ModelView(String vue) {
        data=new HashMap<String, Object>();
        this.vue = vue;
    }

    public String getVue() {
        return vue;
    }

    public void setVue(String vue) {
        this.vue = vue;
    }
    
    public void ajouterElement(String cle, Object valeur) {
        getData().put(cle, valeur);
    }

}
