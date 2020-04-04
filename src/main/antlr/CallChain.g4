grammar CallChain ;

//<digit>   ::= “0” | “1" | “2” | “3" | “4” | “5" | “6” | “7" | “8” | “9"
//<number> ::= <digit> | <digit> <number>
//<operation> ::= “+” | “-” | “*” | “>” | “<” | “=” | “&” | “|”
//<constant-expression> ::= “-” <number> | <number>
//<binary-expression> ::= “(” <expression> <operation> <expression> “)”
//<expression> ::= “element” | <constant-expression> | <binary-expression>
//<map-call> ::= “map{” <expression> “}”
//<filter-call> ::= “filter{” <expression> “}”
//<call> ::= <map-call> | <filter-call>
//<call-chain> ::= <call> | <call> “%>%” <call-chain>

callChain : call ('%>%' call)* ;
call : mapCall | filterCall ;
mapCall : 'map{' expression '}' ;
filterCall : 'filter{' expression '}' ;
expression : itExpression | constantExpression | binaryExpression;
constantExpression : NUMBER | '-'NUMBER;
itExpression : ELEMENT ;
binaryExpression : '(' left=expression operation right=expression ')';
operation : '-' | '+' | '*' | '>' | '<' | '=' | '&' | '|' ;



fragment DIGIT : [0-9] ;
NUMBER : DIGIT+;
ELEMENT : 'element' ;