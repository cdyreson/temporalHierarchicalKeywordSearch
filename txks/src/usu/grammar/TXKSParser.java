// $ANTLR 3.5.3 TXKS.g 2023-10-09 09:40:09

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

@SuppressWarnings("all")
public class TXKSParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "ARROW", "AT", "COMMA", "DIGIT", 
		"DOUBLEWILDCARD", "EOS", "EQUALS", "GE", "GT", "ID", "INT", "K_AFTER", 
		"K_ANY", "K_BEFORE", "K_CONTAINS", "K_CURRENT", "K_DESCENDANTCHANGES", 
		"K_DESCENDANTVERSIONS", "K_DURATION", "K_DURING", "K_EARLIEST", "K_INTERSECTS", 
		"K_LATEST", "K_MEETS", "K_NONSEQUENCED", "K_NONTEMPORAL", "K_SEQUENCED", 
		"K_SLICE", "LBRACE", "LBRACKET", "LE", "LETTER", "LPAREN", "LT", "NAMECHAR", 
		"NOT", "PIPE", "RBRACE", "RBRACKET", "RPAREN", "STRING", "WILDCARD", "WS"
	};
	public static final int EOF=-1;
	public static final int ARROW=4;
	public static final int AT=5;
	public static final int COMMA=6;
	public static final int DIGIT=7;
	public static final int DOUBLEWILDCARD=8;
	public static final int EOS=9;
	public static final int EQUALS=10;
	public static final int GE=11;
	public static final int GT=12;
	public static final int ID=13;
	public static final int INT=14;
	public static final int K_AFTER=15;
	public static final int K_ANY=16;
	public static final int K_BEFORE=17;
	public static final int K_CONTAINS=18;
	public static final int K_CURRENT=19;
	public static final int K_DESCENDANTCHANGES=20;
	public static final int K_DESCENDANTVERSIONS=21;
	public static final int K_DURATION=22;
	public static final int K_DURING=23;
	public static final int K_EARLIEST=24;
	public static final int K_INTERSECTS=25;
	public static final int K_LATEST=26;
	public static final int K_MEETS=27;
	public static final int K_NONSEQUENCED=28;
	public static final int K_NONTEMPORAL=29;
	public static final int K_SEQUENCED=30;
	public static final int K_SLICE=31;
	public static final int LBRACE=32;
	public static final int LBRACKET=33;
	public static final int LE=34;
	public static final int LETTER=35;
	public static final int LPAREN=36;
	public static final int LT=37;
	public static final int NAMECHAR=38;
	public static final int NOT=39;
	public static final int PIPE=40;
	public static final int RBRACE=41;
	public static final int RBRACKET=42;
	public static final int RPAREN=43;
	public static final int STRING=44;
	public static final int WILDCARD=45;
	public static final int WS=46;

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

	@Override public String[] getTokenNames() { return TXKSParser.tokenNames; }
	@Override public String getGrammarFileName() { return "TXKS.g"; }



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



	// $ANTLR start "program"
	// TXKS.g:159:1: program returns [KeywordSearchExpression exp] : exp1= kindOfSearch EOF ;
	public final KeywordSearchExpression program() throws RecognitionException {
		KeywordSearchExpression exp = null;


		KeywordSearchExpression exp1 =null;

		try {
			// TXKS.g:159:47: (exp1= kindOfSearch EOF )
			// TXKS.g:160:5: exp1= kindOfSearch EOF
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
	// TXKS.g:168:1: kindOfSearch returns [KeywordSearchExpression exp] : s= searchType exp1= expression ;
	public final KeywordSearchExpression kindOfSearch() throws RecognitionException {
		KeywordSearchExpression exp = null;


		Integer s =null;
		KeywordSearchExpression exp1 =null;

		try {
			// TXKS.g:168:51: (s= searchType exp1= expression )
			// TXKS.g:169:5: s= searchType exp1= expression
			{
			pushFollow(FOLLOW_searchType_in_kindOfSearch124);
			s=searchType();
			state._fsp--;

			pushFollow(FOLLOW_expression_in_kindOfSearch128);
			exp1=expression();
			state._fsp--;


			      exp = exp1;
			      //System.out.println("Search type is " + s.intValue());
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
	// TXKS.g:176:1: searchType returns [Integer st] : (k= statedSearchType |);
	public final Integer searchType() throws RecognitionException {
		Integer st = null;


		Integer k =null;

		try {
			// TXKS.g:176:32: (k= statedSearchType |)
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==K_CURRENT||LA1_0==K_DURATION||LA1_0==K_EARLIEST||LA1_0==K_LATEST||(LA1_0 >= K_NONSEQUENCED && LA1_0 <= K_SEQUENCED)) ) {
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
					// TXKS.g:177:5: k= statedSearchType
					{
					pushFollow(FOLLOW_statedSearchType_in_searchType157);
					k=statedSearchType();
					state._fsp--;


					      //System.out.println("Have search type " + k);
					      st = k;
					    
					}
					break;
				case 2 :
					// TXKS.g:181:7: 
					{

					      st = K_NONTEMPORAL;
					    
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
	// TXKS.g:186:1: statedSearchType returns [Integer i] : k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT ) ;
	public final Integer statedSearchType() throws RecognitionException {
		Integer i = null;


		Token k=null;

		try {
			// TXKS.g:186:37: (k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT ) )
			// TXKS.g:187:5: k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT )
			{
			k=input.LT(1);
			if ( input.LA(1)==K_CURRENT||input.LA(1)==K_DURATION||input.LA(1)==K_EARLIEST||input.LA(1)==K_LATEST||(input.LA(1) >= K_NONSEQUENCED && input.LA(1) <= K_SEQUENCED) ) {
				input.consume();
				state.errorRecovery=false;
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				throw mse;
			}

			      i = (k!=null?k.getType():0);
			    
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
	// TXKS.g:192:1: expression returns [KeywordSearchExpression exp] : ( ( LPAREN exp1= expression RPAREN ) | (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? ) |);
	public final KeywordSearchExpression expression() throws RecognitionException {
		KeywordSearchExpression exp = null;


		KeywordSearchExpression exp1 =null;
		ParserRuleReturnScope s1 =null;
		int op =0;
		KeywordSearchExpression e2 =null;
		ParserRuleReturnScope s2 =null;

		try {
			// TXKS.g:192:50: ( ( LPAREN exp1= expression RPAREN ) | (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? ) |)
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
					// TXKS.g:193:5: ( LPAREN exp1= expression RPAREN )
					{
					// TXKS.g:193:5: ( LPAREN exp1= expression RPAREN )
					// TXKS.g:193:6: LPAREN exp1= expression RPAREN
					{
					match(input,LPAREN,FOLLOW_LPAREN_in_expression242); 
					pushFollow(FOLLOW_expression_in_expression246);
					exp1=expression();
					state._fsp--;

					match(input,RPAREN,FOLLOW_RPAREN_in_expression248); 
					}

					     
					        //System.out.println("Have expression ");
					        exp = exp1;
					      
					}
					break;
				case 2 :
					// TXKS.g:198:5: (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? )
					{
					// TXKS.g:198:5: (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? )
					// TXKS.g:198:6: s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )?
					{
					pushFollow(FOLLOW_stringOrId_in_expression267);
					s1=stringOrId();
					state._fsp--;


					        exp = new KeywordSearchExpression((s1!=null?input.toString(s1.start,s1.stop):null), operand--);
					        //System.out.println("Have string  " + (s1!=null?input.toString(s1.start,s1.stop):null));
					    
					// TXKS.g:203:5: ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )?
					int alt2=3;
					int LA2_0 = input.LA(1);
					if ( (LA2_0==K_AFTER||(LA2_0 >= K_BEFORE && LA2_0 <= K_CONTAINS)||LA2_0==K_DURING||LA2_0==K_INTERSECTS||LA2_0==K_MEETS) ) {
						alt2=1;
					}
					else if ( (LA2_0==ID||LA2_0==STRING) ) {
						alt2=2;
					}
					switch (alt2) {
						case 1 :
							// TXKS.g:203:6: (op= operator e2= expression )
							{
							// TXKS.g:203:6: (op= operator e2= expression )
							// TXKS.g:203:7: op= operator e2= expression
							{
							pushFollow(FOLLOW_operator_in_expression284);
							op=operator();
							state._fsp--;

							pushFollow(FOLLOW_expression_in_expression288);
							e2=expression();
							state._fsp--;


							        exp = new KeywordSearchExpression(exp, op, e2);
							        //System.out.println("Have expression e2 ");
							      
							}

							}
							break;
						case 2 :
							// TXKS.g:207:9: (s2= stringOrId exp1= stringExpression )
							{
							// TXKS.g:207:9: (s2= stringOrId exp1= stringExpression )
							// TXKS.g:207:10: s2= stringOrId exp1= stringExpression
							{
							pushFollow(FOLLOW_stringOrId_in_expression304);
							s2=stringOrId();
							state._fsp--;

							pushFollow(FOLLOW_stringExpression_in_expression308);
							exp1=stringExpression();
							state._fsp--;

							}


							        String s = (s2!=null?input.toString(s2.start,s2.stop):null);
							        exp = new KeywordSearchExpression(exp, 0, s, operand--);
							        if (exp1 != null) {exp = new KeywordSearchExpression(exp, 0, exp1);}
							        //System.out.println("Have string  " + (s2!=null?input.toString(s2.start,s2.stop):null));
							      
							}
							break;

					}

					}

					}
					break;
				case 3 :
					// TXKS.g:216:7: 
					{

					        //System.out.println("Expression done");
					      
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
	// TXKS.g:221:5: stringExpression returns [KeywordSearchExpression exp] : ( (s2= stringOrId exp1= stringExpression ) |);
	public final KeywordSearchExpression stringExpression() throws RecognitionException {
		KeywordSearchExpression exp = null;


		ParserRuleReturnScope s2 =null;
		KeywordSearchExpression exp1 =null;

		try {
			// TXKS.g:221:60: ( (s2= stringOrId exp1= stringExpression ) |)
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
					// TXKS.g:222:5: (s2= stringOrId exp1= stringExpression )
					{
					// TXKS.g:222:5: (s2= stringOrId exp1= stringExpression )
					// TXKS.g:222:6: s2= stringOrId exp1= stringExpression
					{
					pushFollow(FOLLOW_stringOrId_in_stringExpression378);
					s2=stringOrId();
					state._fsp--;

					pushFollow(FOLLOW_stringExpression_in_stringExpression382);
					exp1=stringExpression();
					state._fsp--;


					        String s = (s2!=null?input.toString(s2.start,s2.stop):null);
					        if (exp1 != null) {exp = new KeywordSearchExpression(s, 0, exp1, operand--);}
					        else {exp = new KeywordSearchExpression((s2!=null?input.toString(s2.start,s2.stop):null), operand--);}
					        //System.out.println("Have string  " + (s2!=null?input.toString(s2.start,s2.stop):null));
					      
					}

					}
					break;
				case 2 :
					// TXKS.g:229:5: 
					{

					      //System.out.println("Done");
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
	// TXKS.g:264:1: operator returns [int code] : c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_MEETS | K_DURING ) ;
	public final int operator() throws RecognitionException {
		int code = 0;


		Token c=null;

		try {
			// TXKS.g:264:28: (c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_MEETS | K_DURING ) )
			// TXKS.g:265:5: c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_MEETS | K_DURING )
			{
			c=input.LT(1);
			if ( input.LA(1)==K_AFTER||(input.LA(1) >= K_BEFORE && input.LA(1) <= K_CONTAINS)||input.LA(1)==K_DURING||input.LA(1)==K_INTERSECTS||input.LA(1)==K_MEETS ) {
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
	// TXKS.g:268:1: stringOrId : (x= STRING |v= ID );
	public final TXKSParser.stringOrId_return stringOrId() throws RecognitionException {
		TXKSParser.stringOrId_return retval = new TXKSParser.stringOrId_return();
		retval.start = input.LT(1);

		Token x=null;
		Token v=null;

		try {
			// TXKS.g:268:34: (x= STRING |v= ID )
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
					// TXKS.g:269:5: x= STRING
					{
					x=(Token)match(input,STRING,FOLLOW_STRING_in_stringOrId486); 
					}
					break;
				case 2 :
					// TXKS.g:270:7: v= ID
					{
					v=(Token)match(input,ID,FOLLOW_ID_in_stringOrId499); 
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
	public static final BitSet FOLLOW_searchType_in_kindOfSearch124 = new BitSet(new long[]{0x0000101000002000L});
	public static final BitSet FOLLOW_expression_in_kindOfSearch128 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statedSearchType_in_searchType157 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_statedSearchType191 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_expression242 = new BitSet(new long[]{0x0000181000002000L});
	public static final BitSet FOLLOW_expression_in_expression246 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_RPAREN_in_expression248 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringOrId_in_expression267 = new BitSet(new long[]{0x000010000A86A002L});
	public static final BitSet FOLLOW_operator_in_expression284 = new BitSet(new long[]{0x0000101000002000L});
	public static final BitSet FOLLOW_expression_in_expression288 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringOrId_in_expression304 = new BitSet(new long[]{0x0000100000002000L});
	public static final BitSet FOLLOW_stringExpression_in_expression308 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringOrId_in_stringExpression378 = new BitSet(new long[]{0x0000100000002000L});
	public static final BitSet FOLLOW_stringExpression_in_stringExpression382 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_operator439 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_stringOrId486 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_stringOrId499 = new BitSet(new long[]{0x0000000000000002L});
}
