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
public class TXKSLexer extends Lexer {
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
    public String getGrammarFileName() { return "D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g"; }

    // $ANTLR start "K_NONTEMPORAL"
    public final void mK_NONTEMPORAL() throws RecognitionException {
        try {
            int _type = K_NONTEMPORAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:273:15: ( '@nontemporal' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:273:17: '@nontemporal'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:274:13: ( '@earliest' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:274:15: '@earliest'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:275:12: ( '@current' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:275:14: '@current'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:276:11: ( '@latest' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:276:13: '@latest'
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

    // $ANTLR start "K_DURATION"
    public final void mK_DURATION() throws RecognitionException {
        try {
            int _type = K_DURATION;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:277:12: ( '@duration' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:277:14: '@duration'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:278:13: ( '@sequenced' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:278:15: '@sequenced'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:279:16: ( '@nonsequenced' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:279:18: '@nonsequenced'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:280:22: ( '@descendantVersions' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:280:24: '@descendantVersions'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:281:22: ( '@descendantChanges' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:281:24: '@descendantChanges'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:282:10: ( '@slice' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:282:13: '@slice'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:283:13: ( '@contains' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:283:15: '@contains'
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

    // $ANTLR start "K_INTERSECTS"
    public final void mK_INTERSECTS() throws RecognitionException {
        try {
            int _type = K_INTERSECTS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:284:15: ( '@intersects' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:284:17: '@intersects'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:285:11: ( '@before' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:285:13: '@before'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:286:10: ( '@after' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:286:12: '@after'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:287:8: ( '@any' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:287:10: '@any'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:289:4: ( ( LETTER | '_' | ':' ) ( NAMECHAR )* )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:289:8: ( LETTER | '_' | ':' ) ( NAMECHAR )*
            {
            if ( input.LA(1)==':'||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:289:30: ( NAMECHAR )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '-' && LA1_0 <= '.')||(LA1_0 >= '0' && LA1_0 <= ':')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            } while (true);


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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:292:5: ( LETTER | DIGIT | '.' | '-' | '_' | ':' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:296:5: ( '0' .. '9' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:300:5: ( 'a' .. 'z' | 'A' .. 'Z' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:304:11: ( '*' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:304:13: '*'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:305:16: ( '**' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:305:18: '**'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:306:6: ( '!' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:306:8: '!'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:307:5: ( ( '0' .. '9' )+ )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:307:7: ( '0' .. '9' )+
            {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:307:7: ( '0' .. '9' )+
            int cnt2=0;
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0 >= '0' && LA2_0 <= '9')) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
                        EarlyExitException eee =
                            new EarlyExitException(2, input);
                        throw eee;
                }
                cnt2++;
            } while (true);


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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:308:10: ( '[' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:308:12: '['
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:309:10: ( ']' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:309:12: ']'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:310:9: ( '}' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:310:11: '}'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:311:9: ( '{' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:311:11: '{'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:312:8: ( ')' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:312:10: ')'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:313:8: ( '(' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:313:10: '('
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:314:10: ( ',' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:314:13: ','
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

    // $ANTLR start "EQUALS"
    public final void mEQUALS() throws RecognitionException {
        try {
            int _type = EQUALS;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:318:8: ( '=' ( '=' )? )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:318:10: '=' ( '=' )?
            {
            match('='); 

            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:318:13: ( '=' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='=') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:318:13: '='
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:319:5: ( '<=' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:319:7: '<='
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:320:5: ( '>=' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:320:7: '>='
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:321:4: ( '>' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:321:7: '>'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:322:4: ( '<' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:322:7: '<'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:9: ( ( '\"' (~ '\"' )* '\"' ) | ( '\\'' (~ '\\'' )* '\\'' ) )
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
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:12: ( '\"' (~ '\"' )* '\"' )
                    {
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:12: ( '\"' (~ '\"' )* '\"' )
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:13: '\"' (~ '\"' )* '\"'
                    {
                    match('\"'); 

                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:17: (~ '\"' )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0 >= '\u0000' && LA4_0 <= '!')||(LA4_0 >= '#' && LA4_0 <= '\uFFFF')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
                    } while (true);


                    match('\"'); 

                    }


                    }
                    break;
                case 2 :
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:32: ( '\\'' (~ '\\'' )* '\\'' )
                    {
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:32: ( '\\'' (~ '\\'' )* '\\'' )
                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:33: '\\'' (~ '\\'' )* '\\''
                    {
                    match('\''); 

                    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:325:38: (~ '\\'' )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0 >= '\u0000' && LA5_0 <= '&')||(LA5_0 >= '(' && LA5_0 <= '\uFFFF')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
                    } while (true);


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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:5: ( ';' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:7: ';'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:327:7: ( '->' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:327:15: '->'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:328:7: ( '|' )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:328:9: '|'
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
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:329:10: ( ( ' ' | '\\r' | '\\n' )+ )
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:329:18: ( ' ' | '\\r' | '\\n' )+
            {
            // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:329:18: ( ' ' | '\\r' | '\\n' )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( (LA7_0=='\n'||LA7_0=='\r'||LA7_0==' ') ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


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

    public void mTokens() throws RecognitionException {
        // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:8: ( K_NONTEMPORAL | K_EARLIEST | K_CURRENT | K_LATEST | K_DURATION | K_SEQUENCED | K_NONSEQUENCED | K_DESCENDANTVERSIONS | K_DESCENDANTCHANGES | K_SLICE | K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_ANY | ID | WILDCARD | DOUBLEWILDCARD | NOT | INT | LBRACKET | RBRACKET | RBRACE | LBRACE | RPAREN | LPAREN | COMMA | EQUALS | LE | GE | GT | LT | STRING | EOS | ARROW | PIPE | WS )
        int alt8=37;
        switch ( input.LA(1) ) {
        case '@':
            {
            switch ( input.LA(2) ) {
            case 'n':
                {
                int LA8_21 = input.LA(3);

                if ( (LA8_21=='o') ) {
                    int LA8_36 = input.LA(4);

                    if ( (LA8_36=='n') ) {
                        int LA8_45 = input.LA(5);

                        if ( (LA8_45=='t') ) {
                            alt8=1;
                        }
                        else if ( (LA8_45=='s') ) {
                            alt8=7;
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 8, 45, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 36, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 21, input);

                    throw nvae;

                }
                }
                break;
            case 'e':
                {
                alt8=2;
                }
                break;
            case 'c':
                {
                int LA8_23 = input.LA(3);

                if ( (LA8_23=='u') ) {
                    alt8=3;
                }
                else if ( (LA8_23=='o') ) {
                    alt8=11;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 23, input);

                    throw nvae;

                }
                }
                break;
            case 'l':
                {
                alt8=4;
                }
                break;
            case 'd':
                {
                int LA8_25 = input.LA(3);

                if ( (LA8_25=='u') ) {
                    alt8=5;
                }
                else if ( (LA8_25=='e') ) {
                    int LA8_40 = input.LA(4);

                    if ( (LA8_40=='s') ) {
                        int LA8_46 = input.LA(5);

                        if ( (LA8_46=='c') ) {
                            int LA8_49 = input.LA(6);

                            if ( (LA8_49=='e') ) {
                                int LA8_50 = input.LA(7);

                                if ( (LA8_50=='n') ) {
                                    int LA8_51 = input.LA(8);

                                    if ( (LA8_51=='d') ) {
                                        int LA8_52 = input.LA(9);

                                        if ( (LA8_52=='a') ) {
                                            int LA8_53 = input.LA(10);

                                            if ( (LA8_53=='n') ) {
                                                int LA8_54 = input.LA(11);

                                                if ( (LA8_54=='t') ) {
                                                    int LA8_55 = input.LA(12);

                                                    if ( (LA8_55=='V') ) {
                                                        alt8=8;
                                                    }
                                                    else if ( (LA8_55=='C') ) {
                                                        alt8=9;
                                                    }
                                                    else {
                                                        NoViableAltException nvae =
                                                            new NoViableAltException("", 8, 55, input);

                                                        throw nvae;

                                                    }
                                                }
                                                else {
                                                    NoViableAltException nvae =
                                                        new NoViableAltException("", 8, 54, input);

                                                    throw nvae;

                                                }
                                            }
                                            else {
                                                NoViableAltException nvae =
                                                    new NoViableAltException("", 8, 53, input);

                                                throw nvae;

                                            }
                                        }
                                        else {
                                            NoViableAltException nvae =
                                                new NoViableAltException("", 8, 52, input);

                                            throw nvae;

                                        }
                                    }
                                    else {
                                        NoViableAltException nvae =
                                            new NoViableAltException("", 8, 51, input);

                                        throw nvae;

                                    }
                                }
                                else {
                                    NoViableAltException nvae =
                                        new NoViableAltException("", 8, 50, input);

                                    throw nvae;

                                }
                            }
                            else {
                                NoViableAltException nvae =
                                    new NoViableAltException("", 8, 49, input);

                                throw nvae;

                            }
                        }
                        else {
                            NoViableAltException nvae =
                                new NoViableAltException("", 8, 46, input);

                            throw nvae;

                        }
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 8, 40, input);

                        throw nvae;

                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 25, input);

                    throw nvae;

                }
                }
                break;
            case 's':
                {
                int LA8_26 = input.LA(3);

                if ( (LA8_26=='e') ) {
                    alt8=6;
                }
                else if ( (LA8_26=='l') ) {
                    alt8=10;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 26, input);

                    throw nvae;

                }
                }
                break;
            case 'i':
                {
                alt8=12;
                }
                break;
            case 'b':
                {
                alt8=13;
                }
                break;
            case 'a':
                {
                int LA8_29 = input.LA(3);

                if ( (LA8_29=='f') ) {
                    alt8=14;
                }
                else if ( (LA8_29=='n') ) {
                    alt8=15;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 8, 29, input);

                    throw nvae;

                }
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 8, 1, input);

                throw nvae;

            }

            }
            break;
        case ':':
        case 'A':
        case 'B':
        case 'C':
        case 'D':
        case 'E':
        case 'F':
        case 'G':
        case 'H':
        case 'I':
        case 'J':
        case 'K':
        case 'L':
        case 'M':
        case 'N':
        case 'O':
        case 'P':
        case 'Q':
        case 'R':
        case 'S':
        case 'T':
        case 'U':
        case 'V':
        case 'W':
        case 'X':
        case 'Y':
        case 'Z':
        case '_':
        case 'a':
        case 'b':
        case 'c':
        case 'd':
        case 'e':
        case 'f':
        case 'g':
        case 'h':
        case 'i':
        case 'j':
        case 'k':
        case 'l':
        case 'm':
        case 'n':
        case 'o':
        case 'p':
        case 'q':
        case 'r':
        case 's':
        case 't':
        case 'u':
        case 'v':
        case 'w':
        case 'x':
        case 'y':
        case 'z':
            {
            alt8=16;
            }
            break;
        case '*':
            {
            int LA8_3 = input.LA(2);

            if ( (LA8_3=='*') ) {
                alt8=18;
            }
            else {
                alt8=17;
            }
            }
            break;
        case '!':
            {
            alt8=19;
            }
            break;
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
            {
            alt8=20;
            }
            break;
        case '[':
            {
            alt8=21;
            }
            break;
        case ']':
            {
            alt8=22;
            }
            break;
        case '}':
            {
            alt8=23;
            }
            break;
        case '{':
            {
            alt8=24;
            }
            break;
        case ')':
            {
            alt8=25;
            }
            break;
        case '(':
            {
            alt8=26;
            }
            break;
        case ',':
            {
            alt8=27;
            }
            break;
        case '=':
            {
            alt8=28;
            }
            break;
        case '<':
            {
            int LA8_14 = input.LA(2);

            if ( (LA8_14=='=') ) {
                alt8=29;
            }
            else {
                alt8=32;
            }
            }
            break;
        case '>':
            {
            int LA8_15 = input.LA(2);

            if ( (LA8_15=='=') ) {
                alt8=30;
            }
            else {
                alt8=31;
            }
            }
            break;
        case '\"':
        case '\'':
            {
            alt8=33;
            }
            break;
        case ';':
            {
            alt8=34;
            }
            break;
        case '-':
            {
            alt8=35;
            }
            break;
        case '|':
            {
            alt8=36;
            }
            break;
        case '\n':
        case '\r':
        case ' ':
            {
            alt8=37;
            }
            break;
        default:
            NoViableAltException nvae =
                new NoViableAltException("", 8, 0, input);

            throw nvae;

        }

        switch (alt8) {
            case 1 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:10: K_NONTEMPORAL
                {
                mK_NONTEMPORAL(); 


                }
                break;
            case 2 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:24: K_EARLIEST
                {
                mK_EARLIEST(); 


                }
                break;
            case 3 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:35: K_CURRENT
                {
                mK_CURRENT(); 


                }
                break;
            case 4 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:45: K_LATEST
                {
                mK_LATEST(); 


                }
                break;
            case 5 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:54: K_DURATION
                {
                mK_DURATION(); 


                }
                break;
            case 6 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:65: K_SEQUENCED
                {
                mK_SEQUENCED(); 


                }
                break;
            case 7 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:77: K_NONSEQUENCED
                {
                mK_NONSEQUENCED(); 


                }
                break;
            case 8 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:92: K_DESCENDANTVERSIONS
                {
                mK_DESCENDANTVERSIONS(); 


                }
                break;
            case 9 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:113: K_DESCENDANTCHANGES
                {
                mK_DESCENDANTCHANGES(); 


                }
                break;
            case 10 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:133: K_SLICE
                {
                mK_SLICE(); 


                }
                break;
            case 11 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:141: K_CONTAINS
                {
                mK_CONTAINS(); 


                }
                break;
            case 12 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:152: K_INTERSECTS
                {
                mK_INTERSECTS(); 


                }
                break;
            case 13 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:165: K_BEFORE
                {
                mK_BEFORE(); 


                }
                break;
            case 14 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:174: K_AFTER
                {
                mK_AFTER(); 


                }
                break;
            case 15 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:182: K_ANY
                {
                mK_ANY(); 


                }
                break;
            case 16 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:188: ID
                {
                mID(); 


                }
                break;
            case 17 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:191: WILDCARD
                {
                mWILDCARD(); 


                }
                break;
            case 18 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:200: DOUBLEWILDCARD
                {
                mDOUBLEWILDCARD(); 


                }
                break;
            case 19 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:215: NOT
                {
                mNOT(); 


                }
                break;
            case 20 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:219: INT
                {
                mINT(); 


                }
                break;
            case 21 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:223: LBRACKET
                {
                mLBRACKET(); 


                }
                break;
            case 22 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:232: RBRACKET
                {
                mRBRACKET(); 


                }
                break;
            case 23 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:241: RBRACE
                {
                mRBRACE(); 


                }
                break;
            case 24 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:248: LBRACE
                {
                mLBRACE(); 


                }
                break;
            case 25 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:255: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 26 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:262: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 27 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:269: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 28 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:275: EQUALS
                {
                mEQUALS(); 


                }
                break;
            case 29 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:282: LE
                {
                mLE(); 


                }
                break;
            case 30 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:285: GE
                {
                mGE(); 


                }
                break;
            case 31 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:288: GT
                {
                mGT(); 


                }
                break;
            case 32 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:291: LT
                {
                mLT(); 


                }
                break;
            case 33 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:294: STRING
                {
                mSTRING(); 


                }
                break;
            case 34 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:301: EOS
                {
                mEOS(); 


                }
                break;
            case 35 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:305: ARROW
                {
                mARROW(); 


                }
                break;
            case 36 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:311: PIPE
                {
                mPIPE(); 


                }
                break;
            case 37 :
                // D:\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:316: WS
                {
                mWS(); 


                }
                break;

        }

    }


 

}