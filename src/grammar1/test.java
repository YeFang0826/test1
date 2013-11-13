package grammar1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import statement.*;

import org.antlr.runtime.*;
import a_expression.*;
import expression.*;


public class test {
	public static void main(String[] args) throws RecognitionException, FileNotFoundException{
		
		Scanner sc= new Scanner(new File("src/input_3.txt"));
		String input = "";
		String temp;
		
		while(sc.hasNext()){
			temp = sc.next();
			input = input + temp + " ";
		}
		
		CharStream charStream = new ANTLRStringStream(input);
		
		PiLexer lexer = new PiLexer(charStream);
		TokenStream tokenStream = new CommonTokenStream(lexer);
		PiParser parser = new PiParser(tokenStream);
		defFunStatement result = parser.program();
		ArrayList<ArrayList<statement>> bp = result.bp();
		/***********************************************************
		for(int i=0;i<bp.size(); i++){
			System.out.println("BASIC PATH: "+i+":");
			for(int j=0; j<bp.get(i).size(); j++){
				if(bp.get(i).get(j).type.equals("annotationStatement"))
					System.out.println(((annotationStatement)bp.get(i).get(j)).a_type);
				else
					System.out.println(bp.get(i).get(j).type);
			}
		}
		***********************************************************/
		ArrayList<a_expression> bpSet = new ArrayList<a_expression>();
		a_expression g;
		a_expression f;
		boolean last;
		statement s;
		//test
		a_expression test;
		for(int i=0;i<bp.size(); i++){
			System.out.println("BASIC PATH: "+i+":");
			g=null;
			f=null;
			last = false;
			
			for(int j=bp.get(i).size()-1; j>=0; j--){
				s = bp.get(i).get(j);
				
				if(j==bp.get(i).size()-1 && s.type.equals("annotationStatement") && 
						((annotationStatement)s).a_type.equals("post")){
						g = ((annotationStatement)s).a;
				}
				
				else if(j==bp.get(i).size()-1 && s.type.equals("cut")){
					last = true; 
				}
				
				else if(last && s.type.equals("annotationStatement")){
					if(((annotationStatement)s).a_type.equals("post")||
							((annotationStatement)s).a_type.equals("pre") ||
							((annotationStatement)s).a_type.equals("invariant")){
						
							g = ((annotationStatement)s).a;
					}
					last = false;
				}
				
				else if(j<bp.get(i).size()-1 && !last){
					if(s.type.equals("defVarStatement")
							&& g!=null && ((defVarStatement)s).assignment!=null){
							g = g.replace(((defVarStatement)s).name, ((defVarStatement)s).assignment);
							if(f!=null)
								f = f.replace(((defVarStatement)s).name, ((defVarStatement)s).assignment);
					}
					else if(s.type.equals("assignStatement") && g!=null){
						if(((assignStatement)s).name!=null){
							g.replace(((assignStatement)s).name, ((assignStatement)s).assignment);
							
							if(f!=null)
								f = f.replace(((defVarStatement)s).name, ((defVarStatement)s).assignment);
						}
						else if(((assignStatement)s).lhs!=null){
							System.out.println("UNDER CONSTRUCTION::\n" + "Array element assignment is not supported for now!");
						}
					}
					else if(s.type.equals("assumeStatement")){
						if(f== null)
							f = ((assumeStatement)s).e.convert();
						else
							f = new a_expression(((assumeStatement)s).e.convert(), f, "and");
					}
					else if(s.type.equals("annotationStatement")){
						if(((annotationStatement)s).a_type.equals("pre")
								||((annotationStatement)s).a_type.equals("invariant")){
							if(f== null)
								f = ((annotationStatement)s).a;
							else
								f = new a_expression(((annotationStatement)s).a, f, "and");
						}
						else if(((annotationStatement)s).a_type.equals("post")){
							g = ((annotationStatement)s).a;
						}
					}
					else if(s.type.equals("returnStatement")){
						g = g.replace("rv", ((returnStatement)s).toreturn);
						if(f!=null)
							f = f.replace(((defVarStatement)s).name, ((defVarStatement)s).assignment);
					}
				}
			}
			if(f!=null && g!=null){
				test = new a_expression(f, g, "implies");
				System.out.println(test.tostring());
				bpSet.add(new a_expression(f,g, "implies"));
			}
			else
				System.out.println("INCOMPLETE PATH");
		}
		/***********************************************************/
	}
}
