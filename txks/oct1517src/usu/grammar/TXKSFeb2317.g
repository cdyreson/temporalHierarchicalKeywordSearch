grammar TXKS;	

options {
  k=2;
  }

@header {
package usu.grammar;
import java.util.List;
import java.util.ArrayList;
import usu.algebra.operator.*;
import usu.algebra.operator.specific.*;
import java.lang.UnsupportedOperationException;
  }

@lexer::header {
package usu.grammar;
import java.util.List;
import java.util.ArrayList;
import usu.algebra.operator.*;
import usu.algebra.operator.specific.*;
import java.lang.UnsupportedOperationException;
  }

@members {

protected static String errorMessage = "";
protected boolean hasError = false;
public static boolean verbose = false;

public boolean hasError() {
   return hasError;
}

public String getErrorMessage() {
   return errorMessage;
}

protected void mismatch (
  IntStream input, 
  int ttype, 
  BitSet follow) throws RecognitionException {
            MismatchedTokenException e = new MismatchedTokenException(ttype, input);       
            errorMessage = buildErrorMessage(e);
        hasError = true;
    throw new TXKSParseException(errorMessage);
  }

public String recoverFromMismatchedSet (
  IntStream input,
  RecognitionException e,
  BitSet follow) throws RecognitionException {
  //BitSet follow) throws TXKSParseException {
          errorMessage = buildErrorMessage(e);
        hasError = true;
    throw e;
    //throw new TXKSParseException(buildErrorMessage(e));
  }

public Object recoverFromMisMatchedToken(
  IntStream input,
  RecognitionException e,
  BitSet follow) throws RecognitionException {
    //BitSet follow) throws TXKSParseException {
            errorMessage = buildErrorMessage(e);
        hasError = true;
    throw e;
    //throw new TXKSParseException(buildErrorMessage(e));
  }
  

       private String buildErrorMessage(
                                   RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        // Now do something with hdr and msg...
        String lines[] = input.toString().split("\n");
        int line = e.line;
        Token token = e.token;
        int position = token.getCharPositionInLine(); //e.charPositionInLine;
        int length = e.charPositionInLine - position;
        length = (length <= 0)? 1 : length;
        String output = "";
        for (String s : lines) {
          output += s + "\n";
          if (line-- == 1) {
             for (int i = position; i > 0; i--) {
                output += " ";
             }
             for (int i = length; i > 0; i--) {
                output += "^";
             }
             output += "Parse Error: \"" + token.getText() + "\" " + msg + "\n"; 
          }
        }
        return output;
    }
    
    
      public void displayRecognitionError(String[] tokenNames,
                                        RecognitionException e) {
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, tokenNames);
        // Now do something with hdr and msg...
        
        errorMessage = buildErrorMessage(e);
        hasError = true;
        //throw new TXKSParseException(errorMessage);
        
    }
}

@lexer::members {
    
    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
        //errorMessage = buildErrorMessage(e);
        String hdr = getErrorHeader(e);
        String msg = getErrorMessage(e, TXKSParser.tokenNames);
        // Now do something with hdr and msg...
        TXKSParser.errorMessage = "^Lexical Error in query at " + hdr + ": " + msg + "\n";
    }
}   

@rulecatch {
  catch (RecognitionException e) {
    throw e;
  }
}

program returns [Operator exp] :
    op=kindOfResult {
        $exp = $op.exp;
    	if (hasError) { throw new TXKSParseException(errorMessage); }
    	}
    EOF ;  
  
kindOfResult returns [Operator exp] :
    K_NONTEMPORAL LPAREN kindOfSearch RPAREN
      {$exp = new NontemporalResult($kindOfSearch.exp);}
    |
    K_SEQUENCEDRESULT LPAREN kindOfSearch RPAREN
      {$exp = new SequencedResult($kindOfSearch.exp);}
    |
    K_NONSEQUENCEDRESULT LPAREN kindOfSearch RPAREN
      {$exp = new NonsequencedResult($kindOfSearch.exp);}
    |
    K_DESCENDANTVERSIONS LPAREN kindOfSearch RPAREN
      {$exp = new DescendantVersions($kindOfSearch.exp);}
    ; 
  
