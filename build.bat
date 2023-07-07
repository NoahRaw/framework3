@REM nom des dossiers
set path_java_framework="framework/java"
set path_classes_framework="framework/classes"

set path_lib_temp="temp/WEB-INF/lib"
set fichier_java_test_Framework="testFramework/java"
set path_classes_temp="temp/WEB-INF/classes"
set path_webapps="C:/Program Files/Apache Software Foundation/Tomcat 9.0/webapps"
set path_fihier_jsp="testFramework\"
set path_WEBINF="temp\WEB-INF"

@REM creation de dossier temp
rd /s /q temp && mkdir temp
mkdir temp
cd temp
mkdir WEB-INF
cd WEB-INF
mkdir classes
mkdir lib
cd ../../

@REM transformation du framework en jar
for /R %path_java_framework% %%f in (*.java) do javac -d . %%f
for /R %path_java_framework% %%f in (*.java) do javac -d %path_classes_framework% %%f
echo Manifest-Version: 1.0 > MANIFEST.MF
jar cvfm framework.jar MANIFEST.MF -C %path_classes_framework% .
copy framework.jar %path_lib_temp%

pause

@REM transformation du dossier temp en war
for /R %fichier_java_test_Framework% %%f in (*.java) do javac -cp temp/WEB-INF/lib/framework.jar -d %path_classes_temp% %%f

@REM copier le fichier jar Gson
xcopy /S /I framework\gson-2.10.1.jar %path_lib_temp%

@REM copie des fichiers jsp
xcopy /S /I %path_fihier_jsp%*.jsp temp

@REM copie des fichiers jsp
xcopy /S /I %path_fihier_jsp%*.html temp

@REM copie du web.xml
xcopy /S /I framework\web.xml %path_WEBINF%

@REM transfomation en war
cd temp
jar cvf testFramework2.war ./*
copy testFramework2.war %path_webapps%

pause
