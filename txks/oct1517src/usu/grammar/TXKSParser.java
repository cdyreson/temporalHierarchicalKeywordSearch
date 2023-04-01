// $ANTLR 3.4 D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g 2017-06-20 09:43:48

package usu.grammar;
import java.util.List;
import java.util.ArrayList;
import usu.algebra.KeywordSearchExpression;
//import usu.algebra.operator.*;
//import usu.algebra.operator.specific.*;
import java.lang.UnsupportedOperationException;
  

import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked"})
public class TXKSParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "ARROW", "COMMA", "DIGIT", "DOUBLEWILDCARD", "EOS", "EQUALS", "GE", "GT", "ID", "INT", "K_AFTER", "K_ANY", "K_BEFORE", "K_CONTAINS", "K_CURRENT", "K_DESCENDANTCHANGES", "K_DESCENDANTVERSIONS", "K_DURATION", "K_EARLIEST", "K_INTERSECTS", "K_LATEST", "K_NONSEQUENCED", "K_NONTEMPORAL", "K_SEQUENCED", "K_SLICE", "LBRACE", "LBRACKET", "LE", "LETTER", "LPAREN", "LT", "NAMECHAR", "NOT", "PIPE", "RBRACE", "RBRACKET", "RPAREN", "STRING", "WILDCARD", "WS"
    };

    public static final int EOF=-1;
    public static final int ARROW=4;
    public static final int COMMA=5;
    public static final int DIGIT=6;
    public static final int DOUBLEWILDCARD=7;
    public static final int EOS=8;
    public static final int EQUALS=9;
    public static final int GE=10;
    public static final int GT=11;
    public static final int ID=12;
    public static final int INT=13;
    public static final int K_AFTER=14;
    public static final int K_ANY=15;
    public static final int K_BEFORE=16;
    public static final int K_CONTAINS=17;
    public static final int K_CURRENT=18;
    public static final int K_DESCENDANTCHANGES=19;
    public static final int K_DESCENDANTVERSIONS=20;
    public static final int K_DURATION=21;
    public static final int K_EARLIEST=22;
    public static final int K_INTERSECTS=23;
    public static final int K_LATEST=24;
    public static final int K_NONSEQUENCED=25;
    public static final int K_NONTEMPORAL=26;
    public static final int K_SEQUENCED=27;
    public static final int K_SLICE=28;
    public static final int LBRACE=29;
    public static final int LBRACKET=30;
    public static final int LE=31;
    public static final int LETTER=32;
    public static final int LPAREN=33;
    public static final int LT=34;
    public static final int NAMECHAR=35;
    public static final int NOT=36;
    public static final int PIPE=37;
    public static final int RBRACE=38;
    public static final int RBRACKET=39;
    public static final int RPAREN=40;
    public static final int STRING=41;
    public static final int WILDCARD=42;
    public static final int WS=43;

    // delegates
    public Parser[] getDelegates() {
        return new Parser[] {};
    }

    // delegators


    public TXKSParser(TokenStream input) {
        this(input, new RecognizerSharedState());
    }
    public TXKSParser(TokenStream input, RecognizerSharedState state) {
        super(input, state);
    }

    public String[] getTokenNames() { return TXKSParser.tokenNames; }
    public String getGrammarFileName() { return "D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g"; }



    protected static String errorMessage = "";
    protected boolean hasError = false;
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



    // $ANTLR start "program"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:158:1: program returns [KeywordSearchExpression exp] : exp1= kindOfSearch EOF ;
    public final KeywordSearchExpression program() throws RecognitionException {
        KeywordSearchExpression exp = null;


        KeywordSearchExpression exp1 =null;


        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:158:47: (exp1= kindOfSearch EOF )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:159:5: exp1= kindOfSearch EOF
            {
            pushFollow(FOLLOW_kindOfSearch_in_program90);
            exp1=kindOfSearch();

            state._fsp--;



                  //exp = null; //$op.exp;
                  if (hasError) { throw new TXKSParseException(errorMessage); }
                  exp = exp1;
                  

            match(input,EOF,FOLLOW_EOF_in_program98); 

            }

        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return exp;
    }
    // $ANTLR end "program"



    // $ANTLR start "kindOfSearch"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:167:1: kindOfSearch returns [KeywordSearchExpression exp] : s= searchType exp1= expression ;
    public final KeywordSearchExpression kindOfSearch() throws RecognitionException {
        KeywordSearchExpression exp = null;


        Integer s =null;

        KeywordSearchExpression exp1 =null;


        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:167:51: (s= searchType exp1= expression )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:168:5: s= searchType exp1= expression
            {
            pushFollow(FOLLOW_searchType_in_kindOfSearch124);
            s=searchType();

            state._fsp--;


            pushFollow(FOLLOW_expression_in_kindOfSearch128);
            exp1=expression();

            state._fsp--;



                  exp = exp1;
                  System.out.println("Search type is " + s.intValue());
                  exp.setSearchType(s);
                

            }

        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return exp;
    }
    // $ANTLR end "kindOfSearch"



    // $ANTLR start "searchType"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:175:1: searchType returns [Integer st] : (k= statedSearchType |);
    public final Integer searchType() throws RecognitionException {
        Integer st = null;


        Integer k =null;


        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:175:32: (k= statedSearchType |)
            int alt1=2;
            int LA1_0 = input.LA(1);

            if ( (LA1_0==K_CURRENT||(LA1_0 >= K_DURATION && LA1_0 <= K_EARLIEST)||(LA1_0 >= K_LATEST && LA1_0 <= K_SEQUENCED)) ) {
                alt1=1;
            }
            else if ( (LA1_0==EOF||LA1_0==ID||LA1_0==LPAREN||LA1_0==STRING) ) {
                alt1=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 1, 0, input);

                throw nvae;

            }
            switch (alt1) {
                case 1 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:176:5: k= statedSearchType
                    {
                    pushFollow(FOLLOW_statedSearchType_in_searchType157);
                    k=statedSearchType();

                    state._fsp--;



                          //System.out.println("Have search type " + k);
                          st = k;
                        

                    }
                    break;
                case 2 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:180:7: 
                    {

                          System.out.println("got herekkk");
                          st = new Integer(K_NONTEMPORAL);
                        

                    }
                    break;

            }
        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return st;
    }
    // $ANTLR end "searchType"



    // $ANTLR start "statedSearchType"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:186:1: statedSearchType returns [Integer i] : k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT ) ;
    public final Integer statedSearchType() throws RecognitionException {
        Integer i = null;


        Token k=null;

        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:186:37: (k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT ) )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:187:5: k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT )
            {
            k=(Token)input.LT(1);

            if ( input.LA(1)==K_CURRENT||(input.LA(1) >= K_DURATION && input.LA(1) <= K_EARLIEST)||(input.LA(1) >= K_LATEST && input.LA(1) <= K_SEQUENCED) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }



                  i = new Integer((k!=null?k.getType():0));
                

            }

        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return i;
    }
    // $ANTLR end "statedSearchType"



    // $ANTLR start "expression"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:192:1: expression returns [KeywordSearchExpression exp] : ( ( LPAREN exp1= expression RPAREN ) | (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? ) |);
    public final KeywordSearchExpression expression() throws RecognitionException {
        KeywordSearchExpression exp = null;


        KeywordSearchExpression exp1 =null;

        TXKSParser.stringOrId_return s1 =null;

        int op =0;

        KeywordSearchExpression e2 =null;

        TXKSParser.stringOrId_return s2 =null;


        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:192:50: ( ( LPAREN exp1= expression RPAREN ) | (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? ) |)
            int alt3=3;
            switch ( input.LA(1) ) {
            case LPAREN:
                {
                alt3=1;
                }
                break;
            case ID:
            case STRING:
                {
                alt3=2;
                }
                break;
            case EOF:
            case RPAREN:
                {
                alt3=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 3, 0, input);

                throw nvae;

            }

            switch (alt3) {
                case 1 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:193:5: ( LPAREN exp1= expression RPAREN )
                    {
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:193:5: ( LPAREN exp1= expression RPAREN )
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:193:6: LPAREN exp1= expression RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_expression242); 

                    pushFollow(FOLLOW_expression_in_expression246);
                    exp1=expression();

                    state._fsp--;


                    match(input,RPAREN,FOLLOW_RPAREN_in_expression248); 

                    }


                         
                            System.out.println("Have expression ");
                            exp = exp1;
                          

                    }
                    break;
                case 2 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:198:5: (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? )
                    {
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:198:5: (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? )
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:198:6: s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )?
                    {
                    pushFollow(FOLLOW_stringOrId_in_expression267);
                    s1=stringOrId();

                    state._fsp--;



                            exp = new KeywordSearchExpression((s1!=null?input.toString(s1.start,s1.stop):null));
                            System.out.println("Have string  " + (s1!=null?input.toString(s1.start,s1.stop):null));
                        

                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:203:5: ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )?
                    int alt2=3;
                    int LA2_0 = input.LA(1);

                    if ( (LA2_0==K_AFTER||(LA2_0 >= K_BEFORE && LA2_0 <= K_CONTAINS)||LA2_0==K_INTERSECTS) ) {
                        alt2=1;
                    }
                    else if ( (LA2_0==ID||LA2_0==STRING) ) {
                        alt2=2;
                    }
                    switch (alt2) {
                        case 1 :
                            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:203:6: (op= operator e2= expression )
                            {
                            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:203:6: (op= operator e2= expression )
                            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:203:7: op= operator e2= expression
                            {
                            pushFollow(FOLLOW_operator_in_expression284);
                            op=operator();

                            state._fsp--;


                            pushFollow(FOLLOW_expression_in_expression288);
                            e2=expression();

                            state._fsp--;



                                    exp = new KeywordSearchExpression(exp, new Integer(op), e2);
                                    System.out.println("Have expression e2 ");
                                  

                            }


                            }
                            break;
                        case 2 :
                            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:207:9: (s2= stringOrId exp1= stringExpression )
                            {
                            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:207:9: (s2= stringOrId exp1= stringExpression )
                            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:207:10: s2= stringOrId exp1= stringExpression
                            {
                            pushFollow(FOLLOW_stringOrId_in_expression304);
                            s2=stringOrId();

                            state._fsp--;


                            pushFollow(FOLLOW_stringExpression_in_expression308);
                            exp1=stringExpression();

                            state._fsp--;


                            }



                                    String s = (s2!=null?input.toString(s2.start,s2.stop):null);
                                    exp = new KeywordSearchExpression(exp, new Integer(0), s);
                                    if (exp1 != null) {exp = new KeywordSearchExpression(exp, new Integer(0), exp1);}
                                    System.out.println("Have string  " + (s2!=null?input.toString(s2.start,s2.stop):null));
                                  

                            }
                            break;

                    }


                    }


                    }
                    break;
                case 3 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:216:2: 
                    {

                            System.out.println("Expression done");
                          

                    }
                    break;

            }
        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return exp;
    }
    // $ANTLR end "expression"



    // $ANTLR start "stringExpression"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:221:5: stringExpression returns [KeywordSearchExpression exp] : ( (s2= stringOrId exp1= stringExpression ) |);
    public final KeywordSearchExpression stringExpression() throws RecognitionException {
        KeywordSearchExpression exp = null;


        TXKSParser.stringOrId_return s2 =null;

        KeywordSearchExpression exp1 =null;


        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:221:60: ( (s2= stringOrId exp1= stringExpression ) |)
            int alt4=2;
            int LA4_0 = input.LA(1);

            if ( (LA4_0==ID||LA4_0==STRING) ) {
                alt4=1;
            }
            else if ( (LA4_0==EOF||LA4_0==RPAREN) ) {
                alt4=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 4, 0, input);

                throw nvae;

            }
            switch (alt4) {
                case 1 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:222:5: (s2= stringOrId exp1= stringExpression )
                    {
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:222:5: (s2= stringOrId exp1= stringExpression )
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:222:6: s2= stringOrId exp1= stringExpression
                    {
                    pushFollow(FOLLOW_stringOrId_in_stringExpression373);
                    s2=stringOrId();

                    state._fsp--;


                    pushFollow(FOLLOW_stringExpression_in_stringExpression377);
                    exp1=stringExpression();

                    state._fsp--;



                            String s = (s2!=null?input.toString(s2.start,s2.stop):null);
                            if (exp1 != null) {exp = new KeywordSearchExpression(s, new Integer(0), exp1);}
                            else {exp = new KeywordSearchExpression((s2!=null?input.toString(s2.start,s2.stop):null));}
                            System.out.println("Have string  " + (s2!=null?input.toString(s2.start,s2.stop):null));
                          

                    }


                    }
                    break;
                case 2 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:229:5: 
                    {

                          System.out.println("Done");
                          //exp = null;
                        

                    }
                    break;

            }
        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return exp;
    }
    // $ANTLR end "stringExpression"



    // $ANTLR start "operator"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:264:1: operator returns [int code] : c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER ) ;
    public final int operator() throws RecognitionException {
        int code = 0;


        Token c=null;

        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:264:28: (c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER ) )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:265:5: c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER )
            {
            c=(Token)input.LT(1);

            if ( input.LA(1)==K_AFTER||(input.LA(1) >= K_BEFORE && input.LA(1) <= K_CONTAINS)||input.LA(1)==K_INTERSECTS ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }


            code = (c!=null?c.getType():0);

            }

        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return code;
    }
    // $ANTLR end "operator"


    public static class stringOrId_return extends ParserRuleReturnScope {
    };


    // $ANTLR start "stringOrId"
    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:268:1: stringOrId : (x= STRING |v= ID );
    public final TXKSParser.stringOrId_return stringOrId() throws RecognitionException {
        TXKSParser.stringOrId_return retval = new TXKSParser.stringOrId_return();
        retval.start = input.LT(1);


        Token x=null;
        Token v=null;

        try {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:268:34: (x= STRING |v= ID )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==STRING) ) {
                alt5=1;
            }
            else if ( (LA5_0==ID) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;

            }
            switch (alt5) {
                case 1 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:269:5: x= STRING
                    {
                    x=(Token)match(input,STRING,FOLLOW_STRING_in_stringOrId473); 

                    }
                    break;
                case 2 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:270:7: v= ID
                    {
                    v=(Token)match(input,ID,FOLLOW_ID_in_stringOrId486); 

                    }
                    break;

            }
            retval.stop = input.LT(-1);


        }

          catch (RecognitionException e) {
            throw e;
          }

        finally {
        	// do for sure before leaving
        }
        return retval;
    }
    // $ANTLR end "stringOrId"

    // Delegated rules


 

    public static final BitSet FOLLOW_kindOfSearch_in_program90 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_program98 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_searchType_in_kindOfSearch124 = new BitSet(new long[]{0x0000020200001000L});
    public static final BitSet FOLLOW_expression_in_kindOfSearch128 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_statedSearchType_in_searchType157 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_statedSearchType191 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_expression242 = new BitSet(new long[]{0x0000030200001000L});
    public static final BitSet FOLLOW_expression_in_expression246 = new BitSet(new long[]{0x0000010000000000L});
    public static final BitSet FOLLOW_RPAREN_in_expression248 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stringOrId_in_expression267 = new BitSet(new long[]{0x0000020000835002L});
    public static final BitSet FOLLOW_operator_in_expression284 = new BitSet(new long[]{0x0000020200001000L});
    public static final BitSet FOLLOW_expression_in_expression288 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stringOrId_in_expression304 = new BitSet(new long[]{0x0000020000001000L});
    public static final BitSet FOLLOW_stringExpression_in_expression308 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_stringOrId_in_stringExpression373 = new BitSet(new long[]{0x0000020000001000L});
    public static final BitSet FOLLOW_stringExpression_in_stringExpression377 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_operator434 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_stringOrId473 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ID_in_stringOrId486 = new BitSet(new long[]{0x0000000000000002L});

}