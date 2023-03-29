/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import etu1874.framework.ClassIdentifier;
import etu1874.framework.ModelView;

/**
 *
 * @author ITU
 */
public class Test {
    @ClassIdentifier(id="test")
    public static ModelView test()
    {
        ModelView m=new ModelView("index.html");
        return m;
    }
}
