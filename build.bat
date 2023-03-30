for /R framework/src/java %%f in (*.java) do javac -d . %%f
echo Manifest-Version: 1.0 > MANIFEST.MF
jar cvfm framework.jar MANIFEST.MF -C framework/build/web/WEB-INF/classes/ .
copy framework.jar testFramework\build\web\WEB-INF\lib
for /R testFramework/src/java %%f in (*.java) do javac -d testFramework/build/web/WEB-INF/classes %%f
jar cvf testFramework.war testFramework/*
pause
