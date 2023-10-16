grammar TXKS;	

options {
  k=2;
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

@lexer::header {
package usu.grammar;
import java.util.List;
import java.util.ArrayList;
import usu.algebra.KeywordSearchExpression;
//import usu.algebra.operator.*;
//import usu.algebra.operator.specific.*;
import java.lang.UnsupportedOperationException;
  }

@members {

protected static String errorMessage = "";
protected boolean hasError = false;
public int operand = -1;
public static boolean verbose = false;
//protected static int keywordCount = 0;
public static List<String> keywords = new ArrayList(10); 

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


    
    
/*
program returns [Operator exp] :
    op=kindOfSearch {
      $exp = $op.exp;
      if (hasError) { throw new TXKSParseException(errorMessage); }
      }
    EOF
    ;


kindOfSearch returns [Operator exp] :
    K_SEQUENCED expression
       {$exp = new SequencedSearch($kindOfMatch.exp);}
    ;
    
    expressionContinues2 returns [Operator exp] :
    (o=operator e=expression {exp = $e.exp;})
    | (s=stringOrId c=expressionContinues2 {exp = $c.exp;})
    ;
  */  

program returns [KeywordSearchExpression exp] :
    exp1=kindOfSearch {
      //exp = null; //$op.exp;
      if (hasError) { throw new TXKSParseException(errorMessage); }
      exp = exp1;
      }
    EOF
    ;
    
kindOfSearch returns [KeywordSearchExpression exp]:
    s=searchType exp1=expression {
      exp = exp1;
      //System.out.println("Search type is " + s.intValue());
      exp.setSearchType(s);
    }
    ;
    
searchType returns [Integer st]:	
    k=statedSearchType {
      //System.out.println("Have search type " + k);
      st = k;
    }
    | {
      st = K_NONTEMPORAL;
    }
    ;
 
statedSearchType returns [Integer i]:	
    k=(K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT ) {
      i = $k.type;
    }
    ;

expression returns [KeywordSearchExpression exp] :
    (LPAREN exp1=expression RPAREN) {     
        //System.out.println("Have expression ");
        exp = exp1;
      }
    | 
    (s1=stringOrId {
        exp = new KeywordSearchExpression($s1.text, operand--);
        //System.out.println("Have string  " + $s1.text);
    }
    
    ((op=operator e2=expression {
        exp = new KeywordSearchExpression(exp, op, e2);
        //System.out.println("Have expression e2 ");
      })
      | (s2=stringOrId exp1=stringExpression) {
        String s = $s2.text;
        exp = new KeywordSearchExpression(exp, 0, s, operand--);
        if (exp1 != null) {exp = new KeywordSearchExpression(exp, 0, exp1);}
        //System.out.println("Have string  " + $s2.text);
      }
      ) ?
      )
      |
      {
        //System.out.println("Expression done");
      }
    ;
    
    stringExpression returns [KeywordSearchExpression exp] : 
    (s2=stringOrId exp1=stringExpression {
        String s = $s2.text;
        if (exp1 != null) {exp = new KeywordSearchExpression(s, 0, exp1, operand--);}
        else {exp = new KeywordSearchExpression($s2.text, operand--);}
        //System.out.println("Have string  " + $s2.text);
      }
      ) |
    {
      //System.out.println("Done");
      //exp = null;
    }
    ;
    
    /*
    expression1 returns [KeywordSearchExpression exp] :
    ((LPAREN exp1=expression RPAREN) {     
        System.out.println("Have expression ");
        exp = exp1;
      }
    | s1=stringOrId {
        exp = new KeywordSearchExpression($s1.text);
        System.out.println("Have string  " + $s1.text);
    }
    )
    ((op=operator {
        System.out.println("Have operator " + op);
      }
      | s2=stringOrId {
        String s = $s2.text;
        exp = new KeywordSearchExpression(exp, new Integer(0), s);
        System.out.println("Have string  " + $s2.text);
      }
      ) e2=expression {
        exp = new KeywordSearchExpression(exp, new Integer(op), e2);
        System.out.println("Have expression e2 ");
      }
      )? {
        System.out.println("Expression done");
      }
    ;
    */
    
operator returns [int code]:
    c = (K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_MEETS | K_DURING) {$code = $c.type;}
    ; 

stringOrId /*returns [String s]*/:
    x = STRING //{s = $x.text; keywords.put(s);}
    | v = ID //{s = $v.text; keywords.put(s);}
    ;
    
K_NONTEMPORAL		:	'@nontemporal';
K_EARLIEST		:	'@earliest';
K_CURRENT		:	'@current';
K_LATEST		:	'@latest';
K_MEETS			: 	'@meets';
K_DURATION		:	'@duration';
K_SEQUENCED		:	'@sequenced';
K_NONSEQUENCED		:	'@nonsequenced';
K_DESCENDANTVERSIONS 	:	'@descendantVersions';
K_DESCENDANTCHANGES 	:	'@descendantChanges';
K_SLICE			: 	'@slice';
K_CONTAINS 		:	'@contains';
K_DURING 		:	'@during';
K_INTERSECTS 		:	'@intersects';
K_BEFORE		:	'@before';
K_AFTER			:	'@after';
K_ANY			:	'@any';	

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

WILDCARD:	'*';
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
AT	:	'@';

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
WS      	:       (' '|'\r'|'\n')+ {$channel = HIDDEN;} ;



