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
		
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("proof", "true");
        Context ctx = new Context(cfg);
        Solver solve = ctx.MkSolver();
        Status q;
        HashMap<String,Expr> symbolTable = new HashMap<String, Expr>();
		parameter p;
		Expr to_put;
		for(int i=0; i<result.inputs.size(); i++){ // input type can be int, double, boolean, int[], double[]
			p = result.inputs.get(i);
			to_put = construct(ctx,p.type,p.name);
			if(to_put!=null)
        		symbolTable.put(p.name, to_put);
        	else
        		System.out.println("Does not support the input type!");
        	
		}
		if(!result.return_type.equals("void")){
			to_put = construct(ctx,result.return_type,"rv");
			if(to_put!=null)
        		symbolTable.put("rv", to_put);
        	else
        		System.out.println("Does not support the output type!");
		}
		
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
					if(s.type.equals("defVarStatement")){
						if(!symbolTable.containsKey(((defVarStatement)s).name)){
							if(construct(ctx,((defVarStatement)s).vtype, ((defVarStatement)s).name)!=null)
				        		symbolTable.put(((defVarStatement)s).name, construct(ctx,((defVarStatement)s).vtype, ((defVarStatement)s).name));
				        	else
				        		System.out.println("Does not support the input type!");
						}
					}
					
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
							g = g.replace(((assignStatement)s).name, ((assignStatement)s).assignment);
							
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
				test = new a_expression(new a_expression(f, g, "implies"), "not");
				System.out.println(test.tostring());
				
				if(am.size()>0){
					for(int j = 0; j<am.size(); j++){
						System.out.println(am.get(j).toString());
						if(symbolTable.containsKey(am.get(j).a)){
							symbolTable.put(am.get(j).a, ctx.MkStore((ArrayExpr)symbolTable.get(am.get(j).a), am.get(i).index.get(0).to_smt(ctx, symbolTable), am.get(i).newvalue.to_smt(ctx, symbolTable)));
						}
					}
						
				}
				
				bpSet.add(test);
				
				Expr z3_f = bp_smt(ctx, test, am, symbolTable);
				System.out.println(z3_f.toString());
				solve.Push();
				solve.Assert((BoolExpr)z3_f);
				q = solve.Check();
				System.out.println(q);
				solve.Pop();
			}
			else
				System.out.println("INCOMPLETE PATH");
		}
	}
	
	public static Expr bp_smt(Context ctx, a_expression bp, ArrayList<arrayModification> am, HashMap<String, Expr> symbolTable) throws Z3Exception{
		
		if(am.size()>0){
			arrayModification x = null;
			for(int i =0; i<am.size(); i++){
				x = am.get(i);
				if(symbolTable.containsKey(x.a)){
					symbolTable.put(x.a, x.to_smt(ctx, symbolTable));
				}
			}
		}
		
		Expr ret = bp.to_smt(ctx, symbolTable);
		return ret;
	}
	
	public static Expr construct(Context ctx, String type, String name) throws Z3Exception{
		if(type.equals("int"))
    		return ctx.MkConst(name,ctx.IntSort());
    	else if(type.equals("double"))
    		return ctx.MkConst(name,ctx.RealSort());
    	else if(type.equals("boolean"))
    		return ctx.MkConst(name,ctx.BoolSort());
    	else if(type.equals("int_array")){
    		//s.Assert(ctx.MkGe(ctx.MkIntConst(name+"_length"), ctx.MkInt(0)));
    		return ctx.MkConst(name, ctx.MkArraySort(ctx.IntSort(), ctx.IntSort()));
    	}
    	else if(type.equals("double_array")){
    		//s.Assert(ctx.MkGe(ctx.MkIntConst(name+"_length"), ctx.MkInt(0)));
    		return ctx.MkConst(name, ctx.MkArraySort(ctx.IntSort(), ctx.RealSort()));
    	}
    	else{
    		System.out.println("Does not support the input type!");
    		return null;
    	}
	}
	
}
