// $ANTLR 3.5 /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g 2013-10-27 22:36:16

  package grammar1;
  import java.util.*;
  import org.antlr.runtime.BitSet;


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

@SuppressWarnings("all")
public class PiParser extends Parser {
	public static final String[] tokenNames = new String[] {
		"<invalid>", "<EOR>", "<DOWN>", "<UP>", "COMMENTS", "ID", "MAIN", "MULTI_COMMENTS", 
		"NUMERIC", "WS"
	};
	public static final int EOF=-1;
	public static final int COMMENTS=4;
	public static final int ID=5;
	public static final int MAIN=6;
	public static final int MULTI_COMMENTS=7;
	public static final int NUMERIC=8;
	public static final int WS=9;

	// delegates
	public Parser[] getDelegates() {
		return new Parser[] {};
	}

	// delegators


	public PiParser(TokenStream input) {
		this(input, new RecognizerSharedState());
	}
	public PiParser(TokenStream input, RecognizerSharedState state) {
		super(input, state);
	}

	@Override public String[] getTokenNames() { return PiParser.tokenNames; }
	@Override public String getGrammarFileName() { return "/Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g"; }





	// $ANTLR start "rule"
	// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:19:1: rule : main ;
	public final void rule() throws RecognitionException {
		try {
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:19:5: ( main )
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:19:7: main
			{
			pushFollow(FOLLOW_main_in_rule42);
			main();
			state._fsp--;

			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "rule"



	// $ANTLR start "main"
	// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:21:1: main : MAIN ;
	public final void main() throws RecognitionException {
		try {
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:21:5: ( MAIN )
			// /Users/yefang/Documents/research/frontEndGen/test1/src/grammar1/Pi.g:21:7: MAIN
			{
			match(input,MAIN,FOLLOW_MAIN_in_main53); 
			}

		}
		catch (RecognitionException re) {
			reportError(re);
			recover(input,re);
		}
		finally {
			// do for sure before leaving
		}
	}
	// $ANTLR end "main"

	// Delegated rules



	public static final BitSet FOLLOW_main_in_rule42 = new BitSet(new long[]{0x0000000000000002L});
	public static final BitSet FOLLOW_MAIN_in_main53 = new BitSet(new long[]{0x0000000000000002L});
}
