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
import com.microsoft.z3.*;
import parameter.*;

public class test_z3 {
	public static void main(String[] args) throws RecognitionException, FileNotFoundException, Z3Exception{
		
		Scanner sc= new Scanner(new File("src/input_4.txt"));
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
		
		ArrayList<a_expression> bpSet = new ArrayList<a_expression>();
		a_expression g;
		a_expression f;
		ArrayList<arrayModification> am;
		boolean last;
		statement s;
		//test
		a_expression test;
		for(int i=0;i<bp.size(); i++){
			System.out.println("BASIC PATH: "+i+":");
			g=null;
			f=null;
			last = false;
			am = new ArrayList<arrayModification>();
			
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
							if(am.size()>0){
								for(int k=0; k<am.size(); k++)
									am.get(k).replace(((defVarStatement)s).name, ((defVarStatement)s).assignment);
							}
					}
					else if(s.type.equals("assignStatement") && g!=null){
						if(((assignStatement)s).name!=null){
							g.replace(((assignStatement)s).name, ((assignStatement)s).assignment);
							
							if(f!=null)
								f = f.replace(((assignStatement)s).name, ((assignStatement)s).assignment);
							
							if(am.size()>0){
								for(int k=0; k<am.size(); k++)
									am.get(k).replace(((assignStatement)s).name, ((assignStatement)s).assignment);
							}
						}
						else if(((assignStatement)s).lhs!=null){
							arrayModification tempam = new arrayModification(((assignStatement)s).lhs.aName, ((assignStatement)s).lhs.index, ((assignStatement)s).assignment);
							am.add(tempam);
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
							f = f.replace("rv", ((returnStatement)s).toreturn);
						if(am.size()>0){
							for(int k=0; k<am.size(); k++)
								am.get(k).replace("rv", ((returnStatement)s).toreturn);
						}
					}
				}
			}
			if(f!=null && g!=null){
				test = new a_expression(f, g, "implies");
				System.out.println(test.tostring());
				if(am.size()>0){
					for(int j = 0; j<am.size(); j++)
						System.out.println(am.get(j).toString());
				}
				bpSet.add(new a_expression(f,g, "implies"));
			}
			else
				System.out.println("INCOMPLETE PATH");
		}
	}
	
	public void prove(a_expression bp, ArrayList<arrayModification> am, ArrayList<parameter> functionInputs) throws Z3Exception{
		//+ function inputs
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("proof", "true");
        Context ctx = new Context(cfg);
        
        // define datatype for java integer array
        ArraySort intArray = ctx.MkArraySort(ctx.IntSort(), ctx.RealSort());
        TupleSort javaArray = ctx.MkTupleSort(ctx.MkSymbol("javaArray"), new Symbol[] { ctx.MkSymbol("array"), ctx.MkSymbol("bound") },new Sort[] {intArray, ctx.IntSort()} );
        // hashMap <string, z3Object>
        HashMap<String,Expr> symbolTable = new HashMap<String, Expr>();
        parameter p;
        for(int i=0; i<functionInputs.size(); i++){
        	p = functionInputs.get(i);
        	if(p.type.equals("int"))
        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.IntSort()));
        	else if(p.type.equals("array")){
        		symbolTable.put(p.name, ctx.MkConst(p.name, javaArray));
        	}
        	else if(p.type.equals("boolean"))
        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.BoolSort()));
        	else if(p.type.equals("double"))
        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.RealSort()));
        }
        
		
	}
}