kindOfSearch returns [Operator exp] :
    K_NONTEMPORALSEARCH LPAREN kindOfMatch RPAREN
       {$exp = new NontemporalSearch($kindOfMatch.exp);}
    |
    K_SEQUENCEDSEARCH LPAREN kindOfMatch RPAREN
       {$exp = new SequencedSearch($kindOfMatch.exp);}
    |
    K_NONSEQUENCEDSEARCH LPAREN kindOfMatch RPAREN
       {$exp = new NonsequencedSearch($kindOfMatch.exp);}
    |
    K_DESCENDANTCHANGES LPAREN kindOfMatch RPAREN
       {$exp = new DescendantChanges($kindOfMatch.exp);}
    ;
 
kindOfMatch returns [Operator exp] :
    K_SLICE LPAREN s=STRING COMMA op1=kindOfMatch RPAREN {$exp = new Slice(null, $op1.exp);} 
    | 
    K_AFTER LPAREN op1=kindOfMatch COMMA op2=kindOfMatch RPAREN {$exp = new After($op1.exp, $op2.exp);}
    |
    K_INTERSECTS LPAREN op1=kindOfMatch COMMA op2=kindOfMatch RPAREN {$exp = new Intersects($op1.exp, $op2.exp);}
    |  
    //K_MATCH LPAREN s=STRING RPAREN {$exp = new Match($s.text);}
    K_MATCH LPAREN a=startStringList RPAREN {$exp = new Match($a.lst);}
    |
    K_ANY LPAREN op1=kindOfMatch COMMA op2=kindOfMatch RPAREN {$exp = new Any($op1.exp, $op2.exp);}
    ;  

startStringList returns [List<String> lst] :
    s=STRING  r=restOfStringList {$lst = $r.lst; $lst.add($s.text);}
    ;
    
restOfStringList returns [List<String> lst] : 
    COMMA s=STRING r=restOfStringList {$lst = $r.lst; $lst.add($s.text);}
    | {$lst = new ArrayList(3); }
    ;
  
integer	returns [ int value]:	
  INT {
    value = Integer.parseInt($INT.text);
    }
  ;
/*
comparator returns [int value]:	 
  EQUALS {$value = Where.EQ;} 
  | LE {$value = Where.LE;} 
  | GE {$value =  Where.GE;}
  | GT {$value = Where.GT;} 
  | LT {$value = Where.LT;}
  ;
*/
K_NONTEMPORALSEARCH
	:	'nontemporalSeach';
K_NONTEMPORAL
	:	'nontemporal';
K_SEQUENCEDSEARCH
	:	'sequencedSearch';
K_NONSEQUENCEDSEARCH 
	:	'nonsequencedSearch';
K_DESCENDANTVERSIONS 
	:	'descendantVersions';
K_DESCENDANTCHANGES 
	:	'descendantChanges';
K_SEQUENCEDRESULT
	: 	'sequencedResult';
K_NONSEQUENCEDRESULT 
	:	'nonsequencedResult';
K_SLICE
	: 	'slice';
K_INTERSECTS 
	:	'@intersects';
K_TEMPORALMATCH
	: 	'temporalMatch';
K_MATCH
	:	'match';
K_AFTER
	:	'after';
K_ANY
	:	'any';	

ID :   ( LETTER | '_' | ':') (NAMECHAR)* ;

fragment NAMECHAR
    : LETTER | DIGIT | '.' | '-' | '_' | ':'
    ;

fragment DIGIT
    :    '0'..'9'
    ;

fragment LETTER
    : 'a'..'z'
    | 'A'..'Z'
    ;

WILDCARD :	'*';
DOUBLEWILDCARD : '**';
NOT 	:	'!';
INT	:	'0'..'9'+ ;
LBRACKET:	'[';
RBRACKET:	']';
RBRACE 	:	'}' ;
LBRACE 	:	'{' ;
RPAREN	:	')';
LPAREN	:	'(';
COMMA   : 	',';

/*COMPARATOR returns [int value]:	 '==' {$value = Where.EQ;} | '=' | '<=' | '>=' ;	*/ 

EQUALS	:	'=''='?;
LE 	:	'<=';
GE 	:	'>=';
GT	: 	'>';
LT	: 	'<';


STRING 	: 	('"' (~'"')* '"') | ('\'' (~'\'')* '\'');
EOS	:	';' ;
ARROW	:       '->';
PIPE 	:	'|';
WS      :       (' '|'\r'|'\n')+ {$channel = HIDDEN;} ;



