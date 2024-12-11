JAVAC = javac
CUP = java -jar lib/java-cup-11b.jar
JFLEX = java -jar lib/jflex-full-1.8.2.jar

all: Parser.java sym.java Lexer.java
	$(JAVAC) Parser.java sym.java Lexer.java

# Regla para generar el parser y la tabla de símbolos con CUP
Parser.java sym.java: V2024Ini.cup
	$(CUP) -parser Parser -symbols sym V2024Ini.cup

# Regla para generar el lexer con JFlex
Lexer.java: lex.jflex
	$(JFLEX) lex.jflex

clean:
	rm -f Parser.java sym.java Lexer.java *.class
