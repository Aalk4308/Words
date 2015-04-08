%{
  import java.io.*;
  import words.environment.*;
  import words.exceptions.*;
  import words.ast.*;
%}

	/* Keyword-related tokens */
%token A
%token AND
%token ANYWHERE
%token AS
%token AT
%token BE
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
%token TOUCHES
%token TURNS
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
%type <obj> immediate_statement
%type <obj> class_create_statement
%type <obj> class_definition_statement_list
%type <obj> class_definition_statement
%type <obj> object_create_statement
%type <obj> object_destroy_statement
%type <obj> property_assign_statement
%type <obj> iteration_statement
%type <obj> conditional_statement
%type <obj> listener_statement
%type <obj> runtime_control_statement
%type <obj> queueing_statement
%type <obj> queueing_custom_action_statement
%type <obj> predicate
%type <obj> basic_action_predicate
%type <obj> boolean_predicate
%type <obj> relational_expression
%type <obj> value_expression
%type <obj> reference_list
%type <obj> identifier_list
%type <obj> parameter_list
%type <obj> queue_assign_property_list
%type <obj> reference
%type <obj> identifier
%type <obj> parameter
%type <obj> subject
%type <obj> alias
%type <obj> queue_assign_property
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
		immediate_statement			{ $$ = $1; }
	|	queueing_statement			{ $$ = $1; }
	|	error { hasError = true; yyerror("Line " + lexer.lineNumber + " near '" + lexer.yytext() + "'"); } '.' { yyerrflag = 0; }

immediate_statement:
		class_create_statement		{ $$ = $1; }
	|	object_create_statement		{ $$ = $1; }
	|	object_destroy_statement	{ $$ = $1; }
	|	property_assign_statement	{ $$ = $1; }
	|	iteration_statement			{ $$ = $1; }
	|	conditional_statement		{ $$ = $1; }
	|	listener_statement			{ $$ = $1; }
	|	runtime_control_statement	{ $$ = $1; }

