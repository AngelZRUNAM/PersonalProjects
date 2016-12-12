//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 2 "parser.y"
package Prueba;

import java.io.*;
import javax.swing.JTextArea;
import java.util.ArrayList;
//#line 23 "Parser.java"




public class Parser
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short NUM=257;
public final static short ID=258;
public final static short CHR=259;
public final static short INT=260;
public final static short FLOAT=261;
public final static short CHAR=262;
public final static short VOID=263;
public final static short RETURN=264;
public final static short DEFINE=265;
public final static short STRUCT=266;
public final static short WHILE=267;
public final static short DO=268;
public final static short FOR=269;
public final static short STR=270;
public final static short IF=271;
public final static short PTOYC=272;
public final static short COMA=273;
public final static short ASIG=274;
public final static short ADDASIG=275;
public final static short MULASIG=276;
public final static short SUBASIG=277;
public final static short DIVASIG=278;
public final static short MODASIG=279;
public final static short OR=280;
public final static short AND=281;
public final static short NOT=282;
public final static short EQUAL=283;
public final static short GT=284;
public final static short LT=285;
public final static short LTEQ=286;
public final static short GTEQ=287;
public final static short NTEQ=288;
public final static short ADD=289;
public final static short SUB=290;
public final static short MUL=291;
public final static short DIV=292;
public final static short MOD=293;
public final static short LPAR=294;
public final static short RPAR=295;
public final static short LCBRK=296;
public final static short RCBRK=297;
public final static short LSBRK=298;
public final static short RSBRK=299;
public final static short IFX=300;
public final static short ELSE=301;
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
   22,    0,   17,   17,   17,   17,   17,   18,   18,   18,
   18,   18,   18,   11,   11,   12,   12,   13,   13,   13,
   13,   13,   13,    7,    7,   14,   14,   15,   15,   15,
    9,    9,   10,   10,   10,   10,   10,   10,   10,   10,
   19,   20,   20,   16,   16,   16,   16,   16,   16,   23,
   24,    5,    5,    8,    8,    8,   26,    4,    4,   27,
    2,   28,    1,    1,    3,    3,    3,    6,    6,   25,
   25,   21,   21,
};
final static short yylen[] = {                            2,
    0,    3,    3,    2,    3,    3,    3,    1,    1,    1,
    1,    1,    1,    3,    1,    3,    1,    3,    1,    1,
    1,    1,    4,    4,    4,    1,    1,    1,    1,    1,
    2,    1,    3,    4,    5,    7,    5,    7,    9,    3,
    3,    3,    3,    1,    1,    1,    1,    1,    1,    0,
    0,   13,    0,    4,    2,    0,    0,    5,    0,    0,
    3,    0,    3,    1,    1,    1,    1,    4,    0,    3,
    1,    3,    1,
};
final static short yydefred[] = {                        59,
    0,    0,   65,   66,   67,   57,   60,    0,    0,    0,
    0,    2,   71,    0,    0,   61,   64,    0,   62,   58,
    0,    0,    0,    0,   70,    0,   50,   63,   68,    0,
    0,    0,   55,    0,   51,    0,    0,   54,   59,    0,
    0,    0,    0,    0,    0,    0,    0,    0,   32,   44,
   45,   46,   47,   48,   49,    0,   20,    0,   21,    0,
    0,    0,    0,   17,    0,    0,    0,    0,    0,    0,
   31,    0,    0,    0,    0,    0,   33,   26,   27,    0,
   28,   29,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   40,   52,   34,    0,    0,    0,   18,    0,    0,
   16,    4,    0,    0,   13,    8,    9,   10,   11,   12,
    0,    0,    0,    0,    0,    0,    0,    0,    0,   23,
   24,   25,    5,    0,    0,    7,   37,    0,    0,    0,
    0,    0,    0,    0,    0,   38,    0,    0,   36,    0,
    0,    0,   42,   43,   39,
};
final static short yydgoto[] = {                          1,
   18,    6,    7,    2,   12,   16,   61,   32,   48,   49,
   86,   63,   64,   80,   83,   56,   87,  111,   90,  138,
   96,    8,   30,   37,   14,    9,   10,   24,
};
final static short yysindex[] = {                         0,
    0, -136,    0,    0,    0,    0,    0, -262, -244, -274,
  -37,    0,    0,  -70, -225,    0,    0, -211,    0,    0,
 -192, -226, -206, -274,    0, -274,    0,    0,    0, -136,
 -164, -258,    0, -136,    0, -153, -189,    0,    0, -165,
  -35, -248, -147, -186, -142, -133, -186, -241,    0,    0,
    0,    0,    0,    0,    0, -248,    0, -250,    0, -248,
 -134, -144,  -92,    0, -251,  -99,  -77, -251, -229, -262,
    0, -123, -248, -248, -277, -248,    0,    0,    0, -248,
    0,    0, -248, -251, -251,  -52,  -97, -122,  -35,  -84,
  -94,    0,    0,    0,  -43, -254,  -93,    0,  -71,  -92,
    0,    0,  -73,  -88,    0,    0,    0,    0,    0,    0,
 -248, -251, -251, -186, -251, -248, -251, -186, -248,    0,
    0,    0,    0,  -43,  -61,    0,    0,  -86,  -43,  -51,
  -74,  -43,  -68,  -50, -186,    0,  -41,  -45,    0,  -38,
  -36, -186,    0,    0,    0,
};
final static short yyrindex[] = {                         0,
    0,    1,    0,    0,    0,    0,    0,  245,    0,   -6,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,   -6,    0,   -6,    0,    0,    0, -220,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0, -172,    0,    0,
 -151,    0, -130,    0,    0,    0,    0,    0,    0,  245,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -215,    0,    0,    0,    0, -110,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,  -90, -203,    0,    0,    0,  -19,    0,
 -207, -208,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,
};
final static short yygindex[] = {                         0,
    0,   29,  244,  217,  187,   60,    0,    0,  211,  -44,
  -40,  179,  177,    0,    0,  172,  -63,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,
};
final static int YYTABLESIZE=266;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         66,
    1,   62,   11,   71,   91,   57,   58,   59,   57,   58,
   59,   78,   79,   13,   34,   72,   41,   98,  119,   75,
  102,  104,   42,   15,   71,   43,   44,   45,   41,   46,
   84,   22,   95,   97,   42,   99,   35,   43,   44,   45,
  120,   46,   85,   73,  103,   60,   23,   74,  125,  126,
   35,  128,   56,  130,   47,   70,   35,   73,   31,   35,
   35,   35,   36,   35,   72,   25,   47,   92,    6,  127,
  124,   41,   26,  131,   56,  129,    6,   42,  132,   73,
   43,   44,   45,   28,   46,   29,   72,   27,   35,   35,
  139,    6,   41,   33,    3,    4,    5,  145,   42,   19,
   19,   43,   44,   45,   38,   46,   39,   19,   19,   47,
   19,   19,   19,   19,   19,   19,   19,   19,   19,   19,
   22,   22,   19,    3,    4,    5,   19,   77,   22,   22,
   47,   22,   22,   22,   22,   22,   22,   22,   22,   22,
   22,   15,   15,   22,   78,   79,   65,   22,   94,   15,
   15,   67,   15,   15,   15,   15,   15,   15,   15,   15,
   68,   14,   14,   76,   15,   78,   79,   88,   15,   14,
   14,  115,   14,   14,   14,   14,   14,   14,   14,   14,
   89,    3,  112,  113,   14,  112,  113,  117,   14,    3,
    3,  112,  113,  112,  113,   78,   79,  114,   81,   82,
  118,   20,   21,  136,    3,  121,  123,  137,  133,  105,
  106,  107,  108,  109,  110,   78,   79,   78,   79,  113,
  134,   98,    3,    4,    5,   17,  135,  122,  112,  113,
  105,  106,  107,  108,  109,  110,   78,   79,   50,   51,
   52,   53,   54,   55,   53,   78,   79,  140,  141,  142,
  143,   69,   41,  144,   19,   40,   93,   69,  100,  101,
  116,    0,    0,    0,    0,    1,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         44,
    0,   42,  265,   48,   68,  257,  258,  259,  257,  258,
  259,  289,  290,  258,  273,   56,  258,  295,  273,   60,
   84,   85,  264,  298,   69,  267,  268,  269,  258,  271,
  282,  257,   73,   74,  264,   76,  295,  267,  268,  269,
  295,  271,  294,  294,   85,  294,  258,  298,  112,  113,
  258,  115,  273,  117,  296,  297,  264,  273,   30,  267,
  268,  269,   34,  271,  273,  258,  296,  297,  272,  114,
  111,  258,  299,  118,  295,  116,  280,  264,  119,  295,
  267,  268,  269,   24,  271,   26,  295,  294,  296,  297,
  135,  295,  258,  258,  260,  261,  262,  142,  264,  272,
  273,  267,  268,  269,  258,  271,  296,  280,  281,  296,
  283,  284,  285,  286,  287,  288,  289,  290,  291,  292,
  272,  273,  295,  260,  261,  262,  299,  272,  280,  281,
  296,  283,  284,  285,  286,  287,  288,  289,  290,  291,
  292,  272,  273,  295,  289,  290,  294,  299,  272,  280,
  281,  294,  283,  284,  285,  286,  287,  288,  289,  290,
  294,  272,  273,  298,  295,  289,  290,  267,  299,  280,
  281,  294,  283,  284,  285,  286,  287,  288,  289,  290,
  258,  272,  280,  281,  295,  280,  281,  272,  299,  280,
  281,  280,  281,  280,  281,  289,  290,  295,  291,  292,
  295,  272,  273,  272,  295,  299,  295,  258,  295,  283,
  284,  285,  286,  287,  288,  289,  290,  289,  290,  281,
  272,  295,  260,  261,  262,  263,  301,  299,  280,  281,
  283,  284,  285,  286,  287,  288,  289,  290,  274,  275,
  276,  277,  278,  279,    0,  289,  290,  289,  290,  295,
  289,  258,  272,  290,   11,   39,   70,   47,   80,   83,
   89,   -1,   -1,   -1,   -1,  265,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=301;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"NUM","ID","CHR","INT","FLOAT","CHAR","VOID","RETURN","DEFINE",
