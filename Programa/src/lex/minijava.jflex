/* JFlex example: partial Java language lexer specification */
package parser;
import tables.TokenInfo;
import java_cup.runtime.*;

/**
 * This class is a simple example lexer.
 */
%%

%public
%class Lexer
%unicode
%cup
%line
%column

%{
  StringBuffer string = new StringBuffer();

  private Symbol symbol(int type) {
    return new Symbol(type, yyline, yycolumn);
  }
  private Symbol symbol(int type, Object value) {
    return new Symbol(type, yyline, yycolumn, value);
  }
%}

LineTerminator = \r|\n|\r\n
InputCharacter = [^\r\n]
WhiteSpace     = {LineTerminator} | [ \t\f]

/* comments */
Comment = "#" {InputCharacter}* {LineTerminator}? | "\\_" ([^\_]|_[^/])* "_/"
CommentContent = ([^\_]|_[^/])*


Identifier = _[a-zA-Z0-9_]+_

DecIntegerLiteral = "-"? (0 | [1-9][0-9]*)
DecFloatLiteral = "-"? [0-9]+ "." [0-9]+
Digit = [0-9]

%state STRING

%%

/* keywords */
<YYINITIAL> "abrecuento"         { return symbol(sym.OPEN_BLOCK, yytext()); }
<YYINITIAL> "cierracuento"         { return symbol(sym.CLOSE_BLOCK, yytext()); }
<YYINITIAL> "rodolfo"            { return symbol(sym.INTEGER, yytext()); }
<YYINITIAL> "bromista"           { return symbol(sym.FLOAT, yytext()); }
<YYINITIAL> "trueno"             { return symbol(sym.BOOLEAN, yytext()); }
<YYINITIAL> "cupido"             { return symbol(sym.CHAR, yytext()); }
<YYINITIAL> "cometa"             { return symbol(sym.STRING, yytext()); }
<YYINITIAL> "abreempaque"        { return symbol(sym.OPEN_BRACKET, yytext()); }
<YYINITIAL> "cierraempaque"      { return symbol(sym.CLOSE_BRACKET, yytext()); }
<YYINITIAL> "abreregalo"         { return symbol(sym.OPEN_PAREN, yytext()); }
<YYINITIAL> "cierraregalo"       { return symbol(sym.CLOSE_PAREN, yytext()); }

/* Operators */
<YYINITIAL> "entrega"            { return symbol(sym.EQ); }

/* Arithmetic operators */

<YYINITIAL> "navidad"            { return symbol(sym.PLUS, yytext()); }
<YYINITIAL> "intercambio"        { return symbol(sym.MINUS, yytext()); }
<YYINITIAL> "nochebuena"         { return symbol(sym.TIMES, yytext()); }
<YYINITIAL> "reyes"              { return symbol(sym.DIV, yytext()); }
<YYINITIAL> "magos"              { return symbol(sym.MOD, yytext()); }
<YYINITIAL> "adviento"           { return symbol(sym.POW, yytext()); }
<YYINITIAL> "quien"              { return symbol(sym.INCREMENT, yytext()); }
<YYINITIAL> "grinch"             { return symbol(sym.DECREMENT, yytext()); }

<YYINITIAL> "mary"               { return symbol(sym.EQEQ, yytext()); }
<YYINITIAL> "openslae"           { return symbol(sym.NEQ, yytext()); }
<YYINITIAL> "snowball"           { return symbol(sym.LT, yytext()); }
<YYINITIAL> "evergreen"          { return symbol(sym.LE, yytext()); }
<YYINITIAL> "minstix"            { return symbol(sym.GT, yytext()); }
<YYINITIAL> "upatree"            { return symbol(sym.GE, yytext()); }

<YYINITIAL> "melchor"            { return symbol(sym.AND, yytext()); }
<YYINITIAL> "gaspar"             { return symbol(sym.OR, yytext()); }
<YYINITIAL> "baltazar"           { return symbol(sym.NOT, yytext()); }
<YYINITIAL> "finregalo"          { return symbol(sym.SEMICOLON, yytext()); }

<YYINITIAL> "elfo"               { return symbol(sym.IF, yytext()); }
<YYINITIAL> "hada"               { return symbol(sym.ELSE, yytext()); }
<YYINITIAL> "envuelve"           { return symbol(sym.WHILE, yytext()); }
<YYINITIAL> "duende"             { return symbol(sym.FOR, yytext()); }
<YYINITIAL> "varios"             { return symbol(sym.SWITCH, yytext()); }
<YYINITIAL> "historia"           { return symbol(sym.CASE, yytext()); }
<YYINITIAL> "ultimo"             { return symbol(sym.DEFAULT, yytext()); }
<YYINITIAL> "corta"              { return symbol(sym.BREAK, yytext()); }
<YYINITIAL> "envia"              { return symbol(sym.RETURN, yytext()); }
<YYINITIAL> "sigue"              { return symbol(sym.COLON, yytext()); }

<YYINITIAL> "narra"              { return symbol(sym.PRINT, yytext()); }
<YYINITIAL> "escucha"            { return symbol(sym.READ, yytext()); }


/* Defaults */
<YYINITIAL> "," { return symbol(sym.COMMA, yytext()); }
<YYINITIAL> \'[^\']\' { return symbol(sym.CHAR_LITERAL, new TokenInfo(yytext(), yyline, yycolumn)); }
<YYINITIAL> "-"?{Digit}+("."{Digit}+) { return symbol(sym.FLOAT_LITERAL, new TokenInfo(yytext(), yyline, yycolumn)); }
<YYINITIAL> "true"  { return symbol(sym.BOOLEAN_LITERAL, new TokenInfo("true", yyline, yycolumn)); }
<YYINITIAL> "false" { return symbol(sym.BOOLEAN_LITERAL, new TokenInfo("false", yyline, yycolumn)); }
<YYINITIAL> \'[^\']*\'    { return symbol(sym.STRING_LITERAL, new TokenInfo(yytext(), yyline, yycolumn)); }

/* Main */
<YYINITIAL> "_verano_"           { return symbol(sym.MAIN, yytext()); }


<YYINITIAL> {
  /* identifiers */
  {Identifier}                   { return symbol(sym.IDENTIFIER, new TokenInfo(yytext(), yyline, yycolumn)); }

  /* literals */
  {DecIntegerLiteral}            { return symbol(sym.INTEGER_LITERAL, new TokenInfo(yytext(), yyline, yycolumn)); }
  {DecFloatLiteral}              { return symbol(sym.FLOAT_LITERAL, new TokenInfo(yytext(), yyline, yycolumn)); }
  \"                             { string.setLength(0); yybegin(STRING); }

  /* comments */
  {Comment}                      { /* ignore */ }

  /* whitespace */
  {WhiteSpace}                   { /* ignore */ }
}

<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   String content = string.toString();
                                   string.setLength(0);
                                   return symbol(sym.STRING_LITERAL, new TokenInfo(content, yyline, yycolumn)); }
  [^\n\r\"\\]+                   { string.append(yytext()); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }
  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\\\                           { string.append('\\'); }
  \\[^\n\r]                      { string.append(yytext().substring(1)); }
  .                              { string.append(yytext()); }
}

/* error fallback */
<YYINITIAL> {
  [a-zA-Z0-9_]+ {
    System.err.println("Error Lexico: <" + yytext() + "> en línea: " + (yyline + 1) + ", columna: " + (yycolumn + 1));
  }
  . {
    System.err.println("Error Lexico: <" + yytext() + "> en línea: " + (yyline + 1) + ", columna: " + (yycolumn + 1));
  }
}