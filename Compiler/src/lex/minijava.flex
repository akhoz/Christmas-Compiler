package lex;

import java_cup.runtime.Symbol;

/** Lexer for the custom language based on the given specifications. */

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%throws UnknownCharacterException

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
Comment = "#" {InputCharacter}* {LineTerminator}? | "\\_" {CommentContent} "\\_"
CommentContent = ([^\_]|\\_+[^/])*

Identifier = _[a-zA-Z0-9_]*_

DecIntegerLiteral = 0 | [1-9][0-9]*

%state STRING

%%

/* keywords */
<YYINITIAL> "_verano_"           { return symbol(sym.MAIN); }
<YYINITIAL> "elfo"               { return symbol(sym.IF); }
<YYINITIAL> "hada"               { return symbol(sym.ELSE); }
<YYINITIAL> "envuelve"           { return symbol(sym.WHILE); }
<YYINITIAL> "duende"             { return symbol(sym.FOR); }
<YYINITIAL> "varios"             { return symbol(sym.SWITCH); }
<YYINITIAL> "historia"           { return symbol(sym.CASE); }
<YYINITIAL> "ultimo"             { return symbol(sym.DEFAULT); }
<YYINITIAL> "corta"              { return symbol(sym.BREAK); }
<YYINITIAL> "envia"              { return symbol(sym.RETURN); }
<YYINITIAL> "rodolfo"            { return symbol(sym.INTEGER); }
<YYINITIAL> "bromista"           { return symbol(sym.FLOAT); }
<YYINITIAL> "trueno"             { return symbol(sym.BOOLEAN); }
<YYINITIAL> "cupido"             { return symbol(sym.CHAR); }
<YYINITIAL> "cometa"             { return symbol(sym.STRING); }
<YYINITIAL> "abrecuento"         { return symbol(sym.OPEN_BLOCK); }
<YYINITIAL> "cierracuento"       { return symbol(sym.CLOSE_BLOCK); }
<YYINITIAL> "abreempaque"        { return symbol(sym.OPEN_BRACKET); }
<YYINITIAL> "cierraempaque"      { return symbol(sym.CLOSE_BRACKET); }
<YYINITIAL> "abreregalo"         { return symbol(sym.OPEN_PAREN); }
<YYINITIAL> "cierraregalo"       { return symbol(sym.CLOSE_PAREN); }
<YYINITIAL> "narra"              { return symbol(sym.PRINT); }
<YYINITIAL> "escucha"            { return symbol(sym.READ); }

/* operators */
<YYINITIAL> "entrega"            { return symbol(sym.EQ); }
<YYINITIAL> "mary"               { return symbol(sym.EQEQ); }
<YYINITIAL> "openslae"           { return symbol(sym.NEQ); }
<YYINITIAL> "navidad"            { return symbol(sym.PLUS); }
<YYINITIAL> "intercambio"        { return symbol(sym.MINUS); }
<YYINITIAL> "nochebuena"         { return symbol(sym.TIMES); }
<YYINITIAL> "reyes"              { return symbol(sym.DIV); }
<YYINITIAL> "magos"              { return symbol(sym.MOD); }
<YYINITIAL> "adviento"           { return symbol(sym.POW); }
<YYINITIAL> "quien"              { return symbol(sym.INCREMENT); }
<YYINITIAL> "grinch"             { return symbol(sym.DECREMENT); }
<YYINITIAL> "snowball"           { return symbol(sym.LT); }
<YYINITIAL> "evergreen"          { return symbol(sym.LE); }
<YYINITIAL> "minstix"            { return symbol(sym.GT); }
<YYINITIAL> "upatree"            { return symbol(sym.GE); }
<YYINITIAL> "melchor"            { return symbol(sym.AND); }
<YYINITIAL> "gaspar"             { return symbol(sym.OR); }
<YYINITIAL> "baltazar"           { return symbol(sym.NOT); }
<YYINITIAL> "sigue"              { return symbol(sym.COLON); }
<YYINITIAL> "finregalo"          { return symbol(sym.SEMICOLON); }

/* literals */
<YYINITIAL> {Identifier}         { return symbol(sym.IDENTIFIER); }
<YYINITIAL> {DecIntegerLiteral}  { return symbol(sym.INTEGER_LITERAL); }
<YYINITIAL> \"                   { string.setLength(0); yybegin(STRING); }

/* comments and whitespace */
<YYINITIAL> {Comment}            { /* ignore */ }
<YYINITIAL> {WhiteSpace}         { /* ignore */ }

<STRING> {
  \"                             { yybegin(YYINITIAL);
                                   return symbol(sym.STRING_LITERAL, string.toString()); }
  [^\n\r\"\\]+                   { string.append(yytext()); }
  \\t                            { string.append('\t'); }
  \\n                            { string.append('\n'); }
  \\r                            { string.append('\r'); }
  \\\"                           { string.append('\"'); }
  \\                             { string.append('\\'); }
}

/* error fallback */
[^]                              { throw new UnknownCharacterException(yytext()); }