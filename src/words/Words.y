%{
  import java.io.*;
  import words.environment.*;
  import words.exceptions.*;
  import words.ast.*;
%}

	/* Keyword-related tokens */
%token A
%token ABOVE
%token AND
%token ANYWHERE
%token AS
%token AT
%token BE
%token BELOW
%token CAN
%token DOWN
%token EXIT
%token HAS
%token IF
%token IS
%token LEFT
%token LONG
%token MAKE
%token MOVE
%token MOVES
%token MEANS
%token NEXT
%token NOT
%token NOTHING
%token NOW
%token OF
%token OR
%token REMOVE
%token REPEAT
%token RESET
%token RIGHT
%token SAY
%token SAYS
%token STOP
%token THEN
%token TIMES
%token TO
%token TOUCHES
%token UP
%token WAIT
%token WAITS
%token WHENEVER
%token WHICH
%token WHILE
%token WITH

	/* Other lexeme-related tokens */
%token <sval> IDENTIFIER
%token <sval> REFERENCE
%token <dval> NUM
%token <sval> STRING

	/* Multi-character operators */
%token GEQ
%token LEQ

	/* Non-terminal symbol types */
%type <obj> program
%type <obj> statement_list
%type <obj> statement
%type <obj> declarative_statement
%type <obj> non_declarative_statement
%type <obj> non_declarative_statement_list
%type <obj> immediate_statement
%type <obj> class_declare_statement
%type <obj> class_definition_statement_list
%type <obj> class_definition_statement
%type <obj> listener_declare_statement
%type <obj> variable_create_statement
%type <obj> object_destroy_statement
%type <obj> property_assign_statement
%type <obj> iteration_statement
%type <obj> conditional_statement
%type <obj> runtime_control_statement
%type <obj> queueing_statement
%type <obj> queueing_custom_action_statement
%type <obj> predicate
%type <obj> basic_action_predicate
%type <obj> boolean_predicate
%type <obj> relational_expression
%type <obj> value_expression
%type <obj> reference_list
%type <obj> parameter_list
%type <obj> argument_list
%type <obj> queue_assign_property_list
%type <obj> reference
%type <obj> identifier
%type <obj> argument
%type <obj> parameter
%type <obj> subject
%type <obj> alias
%type <obj> queue_assign_property
%type <obj> adjacency_expression
%type <obj> direction
%type <obj> now
%type <obj> position
%type <obj> literal

	/* Operators precedence and associativity */
%left OR
%left AND
%left '-' '+'
%left '*' '/'
%right '^'			/* exponentiation */
%nonassoc UMINUS


%%


program:
		statement_list				{ $$ = $1; root = (AST) $$; }
	;

