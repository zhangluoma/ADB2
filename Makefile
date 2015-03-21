JCC = javac
JFLAGS = -g
default: compile
compile:
	javac -cp "./json-20140107.jar" *.java
run:
	java -cp .:json-20140107.jar Test
clean:
	rm *.class