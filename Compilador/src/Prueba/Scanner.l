package Prueba;

import java.io.*;
%%

%byaccj
%line

%{

private Parser yyparser ;

 
public Yylex(java.io.Reader r, Parser p ){
    this(r);
    yyline = 1 ;
    yyparser = p  ;
}

  
public int getYyline(){
     return yyline ;
}


%}


%%
//Palabras reservadas:

"int"   { return Parser.INT;}

"float" { return Parser.FLOAT;}

"if"    { return Parser.IF;}

"else"  { return Parser.ELSE; }

"define" { return Parser.DEFINE; }   

"struct" { return Parser.STRUCT; }   
  
"void"  { return Parser.VOID; }     
 
"char"  { return Parser.CHAR; }     

"while" { return Parser.WHILE; }    

"do"    { return Parser.DO; }      

"for"   { return Parser.FOR; }     

"return" { return Parser.RETURN; }  

"str"   { return Parser.STR; }   


//Operadores logicos

"&&"    { return Parser.AND; }

"||"    { return Parser.OR; }

"!"    { return Parser.NOT; }       

//Operadores de asignacion

"+="    { return Parser.ADDASIG;}

"*="    { return Parser.MULASIG;}

"-="    { return Parser.SUBASIG;}    

"/="    { return Parser.DIVASIG;}    

"%="    { return Parser.MODASIG;}    

"="    { return Parser.ASIG ;}

//Operadores Relacionales

"=="    {return Parser.EQUAL;}

"<"     { return Parser.LT; }

"<="    { return Parser.LTEQ; }   

">"     { return Parser.GT;}

">="    { return Parser.GTEQ; }   

"!="    { return Parser.NTEQ; }   

//Operadores Aritmeticos

"+"    {  return Parser.ADD; }

"*"     { return Parser.MUL ; }

"-"     { return Parser.SUB ; }   

"/"     { return Parser.DIV ; }   

"%"     { return Parser.MOD ; }   

//Simbolos 

"("    { return Parser.LPAR ; }

")"    { return Parser.RPAR ; }

"{"    { return Parser.LCBRK ;}   

"}"    { return Parser.RCBRK ;}   

"["    { return Parser.LSBRK ;}   

"]"    { return Parser.RSBRK ;}   

";"    { return Parser.PTOYC ;}

","    { return Parser.COMA ;}

[0-9]+ {  yyparser.yylval.obj = new Number(new Integer(yytext()),"int");
          return Parser.NUM ; 
       }
       
[0-9]+"."[0-9]* {  yyparser.yylval.obj = new Number(new Double(yytext()),"float" ); 
          return Parser.NUM ; 
       }
       
[a-zA-Z][a-zA-Z0-9_]*
       {  yyparser.yylval = new ParserVal( (String)yytext() ); 
          return Parser.ID ;  
       }

"'"[a-zA-z0-9]"'"   { yyparser.yylval = new ParserVal( (String)yytext() );
                                                return Parser.CHR; 
                                            }    
       
[\r\n]     { yyline ++ ;  }

[ \t]+ {  }


.      {  // token desconocido: se produce un mensaje de error 
          yyparser.yyerror("el(los) carácter(es) '"+yytext()+"' no forma(n) ningún token conocido"); 
       }