statement_list:
		statement					{ $$ = new INodeStatementList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	statement statement_list	{ $$ = new INodeStatementList($1); ((INode) $$).add(((INode) $2).children); ((AST) $$).lineNumber = lexer.lineNumber; }

statement:
		declarative_statement		{ $$ = $1; }
	|	non_declarative_statement	{ $$ = $1; }
	|	error { errorToken = lexer.yytext(); errorLineNumber = lexer.lineNumber; errorCharNumber = lexer.charNumber; } '.' { yyerror("Line " + errorLineNumber + " near '" + errorToken + "'" + "\n" + lexer.line.toString().replace("\t", " ") + "\n" + new String(new char[errorCharNumber-1]).replace("\0", " ") + "^"); yyerrflag = 0; }

non_declarative_statement_list:
		non_declarative_statement									{ $$ = new INodeStatementList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	non_declarative_statement non_declarative_statement_list	{ $$ = new INodeStatementList($1); ((INode) $$).add(((INode) $2).children); ((AST) $$).lineNumber = lexer.lineNumber; }

declarative_statement:
		class_declare_statement		{ $$ = $1; }
	|	listener_declare_statement	{ $$ = $1; }

non_declarative_statement:
		immediate_statement			{ $$ = $1; }
	|	queueing_statement			{ $$ = $1; }

immediate_statement:
		variable_create_statement		{ $$ = $1; }
	|	object_destroy_statement	{ $$ = $1; }
	|	property_assign_statement	{ $$ = $1; }
	|	iteration_statement			{ $$ = $1; }
	|	conditional_statement		{ $$ = $1; }
	|	runtime_control_statement	{ $$ = $1; }

class_declare_statement:
		A identifier IS A identifier '.'											{ $$ = new INodeCreateClass($2, $5, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	A identifier IS A identifier WHICH '{' class_definition_statement_list '}'	{ $$ = new INodeCreateClass($2, $5, $8); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

class_definition_statement_list:
		class_definition_statement													{ $$ = new INodeClassStatementList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	class_definition_statement class_definition_statement_list					{ $$ = new INodeClassStatementList($1); ((INode) $$).add(((INode) $2).children); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

class_definition_statement:
		HAS A identifier OF literal '.'												{ $$ = new INodeDefineProperty($3, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	CAN identifier WHICH MEANS '{' non_declarative_statement_list '}'							{ $$ = new INodeDefineCustomAction($2, null, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	CAN identifier WITH parameter_list WHICH MEANS '{' non_declarative_statement_list '}'		{ $$ = new INodeDefineCustomAction($2, $4, $8); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

listener_declare_statement:
		WHENEVER predicate '{' non_declarative_statement_list '}'									{ $$ = new INodeListener($2, $4, new LNodeBoolean(false)); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	AS LONG AS predicate '{' non_declarative_statement_list '}'									{ $$ = new INodeListener($4, $6, new LNodeBoolean(true)); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

variable_create_statement:
		identifier IS A identifier AT position '.'									{ $$ = new INodeCreateObject($1, $4, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	identifier IS value_expression '.'											{ $$ = new INodeCreateLocalVariable($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

object_destroy_statement:
		REMOVE reference_list identifier '.'										{ $$ = new INodeRemoveObject($2, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

property_assign_statement:
		reference reference_list identifier IS value_expression '.'					{ $$ = new INodeAssign((new INodeReferenceList($1)).add(((INode) $2).children), $3, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

iteration_statement:
		REPEAT value_expression TIMES '{' non_declarative_statement_list '}'						{ $$ = new INodeRepeat($2, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	WHILE boolean_predicate '{' non_declarative_statement_list '}'								{ $$ = new INodeWhile($2, $4); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

conditional_statement:
		IF boolean_predicate THEN '{' non_declarative_statement_list '}'							{ $$ = new INodeIf($2, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

runtime_control_statement:
		RESET '.'																	{ $$ = new INodeReset(); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	EXIT '.'																	{ $$ = new INodeExit(); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

queueing_statement:
		MAKE reference_list queue_assign_property_list '.'							{ $$ = new INodeQueueAssign($2, $3, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list queue_assign_property_list now '.'						{ $$ = new INodeQueueAssign($2, $3, $4); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier MOVE direction '.'							{ $$ = new INodeQueueMove($2, $3, $5, null, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier MOVE direction now '.'						{ $$ = new INodeQueueMove($2, $3, $5, null, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier MOVE direction value_expression '.'			{ $$ = new INodeQueueMove($2, $3, $5, $6, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier MOVE direction value_expression now '.'		{ $$ = new INodeQueueMove($2, $3, $5, $6, $7); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier SAY value_expression '.'						{ $$ = new INodeQueueSay($2, $3, $5, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier SAY value_expression now '.'					{ $$ = new INodeQueueSay($2, $3, $5, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier WAIT value_expression '.'					{ $$ = new INodeQueueWait($2, $3, $5, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier WAIT value_expression now '.'				{ $$ = new INodeQueueWait($2, $3, $5, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	STOP reference_list identifier '.'											{ $$ = new INodeQueueStop($2, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	queueing_custom_action_statement											{ $$ = $1; }
	;

queueing_custom_action_statement:
		MAKE reference_list identifier identifier '.'								{ $$ = new INodeQueueCustomAction($2, $3, $4, null, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier identifier now '.'							{ $$ = new INodeQueueCustomAction($2, $3, $4, null, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier identifier WITH argument_list '.'			{ $$ = new INodeQueueCustomAction($2, $3, $4, $6, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier identifier WITH argument_list now '.'		{ $$ = new INodeQueueCustomAction($2, $3, $4, $6, $7); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

predicate:
		basic_action_predicate														{ $$ = $1; }
	|	boolean_predicate															{ $$ = $1; }
	;

basic_action_predicate:
		subject alias MOVES															{ $$ = new INodeMovesPredicate($1, $2, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	subject alias MOVES direction												{ $$ = new INodeMovesPredicate($1, $2, $4); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	subject alias SAYS value_expression											{ $$ = new INodeSaysPredicate($1, $2, $4); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	subject alias WAITS															{ $$ = new INodeWaitsPredicate($1, $2); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	subject alias TOUCHES subject alias											{ $$ = new INodeTouchesPredicate($1, $2, $4, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	subject alias adjacency_expression subject alias							{ $$ = new INodeAdjacencyPredicate($1, $2, $3, $4, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

boolean_predicate:
		relational_expression						{ $$ = $1; }
	|	'(' boolean_predicate ')'					{ $$ = $2; }
	|	NOT '(' boolean_predicate ')'				{ $$ = new INodeNot($3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	boolean_predicate AND boolean_predicate		{ $$ = new INodeAnd($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	boolean_predicate OR boolean_predicate		{ $$ = new INodeOr($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

relational_expression:
		value_expression '=' value_expression		{ $$ = new INodeEquals($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression '<' value_expression		{ $$ = new INodeLess($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression '>' value_expression		{ $$ = new INodeGreater($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression LEQ value_expression		{ $$ = new INodeLEQ($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression GEQ value_expression		{ $$ = new INodeGEQ($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

value_expression:
		reference_list identifier					{ $$ = new INodeRetrieveProperty($1, $2); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	literal										{ $$ = $1; }
	|	NOTHING										{ $$ = new LNodeNothing(); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	'(' value_expression ')'					{ $$ = $2; }
	|	'-' value_expression %prec UMINUS			{ $$ = new INodeNegate($2); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression '+' value_expression		{ $$ = new INodeAdd($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression '-' value_expression		{ $$ = new INodeSubtract($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression '*' value_expression		{ $$ = new INodeMultiply($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression '/' value_expression		{ $$ = new INodeDivide($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	value_expression '^' value_expression		{ $$ = new INodeExponentiate($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

reference_list:
													{ $$ = new INodeReferenceList(); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	reference reference_list					{ $$ = new INodeReferenceList($1); ((INode) $$).add(((INode) $2).children); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

parameter_list:
		parameter									{ $$ = new INodeParameterList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	parameter ',' parameter_list				{ $$ = new INodeParameterList($1); ((INode) $$).add(((INode) $3).children); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

argument_list:
		argument									{ $$ = new INodeArgumentList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	argument ',' argument_list					{ $$ = new INodeArgumentList($1); ((INode) $$).add(((INode) $3).children); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

queue_assign_property_list:
		queue_assign_property										{ $$ = new INodeQueueAssignPropertyList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	queue_assign_property ',' queue_assign_property_list		{ $$ = new INodeQueueAssignPropertyList($1); ((INode) $$).add(((INode) $3).children); ((AST) $$).lineNumber = lexer.lineNumber; }
	;


reference:
		REFERENCE									{ $$ = new LNodeReference($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

identifier:
		IDENTIFIER									{ $$ = new LNodeIdentifier($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

argument:
		identifier value_expression					{ $$ = new INodeArgument($1, $2); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

parameter:
		identifier									{ $$ = new INodeParameter($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

subject:
		reference_list identifier					{ $$ = new INodeSubject(null, $1, $2); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	A identifier								{ $$ = new INodeSubject($2, null, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

alias:
													{ $$ = new INodeAlias(); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	'[' identifier ']'							{ $$ = new INodeAlias($2); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

queue_assign_property:
		identifier BE value_expression				{ $$ = new INodeQueueAssignProperty($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

adjacency_expression:
		IS NEXT TO									{ $$ = new LNodeDirection(Direction.ANYWHERE); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	IS BELOW									{ $$ = new LNodeDirection(Direction.DOWN); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	IS LEFT OF									{ $$ = new LNodeDirection(Direction.LEFT); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	IS RIGHT OF									{ $$ = new LNodeDirection(Direction.RIGHT); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	IS ABOVE									{ $$ = new LNodeDirection(Direction.UP); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

direction:
		ANYWHERE									{ $$ = new LNodeDirection(Direction.ANYWHERE); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	DOWN										{ $$ = new LNodeDirection(Direction.DOWN); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	LEFT										{ $$ = new LNodeDirection(Direction.LEFT); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	RIGHT										{ $$ = new LNodeDirection(Direction.RIGHT); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	UP											{ $$ = new LNodeDirection(Direction.UP); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

now:
		NOW											{ $$ = new LNodeNow(); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

position:
		value_expression ',' value_expression		{ $$ = new INodePosition($1, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

literal:
		NUM											{ $$ = new LNodeNum($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	STRING										{ $$ = new LNodeString($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	;


%%


private Yylex lexer;

private int yylex() {
	int yyl_return = -1;
	try {
		yylval = new WordsVal(0);
		yyl_return = lexer.yylex();
	} catch (IOException e) {
		printLnToConsole("IO error :"+e);
	}

	return yyl_return;
}


public void yyerror(String error) {
	if (!hideErrors)
		printLnToConsole("Error: " + error);
}

public int getDepth() {
	return lexer.depth;
}

public Words(Reader r) {
	lexer = new Yylex(r, this);
}

private static void printLnToConsole() {
	if (Options.PRINT_TO_CONSOLE) {
		System.err.println();
	}
}

private static void printLnToConsole(String message) {
	if (Options.PRINT_TO_CONSOLE) {
		System.err.println(message);
	}
}


public static FrameLoop frameLoop;
public AST root;
public boolean hideErrors = false;
public String errorToken;
public int errorLineNumber;
public int errorCharNumber;

public static void main(String args[]) throws IOException {
	
	GUI ui = null;
	// Handle testmode Options
	for (int i = 0; i < args.length; ++i) {
		if (args[i].equals("-s") && i < args.length - 1) {
			try {
				Options.TIME_TO_WAIT = Integer.parseInt(args[i+1]);
			} catch (Exception e) {}
		} else if (args[i].equals("-testmode")) {
			Options.GUI = false;
			Options.TIME_TO_WAIT = -1;
			Options.FRAME_LIMIT_ENABLED = true;
			Options.MAX_FRAMES = 100;
			
			boolean turnOffErrorMessages = true;
			for (int j = 0; j < args.length; ++j) {
				if (args[j].equals("-displayerror")) {
					turnOffErrorMessages = false;
				}
			}
			if (turnOffErrorMessages) {
				Options.PRINT_TO_CONSOLE = false;
			}
		}
	}
	
	printLnToConsole("Welcome to Words!");
	
	if (Options.GUI)
		ui = new GUI();

	frameLoop = new FrameLoop(ui);

	// Read and parse program argument, if any
	for (int i = 0; i < args.length; ++i) {
		if (args[i].charAt(0) != '-') {
			// Read in Words program from file
			String filename = args[i];
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				Words parser = new Words(br);
				parser.yyparse();

				if (parser.root != null)
					frameLoop.enqueueAST(parser.root);

				br.close();
			} catch (IOException e) {
				printLnToConsole("Unable to read file " + filename);
			}
			break;
		}
	}

	// Start the frame loop only after the source program has been loaded so that all loaded statements occur in the first frame
	frameLoop.start();

	// If no GUI, then no REPL
	if (!Options.GUI)
		return;

	// Simple REPL interface
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	while (true) {
		String fragment = "";
		int depth = 0;

		while (true) {
			// Prompt user
			if (depth > 0)
				Console.showPromptMore();
			else
				Console.showPrompt();

			// Read next line and exit on EOF
			String line = br.readLine();

			if (line == null)
				System.exit(0);

			fragment = fragment + line;

			// Attempt to parse the fragment
			Words tester = new Words(new StringReader(fragment));
			tester.hideErrors = true;
			tester.yyparse();

			depth = tester.getDepth();

			// If the depth is greater than 0, we have to keep reading lines to get a fragment that is at least potentially complete
			if (depth == 0)
				break;
		}

		Words parser = new Words(new StringReader(fragment));
		parser.yyparse();

		printLnToConsole();
		if (parser.root != null)
			frameLoop.enqueueAST(parser.root);
	}
}
