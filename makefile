# 209007087
# knobela

compile: bin
	find src -name "*.java" > sources.txt
	javac -cp biuoop-1.4.jar:. -d bin @sources.txt
run:
	java -cp biuoop-1.4.jar:bin:resources Ass7Game
jar:
	jar cfm space-invaders.jar MANIFEST.MF -C bin . -C resources .
bin:
	mkdir bin