"STRUCT","WHILE","DO","FOR","STR","IF","PTOYC","COMA","ASIG","ADDASIG",
"MULASIG","SUBASIG","DIVASIG","MODASIG","OR","AND","NOT","EQUAL","GT","LT",
"LTEQ","GTEQ","NTEQ","ADD","SUB","MUL","DIV","MOD","LPAR","RPAR","LCBRK",
"RCBRK","LSBRK","RSBRK","IFX","ELSE",
};
final static String yyrule[] = {
"$accept : program",
"$$1 :",
"program : declVar $$1 declFunc",
"boolValue : expresion operRel expresion",
"boolValue : NOT boolValue",
"boolValue : LPAR boolValue RPAR",
"boolValue : boolValue OR boolValue",
"boolValue : boolValue AND boolValue",
"operRel : GT",
"operRel : LT",
"operRel : LTEQ",
"operRel : GTEQ",
"operRel : NTEQ",
"operRel : EQUAL",
"expresion : expresion sumaresta termino",
"expresion : termino",
"termino : termino muldivmod factor",
"termino : factor",
"factor : LPAR expresion RPAR",
"factor : ID",
"factor : NUM",
"factor : CHR",
"factor : arrayValue",
"factor : ID LPAR paramList RPAR",
"arrayValue : ID LSBRK expresion RSBRK",
"arrayValue : arrayValue LSBRK expresion RSBRK",
"sumaresta : ADD",
"sumaresta : SUB",
"muldivmod : MUL",
"muldivmod : DIV",
"muldivmod : DIV",
"listControl : listControl control",
"listControl : control",
"control : RETURN expresion PTOYC",
"control : ID operAsig expresion PTOYC",
"control : IF LPAR boolValue RPAR control",
"control : IF LPAR boolValue RPAR control ELSE control",
"control : WHILE LPAR boolValue RPAR control",
"control : DO control WHILE LPAR boolValue RPAR PTOYC",
"control : FOR LPAR asig PTOYC boolValue PTOYC increment RPAR control",
"control : LCBRK listControl RCBRK",
"asig : ID operAsig expresion",
"increment : ID ADD ADD",
"increment : ID SUB SUB",
"operAsig : ASIG",
"operAsig : ADDASIG",
"operAsig : MULASIG",
"operAsig : SUBASIG",
"operAsig : DIVASIG",
"operAsig : MODASIG",
"$$2 :",
"$$3 :",
"declFunc : DEFINE typeFunc ID LPAR $$2 parameters RPAR $$3 LCBRK declVar listControl RCBRK declFunc",
"declFunc :",
"parameters : parameters COMA typeVar ID",
"parameters : typeVar ID",
"parameters :",
"$$4 :",
"declVar : declVar typeVar $$4 lista PTOYC",
"declVar :",
"$$5 :",
"typeVar : basic $$5 array",
"$$6 :",
"typeFunc : basic $$6 array",
"typeFunc : VOID",
"basic : INT",
"basic : FLOAT",
"basic : CHAR",
"array : LSBRK NUM RSBRK array",
"array :",
"lista : lista COMA ID",
"lista : ID",
"paramList : paramList COMA expresion",
"paramList : expresion",
};

