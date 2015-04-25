package words;
import java.lang.StringBuilder;

%%


%byaccj

%{
  private Words yyparser;
  public StringBuilder line = new StringBuilder();
  public int lineNumber = 1;
  public int charNumber = 0;
  public int depth = 0;

  public Yylex(java.io.Reader r, Words yyparser) {
    this(r);
    this.yyparser = yyparser;
  }

  private void onMatch() {
  	line.append(yytext());
  	charNumber = charNumber + yytext().length();
  }

  private void onNewLine() {
 	lineNumber++;
 	line = new StringBuilder();
 	charNumber = 0;
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
"."		{ onMatch(); return (int) yycharat(0); }

"{"		{ onMatch(); depth++; return (int) yycharat(0); }
"}"		{ onMatch(); depth--; return (int) yycharat(0); }

	/* Keywords */
"A" | "a" | "An" | "an"			{ onMatch(); return Words.A; }
"above"							{ onMatch(); return Words.ABOVE; }
"and"							{ onMatch(); return Words.AND; }
"anywhere"						{ onMatch(); return Words.ANYWHERE; }
"As" | "as"						{ onMatch(); return Words.AS; }
"at"							{ onMatch(); return Words.AT; }
"be"							{ onMatch(); return Words.BE; }
"below"							{ onMatch(); return Words.BELOW; }
"can"							{ onMatch(); return Words.CAN; }
"down"							{ onMatch(); return Words.DOWN; }
"Exit"							{ onMatch(); return Words.EXIT; }
"has"							{ onMatch(); return Words.HAS; }
"If"							{ onMatch(); return Words.IF; }
"is"							{ onMatch(); return Words.IS; }
"left"							{ onMatch(); return Words.LEFT; }
"long"							{ onMatch(); return Words.LONG; }
"Make"							{ onMatch(); return Words.MAKE; }
"move"							{ onMatch(); return Words.MOVE; }
"moves"							{ onMatch(); return Words.MOVES; }
"means"							{ onMatch(); return Words.MEANS; }
"next"							{ onMatch(); return Words.NEXT; }
"not"							{ onMatch(); return Words.NOT; }
"nothing"						{ onMatch(); return Words.NOTHING; }
"now"							{ onMatch(); return Words.NOW; }
"of"							{ onMatch(); return Words.OF; }
"or"							{ onMatch(); return Words.OR; }
"Remove"						{ onMatch(); return Words.REMOVE; }
"Repeat"						{ onMatch(); return Words.REPEAT; }
"Reset"							{ onMatch(); return Words.RESET; }
"right"							{ onMatch(); return Words.RIGHT; }
"say"							{ onMatch(); return Words.SAY; }
"says"							{ onMatch(); return Words.SAYS; }
"Stop"							{ onMatch(); return Words.STOP; }
"then"							{ onMatch(); return Words.THEN; }
"times"							{ onMatch(); return Words.TIMES; }
"to"							{ onMatch(); return Words.TO; }
"touches"						{ onMatch(); return Words.TOUCHES; }
"up"							{ onMatch(); return Words.UP; }
"wait"							{ onMatch(); return Words.WAIT; }
"waits"							{ onMatch(); return Words.WAITS; }
"Whenever"						{ onMatch(); return Words.WHENEVER; }
"which" | "that"				{ onMatch(); return Words.WHICH; }
"While"							{ onMatch(); return Words.WHILE; }
"with"							{ onMatch(); return Words.WITH; }

	/* Other lexemes */
"her" 			{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"his" 			{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"its" 			{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"their" 		{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"Her" 			{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"His" 			{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"Its" 			{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
"Their" 		{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
{REFERENCE} 	{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.REFERENCE; }
{IDENTIFIER} 	{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.IDENTIFIER; }
{NUM}			{ onMatch(); yyparser.yylval = new WordsVal(Double.parseDouble(yytext())); return Words.NUM; }
{STRING}		{ onMatch(); yyparser.yylval = new WordsVal(yytext()); return Words.STRING; }

	/* Multi-character operators */
{GEQ}			{ onMatch(); return Words.GEQ; }
{LEQ}			{ onMatch(); return Words.LEQ; }

	/* Whitespace (no action except counting line numbers) */
[ \t\r]+ 		{ onMatch(); }
\n				{ onNewLine(); }

	/* Backspace (error) */
\b				{ System.err.println("Sorry, backspace doesn't work"); }

	/* Error fallback */
[^]				{ System.err.println("Error: unexpected character '" + yytext() + "'"); return -1; }
