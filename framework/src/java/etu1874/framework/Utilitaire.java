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

/**
 *
 * @author ITU
 */
public class Utilitaire {
    public static String infoUrl(String url, String nomDomaine) throws Exception {
        int index = url.indexOf(nomDomaine);
        if (index == -1) {
            // La sous-chaîne n'a pas été trouvée dans la chaîne originale
            throw new Exception("nom de domaine non valide");
        } else {
            // Supprime la sous-chaîne de la chaîne originale et renvoie la nouvelle chaîne
             url=url.substring(0, index) + url.substring(index + nomDomaine.length());
        }
        
        index = url.indexOf("?");
        if (index == -1) {
            // La sous-chaîne n'a pas été trouvée dans la chaîne originale
            return url;
        } else {
            // Supprime la sous-chaîne de la chaîne originale et renvoie la nouvelle chaîne
            return url.substring(0, index);
        }
    }
    
    public static String infoUrl2(String url) throws Exception {
          return url.substring(1,url.length());
    }
    
    public static List<Class<?>> getClasses2(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                classes.add(Class.forName(className));
            }
        }
        return classes;
    }
   
    
    public static HashMap<String,Mapping> getAnnotatedMethods(String packageName, Class<? extends Annotation> annotationClass) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = getClasses2(packageName);
        HashMap<String,Mapping> annotatedMethods = new HashMap<String,Mapping>();
        for (Class<?> cls : classes) {
            Method[] methods = cls.getDeclaredMethods();
            for (Method method : methods) {
                Annotation annotation = method.getAnnotation(annotationClass);
                if (annotation != null) {
                    annotatedMethods.put(((ClassIdentifier) annotation).id(), new Mapping( cls.getName(), method.getName()));
                }
            }
        }
        return annotatedMethods;
    }
    
    public static Mapping findInHashMap(HashMap<String,Mapping> mappingUrls,String url) throws Exception
    {
        for (Map.Entry<String, Mapping> entry : mappingUrls.entrySet()) {
            String key = entry.getKey();
            Mapping value = entry.getValue();
            if(key.equalsIgnoreCase(url))
                return value;
        }
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
        return listeParmetre;
    }
    
    public static ModelView getAssociatedView(Mapping map,Object o,Object[] listParametre) throws Exception, IllegalAccessException, ClassNotFoundException, InstantiationException, InvocationTargetException
    {
        Method m=Utilitaire.searchMethod(o.getClass().getMethods(),map.getMethod());;
        ModelView mv=(ModelView)m.invoke(o,listParametre);
        return mv;
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
        
//        int count; 
//        byte buf[] = new byte[4096];
//        while ((count = is.read(buf)) > -1) 
//            os.write(buf, 0, count); 
    }
}
    
