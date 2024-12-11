%% 
/* Configuración general del escáner */
%public 
%class MiAnalizador 
%unicode 
%cup 

%% 
/* Definición de los tokens (expresiones regulares) */

/* Espacios en blanco (se ignoran) */
\s+ { /* no hacer nada */ }

/* Números enteros */
[0-9]+ { System.out.println("TOKEN: NUMERO (" + yytext() + ")"); }

/* Palabras reservadas */
"if" { System.out.println("TOKEN: IF"); }
"else" { System.out.println("TOKEN: ELSE"); }

/* Identificadores */
[a-zA-Z_][a-zA-Z0-9_]* { System.out.println("TOKEN: IDENTIFICADOR (" + yytext() + ")"); }

/* Símbolos */
"(" { System.out.println("TOKEN: PAREN_ABRE"); }
")" { System.out.println("TOKEN: PAREN_CIERRA"); }
"{" { System.out.println("TOKEN: LLAVE_ABRE"); }
"}" { System.out.println("TOKEN: LLAVE_CIERRA"); }
";" { System.out.println("TOKEN: PUNTO_Y_COMA"); }

/* Descartar cualquier otro carácter no reconocido */
. { System.out.println("TOKEN: DESCONOCIDO (" + yytext() + ")"); }
