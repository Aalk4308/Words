%{
  import java.io.*;
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
		statement					{ $$ = new INodeStatementList($1); }
	|	statement statement_list	{ $$ = new INodeStatementList($1); ((INode) $$).add(((INode) $2).children); }

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
		A identifier IS A identifier '.'											{ $$ = new INodeCreateClass($2, $5, null); }
	|	A identifier IS A identifier WHICH '{' class_definition_statement_list '}'	{ $$ = new INodeCreateClass($2, $5, $8); }
	;

class_definition_statement_list:
		class_definition_statement													{ $$ = new INodeClassStatementList($1); }
	|	class_definition_statement class_definition_statement_list					{ $$ = new INodeClassStatementList($1); ((INode) $$).add(((INode) $2).children); }
	;

class_definition_statement:
		HAS A identifier '.'														{ $$ = new INodeDefineProperty($3, null); }
	|	HAS A identifier OF literal '.'												{ $$ = new INodeDefineProperty($3, $5); }
	|	CAN identifier WHICH MEANS '{' statement_list '}'							{ $$ = new INodeDefineCustomAction($2, null, $6); }
	|	CAN identifier WITH identifier_list WHICH MEANS '{' statement_list '}'		{ $$ = new INodeDefineCustomAction($2, $4, $8); }
	;

object_create_statement:
		identifier IS A identifier AT position '.'									{ $$ = new INodeCreateObject($1, $4, null, $6); }
	|	identifier IS A identifier WITH parameter_list AT position '.'				{ $$ = new INodeCreateObject($1, $4, $6, $8); }
	;

object_destroy_statement:
		REMOVE reference_list identifier '.'										{ $$ = new INodeRemoveObject($2, $3); }
	;

property_assign_statement:
		reference reference_list identifier IS value_expression '.'					{ $$ = new INodeAssign((new INodeReferenceList($1)).add(((INode) $2).children), $3, $5); }
	;

iteration_statement:
		REPEAT value_expression TIMES '{' statement_list '}'						{ $$ = new INodeRepeat($2, $5); }
	|	WHILE boolean_predicate '{' statement_list '}'								{ $$ = new INodeWhile($2, $4); }
	;

conditional_statement:
		IF boolean_predicate THEN '{' statement_list '}'							{ $$ = new INodeIf($2, $5); }
	;

listener_statement:
		WHENEVER predicate '{' statement_list '}'									{ $$ = new INodeListenerPerm($2, $4); }
	|	AS LONG AS predicate '{' statement_list '}'									{ $$ = new INodeListenerTemp($4, $6); }
	;

runtime_control_statement:
		RESET '.'																	{ $$ = new INodeReset(); }
	|	EXIT '.'																	{ $$ = new INodeExit(); }
	;

queueing_statement:
		MAKE reference_list queue_assign_property_list '.'							{ $$ = new INodeQueueAssign($2, $3, null); }
	|	MAKE reference_list queue_assign_property_list now '.'						{ $$ = new INodeQueueAssign($2, $3, $4); }
	|	MAKE reference_list identifier MOVE direction '.'							{ $$ = new INodeQueueMove($2, $3, $5, null, null); }
	|	MAKE reference_list identifier MOVE direction now '.'						{ $$ = new INodeQueueMove($2, $3, $5, null, $6); }
	|	MAKE reference_list identifier MOVE direction value_expression '.'			{ $$ = new INodeQueueMove($2, $3, $5, $6, null); }
	|	MAKE reference_list identifier MOVE direction value_expression now '.'		{ $$ = new INodeQueueMove($2, $3, $5, $6, $7); }
	|	MAKE reference_list identifier SAY value_expression '.'						{ $$ = new INodeQueueSay($2, $3, $5, null); }
	|	MAKE reference_list identifier SAY value_expression now '.'					{ $$ = new INodeQueueSay($2, $3, $5, $6); }
	|	MAKE reference_list identifier WAIT value_expression TURNS '.'				{ $$ = new INodeQueueWait($2, $3, $5, null); }
	|	MAKE reference_list identifier WAIT value_expression TURNS now '.'			{ $$ = new INodeQueueWait($2, $3, $5, $7); }
	|	STOP reference_list identifier '.'											{ $$ = new INodeQueueStop($2, $3); }
	|	queueing_custom_action_statement											{ $$ = $1; }
	;

queueing_custom_action_statement:
		MAKE reference_list identifier identifier '.'								{ $$ = new INodeQueueCustomAction($2, $3, $4, null, null); }
	|	MAKE reference_list identifier identifier now '.'							{ $$ = new INodeQueueCustomAction($2, $3, $4, null, $5); }
	|	MAKE reference_list identifier identifier WITH parameter_list '.'			{ $$ = new INodeQueueCustomAction($2, $3, $4, $6, null); }
	|	MAKE reference_list identifier identifier WITH parameter_list now '.'		{ $$ = new INodeQueueCustomAction($2, $3, $4, $6, $7); }
	;

predicate:
		basic_action_predicate														{ $$ = $1; }
	|	boolean_predicate															{ $$ = $1; }
	;

basic_action_predicate:
		subject alias MOVES															{ $$ = new INodeMovesPredicate($1, $2, null); }
	|	subject alias MOVES direction												{ $$ = new INodeMovesPredicate($1, $2, $4); }
	|	subject alias SAYS value_expression											{ $$ = new INodeSaysPredicate($1, $2, $4); }
	|	subject alias WAITS															{ $$ = new INodeWaitsPredicate($1, $2); }
	|	subject alias TOUCHES subject alias											{ $$ = new INodeTouchesPredicate($1, $2, $4, $5); }
	;

boolean_predicate:
		relational_expression						{ $$ = $1; }
	|	'(' boolean_predicate ')'					{ $$ = $2; }
	|	NOT '(' boolean_predicate ')'				{ $$ = new INodeNot($3); }
	|	boolean_predicate AND boolean_predicate		{ $$ = new INodeAnd($1, $3); }
	|	boolean_predicate OR boolean_predicate		{ $$ = new INodeOr($1, $3); }
	;

relational_expression:
		value_expression '=' value_expression		{ $$ = new INodeEquals($1, $3); }
	|	value_expression '<' value_expression		{ $$ = new INodeLess($1, $3); }
	|	value_expression '>' value_expression		{ $$ = new INodeGreater($1, $3); }
	|	value_expression LEQ value_expression		{ $$ = new INodeLEQ($1, $3); }
	|	value_expression GEQ value_expression		{ $$ = new INodeGEQ($1, $3); }
	;

value_expression:
		reference_list identifier					{ $$ = new INodeRetrieveProperty($1, $2); }
	|	literal										{ $$ = $1; }
	|	NOTHING										{ $$ = new LNodeNothing(); }
	|	'(' value_expression ')'					{ $$ = $2; }
	|	'-' value_expression %prec UMINUS			{ $$ = new INodeNegate($2); }
	|	value_expression '+' value_expression		{ $$ = new INodeAdd($1, $3); }
	|	value_expression '-' value_expression		{ $$ = new INodeSubtract($1, $3); }
	|	value_expression '*' value_expression		{ $$ = new INodeMultiply($1, $3); }
	|	value_expression '/' value_expression		{ $$ = new INodeDivide($1, $3); }
	|	value_expression '^' value_expression		{ $$ = new INodeExponentiate($1, $3); }
	;

reference_list:
													{ $$ = new INodeReferenceList(); }
	|	reference reference_list					{ $$ = new INodeReferenceList($1); ((INode) $$).add(((INode) $2).children); }
	;

identifier_list:
		identifier									{ $$ = new INodeIdentifierList($1); }
	|	identifier ',' identifier_list				{ $$ = new INodeIdentifierList($1); ((INode) $$).add(((INode) $3).children); }
	;

parameter_list:
		parameter									{ $$ = new INodeParameterList($1); }
	|	parameter ',' parameter_list				{ $$ = new INodeParameterList($1); ((INode) $$).add(((INode) $3).children); }
	;

queue_assign_property_list:
		queue_assign_property										{ $$ = new INodeQueueAssignPropertyList($1); }
	|	queue_assign_property ',' queue_assign_property_list		{ $$ = new INodeQueueAssignPropertyList($1); ((INode) $$).add(((INode) $3).children); }
	;


reference:
		REFERENCE									{ $$ = new LNodeReference($1); }
	;

identifier:
		IDENTIFIER									{ $$ = new LNodeIdentifier($1); }
	;

parameter:
		identifier value_expression					{ $$ = new INodeParameter($1, $2); }
	;

subject:
		reference_list identifier					{ $$ = new INodeSubject(null, $1, $2); }
	|	A identifier								{ $$ = new INodeSubject($2, null, null); }
	;

alias:
													{ $$ = new INodeAlias(); }
	|	'[' identifier ']'							{ $$ = new INodeAlias($2); }
	;

queue_assign_property:
		identifier BE value_expression				{ $$ = new INodeQueueAssignProperty($1, $3); }
	;

direction:
		ANYWHERE									{ $$ = new LNodeDirection(Direction.Type.ANYWHERE); }
	|	DOWN										{ $$ = new LNodeDirection(Direction.Type.DOWN); }
	|	LEFT										{ $$ = new LNodeDirection(Direction.Type.LEFT); }
	|	RIGHT										{ $$ = new LNodeDirection(Direction.Type.RIGHT); }
	|	UP											{ $$ = new LNodeDirection(Direction.Type.UP); }
	;

now:
		NOW											{ $$ = new LNodeNow(); }
	;

position:
		value_expression ',' value_expression		{ $$ = new INodePosition($1, $3); }
	;

literal:
		NUM											{ $$ = new LNodeNum($1); }
	|	STRING										{ $$ = new LNodeString($1); }
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
	System.out.println("Welcome to Words!");

	WordsUI ui = null;
	// Handle GUI option
	for (int i = 0; i < args.length; ++i) {
		if (args[i].equals("-nogui")) {
			System.out.println("GUI turned off");
			Option.GUI = false;
			Option.TIME_TO_WAIT = 100;
		}
	}
	if (Option.GUI)
		ui = new WordsUI();

	frameLoop = new FrameLoop(ui);
	frameLoop.start();

	// Read and parse program argument, if any
	for (int i = 0; i < args.length; ++i) {
		if (args[i].charAt(0) != '-') {
			// Read in Words program from file
			String filename = args[i];
			try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
				Words parser = new Words(br);
				parser.yyparse();

				// Temporary: dump AST to console.  (TODO: Enqueue AST for evaluation by frame loop thread.)
				System.err.println();
				System.err.println();
				if (parser.root != null)
					frameLoop.enqueueAST(parser.root);
				else
					System.err.println("Failed to generate AST");

				br.close();
			} catch (IOException e) {
				System.err.println("Unable to read file " + filename);
			}
			break;
		}
	}

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
				System.out.printf("... ");
			else
				System.out.printf("> ");

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

		// Temporary: dump AST to console.  (TODO: Enqueue AST for evaluation by frame loop thread.)
		// In REPL interface, we might want to evaluate only ASTs that had no syntax errors
		System.err.println();
		System.err.println();
		if (parser.root != null)
			frameLoop.enqueueAST(parser.root);
		else
			System.err.println("Failed to generate AST");
	}
}
