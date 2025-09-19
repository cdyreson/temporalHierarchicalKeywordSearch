<<<<<<< HEAD
// $ANTLR 3.5.3 TXKS.g 2025-04-29 11:04:16
=======
// $ANTLR 3.5.3 TXKS.g 2025-02-18 11:17:22
>>>>>>> c363413bb1829df41520fde06ce5fffbcee5b7da

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
public class TXKSLexer extends Lexer {
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

	    
	    public void displayRecognitionError(String[] tokenNames, RecognitionException e) {
	        //errorMessage = buildErrorMessage(e);
	        String hdr = getErrorHeader(e);
	        String msg = getErrorMessage(e, TXKSParser.tokenNames);
	        // Now do something with hdr and msg...
	        TXKSParser.errorMessage = "^Lexical Error in query at " + hdr + ": " + msg + "\n";
	    }


	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public TXKSLexer() {} 
	public TXKSLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public TXKSLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "TXKS.g"; }

	// $ANTLR start "T__47"
	public final void mT__47() throws RecognitionException {
		try {
			int _type = T__47;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:22:7: ( '-' )
			// TXKS.g:22:9: '-'
			{
			match('-'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "T__47"

	// $ANTLR start "K_NONTEMPORAL"
	public final void mK_NONTEMPORAL() throws RecognitionException {
		try {
			int _type = K_NONTEMPORAL;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:257:16: ( '@nontemporal' )
			// TXKS.g:257:18: '@nontemporal'
			{
			match("@nontemporal"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_NONTEMPORAL"

	// $ANTLR start "K_EARLIEST"
	public final void mK_EARLIEST() throws RecognitionException {
		try {
			int _type = K_EARLIEST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:258:13: ( '@earliest' )
			// TXKS.g:258:15: '@earliest'
			{
			match("@earliest"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_EARLIEST"

	// $ANTLR start "K_CURRENT"
	public final void mK_CURRENT() throws RecognitionException {
		try {
			int _type = K_CURRENT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:259:12: ( '@current' )
			// TXKS.g:259:14: '@current'
			{
			match("@current"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_CURRENT"

	// $ANTLR start "K_LATEST"
	public final void mK_LATEST() throws RecognitionException {
		try {
			int _type = K_LATEST;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:260:11: ( '@latest' )
			// TXKS.g:260:13: '@latest'
			{
			match("@latest"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_LATEST"

	// $ANTLR start "K_MEETS"
	public final void mK_MEETS() throws RecognitionException {
		try {
			int _type = K_MEETS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:261:11: ( '@meets' )
			// TXKS.g:261:14: '@meets'
			{
			match("@meets"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_MEETS"

	// $ANTLR start "K_DURATION"
	public final void mK_DURATION() throws RecognitionException {
		try {
			int _type = K_DURATION;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:262:13: ( '@duration' )
			// TXKS.g:262:15: '@duration'
			{
			match("@duration"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_DURATION"

	// $ANTLR start "K_SEQUENCED"
	public final void mK_SEQUENCED() throws RecognitionException {
		try {
			int _type = K_SEQUENCED;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:263:14: ( '@sequenced' )
			// TXKS.g:263:16: '@sequenced'
			{
			match("@sequenced"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_SEQUENCED"

	// $ANTLR start "K_NONSEQUENCED"
	public final void mK_NONSEQUENCED() throws RecognitionException {
		try {
			int _type = K_NONSEQUENCED;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:264:17: ( '@nonsequenced' )
			// TXKS.g:264:19: '@nonsequenced'
			{
			match("@nonsequenced"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_NONSEQUENCED"

	// $ANTLR start "K_DESCENDANTVERSIONS"
	public final void mK_DESCENDANTVERSIONS() throws RecognitionException {
		try {
			int _type = K_DESCENDANTVERSIONS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:265:23: ( '@descendantVersions' )
			// TXKS.g:265:25: '@descendantVersions'
			{
			match("@descendantVersions"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_DESCENDANTVERSIONS"

	// $ANTLR start "K_DESCENDANTCHANGES"
	public final void mK_DESCENDANTCHANGES() throws RecognitionException {
		try {
			int _type = K_DESCENDANTCHANGES;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:266:22: ( '@descendantChanges' )
			// TXKS.g:266:24: '@descendantChanges'
			{
			match("@descendantChanges"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_DESCENDANTCHANGES"

	// $ANTLR start "K_SLICE"
	public final void mK_SLICE() throws RecognitionException {
		try {
			int _type = K_SLICE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:267:11: ( '@slice' )
			// TXKS.g:267:14: '@slice'
			{
			match("@slice"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_SLICE"

	// $ANTLR start "K_CONTAINS"
	public final void mK_CONTAINS() throws RecognitionException {
		try {
			int _type = K_CONTAINS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:268:14: ( '@contains' )
			// TXKS.g:268:16: '@contains'
			{
			match("@contains"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_CONTAINS"

	// $ANTLR start "K_DURING"
	public final void mK_DURING() throws RecognitionException {
		try {
			int _type = K_DURING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:269:12: ( '@during' )
			// TXKS.g:269:14: '@during'
			{
			match("@during"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_DURING"

	// $ANTLR start "K_INTERSECTS"
	public final void mK_INTERSECTS() throws RecognitionException {
		try {
			int _type = K_INTERSECTS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:270:16: ( '@intersects' )
			// TXKS.g:270:18: '@intersects'
			{
			match("@intersects"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_INTERSECTS"

	// $ANTLR start "K_BEFORE"
	public final void mK_BEFORE() throws RecognitionException {
		try {
			int _type = K_BEFORE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:271:11: ( '@before' )
			// TXKS.g:271:13: '@before'
			{
			match("@before"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_BEFORE"

	// $ANTLR start "K_AFTER"
	public final void mK_AFTER() throws RecognitionException {
		try {
			int _type = K_AFTER;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:272:11: ( '@after' )
			// TXKS.g:272:13: '@after'
			{
			match("@after"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_AFTER"

	// $ANTLR start "K_ANY"
	public final void mK_ANY() throws RecognitionException {
		try {
			int _type = K_ANY;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:273:9: ( '@any' )
			// TXKS.g:273:11: '@any'
			{
			match("@any"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "K_ANY"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:275:4: ( ( LETTER | '_' | ':' ) ( NAMECHAR )* )
			// TXKS.g:275:8: ( LETTER | '_' | ':' ) ( NAMECHAR )*
			{
			if ( input.LA(1)==':'||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// TXKS.g:275:30: ( NAMECHAR )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( ((LA1_0 >= '-' && LA1_0 <= '.')||(LA1_0 >= '0' && LA1_0 <= ':')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// TXKS.g:
					{
					if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					break loop1;
				}
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ID"

	// $ANTLR start "NAMECHAR"
	public final void mNAMECHAR() throws RecognitionException {
		try {
			// TXKS.g:278:5: ( LETTER | DIGIT | '.' | '-' | '_' | ':' )
			// TXKS.g:
			{
			if ( (input.LA(1) >= '-' && input.LA(1) <= '.')||(input.LA(1) >= '0' && input.LA(1) <= ':')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NAMECHAR"

	// $ANTLR start "DIGIT"
	public final void mDIGIT() throws RecognitionException {
		try {
			// TXKS.g:282:5: ( '0' .. '9' )
			// TXKS.g:
			{
			if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DIGIT"

	// $ANTLR start "LETTER"
	public final void mLETTER() throws RecognitionException {
		try {
			// TXKS.g:286:5: ( 'a' .. 'z' | 'A' .. 'Z' )
			// TXKS.g:
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			}

		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LETTER"

	// $ANTLR start "WILDCARD"
	public final void mWILDCARD() throws RecognitionException {
		try {
			int _type = WILDCARD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:290:9: ( '*' )
			// TXKS.g:290:11: '*'
			{
			match('*'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WILDCARD"

	// $ANTLR start "DOUBLEWILDCARD"
	public final void mDOUBLEWILDCARD() throws RecognitionException {
		try {
			int _type = DOUBLEWILDCARD;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:291:16: ( '**' )
			// TXKS.g:291:18: '**'
			{
			match("**"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "DOUBLEWILDCARD"

	// $ANTLR start "NOT"
	public final void mNOT() throws RecognitionException {
		try {
			int _type = NOT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:292:6: ( '!' )
			// TXKS.g:292:8: '!'
			{
			match('!'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "NOT"

	// $ANTLR start "INT"
	public final void mINT() throws RecognitionException {
		try {
			int _type = INT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:293:5: ( ( '0' .. '9' )+ )
			// TXKS.g:293:7: ( '0' .. '9' )+
			{
			// TXKS.g:293:7: ( '0' .. '9' )+
			int cnt2=0;
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// TXKS.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9') ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt2 >= 1 ) break loop2;
					EarlyExitException eee = new EarlyExitException(2, input);
					throw eee;
				}
				cnt2++;
			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "INT"

	// $ANTLR start "LBRACKET"
	public final void mLBRACKET() throws RecognitionException {
		try {
			int _type = LBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:294:9: ( '[' )
			// TXKS.g:294:11: '['
			{
			match('['); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LBRACKET"

	// $ANTLR start "RBRACKET"
	public final void mRBRACKET() throws RecognitionException {
		try {
			int _type = RBRACKET;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:295:9: ( ']' )
			// TXKS.g:295:11: ']'
			{
			match(']'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACKET"

	// $ANTLR start "RBRACE"
	public final void mRBRACE() throws RecognitionException {
		try {
			int _type = RBRACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:296:9: ( '}' )
			// TXKS.g:296:11: '}'
			{
			match('}'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RBRACE"

	// $ANTLR start "LBRACE"
	public final void mLBRACE() throws RecognitionException {
		try {
			int _type = LBRACE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:297:9: ( '{' )
			// TXKS.g:297:11: '{'
			{
			match('{'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LBRACE"

	// $ANTLR start "RPAREN"
	public final void mRPAREN() throws RecognitionException {
		try {
			int _type = RPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:298:8: ( ')' )
			// TXKS.g:298:10: ')'
			{
			match(')'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "RPAREN"

	// $ANTLR start "LPAREN"
	public final void mLPAREN() throws RecognitionException {
		try {
			int _type = LPAREN;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:299:8: ( '(' )
			// TXKS.g:299:10: '('
			{
			match('('); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LPAREN"

	// $ANTLR start "COMMA"
	public final void mCOMMA() throws RecognitionException {
		try {
			int _type = COMMA;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:300:9: ( ',' )
			// TXKS.g:300:12: ','
			{
			match(','); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "COMMA"

	// $ANTLR start "AT"
	public final void mAT() throws RecognitionException {
		try {
			int _type = AT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:301:4: ( '@' )
			// TXKS.g:301:6: '@'
			{
			match('@'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "AT"

	// $ANTLR start "EQUALS"
	public final void mEQUALS() throws RecognitionException {
		try {
			int _type = EQUALS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:305:8: ( '=' ( '=' )? )
			// TXKS.g:305:10: '=' ( '=' )?
			{
			match('='); 
			// TXKS.g:305:13: ( '=' )?
			int alt3=2;
			int LA3_0 = input.LA(1);
			if ( (LA3_0=='=') ) {
				alt3=1;
			}
			switch (alt3) {
				case 1 :
					// TXKS.g:305:13: '='
					{
					match('='); 
					}
					break;

			}

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EQUALS"

	// $ANTLR start "LE"
	public final void mLE() throws RecognitionException {
		try {
			int _type = LE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:306:5: ( '<=' )
			// TXKS.g:306:7: '<='
			{
			match("<="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LE"

	// $ANTLR start "GE"
	public final void mGE() throws RecognitionException {
		try {
			int _type = GE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:307:5: ( '>=' )
			// TXKS.g:307:7: '>='
			{
			match(">="); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GE"

	// $ANTLR start "GT"
	public final void mGT() throws RecognitionException {
		try {
			int _type = GT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:308:4: ( '>' )
			// TXKS.g:308:7: '>'
			{
			match('>'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "GT"

	// $ANTLR start "LT"
	public final void mLT() throws RecognitionException {
		try {
			int _type = LT;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:309:4: ( '<' )
			// TXKS.g:309:7: '<'
			{
			match('<'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "LT"

	// $ANTLR start "STRING"
	public final void mSTRING() throws RecognitionException {
		try {
			int _type = STRING;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:312:9: ( ( '\"' (~ '\"' )* '\"' ) | ( '\\'' (~ '\\'' )* '\\'' ) )
			int alt6=2;
			int LA6_0 = input.LA(1);
			if ( (LA6_0=='\"') ) {
				alt6=1;
			}
			else if ( (LA6_0=='\'') ) {
				alt6=2;
			}

			else {
				NoViableAltException nvae =
					new NoViableAltException("", 6, 0, input);
				throw nvae;
			}

			switch (alt6) {
				case 1 :
					// TXKS.g:312:12: ( '\"' (~ '\"' )* '\"' )
					{
					// TXKS.g:312:12: ( '\"' (~ '\"' )* '\"' )
					// TXKS.g:312:13: '\"' (~ '\"' )* '\"'
					{
					match('\"'); 
					// TXKS.g:312:17: (~ '\"' )*
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( ((LA4_0 >= '\u0000' && LA4_0 <= '!')||(LA4_0 >= '#' && LA4_0 <= '\uFFFF')) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// TXKS.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '!')||(input.LA(1) >= '#' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop4;
						}
					}

					match('\"'); 
					}

					}
					break;
				case 2 :
					// TXKS.g:312:32: ( '\\'' (~ '\\'' )* '\\'' )
					{
					// TXKS.g:312:32: ( '\\'' (~ '\\'' )* '\\'' )
					// TXKS.g:312:33: '\\'' (~ '\\'' )* '\\''
					{
					match('\''); 
					// TXKS.g:312:38: (~ '\\'' )*
					loop5:
					while (true) {
						int alt5=2;
						int LA5_0 = input.LA(1);
						if ( ((LA5_0 >= '\u0000' && LA5_0 <= '&')||(LA5_0 >= '(' && LA5_0 <= '\uFFFF')) ) {
							alt5=1;
						}

						switch (alt5) {
						case 1 :
							// TXKS.g:
							{
							if ( (input.LA(1) >= '\u0000' && input.LA(1) <= '&')||(input.LA(1) >= '(' && input.LA(1) <= '\uFFFF') ) {
								input.consume();
							}
							else {
								MismatchedSetException mse = new MismatchedSetException(null,input);
								recover(mse);
								throw mse;
							}
							}
							break;

						default :
							break loop5;
						}
					}

					match('\''); 
					}

					}
					break;

			}
			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "STRING"

	// $ANTLR start "EOS"
	public final void mEOS() throws RecognitionException {
		try {
			int _type = EOS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:313:5: ( ';' )
			// TXKS.g:313:7: ';'
			{
			match(';'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "EOS"

	// $ANTLR start "ARROW"
	public final void mARROW() throws RecognitionException {
		try {
			int _type = ARROW;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:314:7: ( '->' )
			// TXKS.g:314:15: '->'
			{
			match("->"); 

			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "ARROW"

	// $ANTLR start "PIPE"
	public final void mPIPE() throws RecognitionException {
		try {
			int _type = PIPE;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:315:7: ( '|' )
			// TXKS.g:315:9: '|'
			{
			match('|'); 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "PIPE"

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// TXKS.g:316:10: ( ( ' ' | '\\r' | '\\n' )+ )
			// TXKS.g:316:18: ( ' ' | '\\r' | '\\n' )+
			{
			// TXKS.g:316:18: ( ' ' | '\\r' | '\\n' )+
			int cnt7=0;
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( (LA7_0=='\n'||LA7_0=='\r'||LA7_0==' ') ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// TXKS.g:
					{
					if ( input.LA(1)=='\n'||input.LA(1)=='\r'||input.LA(1)==' ' ) {
						input.consume();
					}
					else {
						MismatchedSetException mse = new MismatchedSetException(null,input);
						recover(mse);
						throw mse;
					}
					}
					break;

				default :
					if ( cnt7 >= 1 ) break loop7;
					EarlyExitException eee = new EarlyExitException(7, input);
					throw eee;
				}
				cnt7++;
			}

			_channel = HIDDEN;
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "WS"

	@Override
	public void mTokens() throws RecognitionException {
		// TXKS.g:1:8: ( T__47 | K_NONTEMPORAL | K_EARLIEST | K_CURRENT | K_LATEST | K_MEETS | K_DURATION | K_SEQUENCED | K_NONSEQUENCED | K_DESCENDANTVERSIONS | K_DESCENDANTCHANGES | K_SLICE | K_CONTAINS | K_DURING | K_INTERSECTS | K_BEFORE | K_AFTER | K_ANY | ID | WILDCARD | DOUBLEWILDCARD | NOT | INT | LBRACKET | RBRACKET | RBRACE | LBRACE | RPAREN | LPAREN | COMMA | AT | EQUALS | LE | GE | GT | LT | STRING | EOS | ARROW | PIPE | WS )
		int alt8=41;
		alt8 = dfa8.predict(input);
		switch (alt8) {
			case 1 :
				// TXKS.g:1:10: T__47
				{
				mT__47(); 

				}
				break;
			case 2 :
				// TXKS.g:1:16: K_NONTEMPORAL
				{
				mK_NONTEMPORAL(); 

				}
				break;
			case 3 :
				// TXKS.g:1:30: K_EARLIEST
				{
				mK_EARLIEST(); 

				}
				break;
			case 4 :
				// TXKS.g:1:41: K_CURRENT
				{
				mK_CURRENT(); 

				}
				break;
			case 5 :
				// TXKS.g:1:51: K_LATEST
				{
				mK_LATEST(); 

				}
				break;
			case 6 :
				// TXKS.g:1:60: K_MEETS
				{
				mK_MEETS(); 

				}
				break;
			case 7 :
				// TXKS.g:1:68: K_DURATION
				{
				mK_DURATION(); 

				}
				break;
			case 8 :
				// TXKS.g:1:79: K_SEQUENCED
				{
				mK_SEQUENCED(); 

				}
				break;
			case 9 :
				// TXKS.g:1:91: K_NONSEQUENCED
				{
				mK_NONSEQUENCED(); 

				}
				break;
			case 10 :
				// TXKS.g:1:106: K_DESCENDANTVERSIONS
				{
				mK_DESCENDANTVERSIONS(); 

				}
				break;
			case 11 :
				// TXKS.g:1:127: K_DESCENDANTCHANGES
				{
				mK_DESCENDANTCHANGES(); 

				}
				break;
			case 12 :
				// TXKS.g:1:147: K_SLICE
				{
				mK_SLICE(); 

				}
				break;
			case 13 :
				// TXKS.g:1:155: K_CONTAINS
				{
				mK_CONTAINS(); 

				}
				break;
			case 14 :
				// TXKS.g:1:166: K_DURING
				{
				mK_DURING(); 

				}
				break;
			case 15 :
				// TXKS.g:1:175: K_INTERSECTS
				{
				mK_INTERSECTS(); 

				}
				break;
			case 16 :
				// TXKS.g:1:188: K_BEFORE
				{
				mK_BEFORE(); 

				}
				break;
			case 17 :
				// TXKS.g:1:197: K_AFTER
				{
				mK_AFTER(); 

				}
				break;
			case 18 :
				// TXKS.g:1:205: K_ANY
				{
				mK_ANY(); 

				}
				break;
			case 19 :
				// TXKS.g:1:211: ID
				{
				mID(); 

				}
				break;
			case 20 :
				// TXKS.g:1:214: WILDCARD
				{
				mWILDCARD(); 

				}
				break;
			case 21 :
				// TXKS.g:1:223: DOUBLEWILDCARD
				{
				mDOUBLEWILDCARD(); 

				}
				break;
			case 22 :
				// TXKS.g:1:238: NOT
				{
				mNOT(); 

				}
				break;
			case 23 :
				// TXKS.g:1:242: INT
				{
				mINT(); 

				}
				break;
			case 24 :
				// TXKS.g:1:246: LBRACKET
				{
				mLBRACKET(); 

				}
				break;
			case 25 :
				// TXKS.g:1:255: RBRACKET
				{
				mRBRACKET(); 

				}
				break;
			case 26 :
				// TXKS.g:1:264: RBRACE
				{
				mRBRACE(); 

				}
				break;
			case 27 :
				// TXKS.g:1:271: LBRACE
				{
				mLBRACE(); 

				}
				break;
			case 28 :
				// TXKS.g:1:278: RPAREN
				{
				mRPAREN(); 

				}
				break;
			case 29 :
				// TXKS.g:1:285: LPAREN
				{
				mLPAREN(); 

				}
				break;
			case 30 :
				// TXKS.g:1:292: COMMA
				{
				mCOMMA(); 

				}
				break;
			case 31 :
				// TXKS.g:1:298: AT
				{
				mAT(); 

				}
				break;
			case 32 :
				// TXKS.g:1:301: EQUALS
				{
				mEQUALS(); 

				}
				break;
			case 33 :
				// TXKS.g:1:308: LE
				{
				mLE(); 

				}
				break;
			case 34 :
				// TXKS.g:1:311: GE
				{
				mGE(); 

				}
				break;
			case 35 :
				// TXKS.g:1:314: GT
				{
				mGT(); 

				}
				break;
			case 36 :
				// TXKS.g:1:317: LT
				{
				mLT(); 

				}
				break;
			case 37 :
				// TXKS.g:1:320: STRING
				{
				mSTRING(); 

				}
				break;
			case 38 :
				// TXKS.g:1:327: EOS
				{
				mEOS(); 

				}
				break;
			case 39 :
				// TXKS.g:1:331: ARROW
				{
				mARROW(); 

				}
				break;
			case 40 :
				// TXKS.g:1:337: PIPE
				{
				mPIPE(); 

				}
				break;
			case 41 :
				// TXKS.g:1:342: WS
				{
				mWS(); 

				}
				break;

		}
	}


	protected DFA8 dfa8 = new DFA8(this);
	static final String DFA8_eotS =
		"\1\uffff\1\26\1\41\1\uffff\1\43\12\uffff\1\45\1\47\60\uffff";
	static final String DFA8_eofS =
		"\101\uffff";
	static final String DFA8_minS =
		"\1\12\1\76\1\141\1\uffff\1\52\12\uffff\2\75\6\uffff\1\157\1\uffff\1\157"+
		"\2\uffff\2\145\2\uffff\1\146\7\uffff\1\156\2\uffff\1\162\1\163\4\uffff"+
		"\1\163\1\141\1\143\4\uffff\1\145\1\156\1\144\1\141\1\156\1\164\1\103\2"+
		"\uffff";
	static final String DFA8_maxS =
		"\1\175\1\76\1\163\1\uffff\1\52\12\uffff\2\75\6\uffff\1\157\1\uffff\1\165"+
		"\2\uffff\1\165\1\154\2\uffff\1\156\7\uffff\1\156\2\uffff\1\162\1\163\4"+
		"\uffff\1\164\1\151\1\143\4\uffff\1\145\1\156\1\144\1\141\1\156\1\164\1"+
		"\126\2\uffff";
	static final String DFA8_acceptS =
		"\3\uffff\1\23\1\uffff\1\26\1\27\1\30\1\31\1\32\1\33\1\34\1\35\1\36\1\40"+
		"\2\uffff\1\45\1\46\1\50\1\51\1\47\1\1\1\uffff\1\3\1\uffff\1\5\1\6\2\uffff"+
		"\1\17\1\20\1\uffff\1\37\1\25\1\24\1\41\1\44\1\42\1\43\1\uffff\1\4\1\15"+
		"\2\uffff\1\10\1\14\1\21\1\22\3\uffff\1\2\1\11\1\7\1\16\7\uffff\1\12\1"+
		"\13";
	static final String DFA8_specialS =
		"\101\uffff}>";
	static final String[] DFA8_transitionS = {
			"\1\24\2\uffff\1\24\22\uffff\1\24\1\5\1\21\4\uffff\1\21\1\14\1\13\1\4"+
			"\1\uffff\1\15\1\1\2\uffff\12\6\1\3\1\22\1\17\1\16\1\20\1\uffff\1\2\32"+
			"\3\1\7\1\uffff\1\10\1\uffff\1\3\1\uffff\32\3\1\12\1\23\1\11",
			"\1\25",
			"\1\40\1\37\1\31\1\34\1\30\3\uffff\1\36\2\uffff\1\32\1\33\1\27\4\uffff"+
			"\1\35",
			"",
			"\1\42",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\44",
			"\1\46",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\50",
			"",
			"\1\52\5\uffff\1\51",
			"",
			"",
			"\1\54\17\uffff\1\53",
			"\1\55\6\uffff\1\56",
			"",
			"",
			"\1\57\7\uffff\1\60",
			"",
			"",
			"",
			"",
			"",
			"",
			"",
			"\1\61",
			"",
			"",
			"\1\62",
			"\1\63",
			"",
			"",
			"",
			"",
			"\1\65\1\64",
			"\1\66\7\uffff\1\67",
			"\1\70",
			"",
			"",
			"",
			"",
			"\1\71",
			"\1\72",
			"\1\73",
			"\1\74",
			"\1\75",
			"\1\76",
			"\1\100\22\uffff\1\77",
			"",
			""
	};

	static final short[] DFA8_eot = DFA.unpackEncodedString(DFA8_eotS);
	static final short[] DFA8_eof = DFA.unpackEncodedString(DFA8_eofS);
	static final char[] DFA8_min = DFA.unpackEncodedStringToUnsignedChars(DFA8_minS);
	static final char[] DFA8_max = DFA.unpackEncodedStringToUnsignedChars(DFA8_maxS);
	static final short[] DFA8_accept = DFA.unpackEncodedString(DFA8_acceptS);
	static final short[] DFA8_special = DFA.unpackEncodedString(DFA8_specialS);
	static final short[][] DFA8_transition;

	static {
		int numStates = DFA8_transitionS.length;
		DFA8_transition = new short[numStates][];
		for (int i=0; i<numStates; i++) {
			DFA8_transition[i] = DFA.unpackEncodedString(DFA8_transitionS[i]);
		}
	}

	protected class DFA8 extends DFA {

		public DFA8(BaseRecognizer recognizer) {
			this.recognizer = recognizer;
			this.decisionNumber = 8;
			this.eot = DFA8_eot;
			this.eof = DFA8_eof;
			this.min = DFA8_min;
			this.max = DFA8_max;
			this.accept = DFA8_accept;
			this.special = DFA8_special;
			this.transition = DFA8_transition;
		}
		@Override
		public String getDescription() {
			return "1:1: Tokens : ( T__47 | K_NONTEMPORAL | K_EARLIEST | K_CURRENT | K_LATEST | K_MEETS | K_DURATION | K_SEQUENCED | K_NONSEQUENCED | K_DESCENDANTVERSIONS | K_DESCENDANTCHANGES | K_SLICE | K_CONTAINS | K_DURING | K_INTERSECTS | K_BEFORE | K_AFTER | K_ANY | ID | WILDCARD | DOUBLEWILDCARD | NOT | INT | LBRACKET | RBRACKET | RBRACE | LBRACE | RPAREN | LPAREN | COMMA | AT | EQUALS | LE | GE | GT | LT | STRING | EOS | ARROW | PIPE | WS );";
		}
	}

}
