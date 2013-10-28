// $ANTLR 3.5 /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g 2013-10-27 22:36:16

  package grammar1;
  import java.util.*;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class PiLexer extends Lexer {
	public static final int EOF=-1;
	public static final int COMMENTS=4;
	public static final int ID=5;
	public static final int MAIN=6;
	public static final int MULTI_COMMENTS=7;
	public static final int NUMERIC=8;
	public static final int WS=9;

	// delegates
	// delegators
	public Lexer[] getDelegates() {
		return new Lexer[] {};
	}

	public PiLexer() {} 
	public PiLexer(CharStream input) {
		this(input, new RecognizerSharedState());
	}
	public PiLexer(CharStream input, RecognizerSharedState state) {
		super(input,state);
	}
	@Override public String getGrammarFileName() { return "/Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g"; }

	// $ANTLR start "MULTI_COMMENTS"
	public final void mMULTI_COMMENTS() throws RecognitionException {
		try {
			int _type = MULTI_COMMENTS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:24:15: ( '/*' ( . )* '*/' )
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:24:17: '/*' ( . )* '*/'
			{
			match("/*"); 

			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:24:22: ( . )*
			loop1:
			while (true) {
				int alt1=2;
				int LA1_0 = input.LA(1);
				if ( (LA1_0=='*') ) {
					int LA1_1 = input.LA(2);
					if ( (LA1_1=='/') ) {
						alt1=2;
					}
					else if ( ((LA1_1 >= '\u0000' && LA1_1 <= '.')||(LA1_1 >= '0' && LA1_1 <= '\uFFFF')) ) {
						alt1=1;
					}

				}
				else if ( ((LA1_0 >= '\u0000' && LA1_0 <= ')')||(LA1_0 >= '+' && LA1_0 <= '\uFFFF')) ) {
					alt1=1;
				}

				switch (alt1) {
				case 1 :
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:24:22: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop1;
				}
			}

			match("*/"); 

			 _channel = HIDDEN; 
			}

			state.type = _type;
			state.channel = _channel;
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "MULTI_COMMENTS"

	// $ANTLR start "COMMENTS"
	public final void mCOMMENTS() throws RecognitionException {
		try {
			int _type = COMMENTS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:25:9: ( '//' ( . )* ( '\\n' | '\\r' ) )
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:25:11: '//' ( . )* ( '\\n' | '\\r' )
			{
			match("//"); 

			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:25:16: ( . )*
			loop2:
			while (true) {
				int alt2=2;
				int LA2_0 = input.LA(1);
				if ( (LA2_0=='\n'||LA2_0=='\r') ) {
					alt2=2;
				}
				else if ( ((LA2_0 >= '\u0000' && LA2_0 <= '\t')||(LA2_0 >= '\u000B' && LA2_0 <= '\f')||(LA2_0 >= '\u000E' && LA2_0 <= '\uFFFF')) ) {
					alt2=1;
				}

				switch (alt2) {
				case 1 :
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:25:16: .
					{
					matchAny(); 
					}
					break;

				default :
					break loop2;
				}
			}

			if ( input.LA(1)=='\n'||input.LA(1)=='\r' ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
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
	// $ANTLR end "COMMENTS"

	// $ANTLR start "NUMERIC"
	public final void mNUMERIC() throws RecognitionException {
		try {
			int _type = NUMERIC;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:8: ( ( '0' .. '9' )+ (| ( '.' ( ( '0' .. '9' )+ ) ) ) )
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:10: ( '0' .. '9' )+ (| ( '.' ( ( '0' .. '9' )+ ) ) )
			{
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:10: ( '0' .. '9' )+
			int cnt3=0;
			loop3:
			while (true) {
				int alt3=2;
				int LA3_0 = input.LA(1);
				if ( ((LA3_0 >= '0' && LA3_0 <= '9')) ) {
					alt3=1;
				}

				switch (alt3) {
				case 1 :
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:
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
					if ( cnt3 >= 1 ) break loop3;
					EarlyExitException eee = new EarlyExitException(3, input);
					throw eee;
				}
				cnt3++;
			}

			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:22: (| ( '.' ( ( '0' .. '9' )+ ) ) )
			int alt5=2;
			int LA5_0 = input.LA(1);
			if ( (LA5_0=='.') ) {
				alt5=2;
			}

			else {
				alt5=1;
			}

			switch (alt5) {
				case 1 :
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:23: 
					{
					}
					break;
				case 2 :
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:24: ( '.' ( ( '0' .. '9' )+ ) )
					{
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:24: ( '.' ( ( '0' .. '9' )+ ) )
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:25: '.' ( ( '0' .. '9' )+ )
					{
					match('.'); 
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:28: ( ( '0' .. '9' )+ )
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:29: ( '0' .. '9' )+
					{
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:26:29: ( '0' .. '9' )+
					int cnt4=0;
					loop4:
					while (true) {
						int alt4=2;
						int LA4_0 = input.LA(1);
						if ( ((LA4_0 >= '0' && LA4_0 <= '9')) ) {
							alt4=1;
						}

						switch (alt4) {
						case 1 :
							// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:
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
							if ( cnt4 >= 1 ) break loop4;
							EarlyExitException eee = new EarlyExitException(4, input);
							throw eee;
						}
						cnt4++;
					}

					}

					}

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
	// $ANTLR end "NUMERIC"

	// $ANTLR start "ID"
	public final void mID() throws RecognitionException {
		try {
			int _type = ID;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:27:7: ( ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )* )
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:27:9: ( 'a' .. 'z' | 'A' .. 'Z' | '_' ) ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			{
			if ( (input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
				input.consume();
			}
			else {
				MismatchedSetException mse = new MismatchedSetException(null,input);
				recover(mse);
				throw mse;
			}
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:27:33: ( 'a' .. 'z' | 'A' .. 'Z' | '_' | '0' .. '9' )*
			loop6:
			while (true) {
				int alt6=2;
				int LA6_0 = input.LA(1);
				if ( ((LA6_0 >= '0' && LA6_0 <= '9')||(LA6_0 >= 'A' && LA6_0 <= 'Z')||LA6_0=='_'||(LA6_0 >= 'a' && LA6_0 <= 'z')) ) {
					alt6=1;
				}

				switch (alt6) {
				case 1 :
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:
					{
					if ( (input.LA(1) >= '0' && input.LA(1) <= '9')||(input.LA(1) >= 'A' && input.LA(1) <= 'Z')||input.LA(1)=='_'||(input.LA(1) >= 'a' && input.LA(1) <= 'z') ) {
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
					break loop6;
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

	// $ANTLR start "WS"
	public final void mWS() throws RecognitionException {
		try {
			int _type = WS;
			int _channel = DEFAULT_TOKEN_CHANNEL;
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:28:4: ( ( ' ' | '\\t' | '\\n' | '\\r' | '\\u000C' )+ )
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:28:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\u000C' )+
			{
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:28:6: ( ' ' | '\\t' | '\\n' | '\\r' | '\\u000C' )+
			int cnt7=0;
			loop7:
			while (true) {
				int alt7=2;
				int LA7_0 = input.LA(1);
				if ( ((LA7_0 >= '\t' && LA7_0 <= '\n')||(LA7_0 >= '\f' && LA7_0 <= '\r')||LA7_0==' ') ) {
					alt7=1;
				}

				switch (alt7) {
				case 1 :
					// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:
					{
					if ( (input.LA(1) >= '\t' && input.LA(1) <= '\n')||(input.LA(1) >= '\f' && input.LA(1) <= '\r')||input.LA(1)==' ' ) {
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
		// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:1:8: ( MULTI_COMMENTS | COMMENTS | NUMERIC | ID | WS )
		int alt8=5;
		switch ( input.LA(1) ) {
		case '/':
			{
			int LA8_1 = input.LA(2);
			if ( (LA8_1=='*') ) {
				alt8=1;
			}
			else if ( (LA8_1=='/') ) {
				alt8=2;
			}

			else {
				int nvaeMark = input.mark();
				try {
					input.consume();
					NoViableAltException nvae =
						new NoViableAltException("", 8, 1, input);
					throw nvae;
				} finally {
					input.rewind(nvaeMark);
				}
			}

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
			alt8=3;
			}
			break;
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
			alt8=4;
			}
			break;
		case '\t':
		case '\n':
		case '\f':
		case '\r':
		case ' ':
			{
			alt8=5;
			}
			break;
		default:
			NoViableAltException nvae =
				new NoViableAltException("", 8, 0, input);
			throw nvae;
		}
		switch (alt8) {
			case 1 :
				// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:1:10: MULTI_COMMENTS
				{
				mMULTI_COMMENTS(); 

				}
				break;
			case 2 :
				// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:1:25: COMMENTS
				{
				mCOMMENTS(); 

				}
				break;
			case 3 :
				// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:1:34: NUMERIC
				{
				mNUMERIC(); 

				}
				break;
			case 4 :
				// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:1:42: ID
				{
				mID(); 

				}
				break;
			case 5 :
				// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:1:45: WS
				{
				mWS(); 

				}
				break;

		}
	}



}