//#line 146 "parser.y"

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


//#line 436 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 50 "parser.y"
{SemanticAcc.closeGlobal();}
break;
case 2:
//#line 50 "parser.y"
{ yyval.obj = SemanticAcc.getProgram((Sentencia)val_peek(0).obj);}
break;
case 3:
//#line 53 "parser.y"
{yyval.obj = SemanticAcc.boolValue((Expresion)val_peek(2).obj,(String)val_peek(1).obj,(Expresion)val_peek(0).obj);}
break;
case 4:
//#line 54 "parser.y"
{ yyval.obj = SemanticAcc.boolValueNot((Bool)val_peek(0).obj);}
break;
case 5:
//#line 55 "parser.y"
{ yyval.obj = (Bool)val_peek(1).obj; }
break;
case 6:
//#line 56 "parser.y"
{ yyval.obj = SemanticAcc.boolValueOR((Bool)val_peek(2).obj,(Bool)val_peek(0).obj);}
break;
case 7:
//#line 57 "parser.y"
{ yyval.obj = SemanticAcc.boolValueAND((Bool)val_peek(2).obj,(Bool)val_peek(0).obj);}
break;
case 8:
//#line 60 "parser.y"
{ yyval.obj = new String(">"); }
break;
case 9:
//#line 61 "parser.y"
{ yyval.obj = new String("<"); }
break;
case 10:
//#line 62 "parser.y"
{ yyval.obj = new String("<="); }
break;
case 11:
//#line 63 "parser.y"
{ yyval.obj = new String(">="); }
break;
case 12:
//#line 64 "parser.y"
{ yyval.obj = new String("!="); }
break;
case 13:
//#line 65 "parser.y"
{ yyval.obj = new String("=="); }
break;
case 14:
//#line 68 "parser.y"
{ yyval.obj = SemanticAcc.sumaresta(((Expresion)val_peek(2).obj),((String)val_peek(1).obj),((Expresion)val_peek(0).obj));}
break;
case 15:
//#line 69 "parser.y"
{yyval.obj = val_peek(0).obj ;}
break;
case 16:
//#line 71 "parser.y"
{ yyval.obj = SemanticAcc.muldivmod(((Expresion)val_peek(2).obj),((String)val_peek(1).obj),((Expresion)val_peek(0).obj));}
break;
case 17:
//#line 72 "parser.y"
{ yyval.obj = val_peek(0).obj; }
break;
case 18:
//#line 74 "parser.y"
{ yyval.obj = val_peek(1).obj ;}
break;
case 19:
//#line 75 "parser.y"
{ yyval.obj = new Expresion((String)val_peek(0).sval); }
break;
case 20:
//#line 76 "parser.y"
{ yyval.obj = new Expresion((Number)val_peek(0).obj); }
break;
case 21:
//#line 77 "parser.y"
{ yyval.obj = new Expresion((String)val_peek(0).sval,"char");}
break;
case 22:
//#line 78 "parser.y"
{ yyval.obj = val_peek(0).obj ; }
break;
case 23:
//#line 79 "parser.y"
{ yyval.obj = SemanticAcc.callFunc((String)val_peek(3).sval,(ParamList)val_peek(1).obj);}
break;
case 24:
//#line 81 "parser.y"
{ yyval.obj = SemanticAcc.arrayValue((String)val_peek(3).sval,(Expresion)val_peek(1).obj); }
break;
case 25:
//#line 82 "parser.y"
{ yyval.obj = SemanticAcc.arrayValue((Expresion)val_peek(3).obj,(Expresion)val_peek(1).obj); }
break;
case 26:
//#line 84 "parser.y"
{ yyval.obj = new String("+"); }
break;
case 27:
//#line 85 "parser.y"
{ yyval.obj = new String("-"); }
break;
case 28:
//#line 87 "parser.y"
{ yyval.obj = new String("*"); }
break;
case 29:
//#line 88 "parser.y"
{ yyval.obj = new String("/"); }
break;
case 30:
//#line 89 "parser.y"
{ yyval.obj = new String("%"); }
break;
case 31:
//#line 91 "parser.y"
{ yyval.obj = SemanticAcc.listControl((Sentencia)val_peek(1).obj,(Sentencia)val_peek(0).obj);}
break;
case 32:
//#line 92 "parser.y"
{yyval.obj = val_peek(0).obj;}
break;
case 33:
//#line 94 "parser.y"
{yyval.obj = SemanticAcc.controlReturn((Expresion)val_peek(1).obj,(Type)funcTipo);}
break;
case 34:
//#line 95 "parser.y"
{yyval.obj = SemanticAcc.controlAsig(((String)val_peek(3).sval),((String)val_peek(2).obj),(Expresion)val_peek(1).obj);}
break;
case 35:
//#line 96 "parser.y"
{ yyval.obj = SemanticAcc.controIF((Bool)val_peek(2).obj,(Sentencia)val_peek(0).obj);}
break;
case 36:
//#line 97 "parser.y"
{ yyval.obj = SemanticAcc.controlIFEl((Bool)val_peek(4).obj,(Sentencia)val_peek(2).obj, (Sentencia)val_peek(0).obj);}
break;
case 37:
//#line 98 "parser.y"
{ yyval.obj = SemanticAcc.controlWhile((Bool)val_peek(2).obj,(Sentencia)val_peek(0).obj);}
break;
case 38:
//#line 99 "parser.y"
{ yyval.obj = SemanticAcc.controlDO((Sentencia)val_peek(5).obj,(Bool)val_peek(2).obj); }
break;
case 39:
//#line 100 "parser.y"
{ yyval.obj = SemanticAcc.controlFor((Sentencia)val_peek(6).obj,(Bool)val_peek(4).obj, (Code)val_peek(2).obj, (Sentencia)val_peek(0).obj);}
break;
case 40:
//#line 101 "parser.y"
{ yyval.obj = (Sentencia)val_peek(1).obj ; }
break;
case 41:
//#line 103 "parser.y"
{yyval.obj = SemanticAcc.controlAsig(((String)val_peek(2).sval),((String)val_peek(1).obj),(Expresion)val_peek(0).obj);}
break;
case 42:
//#line 105 "parser.y"
{ yyval.obj = SemanticAcc.increment((String)val_peek(2).sval,"+"); }
break;
case 43:
//#line 106 "parser.y"
{ yyval.obj = SemanticAcc.increment((String)val_peek(2).sval,"-"); }
break;
case 44:
//#line 108 "parser.y"
{yyval.obj = new String("=");}
break;
case 45:
//#line 109 "parser.y"
{yyval.obj = new String("+");}
break;
case 46:
//#line 110 "parser.y"
{yyval.obj = new String("*");}
break;
case 47:
//#line 111 "parser.y"
{yyval.obj = new String("-");}
break;
case 48:
//#line 112 "parser.y"
{yyval.obj = new String("/");}
break;
case 49:
//#line 113 "parser.y"
{yyval.obj = new String("%");}
break;
case 50:
//#line 116 "parser.y"
{SemanticAcc.nuevoEntorno();}
break;
case 51:
//#line 116 "parser.y"
{funcTipo = SemanticAcc.setFuncType((Type)val_peek(5).obj,(String)val_peek(4).sval,(ArrayList<Tipo>)val_peek(1).obj);}
break;
case 52:
//#line 116 "parser.y"
{ yyval.obj = SemanticAcc.checkFunc((Sentencia)val_peek(2).obj, (Sentencia)val_peek(0).obj, funcTipo, (String)val_peek(10).sval ) ;}
break;
case 53:
//#line 117 "parser.y"
{ yyval.obj = new Sentencia(); }
break;
case 54:
//#line 119 "parser.y"
{ yyval.obj = SemanticAcc.parameters((Type)val_peek(1).obj, (String)val_peek(0).sval, (ArrayList<Tipo>)val_peek(3).obj); }
break;
case 55:
//#line 120 "parser.y"
{ yyval.obj = SemanticAcc.parameters((Type)val_peek(1).obj, (String)val_peek(0).sval); }
break;
case 56:
//#line 121 "parser.y"
{ yyval.obj = new ArrayList<Tipo>();}
break;
case 57:
//#line 124 "parser.y"
{tipo = (Type)val_peek(0).obj;}
break;
case 58:
//#line 124 "parser.y"
{ yyval.obj = new Object(); }
break;
case 59:
//#line 125 "parser.y"
{yyval.obj = new Object();}
break;
case 60:
//#line 127 "parser.y"
{arrayBase = new Type(((Type)val_peek(0).obj).type,((Type)val_peek(0).obj).width);}
break;
case 61:
//#line 127 "parser.y"
{ yyval.obj = (Type)val_peek(0).obj; }
break;
case 62:
//#line 129 "parser.y"
{arrayBase = new Type(((Type)val_peek(0).obj).type,((Type)val_peek(0).obj).width);}
break;
case 63:
//#line 129 "parser.y"
{ yyval.obj = (Type)val_peek(0).obj; }
break;
case 64:
//#line 130 "parser.y"
{ yyval.obj = new Type(Parser.VOID); }
break;
case 65:
//#line 132 "parser.y"
{ yyval.obj = new Type(Parser.INT); }
break;
case 66:
//#line 133 "parser.y"
{ yyval.obj = new Type(Parser.FLOAT); }
break;
case 67:
//#line 134 "parser.y"
{ yyval.obj = new Type(Parser.CHAR); }
break;
case 68:
//#line 136 "parser.y"
{ yyval.obj = SemanticAcc.insertType(val_peek(2).obj,val_peek(0).obj,arrayBase.type); }
break;
case 69:
//#line 137 "parser.y"
{ yyval.obj = new Type(arrayBase.type,arrayBase.width); }
break;
case 70:
//#line 139 "parser.y"
{ SemanticAcc.addID(val_peek(0).sval,tipo,0);}
break;
case 71:
//#line 140 "parser.y"
{ SemanticAcc.addID(val_peek(0).sval,tipo,0);}
break;
case 72:
//#line 142 "parser.y"
{yyval.obj = SemanticAcc.parmList((ParamList)val_peek(2).obj, (Expresion)val_peek(0).obj); }
break;
case 73:
//#line 143 "parser.y"
{ yyval.obj = SemanticAcc.parmList((Expresion)val_peek(0).obj);}
break;
//#line 877 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
