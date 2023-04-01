// $ANTLR 3.4 C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g 2017-10-26 13:16:07

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
    public static final int K_EARLIEST=23;
    public static final int K_INTERSECTS=24;
    public static final int K_LATEST=25;
    public static final int K_MEETS=26;
    public static final int K_NONSEQUENCED=27;
    public static final int K_NONTEMPORAL=28;
    public static final int K_SEQUENCED=29;
    public static final int K_SLICE=30;
    public static final int LBRACE=31;
    public static final int LBRACKET=32;
    public static final int LE=33;
    public static final int LETTER=34;
    public static final int LPAREN=35;
    public static final int LT=36;
    public static final int NAMECHAR=37;
    public static final int NOT=38;
    public static final int PIPE=39;
    public static final int RBRACE=40;
    public static final int RBRACKET=41;
    public static final int RPAREN=42;
    public static final int STRING=43;
    public static final int WILDCARD=44;
    public static final int WS=45;

        
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
    public String getGrammarFileName() { return "C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g"; }

    // $ANTLR start "K_NONTEMPORAL"
    public final void mK_NONTEMPORAL() throws RecognitionException {
        try {
            int _type = K_NONTEMPORAL;
            int _channel = DEFAULT_TOKEN_CHANNEL;
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:272:16: ( '@nontemporal' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:272:18: '@nontemporal'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:273:13: ( '@earliest' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:273:15: '@earliest'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:274:12: ( '@current' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:274:14: '@current'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:275:11: ( '@latest' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:275:13: '@latest'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:276:11: ( '@meets' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:276:14: '@meets'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:277:13: ( '@duration' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:277:15: '@duration'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:278:14: ( '@sequenced' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:278:16: '@sequenced'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:279:17: ( '@nonsequenced' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:279:19: '@nonsequenced'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:280:23: ( '@descendantVersions' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:280:25: '@descendantVersions'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:281:22: ( '@descendantChanges' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:281:24: '@descendantChanges'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:282:11: ( '@slice' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:282:14: '@slice'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:283:14: ( '@contains' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:283:16: '@contains'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:284:16: ( '@intersects' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:284:18: '@intersects'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:285:11: ( '@before' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:285:13: '@before'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:286:11: ( '@after' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:286:13: '@after'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:287:9: ( '@any' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:287:11: '@any'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:289:4: ( ( LETTER | '_' | ':' ) ( NAMECHAR )* )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:289:8: ( LETTER | '_' | ':' ) ( NAMECHAR )*
            {
            if ( input.LA(1)==':'||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
                input.consume();
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                recover(mse);
                throw mse;
            }


            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:289:30: ( NAMECHAR )*
            loop1:
            do {
                int alt1=2;
                int LA1_0 = input.LA(1);

                if ( ((LA1_0 >= '-' && LA1_0 <= '.')||(LA1_0 >= '0' && LA1_0 <= ':')||(LA1_0 >= 'A' && LA1_0 <= 'Z')||LA1_0=='_'||(LA1_0 >= 'a' && LA1_0 <= 'z')) ) {
                    alt1=1;
                }


                switch (alt1) {
            	case 1 :
            	    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:292:5: ( LETTER | DIGIT | '.' | '-' | '_' | ':' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:296:5: ( '0' .. '9' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:300:5: ( 'a' .. 'z' | 'A' .. 'Z' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:304:9: ( '*' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:304:11: '*'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:305:16: ( '**' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:305:18: '**'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:306:6: ( '!' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:306:8: '!'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:307:5: ( ( '0' .. '9' )+ )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:307:7: ( '0' .. '9' )+
            {
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:307:7: ( '0' .. '9' )+
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
            	    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:308:9: ( '[' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:308:11: '['
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:309:9: ( ']' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:309:11: ']'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:310:9: ( '}' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:310:11: '}'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:311:9: ( '{' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:311:11: '{'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:312:8: ( ')' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:312:10: ')'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:313:8: ( '(' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:313:10: '('
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:314:9: ( ',' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:314:12: ','
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:315:4: ( '@' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:315:6: '@'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:319:8: ( '=' ( '=' )? )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:319:10: '=' ( '=' )?
            {
            match('='); 

            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:319:13: ( '=' )?
            int alt3=2;
            int LA3_0 = input.LA(1);

            if ( (LA3_0=='=') ) {
                alt3=1;
            }
            switch (alt3) {
                case 1 :
                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:319:13: '='
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:320:5: ( '<=' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:320:7: '<='
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:321:5: ( '>=' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:321:7: '>='
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:322:4: ( '>' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:322:7: '>'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:323:4: ( '<' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:323:7: '<'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:9: ( ( '\"' (~ '\"' )* '\"' ) | ( '\\'' (~ '\\'' )* '\\'' ) )
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
                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:12: ( '\"' (~ '\"' )* '\"' )
                    {
                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:12: ( '\"' (~ '\"' )* '\"' )
                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:13: '\"' (~ '\"' )* '\"'
                    {
                    match('\"'); 

                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:17: (~ '\"' )*
                    loop4:
                    do {
                        int alt4=2;
                        int LA4_0 = input.LA(1);

                        if ( ((LA4_0 >= '\u0000' && LA4_0 <= '!')||(LA4_0 >= '#' && LA4_0 <= '\uFFFF')) ) {
                            alt4=1;
                        }


                        switch (alt4) {
                    	case 1 :
                    	    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:32: ( '\\'' (~ '\\'' )* '\\'' )
                    {
                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:32: ( '\\'' (~ '\\'' )* '\\'' )
                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:33: '\\'' (~ '\\'' )* '\\''
                    {
                    match('\''); 

                    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:326:38: (~ '\\'' )*
                    loop5:
                    do {
                        int alt5=2;
                        int LA5_0 = input.LA(1);

                        if ( ((LA5_0 >= '\u0000' && LA5_0 <= '&')||(LA5_0 >= '(' && LA5_0 <= '\uFFFF')) ) {
                            alt5=1;
                        }


                        switch (alt5) {
                    	case 1 :
                    	    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:327:5: ( ';' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:327:7: ';'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:328:7: ( '->' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:328:15: '->'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:329:7: ( '|' )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:329:9: '|'
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
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:330:10: ( ( ' ' | '\\r' | '\\n' )+ )
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:330:18: ( ' ' | '\\r' | '\\n' )+
            {
            // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:330:18: ( ' ' | '\\r' | '\\n' )+
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
            	    // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:
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
        // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:8: ( K_NONTEMPORAL | K_EARLIEST | K_CURRENT | K_LATEST | K_MEETS | K_DURATION | K_SEQUENCED | K_NONSEQUENCED | K_DESCENDANTVERSIONS | K_DESCENDANTCHANGES | K_SLICE | K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_ANY | ID | WILDCARD | DOUBLEWILDCARD | NOT | INT | LBRACKET | RBRACKET | RBRACE | LBRACE | RPAREN | LPAREN | COMMA | AT | EQUALS | LE | GE | GT | LT | STRING | EOS | ARROW | PIPE | WS )
        int alt8=39;
        alt8 = dfa8.predict(input);
        switch (alt8) {
            case 1 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:10: K_NONTEMPORAL
                {
                mK_NONTEMPORAL(); 


                }
                break;
            case 2 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:24: K_EARLIEST
                {
                mK_EARLIEST(); 


                }
                break;
            case 3 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:35: K_CURRENT
                {
                mK_CURRENT(); 


                }
                break;
            case 4 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:45: K_LATEST
                {
                mK_LATEST(); 


                }
                break;
            case 5 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:54: K_MEETS
                {
                mK_MEETS(); 


                }
                break;
            case 6 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:62: K_DURATION
                {
                mK_DURATION(); 


                }
                break;
            case 7 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:73: K_SEQUENCED
                {
                mK_SEQUENCED(); 


                }
                break;
            case 8 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:85: K_NONSEQUENCED
                {
                mK_NONSEQUENCED(); 


                }
                break;
            case 9 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:100: K_DESCENDANTVERSIONS
                {
                mK_DESCENDANTVERSIONS(); 


                }
                break;
            case 10 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:121: K_DESCENDANTCHANGES
                {
                mK_DESCENDANTCHANGES(); 


                }
                break;
            case 11 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:141: K_SLICE
                {
                mK_SLICE(); 


                }
                break;
            case 12 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:149: K_CONTAINS
                {
                mK_CONTAINS(); 


                }
                break;
            case 13 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:160: K_INTERSECTS
                {
                mK_INTERSECTS(); 


                }
                break;
            case 14 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:173: K_BEFORE
                {
                mK_BEFORE(); 


                }
                break;
            case 15 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:182: K_AFTER
                {
                mK_AFTER(); 


                }
                break;
            case 16 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:190: K_ANY
                {
                mK_ANY(); 


                }
                break;
            case 17 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:196: ID
                {
                mID(); 


                }
                break;
            case 18 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:199: WILDCARD
                {
                mWILDCARD(); 


                }
                break;
            case 19 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:208: DOUBLEWILDCARD
                {
                mDOUBLEWILDCARD(); 


                }
                break;
            case 20 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:223: NOT
                {
                mNOT(); 


                }
                break;
            case 21 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:227: INT
                {
                mINT(); 


                }
                break;
            case 22 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:231: LBRACKET
                {
                mLBRACKET(); 


                }
                break;
            case 23 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:240: RBRACKET
                {
                mRBRACKET(); 


                }
                break;
            case 24 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:249: RBRACE
                {
                mRBRACE(); 


                }
                break;
            case 25 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:256: LBRACE
                {
                mLBRACE(); 


                }
                break;
            case 26 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:263: RPAREN
                {
                mRPAREN(); 


                }
                break;
            case 27 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:270: LPAREN
                {
                mLPAREN(); 


                }
                break;
            case 28 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:277: COMMA
                {
                mCOMMA(); 


                }
                break;
            case 29 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:283: AT
                {
                mAT(); 


                }
                break;
            case 30 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:286: EQUALS
                {
                mEQUALS(); 


                }
                break;
            case 31 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:293: LE
                {
                mLE(); 


                }
                break;
            case 32 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:296: GE
                {
                mGE(); 


                }
                break;
            case 33 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:299: GT
                {
                mGT(); 


                }
                break;
            case 34 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:302: LT
                {
                mLT(); 


                }
                break;
            case 35 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:305: STRING
                {
                mSTRING(); 


                }
                break;
            case 36 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:312: EOS
                {
                mEOS(); 


                }
                break;
            case 37 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:316: ARROW
                {
                mARROW(); 


                }
                break;
            case 38 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:322: PIPE
                {
                mPIPE(); 


                }
                break;
            case 39 :
                // C:\\Users\\curt\\Dropbox\\netbeans\\keywordSearch\\txks\\src\\usu\\grammar\\TXKS.g:1:327: WS
                {
                mWS(); 


                }
                break;

        }

    }


    protected DFA8 dfa8 = new DFA8(this);
    static final String DFA8_eotS =
        "\1\uffff\1\37\1\uffff\1\41\12\uffff\1\43\1\45\54\uffff";
    static final String DFA8_eofS =
        "\74\uffff";
    static final String DFA8_minS =
        "\1\12\1\141\1\uffff\1\52\12\uffff\2\75\5\uffff\1\157\1\uffff\1\157"+
        "\2\uffff\2\145\2\uffff\1\146\7\uffff\1\156\3\uffff\1\163\4\uffff"+
        "\1\163\1\143\2\uffff\1\145\1\156\1\144\1\141\1\156\1\164\1\103\2"+
        "\uffff";
    static final String DFA8_maxS =
        "\1\175\1\163\1\uffff\1\52\12\uffff\2\75\5\uffff\1\157\1\uffff\1"+
        "\165\2\uffff\1\165\1\154\2\uffff\1\156\7\uffff\1\156\3\uffff\1\163"+
        "\4\uffff\1\164\1\143\2\uffff\1\145\1\156\1\144\1\141\1\156\1\164"+
        "\1\126\2\uffff";
    static final String DFA8_acceptS =
        "\2\uffff\1\21\1\uffff\1\24\1\25\1\26\1\27\1\30\1\31\1\32\1\33\1"+
        "\34\1\36\2\uffff\1\43\1\44\1\45\1\46\1\47\1\uffff\1\2\1\uffff\1"+
        "\4\1\5\2\uffff\1\15\1\16\1\uffff\1\35\1\23\1\22\1\37\1\42\1\40\1"+
        "\41\1\uffff\1\3\1\14\1\6\1\uffff\1\7\1\13\1\17\1\20\2\uffff\1\1"+
        "\1\10\7\uffff\1\11\1\12";
    static final String DFA8_specialS =
        "\74\uffff}>";
    static final String[] DFA8_transitionS = {
            "\1\24\2\uffff\1\24\22\uffff\1\24\1\4\1\20\4\uffff\1\20\1\13"+
            "\1\12\1\3\1\uffff\1\14\1\22\2\uffff\12\5\1\2\1\21\1\16\1\15"+
            "\1\17\1\uffff\1\1\32\2\1\6\1\uffff\1\7\1\uffff\1\2\1\uffff\32"+
            "\2\1\11\1\23\1\10",
            "\1\36\1\35\1\27\1\32\1\26\3\uffff\1\34\2\uffff\1\30\1\31\1"+
            "\25\4\uffff\1\33",
            "",
            "\1\40",
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
            "\1\42",
            "\1\44",
            "",
            "",
            "",
            "",
            "",
            "\1\46",
            "",
            "\1\50\5\uffff\1\47",
            "",
            "",
            "\1\52\17\uffff\1\51",
            "\1\53\6\uffff\1\54",
            "",
            "",
            "\1\55\7\uffff\1\56",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "\1\57",
            "",
            "",
            "",
            "\1\60",
            "",
            "",
            "",
            "",
            "\1\62\1\61",
            "\1\63",
            "",
            "",
            "\1\64",
            "\1\65",
            "\1\66",
            "\1\67",
            "\1\70",
            "\1\71",
            "\1\73\22\uffff\1\72",
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

    class DFA8 extends DFA {

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
        public String getDescription() {
            return "1:1: Tokens : ( K_NONTEMPORAL | K_EARLIEST | K_CURRENT | K_LATEST | K_MEETS | K_DURATION | K_SEQUENCED | K_NONSEQUENCED | K_DESCENDANTVERSIONS | K_DESCENDANTCHANGES | K_SLICE | K_CONTAINS | K_INTERSECTS | K_BEFORE | K_AFTER | K_ANY | ID | WILDCARD | DOUBLEWILDCARD | NOT | INT | LBRACKET | RBRACKET | RBRACE | LBRACE | RPAREN | LPAREN | COMMA | AT | EQUALS | LE | GE | GT | LT | STRING | EOS | ARROW | PIPE | WS );";
        }
    }
 

}