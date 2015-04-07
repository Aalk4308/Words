package words;

%%


%byaccj

%{
  private Words yyparser;
  public int lineNumber = 1;
  public int depth = 0;

  public Yylex(java.io.Reader r, Words yyparser) {
    this(r);
    this.yyparser = yyparser;
  }
%}

	/* Lexemes */
IDENTIFIER = [a-zA-Z_][a-zA-Z0-9_]*
REFERENCE = [a-zA-Z_][a-zA-Z0-9_]*'s
NUM = [0-9]+ ("." [0-9]+)?
STRING = \"(\\.|[^\"\n])*\"

	/* Multi-character Operators */
GEQ = ">="
LEQ = "<="


%%


	/* Comments (no action) */
"//".*			{ }

	/* Single-character Operators and Separators */
"+" | 
"-" | 
"*" | 
"/" | 
"^" |
"=" |
"<" |
">" | 
"(" | 
")" |
"[" | 
"]" |
","	|
"."		{ return (int) yycharat(0); }

"{"		{ depth++; return (int) yycharat(0); }
"}"		{ depth--; return (int) yycharat(0); }

	/* Keywords */
"A" | "a" | "An" | "an"			{ return Words.A; }
"and"							{ return Words.AND; }
"anywhere"						{ return Words.ANYWHERE; }
"As" | "as"						{ return Words.AS; }
"at"							{ return Words.AT; }
"be"							{ return Words.BE; }
"can"							{ return Words.CAN; }
"down"							{ return Words.DOWN; }
"Exit"							{ return Words.EXIT; }
"has"							{ return Words.HAS; }
"If"							{ return Words.IF; }
"is"							{ return Words.IS; }
"left"							{ return Words.LEFT; }
"long"							{ return Words.LONG; }
"Make"							{ return Words.MAKE; }
"move"							{ return Words.MOVE; }
"moves"							{ return Words.MOVES; }
"means"							{ return Words.MEANS; }
"not"							{ return Words.NOT; }
"nothing"						{ return Words.NOTHING; }
"now"							{ return Words.NOW; }
"of"							{ return Words.OF; }
"or"							{ return Words.OR; }
"Remove"						{ return Words.REMOVE; }
"Repeat"						{ return Words.REPEAT; }
"Reset"							{ return Words.RESET; }
"right"							{ return Words.RIGHT; }
"say"							{ return Words.SAY; }
"says"							{ return Words.SAYS; }
"Stop"							{ return Words.STOP; }
"then"							{ return Words.THEN; }
"times"							{ return Words.TIMES; }
"touches"						{ return Words.TOUCHES; }
"turns"							{ return Words.TURNS; }
"up"							{ return Words.UP; }
"wait"							{ return Words.WAIT; }
"waits"							{ return Words.WAITS; }
"Whenever"						{ return Words.WHENEVER; }
"which" | "that"				{ return Words.WHICH; }
"While"							{ return Words.WHILE; }
"with"							{ return Words.WITH; }

	/* Other lexemes */
{IDENTIFIER} 	{ yyparser.yylval = new WordsVal(yytext()); return Words.IDENTIFIER; }
"her" 			{ yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"his" 			{ yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"its" 			{ yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"their" 		{ yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
{REFERENCE} 	{ yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
{NUM}			{ yyparser.yylval = new WordsVal(Double.parseDouble(yytext())); return Words.NUM; }
{STRING}		{ yyparser.yylval = new WordsVal(yytext());	return Words.STRING; }

	/* Multi-character operators */
{GEQ}			{ return Words.GEQ; }
{LEQ}			{ return Words.LEQ; }

	/* Whitespace (no action except counting line numbers) */
[ \t\r]+ 		{ }
\n				{ lineNumber++; }

	/* Backspace (error) */
\b				{ System.err.println("Sorry, backspace doesn't work"); }

	/* Error fallback */
[^]				{ System.err.println("Error: unexpected character '" + yytext() + "'"); return -1; }
