/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1874.framework;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author ITU
 */
//@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonAnnotation {
    boolean isJson();
}
