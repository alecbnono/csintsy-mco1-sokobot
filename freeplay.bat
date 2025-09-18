del /s /q *.class
javac src/Driver.java -cp src
java -classpath src Driver %1 fp
