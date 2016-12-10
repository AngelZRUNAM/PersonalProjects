%{
package Prueba;

import java.io.*;
import javax.swing.JTextArea;
%}


%token<obj> NUM
%token<sval> ID
%token INT FLOAT CHAR VOID RETURN
%token DEFINE STRUCT WHILE DO FOR
%token CHR STR 
%token IF
%token PTOYC

%left COMA
%left ASIG ADDASIG MULASIG SUBASIG DIVASIG MODASIG
%left OR
%left AND
%left NOT
%left EQUAL
%left GT LT LTEQ GTEQ NTEQ
%left ADD SUB
%left MUL DIV MOD
%left LPAR RPAR LCBRK RCBRK LSBRK RSBRK 
%nonassoc IFX
%nonassoc ELSE

%type<obj> type
%type<obj> basic
%type<obj> declVar 
%type<obj> declFunc 
%type<obj> array 
%type<obj> parameters
%type<obj> listControl control 
%type<obj> expresion termino factor
%type<obj> sumaresta
%type<obj> muldivmod

%start expresion

%%

expresion   : expresion sumaresta termino { $$ = new Object();}
            | termino {$$ = $1 ;} ;

termino     : termino muldivmod factor { $$ = new Object();} 
            | factor { $$ = $1; } ;

factor      : LPAR expresion RPAR { $$ = $2 ;}
            | ID { $$ = new Object(); } 
            | NUM { $$ = new Object(); } ;

sumaresta   : ADD { $$ = new String("ADD"); }
            | SUB { $$ = new String("SUB"); } ;

muldivmod   : MUL { $$ = new String("MUL"); } 
            | DIV { $$ = new String("DIV"); }
            | DIV { $$ = new String("MOD"); } ;






declFunc    : DEFINE type ID LPAR parameters RPAR LCBRK declVar listControl RCBRK declFunc { $$ = new Object(); }
            | { $$ = new Object(); } ;

listControl : listControl control { $$ = SemanticAcc.listControl($1,$2);}
            | control {$$ = $1;};

control     : RETURN PTOYC {$$ = new Sentencia();};


parameters  : parameters COMA type ID { $$ = $1; }
            | type ID { $$ = new Object(); };

declVar     : declVar type{tipo = (Type)$2;} lista PTOYC { $$ = SemanticAcc.declVar(); }
            | {$$ = new Object();};

type        : basic {arrayBase = new Type(((Type)$1).type,((Type)$1).width);} array { $$ = (Type)$3; };

basic       : INT { $$ = new Type(Parser.INT); }
            | FLOAT { $$ = new Type(Parser.FLOAT); }
            | VOID { $$ = new Type(Parser.VOID); }
            | CHAR { $$ = new Type(Parser.CHAR); };

array       : LSBRK NUM RSBRK array  { $$ = SemanticAcc.insertType($2,$4);
                                      }
            | { $$ = new Type(arrayBase.type,arrayBase.width); System.out.println(((Type)arrayBase).toString());};

lista       : lista COMA ID { SemanticAcc.addID($3,tipo);}
            | ID { SemanticAcc.addID($1,tipo);};
%%

JTextArea ts;
int width;
Type tipo;
Type arrayBase;

// Referencia al analizador Léxicio
Yylex lexer;

/*
 * Constructor: recibe una referencia del archivo a leer
 * y crea el objeto del analizador léxico
 */
public Parser(Reader r, JTextArea ts){
    lexer = new Yylex(r, this) ;
    this.ts = ts;
    SemanticAcc.yyparser = this;
    SemanticAcc.init(ts);
}
    
/*
 * Método que encapsula el método yylex de la clase Yylex
 * retorna el token siguiente.
 */
private int yylex (){
    int yyl_return = -1;
    try{  
        yylval = new ParserVal(0);
        yyl_return = lexer.yylex();
    }catch (IOException e){
        System.err.println("error de E/S:"+e);
    }
    return yyl_return;
}

/*
 * Método para el manejo de errores
 */
public void yyerror (String msg) 
{
    System.err.println("Error en línea "+Integer.toString(lexer.getYyline())+" : "+msg);
}


