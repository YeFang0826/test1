package grammar1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

import org.antlr.runtime.*;


public class test {
	public static void main(String[] args) throws RecognitionException, FileNotFoundException{
		
		Scanner sc= new Scanner(new File("src/input.txt"));
		String input = "";
		String temp;
		while(sc.hasNext()){
			temp = sc.next();
			input = input + temp + " ";
			System.out.println(temp);
		}
		
		CharStream charStream = new ANTLRStringStream(input);
		
		PiLexer lexer = new PiLexer(charStream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		PiParser parser = new PiParser(tokenStream);
		rule result = parser.rule();
		System.out.println(result.functions.size());
		
	}
}
