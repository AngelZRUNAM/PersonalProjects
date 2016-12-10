%{
package Prueba;

import java.io.*;
import javax.swing.JTextArea;
import java.util.ArrayList;
%}


%token<obj> NUM
%token<sval> ID CHR 
%token INT FLOAT CHAR VOID RETURN
%token DEFINE STRUCT WHILE DO FOR STR
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

%type<obj> typeFunc typeVar
%type<obj> basic 
%type<obj> declVar 
%type<obj> declFunc 
%type<obj> array arrayValue
%type<obj> parameters
%type<obj> listControl control 
%type<obj> expresion termino factor
%type<obj> sumaresta
%type<obj> muldivmod
%type<obj> operAsig 
%type<obj> boolValue operRel 
%type<obj> asig increment
%type<obj> paramList
%type<obj> program

%start program

%%

program     : declVar {SemanticAcc.closeGlobal();} declFunc { $$ = SemanticAcc.getProgram((Sentencia)$3);} ;


boolValue   : expresion operRel expresion {$$ = SemanticAcc.boolValue((Expresion)$1,(String)$2,(Expresion)$3);}
            | NOT boolValue { $$ = SemanticAcc.boolValueNot((Bool)$2);}
            | LPAR boolValue RPAR { $$ = (Bool)$2; }
            | boolValue OR boolValue { $$ = SemanticAcc.boolValueOR((Bool)$1,(Bool)$3);}
            | boolValue AND boolValue { $$ = SemanticAcc.boolValueAND((Bool)$1,(Bool)$3);};

    
operRel     : GT        { $$ = new String(">"); }
            | LT        { $$ = new String("<"); }
            | LTEQ      { $$ = new String("<="); }
            | GTEQ      { $$ = new String(">="); }
            | NTEQ      { $$ = new String("!="); }
            | EQUAL     { $$ = new String("=="); }


expresion   : expresion sumaresta termino { $$ = SemanticAcc.sumaresta(((Expresion)$1),((String)$2),((Expresion)$3));}
            | termino {$$ = $1 ;} ;

termino     : termino muldivmod factor { $$ = SemanticAcc.muldivmod(((Expresion)$1),((String)$2),((Expresion)$3));} 
            | factor { $$ = $1; } ;

factor      : LPAR expresion RPAR { $$ = $2 ;}
            | ID { $$ = new Expresion((String)$1); } 
            | NUM { $$ = new Expresion((Number)$1); } 
            | CHR { $$ = new Expresion((String)$1,"char");}
            | arrayValue { $$ = $1 ; } 
            | ID LPAR paramList RPAR { $$ = SemanticAcc.callFunc((String)$1,(ParamList)$3);};
                
arrayValue  : ID LSBRK expresion RSBRK { $$ = SemanticAcc.arrayValue((String)$1,(Expresion)$3); }
            | arrayValue LSBRK expresion RSBRK { $$ = SemanticAcc.arrayValue((Expresion)$1,(Expresion)$3); }

sumaresta   : ADD { $$ = new String("+"); }
            | SUB { $$ = new String("-"); } ;

muldivmod   : MUL { $$ = new String("*"); } 
            | DIV { $$ = new String("/"); }
            | DIV { $$ = new String("%"); } ;

listControl : listControl control { $$ = SemanticAcc.listControl((Sentencia)$1,(Sentencia)$2);}
            | control {$$ = $1;};

control     : RETURN expresion PTOYC {$$ = SemanticAcc.controlReturn((Expresion)$2,(Type)funcTipo);}
            | ID operAsig expresion PTOYC {$$ = SemanticAcc.controlAsig(((String)$1),((String)$2),(Expresion)$3);} 
            | IF LPAR boolValue RPAR control { $$ = SemanticAcc.controIF((Bool)$3,(Sentencia)$5);}
            | IF LPAR boolValue RPAR control ELSE control { $$ = SemanticAcc.controlIFEl((Bool)$3,(Sentencia)$5, (Sentencia)$7);}
            | WHILE LPAR boolValue RPAR control { $$ = SemanticAcc.controlWhile((Bool)$3,(Sentencia)$5);} 
            | DO control WHILE LPAR boolValue RPAR PTOYC { $$ = SemanticAcc.controlDO((Sentencia)$2,(Bool)$5); } 
            | FOR LPAR asig PTOYC boolValue PTOYC increment RPAR control { $$ = SemanticAcc.controlFor((Sentencia)$3,(Bool)$5, (Code)$7, (Sentencia)$9);}
            | LCBRK listControl RCBRK { $$ = (Sentencia)$2 ; };

asig        : ID operAsig expresion {$$ = SemanticAcc.controlAsig(((String)$1),((String)$2),(Expresion)$3);} ;

increment   : ID ADD ADD { $$ = SemanticAcc.increment((String)$1,"+"); }
            | ID SUB SUB { $$ = SemanticAcc.increment((String)$1,"-"); };

operAsig    : ASIG          {$$ = new String("=");}
            | ADDASIG       {$$ = new String("+");}
            | MULASIG       {$$ = new String("*");}
            | SUBASIG       {$$ = new String("-");}
            | DIVASIG       {$$ = new String("/");}
            | MODASIG       {$$ = new String("%");} ;
            

declFunc    : DEFINE typeFunc ID LPAR {SemanticAcc.nuevoEntorno();} parameters RPAR {funcTipo = SemanticAcc.setFuncType((Type)$2,(String)$3,(ArrayList<Tipo>)$6);} LCBRK declVar listControl RCBRK declFunc { $$ = SemanticAcc.chackFunc((Sentencia)$11, (Sentencia)$13, funcTipo, (String)$3 ) ;}
            | { $$ = new Sentencia(); } ;

parameters  : parameters COMA typeVar ID { $$ = SemanticAcc.parameters((Type)$3, (String)$4, (ArrayList<Tipo>)$1); }
            | typeVar ID { $$ = SemanticAcc.parameters((Type)$1, (String)$2); }    
            | { $$ = new ArrayList<Tipo>();};
      

declVar     : declVar typeVar {tipo = (Type)$2;} lista PTOYC { $$ = SemanticAcc.declVar(); }
            | {$$ = new Object();};

typeVar     : basic {arrayBase = new Type(((Type)$1).type,((Type)$1).width);} array { $$ = (Type)$3; };

typeFunc    : basic {arrayBase = new Type(((Type)$1).type,((Type)$1).width);} array { $$ = (Type)$3; };
            | VOID { $$ = new Type(Parser.VOID); }

basic       : INT { $$ = new Type(Parser.INT); }
            | FLOAT { $$ = new Type(Parser.FLOAT); }
            | CHAR { $$ = new Type(Parser.CHAR); };

array       : LSBRK NUM RSBRK array  { $$ = SemanticAcc.insertType($2,$4,arrayBase.type); }
            | { $$ = new Type(arrayBase.type,arrayBase.width); };

lista       : lista COMA ID { SemanticAcc.addID($3,tipo,0);}
            | ID { SemanticAcc.addID($1,tipo,0);};

paramList   : paramList COMA expresion {$$ = SemanticAcc.parmList((ParamList)$1, (Expresion)$3); }
            | expresion { $$ = SemanticAcc.parmList((Expresion)$1);};

%%

JTextArea ts;
int width;
Type tipo;
Type arrayBase;
Type funcTipo;

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
    }catch (Exception ex){
        System.err.println("error : "+ex);
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


