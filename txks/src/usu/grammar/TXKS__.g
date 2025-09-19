lexer grammar TXKS;
@members {
    
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        //errorMessage = buildErrorMessage(e);
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, TXKSParser.tokenNames);
        // Now do something with hdr and msg...
        TXKSParser.errorMessage = "^Lexical Error in query at " + hdr + ": " + msg + "\n";
    }
}
@header {
package usu.grammar;
import java.util.List;
import java.util.ArrayList;
import usu.algebra.KeywordSearchExpression;
//import usu.algebra.operator.*;
//import usu.algebra.operator.specific.*;
import java.lang.UnsupportedOperationException;
  }

T__47 : '-' ;

// $ANTLR src "TXKS.g" 257
K_NONTEMPORAL		:	'@nontemporal';// $ANTLR src "TXKS.g" 258
K_EARLIEST		:	'@earliest';// $ANTLR src "TXKS.g" 259
K_CURRENT		:	'@current';// $ANTLR src "TXKS.g" 260
K_LATEST		:	'@latest';// $ANTLR src "TXKS.g" 261
K_MEETS			: 	'@meets';// $ANTLR src "TXKS.g" 262
K_DURATION		:	'@duration';// $ANTLR src "TXKS.g" 263
K_SEQUENCED		:	'@sequenced';// $ANTLR src "TXKS.g" 264
K_NONSEQUENCED		:	'@nonsequenced';// $ANTLR src "TXKS.g" 265
K_DESCENDANTVERSIONS 	:	'@descendantVersions';// $ANTLR src "TXKS.g" 266
K_DESCENDANTCHANGES 	:	'@descendantChanges';// $ANTLR src "TXKS.g" 267
K_SLICE			: 	'@slice';// $ANTLR src "TXKS.g" 268
K_CONTAINS 		:	'@contains';// $ANTLR src "TXKS.g" 269
K_DURING 		:	'@during';// $ANTLR src "TXKS.g" 270
K_INTERSECTS 		:	'@intersects';// $ANTLR src "TXKS.g" 271
K_BEFORE		:	'@before';// $ANTLR src "TXKS.g" 272
K_AFTER			:	'@after';// $ANTLR src "TXKS.g" 273
K_ANY			:	'@any';// $ANTLR src "TXKS.g" 275
ID :   ( LETTER | '_' | ':') (NAMECHAR)* ;// $ANTLR src "TXKS.g" 277
fragment NAMECHAR
    : LETTER | DIGIT | '.' | '-' | '_' | ':'
    ;// $ANTLR src "TXKS.g" 281
fragment DIGIT
    :    '0'..'9'
    ;// $ANTLR src "TXKS.g" 285
fragment LETTER
    : 'a'..'z'
    | 'A'..'Z'
    ;// $ANTLR src "TXKS.g" 290
WILDCARD:	'*';// $ANTLR src "TXKS.g" 291
DOUBLEWILDCARD : '**';// $ANTLR src "TXKS.g" 292
NOT 	:	'!';// $ANTLR src "TXKS.g" 293
INT	:	'0'..'9'+ ;// $ANTLR src "TXKS.g" 294
LBRACKET:	'[';// $ANTLR src "TXKS.g" 295
RBRACKET:	']';// $ANTLR src "TXKS.g" 296
RBRACE 	:	'}' ;// $ANTLR src "TXKS.g" 297
LBRACE 	:	'{' ;// $ANTLR src "TXKS.g" 298
RPAREN	:	')';// $ANTLR src "TXKS.g" 299
LPAREN	:	'(';// $ANTLR src "TXKS.g" 300
COMMA   : 	',';// $ANTLR src "TXKS.g" 301
AT	:	'@';// $ANTLR src "TXKS.g" 305
EQUALS	:	'=''='?;// $ANTLR src "TXKS.g" 306
LE 	:	'<=';// $ANTLR src "TXKS.g" 307
GE 	:	'>=';// $ANTLR src "TXKS.g" 308
GT	: 	'>';// $ANTLR src "TXKS.g" 309
LT	: 	'<';// $ANTLR src "TXKS.g" 312
STRING 	: 	('"' (~'"')* '"') | ('\'' (~'\'')* '\'');// $ANTLR src "TXKS.g" 313
EOS	:	';' ;// $ANTLR src "TXKS.g" 314
ARROW	:       '->';// $ANTLR src "TXKS.g" 315
PIPE 	:	'|';// $ANTLR src "TXKS.g" 316
WS      	:       (' '|'\r'|'\n')+ {$channel = HIDDEN;} ;