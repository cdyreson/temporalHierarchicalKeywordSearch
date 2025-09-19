// $ANTLR 3.5.3 TXKS.g 2025-04-29 11:04:16

package usu.grammar;
import java.util.List;
import java.util.ArrayList;
import usu.algebra.KeywordSearchExpression;
//import usu.algebra.operator.*;
//import usu.algebra.operator.specific.*;
import java.lang.UnsupportedOperationException;
import usu.temporal.Time;
  

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
		"NOT", "PIPE", "RBRACE", "RBRACKET", "RPAREN", "STRING", "WILDCARD", "WS", 
		"'-'"
	};
	public static final int EOF=-1;
	public static final int T__47=47;
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
	// TXKS.g:136:1: program returns [KeywordSearchExpression exp] : exp1= kindOfSearch EOF ;
	public final KeywordSearchExpression program() throws RecognitionException {
		KeywordSearchExpression exp = null;


		KeywordSearchExpression exp1 =null;

		try {
			// TXKS.g:136:47: (exp1= kindOfSearch EOF )
			// TXKS.g:137:5: exp1= kindOfSearch EOF
			{
			pushFollow(FOLLOW_kindOfSearch_in_program74);
			exp1=kindOfSearch();
			state._fsp--;


			      //exp = null; //$op.exp;
			      if (hasError) { throw new TXKSParseException(errorMessage); }
			      exp = exp1;
			      
			match(input,EOF,FOLLOW_EOF_in_program82); 
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
	// TXKS.g:145:1: kindOfSearch returns [KeywordSearchExpression exp] : s= searchType exp1= expression ;
	public final KeywordSearchExpression kindOfSearch() throws RecognitionException {
		KeywordSearchExpression exp = null;


		Integer s =null;
		KeywordSearchExpression exp1 =null;

		try {
			// TXKS.g:145:51: (s= searchType exp1= expression )
			// TXKS.g:146:5: s= searchType exp1= expression
			{
			pushFollow(FOLLOW_searchType_in_kindOfSearch108);
			s=searchType();
			state._fsp--;

			pushFollow(FOLLOW_expression_in_kindOfSearch112);
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
	// TXKS.g:153:1: searchType returns [Integer st] : (k= statedSearchType |);
	public final Integer searchType() throws RecognitionException {
		Integer st = null;


		Integer k =null;

		try {
			// TXKS.g:153:32: (k= statedSearchType |)
			int alt1=2;
			int LA1_0 = input.LA(1);
			if ( (LA1_0==K_CURRENT||LA1_0==K_DURATION||LA1_0==K_EARLIEST||LA1_0==K_LATEST||(LA1_0 >= K_NONSEQUENCED && LA1_0 <= K_SEQUENCED)) ) {
				alt1=1;
			}
			else if ( (LA1_0==ID||LA1_0==K_SLICE||LA1_0==LPAREN||LA1_0==STRING) ) {
				alt1=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 1, 0, input);
				throw nvae;
			}

			switch (alt1) {
				case 1 :
					// TXKS.g:154:5: k= statedSearchType
					{
					pushFollow(FOLLOW_statedSearchType_in_searchType141);
					k=statedSearchType();
					state._fsp--;


					      //System.out.println("Have search type " + k);
					      st = k;
					    
					}
					break;
				case 2 :
					// TXKS.g:158:7: 
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
	// TXKS.g:163:1: statedSearchType returns [Integer i] : k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT ) ;
	public final Integer statedSearchType() throws RecognitionException {
		Integer i = null;


		Token k=null;

		try {
			// TXKS.g:163:37: (k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT ) )
			// TXKS.g:164:5: k= ( K_SEQUENCED | K_NONSEQUENCED | K_EARLIEST | K_DURATION | K_LATEST | K_NONTEMPORAL | K_CURRENT )
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

			        //System.out.println("Search type is " + (k!=null?k.getType():0));
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
	// TXKS.g:170:1: expression returns [KeywordSearchExpression exp] : ( ( LPAREN exp1= expression RPAREN ) | (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? ) | (sliceTime= sliceOperator expS= expression ) );
	public final KeywordSearchExpression expression() throws RecognitionException {
		KeywordSearchExpression exp = null;


		KeywordSearchExpression exp1 =null;
		ParserRuleReturnScope s1 =null;
		int op =0;
		KeywordSearchExpression e2 =null;
		ParserRuleReturnScope s2 =null;
		ParserRuleReturnScope sliceTime =null;
		KeywordSearchExpression expS =null;

		try {
			// TXKS.g:170:50: ( ( LPAREN exp1= expression RPAREN ) | (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? ) | (sliceTime= sliceOperator expS= expression ) )
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
			case K_SLICE:
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
					// TXKS.g:171:5: ( LPAREN exp1= expression RPAREN )
					{
					// TXKS.g:171:5: ( LPAREN exp1= expression RPAREN )
					// TXKS.g:171:6: LPAREN exp1= expression RPAREN
					{
					match(input,LPAREN,FOLLOW_LPAREN_in_expression226); 
					pushFollow(FOLLOW_expression_in_expression230);
					exp1=expression();
					state._fsp--;

					match(input,RPAREN,FOLLOW_RPAREN_in_expression232); 
					}

					     
					        //System.out.println("Have expression ");
					        exp = exp1;
					      
					}
					break;
				case 2 :
					// TXKS.g:176:5: (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? )
					{
					// TXKS.g:176:5: (s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )? )
					// TXKS.g:176:6: s1= stringOrId ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )?
					{
					pushFollow(FOLLOW_stringOrId_in_expression251);
					s1=stringOrId();
					state._fsp--;


					                  //System.out.println("String or id " + (s1!=null?input.toString(s1.start,s1.stop):null));
					        exp = new KeywordSearchExpression((s1!=null?input.toString(s1.start,s1.stop):null), operand--);
					        //System.out.println("Have string  " + (s1!=null?input.toString(s1.start,s1.stop):null));
					       
					// TXKS.g:182:8: ( (op= operator e2= expression ) | (s2= stringOrId exp1= stringExpression ) )?
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
							// TXKS.g:182:9: (op= operator e2= expression )
							{
							// TXKS.g:182:9: (op= operator e2= expression )
							// TXKS.g:182:10: op= operator e2= expression
							{
							pushFollow(FOLLOW_operator_in_expression271);
							op=operator();
							state._fsp--;

							pushFollow(FOLLOW_expression_in_expression275);
							e2=expression();
							state._fsp--;


							          exp = new KeywordSearchExpression(exp, op, e2);
							          //System.out.println("Have expression e2 ");
							          
							}

							}
							break;
						case 2 :
							// TXKS.g:186:12: (s2= stringOrId exp1= stringExpression )
							{
							// TXKS.g:186:12: (s2= stringOrId exp1= stringExpression )
							// TXKS.g:186:13: s2= stringOrId exp1= stringExpression
							{
							pushFollow(FOLLOW_stringOrId_in_expression294);
							s2=stringOrId();
							state._fsp--;

							pushFollow(FOLLOW_stringExpression_in_expression298);
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
					// TXKS.g:194:9: (sliceTime= sliceOperator expS= expression )
					{
					// TXKS.g:194:9: (sliceTime= sliceOperator expS= expression )
					// TXKS.g:194:10: sliceTime= sliceOperator expS= expression
					{
					pushFollow(FOLLOW_sliceOperator_in_expression334);
					sliceTime=sliceOperator();
					state._fsp--;

					pushFollow(FOLLOW_expression_in_expression338);
					expS=expression();
					state._fsp--;


					          
					          String sliceText = (sliceTime!=null?input.toString(sliceTime.start,sliceTime.stop):null);
					          // Take out @slice keyword
					          sliceText = sliceText.substring(6).trim();
					          // Lop off the square brackets
					          sliceText = sliceText.substring( 1, sliceText.length() - 1 ).trim();
					          
					          exp = new KeywordSearchExpression(new Time(sliceText), expS);
					          
					          //System.out.println("Slice expression is " + sliceText);
					          
					}


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
	// TXKS.g:212:5: stringExpression returns [KeywordSearchExpression exp] : ( (s2= stringOrId exp1= stringExpression ) |);
	public final KeywordSearchExpression stringExpression() throws RecognitionException {
		KeywordSearchExpression exp = null;


		ParserRuleReturnScope s2 =null;
		KeywordSearchExpression exp1 =null;

		try {
			// TXKS.g:212:60: ( (s2= stringOrId exp1= stringExpression ) |)
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
					// TXKS.g:213:5: (s2= stringOrId exp1= stringExpression )
					{
					// TXKS.g:213:5: (s2= stringOrId exp1= stringExpression )
					// TXKS.g:213:6: s2= stringOrId exp1= stringExpression
					{
					pushFollow(FOLLOW_stringOrId_in_stringExpression391);
					s2=stringOrId();
					state._fsp--;

					pushFollow(FOLLOW_stringExpression_in_stringExpression395);
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
					// TXKS.g:220:5: 
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


	public static class sliceOperator_return extends ParserRuleReturnScope {
		public Time time;
	};


	// $ANTLR start "sliceOperator"
	// TXKS.g:228:1: sliceOperator returns [Time time] : K_SLICE '[' start= INT ( '-' stop= INT )? ']' ;
	public final TXKSParser.sliceOperator_return sliceOperator() throws RecognitionException {
		TXKSParser.sliceOperator_return retval = new TXKSParser.sliceOperator_return();
		retval.start = input.LT(1);

		Token start=null;
		Token stop=null;

		try {
			// TXKS.g:228:34: ( K_SLICE '[' start= INT ( '-' stop= INT )? ']' )
			// TXKS.g:229:4: K_SLICE '[' start= INT ( '-' stop= INT )? ']'
			{
			match(input,K_SLICE,FOLLOW_K_SLICE_in_sliceOperator438); 
			match(input,LBRACKET,FOLLOW_LBRACKET_in_sliceOperator440); 
			start=(Token)match(input,INT,FOLLOW_INT_in_sliceOperator444); 
			// TXKS.g:229:26: ( '-' stop= INT )?
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0==47) ) {
				alt5=1;
			}
			switch (alt5) {
				case 1 :
					// TXKS.g:229:27: '-' stop= INT
					{
					match(input,47,FOLLOW_47_in_sliceOperator447); 
					stop=(Token)match(input,INT,FOLLOW_INT_in_sliceOperator451); 
					}
					break;

			}

			match(input,RBRACKET,FOLLOW_RBRACKET_in_sliceOperator455); 

			        //System.out.println("Unary time is " + (start!=null?start.getText():null) + " " + stop + " " + $c.text);
			        /*
			        if (stop == null) {     
			          time = new Time((start!=null?start.getText():null) + "-" + (start!=null?start.getText():null));
			       } else {
			          time = new Time((start!=null?start.getText():null) + "-" + (stop!=null?stop.getText():null));
			       }
			        */
			    
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
	// $ANTLR end "sliceOperator"



	// $ANTLR start "operator"
	// TXKS.g:242:1: operator returns [int code] : c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_MEETS | K_DURING ) ;
	public final int operator() throws RecognitionException {
		int code = 0;


		Token c=null;

		try {
			// TXKS.g:242:28: (c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_MEETS | K_DURING ) )
			// TXKS.g:243:5: c= ( K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_MEETS | K_DURING )
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
	// TXKS.g:252:1: stringOrId : (x= STRING |v= ID );
	public final TXKSParser.stringOrId_return stringOrId() throws RecognitionException {
		TXKSParser.stringOrId_return retval = new TXKSParser.stringOrId_return();
		retval.start = input.LT(1);

		Token x=null;
		Token v=null;

		try {
			// TXKS.g:252:34: (x= STRING |v= ID )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0==STRING) ) {
				alt6=1;
			}
			else if ( (LA6_0==ID) ) {
				alt6=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// TXKS.g:253:5: x= STRING
					{
					x=(Token)match(input,STRING,FOLLOW_STRING_in_stringOrId594); 
					}
					break;
				case 2 :
					// TXKS.g:254:7: v= ID
					{
					v=(Token)match(input,ID,FOLLOW_ID_in_stringOrId607); 
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



	public static final BitSet FOLLOW_kindOfSearch_in_program74 = new BitSet(new long[]{0x0000000000000000L});
	public static final BitSet FOLLOW_EOF_in_program82 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_searchType_in_kindOfSearch108 = new BitSet(new long[]{0x0000101080002000L});
	public static final BitSet FOLLOW_expression_in_kindOfSearch112 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_statedSearchType_in_searchType141 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_statedSearchType175 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_LPAREN_in_expression226 = new BitSet(new long[]{0x0000101080002000L});
	public static final BitSet FOLLOW_expression_in_expression230 = new BitSet(new long[]{0x0000080000000000L});
	public static final BitSet FOLLOW_RPAREN_in_expression232 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringOrId_in_expression251 = new BitSet(new long[]{0x000010000A86A002L});
	public static final BitSet FOLLOW_operator_in_expression271 = new BitSet(new long[]{0x0000101080002000L});
	public static final BitSet FOLLOW_expression_in_expression275 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringOrId_in_expression294 = new BitSet(new long[]{0x0000100000002000L});
	public static final BitSet FOLLOW_stringExpression_in_expression298 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_sliceOperator_in_expression334 = new BitSet(new long[]{0x0000101080002000L});
	public static final BitSet FOLLOW_expression_in_expression338 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_stringOrId_in_stringExpression391 = new BitSet(new long[]{0x0000100000002000L});
	public static final BitSet FOLLOW_stringExpression_in_stringExpression395 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_K_SLICE_in_sliceOperator438 = new BitSet(new long[]{0x0000000200000000L});
	public static final BitSet FOLLOW_LBRACKET_in_sliceOperator440 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_INT_in_sliceOperator444 = new BitSet(new long[]{0x0000840000000000L});
	public static final BitSet FOLLOW_47_in_sliceOperator447 = new BitSet(new long[]{0x0000000000004000L});
	public static final BitSet FOLLOW_INT_in_sliceOperator451 = new BitSet(new long[]{0x0000040000000000L});
	public static final BitSet FOLLOW_RBRACKET_in_sliceOperator455 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_set_in_operator486 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_STRING_in_stringOrId594 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_ID_in_stringOrId607 = new BitSet(new long[]{0x0000000000000002L});
}
