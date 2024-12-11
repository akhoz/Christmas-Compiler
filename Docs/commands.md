# Create Lexer file

```
jflex flex.jflex
```

# Create sym file

```
java -jar lib/java-cup-11b.jar -parser lex/Lexer.java -symbols parser/sym lex/parser
V2024Ini.cup
```