class_create_statement:
		A identifier IS A identifier '.'											{ $$ = new INodeCreateClass($2, $5, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	A identifier IS A identifier WHICH '{' class_definition_statement_list '}'	{ $$ = new INodeCreateClass($2, $5, $8); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

class_definition_statement_list:
		class_definition_statement													{ $$ = new INodeClassStatementList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	class_definition_statement class_definition_statement_list					{ $$ = new INodeClassStatementList($1); ((INode) $$).add(((INode) $2).children); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

class_definition_statement:
		HAS A identifier '.'														{ $$ = new INodeDefineProperty($3, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	HAS A identifier OF literal '.'												{ $$ = new INodeDefineProperty($3, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	CAN identifier WHICH MEANS '{' statement_list '}'							{ $$ = new INodeDefineCustomAction($2, null, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	CAN identifier WITH identifier_list WHICH MEANS '{' statement_list '}'		{ $$ = new INodeDefineCustomAction($2, $4, $8); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

object_create_statement:
		identifier IS A identifier AT position '.'									{ $$ = new INodeCreateObject($1, $4, null, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	identifier IS A identifier WITH parameter_list AT position '.'				{ $$ = new INodeCreateObject($1, $4, $6, $8); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

object_destroy_statement:
		REMOVE reference_list identifier '.'										{ $$ = new INodeRemoveObject($2, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

property_assign_statement:
		reference reference_list identifier IS value_expression '.'					{ $$ = new INodeAssign((new INodeReferenceList($1)).add(((INode) $2).children), $3, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

iteration_statement:
		REPEAT value_expression TIMES '{' statement_list '}'						{ $$ = new INodeRepeat($2, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	WHILE boolean_predicate '{' statement_list '}'								{ $$ = new INodeWhile($2, $4); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

conditional_statement:
		IF boolean_predicate THEN '{' statement_list '}'							{ $$ = new INodeIf($2, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

listener_statement:
		WHENEVER predicate '{' statement_list '}'									{ $$ = new INodeListenerPerm($2, $4); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	AS LONG AS predicate '{' statement_list '}'									{ $$ = new INodeListenerTemp($4, $6); ((AST) $$).lineNumber = lexer.lineNumber; }
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
	|	MAKE reference_list identifier WAIT value_expression TURNS '.'				{ $$ = new INodeQueueWait($2, $3, $5, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier WAIT value_expression TURNS now '.'			{ $$ = new INodeQueueWait($2, $3, $5, $7); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	STOP reference_list identifier '.'											{ $$ = new INodeQueueStop($2, $3); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	queueing_custom_action_statement											{ $$ = $1; }
	;

queueing_custom_action_statement:
		MAKE reference_list identifier identifier '.'								{ $$ = new INodeQueueCustomAction($2, $3, $4, null, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier identifier now '.'							{ $$ = new INodeQueueCustomAction($2, $3, $4, null, $5); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier identifier WITH parameter_list '.'			{ $$ = new INodeQueueCustomAction($2, $3, $4, $6, null); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	MAKE reference_list identifier identifier WITH parameter_list now '.'		{ $$ = new INodeQueueCustomAction($2, $3, $4, $6, $7); ((AST) $$).lineNumber = lexer.lineNumber; }
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

identifier_list:
		identifier									{ $$ = new INodeIdentifierList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	identifier ',' identifier_list				{ $$ = new INodeIdentifierList($1); ((INode) $$).add(((INode) $3).children); ((AST) $$).lineNumber = lexer.lineNumber; }
	;

parameter_list:
		parameter									{ $$ = new INodeParameterList($1); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	parameter ',' parameter_list				{ $$ = new INodeParameterList($1); ((INode) $$).add(((INode) $3).children); ((AST) $$).lineNumber = lexer.lineNumber; }
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

parameter:
		identifier value_expression					{ $$ = new INodeParameter($1, $2); ((AST) $$).lineNumber = lexer.lineNumber; }
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

direction:
		ANYWHERE									{ $$ = new LNodeDirection(Direction.Type.ANYWHERE); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	DOWN										{ $$ = new LNodeDirection(Direction.Type.DOWN); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	LEFT										{ $$ = new LNodeDirection(Direction.Type.LEFT); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	RIGHT										{ $$ = new LNodeDirection(Direction.Type.RIGHT); ((AST) $$).lineNumber = lexer.lineNumber; }
	|	UP											{ $$ = new LNodeDirection(Direction.Type.UP); ((AST) $$).lineNumber = lexer.lineNumber; }
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
		System.err.println("IO error :"+e);
	}

	return yyl_return;
}


public void yyerror(String error) {
	if (!hideErrors)
		System.err.println("Error: " + error);
}

public int getDepth() {
	return lexer.depth;
}

public Words(Reader r) {
	lexer = new Yylex(r, this);
}


public static FrameLoop frameLoop;
public AST root;
public boolean hideErrors = false;
public boolean hasError = false;

public static void main(String args[]) throws IOException {
	System.err.println("Welcome to Words!");

	WordsUI ui = null;
	// Handle GUI option
	for (int i = 0; i < args.length; ++i) {
		if (args[i].equals("-nogui")) {
			System.err.println("GUI turned off");
			Option.GUI = false;
			Option.TIME_TO_WAIT = -1;
			Option.FRAME_LIMIT_ENABLED = true;
			Option.MAX_FRAMES = 500;
		}
	}

	if (Option.GUI)
		ui = new WordsUI();

	frameLoop = new FrameLoop(ui);

	// Read and parse program argument, if any
	for (int i = 0; i < args.length; ++i) {
		if (args[i].charAt(0) != '-') {
			// Read in Words program from file
			String filename = args[i];
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				Words parser = new Words(br);
				parser.yyparse();

				System.err.println();
				System.err.println();
				if (parser.root != null)
					frameLoop.enqueueAST(parser.root);

				br.close();
			} catch (IOException e) {
				System.err.println("Unable to read file " + filename);
			}
			break;
		}
	}

	// Start the frame loop only after the source program has been loaded so that all loaded statements occur in the first frame
	frameLoop.start();

	// If no GUI, then no REPL
	if (!Option.GUI)
		return;

	// Simple REPL interface
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	while (true) {
		String fragment = "";
		int depth = 0;

		while (true) {
			// Prompt user
			if (depth > 0)
				System.err.printf("... ");
			else
				System.err.printf("> ");

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

		// In REPL interface, we might want to evaluate only ASTs that had no syntax errors
		System.err.println();
		System.err.println();
		if (parser.root != null)
			frameLoop.enqueueAST(parser.root);
	}
}
