#Helper script to install the JHaml libs into local Maven
#wget --no-check-certificate https://github.com/raymyers/JHaml/blob/master/dist/jhaml-0.1.3.jar

mvn install:install-file -Dfile=jhaml-0.1.3.jar -DgroupId=com.cardlife \
-DartifactId=jhaml -Dversion=0.1.3 -Dpackaging=jar -DgeneratePom=true

#rm jhaml-0.1.3.jar
