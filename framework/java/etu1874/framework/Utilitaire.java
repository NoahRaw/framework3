/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package etu1874.framework;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import etu1874.framework.*;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author ITU
 */
public class Utilitaire {
    public static Mapping findInHashMap(HashMap<String,Mapping> mappingUrls,String url) throws Exception
    {
        if(mappingUrls.get(url)!=null)
            return mappingUrls.get(url);
        throw new Exception("cette fonction n existe pas");
    }
    
    public static Method searchMethod(Method[] listMethod,String name)
    {
        for(int i=0;i<listMethod.length;i++)
        {
            if(listMethod[i].getName().equalsIgnoreCase(name))
            {
                return listMethod[i];
            }
        }
        return null;
    }
    
    public static Object[] getValue(String type,String value)
    {
        Object[] listeParmetre=new Object[1];
        if(type.equalsIgnoreCase("int"))
        {
            listeParmetre[0]= Integer.parseInt(value);
        }
        if(type.equalsIgnoreCase("double"))
        {
            listeParmetre[0]=Double.valueOf(value);
        }
        if(type.equalsIgnoreCase("String"))
        {
            listeParmetre[0]=value;
        }
        if(type.equalsIgnoreCase("null"))
        {
            listeParmetre[0]=value;
        }
        return listeParmetre;
    }
    
    public static ModelView getAssociatedView(Mapping map,Object o,Object[] listParametre) throws Exception, IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException
    {
        Method m=Utilitaire.searchMethod(o.getClass().getMethods(),map.getMethod());;
        ModelView mv=(ModelView)m.invoke(o,listParametre);
        return mv;
    }
    
    public static boolean isMethodAnnoteJson(Mapping map,Object o) throws Exception, IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException
    {
        Method m=Utilitaire.searchMethod(o.getClass().getMethods(),map.getMethod());;
        Annotation annotation = m.getAnnotation(JsonAnnotation.class);
        if(annotation!=null)
            if(((JsonAnnotation)annotation).isJson()==true)
                return true;
        return false;
    }
    
    public static String[] get_parameters_name(Method method) throws Exception {
    Annotation[][] annotations = method.getParameterAnnotations();
    String[] result=new String[method.getParameterCount()];

    for (int i = 0; i < annotations.length; i++) {
      for (Annotation annotation : annotations[i]) {
        if (annotation instanceof Param) {
          Param param = (Param) annotation;
           result[i]=param.value();
        }
      }
    }
    
    return result;
    }
    
    //sauvegarder le fichier uploder
    public static void saveToFile(byte[] data, String filePath) throws IOException {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            fos.write(data);
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
    }
    
    //recuperer l'instance d'un objet dans le hashMap
    public static Object getObjectSingleton(HashMap<String,Object> singletonObject,String className) throws Exception
    {
        for (Map.Entry<String, Object> entry : singletonObject.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if(key.equalsIgnoreCase(className)){
                if(value==null)
                    value=Class.forName(className).newInstance();
                singletonObject.put(key, value);
                return value;
            }
        }
        throw new Exception("cette classe n'est pas un singleton");
    }
    
    //recuperer les classes singleton
    public static HashMap<String,Object> getSingletonClasses(String pathProjet, Class<? extends Annotation> annotationClass) throws ClassNotFoundException, IOException {
        Vector<String> allClasse=listeClasse(pathProjet);
        HashMap<String,Object> annotatedMethods = new HashMap<String,Object>();
        for (String stringcCls : allClasse) {
                Class cls=Class.forName(stringcCls); 
                Annotation annotation = cls.getAnnotation(annotationClass);
                if (annotation != null) {
                    if(((Scop)annotation).value().equalsIgnoreCase("singleton"))
                        annotatedMethods.put(stringcCls, null);
                }
        }
        return annotatedMethods;
    }
    
    //determine si une classe est singleton
    public static boolean isObjectSingleton(HashMap<String,Object> singletonObject,String className) throws Exception
    {
        for (Map.Entry<String, Object> entry : singletonObject.entrySet()) {
            String key = entry.getKey();
            if(key.equalsIgnoreCase(className)){
                return true;
            }
        }
        return false;
    }
    
    //recuperation des classes annote numero deux
    public static void getsousdossier(String pathProjet,String pack,Vector<String> tableau){
        String[] noslash=pack.split("\\.");
        File folder=new File(pathProjet+"\\"+noslash[noslash.length-1]);
        File[] listedossier=folder.listFiles();
        String enrepack=pack;
        String newPath="";
        String fileName="";
        for(File file : listedossier){
            if(file.isDirectory()){
                enrepack=enrepack+"."+file.getName();
                newPath=pathProjet+"\\"+pack;
                getsousdossier(newPath,enrepack,tableau);
            }else{
                fileName=file.getName();
                enrepack=enrepack+"."+fileName.substring(0,fileName.lastIndexOf('.'));
                tableau.add(enrepack);
            }
            enrepack=pack;
        }
    }
    
    public static HashMap<String,Mapping> listemethode(String nomClasse) throws ClassNotFoundException{
        Class classe=Class.forName(nomClasse);
        Method[] listemethode=classe.getDeclaredMethods();
        HashMap<String,Mapping> map=new HashMap<String,Mapping>();
        for(int i=0;i<listemethode.length;i++){
            if(listemethode[i].isAnnotationPresent(ClassIdentifier.class)){
                ClassIdentifier an=listemethode[i].getAnnotation(ClassIdentifier.class);
                Mapping mapping=new Mapping(classe.getName(),listemethode[i].getName());
                map.put(an.id(),mapping);
            }
        }
        return map;
    }
    
    public static Vector<String> listeClasse(String pathProjet){
        String path=pathProjet+"\\WEB-INF\\classes";
        File folder=new File(path);
        File[] listedossier=folder.listFiles();
        Vector<String> enregistrement=new Vector<String>();
        for(File file : listedossier){
            if(file.isDirectory()){   //si file est un dossier
                Vector<String> mini=new Vector<String>();
                getsousdossier(path,file.getName(),mini);
                enregistrement.addAll(mini);
            }
        }
        return enregistrement;
    }
    
    @JsonAnnotation(isJson = true)
    public static HashMap<String,Mapping> listeHashMapAllClass(String pathProjet) throws ClassNotFoundException{
        Vector<String> allClasse=listeClasse(pathProjet);
        HashMap<String,Mapping> newmap=new HashMap<String,Mapping>(); 
        String key="";
        for(int i=0;i<allClasse.size();i++){
            HashMap<String,Mapping> map=listemethode(allClasse.get(i));
            Set<String> keys=map.keySet();
            for(String j : keys){
               newmap.put(j,map.get(j));
            }
        }
        return newmap;
    }
    
    public static void main(String[] args) throws Exception
    {
        Mapping map=new Mapping("Utilitaire", "listeClasse");
        Utilitaire u=new Utilitaire();
        System.out.println(Utilitaire.isMethodAnnoteJson(map, u));
    }
}
    
