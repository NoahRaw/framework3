1)Mettez "framework.jar"


-------------------------------------------------------------------------------------------------------------------------------------------------
2)Ajoutez les lignes suivants dans votre web.xml:

	<servlet>
        <servlet-name>FrontServlet</servlet-name>
        <servlet-class>etu1874.framework.servlet.FrontServlet</servlet-class>
        <init-param> 
            <param-name>packageName</param-name> 
            <param-value>test</param-value> 
            <description>Nom du package</description> 
        </init-param> 
    </servlet>
    <servlet-mapping>
        <servlet-name>FrontServlet</servlet-name>
        <url-pattern>/f/*</url-pattern>
    </servlet-mapping>


-------------------------------------------------------------------------------------------------------------------------------------------------
3)Le "name" des formulaires doivent etre similaire a celui des attributs de la classe qui va appeler la fonction pour traiter la valeur de ces champs



-------------------------------------------------------------------------------------------------------------------------------------------------
4)les noms des parametres en lien doivent etre similaire a celui des arguments de la fonction qu'on souhaite appeler




----------------------------------------------------------------------------------------------------------------------------------------------
5)Tous les fonctions que l'on souhaite appeler a partir d'un lien doit etre anoter par avec l'annotation "@ClassIdentifier" dans le jar donner
    exemple:
        @ClassIdentifier(id="test2")
            public ModelView test2(......)
            {
                .........
            }



-------------------------------------------------------------------------------------------------------------------------------------------------
6)vous devez annoter les arguments de votre fonction avec l'annotation "@Param" dans le jar donner:
   Exemple de fonction:

        @ClassIdentifier(id="test2")
            public ModelView test2(@Param("test") String test)
            {
                .........
            }


-------------------------------------------------------------------------------------------------------------------------------------------------
7)Votre fonction doit retourner un "ModelView" (une classe dans le jar), avec les arguments "vue":nom de la vue ou vous souhaite diriger
et data qui contient la liste des donnees a envoyer dans la vue. Pour ajouter un element dans la vue utiliser la fonction <ModelView>.ajouterElement(<nom de l'element>:String, <element>:Object);
    Exemple:
        @ClassIdentifier(id="test2")
            public ModelView test2(@Param("test") String test)
            {
                ModelView m=new ModelView("index.jsp");
                m.ajouterElement("emp", this);
                m.ajouterElement("test", test);
                return m;
            }

Pour appeler une fonction via lien utiliser le modele suivant :

    < nom_domaine+nom_projet >/f/<nomFonction>?<parametre1=....>&<parametre2=....>...<parametren=....>
    
    exemple: "http://localhost:8080/testFramework/f/test?nom=noah&test=milay"