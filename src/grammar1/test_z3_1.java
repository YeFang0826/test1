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

public class test_z3_1 {
	public static void main(String[] args) throws RecognitionException, FileNotFoundException, Z3Exception{
		
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
		
		/*********************************************************************************************/
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("proof", "true");
        Context ctx = new Context(cfg);
        Solver solve = ctx.MkSolver();
        Status q;
        HashMap<String,Expr> symbolTable = new HashMap<String, Expr>(); // defines variable appeared in formula
		parameter p;
		Expr to_put;
		Expr f_array_bound=null; // each array has a_lastIndex >= 0
		a_expression test;
		for(int i=0; i<result.inputs.size(); i++){ // input type can be int, double, boolean, int[], double[]
			p = result.inputs.get(i);
			to_put = construct(ctx,p.type,p.name);
			f_array_bound = array_bound(ctx,p.type,p.name,f_array_bound);
			if(to_put!=null)
        		symbolTable.put(p.name, to_put);
        	else
        		System.out.println("Does not support the input type!");
        	
		}
		if(!result.return_type.equals("void")){
			to_put = construct(ctx,result.return_type,"rv");
    		f_array_bound = array_bound(ctx,result.return_type,"rv",f_array_bound);
			if(to_put!=null)
        		symbolTable.put("rv", to_put);
        	
        	else
        		System.out.println("Does not support the output type!");
		}
		/*********************************************************************************************/
		HashMap<String, expression> valueTable;
		ArrayList<a_expression> bpSet = new ArrayList<a_expression>();
		a_expression g;
		a_expression f;
		Expr f_assume;
		boolean postCond = false;
		statement s;
		
		for(int i=0;i<bp.size(); i++){
			System.out.println("BASIC PATH: "+i+":");
			valueTable = new HashMap<String,expression>();
			g=null;
			f = null;
			f_assume=null;
			postCond = false; //F, G
			
			for(int j=0; j<bp.get(i).size(); j++){
				s = bp.get(i).get(j);
				if(s.type.equals("annotationStatement")){
					if(!postCond){
							f = ((annotationStatement)s).a;
							postCond = true;
					}
					else {
						g = ((annotationStatement)s).a;
					}
				}
				else if(s.type.equals("assumeStatement")){
					if(((assumeStatement)s).e!=null){
						if(f_assume== null){
							if(valueTable.size()>0)
								f_assume = ((assumeStatement)s).e.convert().replace(valueTable).to_smt(ctx, symbolTable );
							else
								f_assume = ((assumeStatement)s).e.convert().to_smt(ctx, symbolTable );
						}
							
						else{
							if(valueTable.size()>0)
								f_assume = ctx.MkAnd(new BoolExpr[]{(BoolExpr)((assumeStatement)s).e.convert().replace(valueTable).to_smt(ctx, symbolTable ),
										(BoolExpr)f_assume});
							else
								f_assume = ctx.MkAnd(new BoolExpr[]{(BoolExpr)((assumeStatement)s).e.convert().to_smt(ctx, symbolTable ),
										(BoolExpr)f_assume});
						}
					}
					if(((assumeStatement)s).a_e!=null){
						if(f_assume == null)
							f_assume = ((assumeStatement)s).a_e.replace(valueTable).to_smt(ctx, symbolTable);
						else
							f_assume = ctx.MkAnd(new BoolExpr[]{(BoolExpr)((assumeStatement)s).a_e.replace(valueTable).to_smt(ctx, symbolTable),
									(BoolExpr)f_assume});
					}
					
						
				}
				
				else if(s.type.equals("defVarStatement")){
					to_put = construct(ctx,((defVarStatement)s).vtype,((defVarStatement)s).name);
	        		f_array_bound = array_bound(ctx,((defVarStatement)s).vtype,((defVarStatement)s).name,f_array_bound);
					if(to_put!=null)
						symbolTable.put(((defVarStatement)s).name, to_put);
					
					if(((defVarStatement)s).assignment!=null){
						valueTable.put(((defVarStatement)s).name, ((defVarStatement)s).assignment);
					}
				}
				else if(s.type.equals("assignStatement")){
						if(((assignStatement)s).name !=null){							
							valueTable.put(((assignStatement)s).name, ((assignStatement)s).assignment);
						}
						else if(((assignStatement)s).lhs!=null && symbolTable.containsKey(((assignStatement)s).lhs.aName)){
							if(valueTable.size()>0){
								symbolTable.put(((assignStatement)s).lhs.aName, 
										ctx.MkStore((ArrayExpr)symbolTable.get(((assignStatement)s).lhs.aName),
													((assignStatement)s).lhs.index.get(0).convert().replace(valueTable).to_smt(ctx, symbolTable ), 
													((assignStatement)s).assignment.convert().replace(valueTable).to_smt(ctx, symbolTable ))
													);
							}
							else
								symbolTable.put(((assignStatement)s).lhs.aName, 
										ctx.MkStore((ArrayExpr)symbolTable.get(((assignStatement)s).lhs.aName),
													((assignStatement)s).lhs.index.get(0).convert().to_smt(ctx, symbolTable ), 
													((assignStatement)s).assignment.convert().to_smt(ctx, symbolTable ))
													);
								
						}
					}
				else if(s.type.equals("returnStatement")){
					if(valueTable.size()>0)
						valueTable.put("rv", ((returnStatement)s).toreturn);
					else
						valueTable.put("rv", ((returnStatement)s).toreturn);
				}
			}
			
		if(f!=null && g!=null){
			//System.out.println("pre:" + f.tostring());
			Expr and_clauses = null;
			if(f_array_bound!=null && f_assume!=null){
				and_clauses = ctx.MkAnd(new BoolExpr[]{ (BoolExpr)f.to_smt(ctx, symbolTable), (BoolExpr) f_array_bound, (BoolExpr)f_assume});
			}
			else if(f_array_bound!=null)
				and_clauses = ctx.MkAnd(new BoolExpr[]{ (BoolExpr)f.to_smt(ctx, symbolTable), (BoolExpr) f_array_bound});
			else if(f_assume!=null)
				and_clauses = ctx.MkAnd(new BoolExpr[]{ (BoolExpr)f.to_smt(ctx, symbolTable), (BoolExpr) f_assume});
				
			else
				and_clauses = (BoolExpr)f.to_smt(ctx, symbolTable);
			
			if(valueTable.size()>0)
				g = g.replace(valueTable);

			//System.out.println("post:" + g.tostring());
			
			Expr z3_f = ctx.MkNot(ctx.MkImplies( (BoolExpr) and_clauses,(BoolExpr)g.to_smt(ctx, symbolTable)));
			solve.Push();
			solve.Assert((BoolExpr)z3_f);
			q = solve.Check();
			System.out.println(q);
			if(q.toString().equals("SATISFIABLE")){
				System.out.println(z3_f.toString());
				System.out.println(solve.Model());
			}
			solve.Pop();
			
		}
		else
			System.out.println("INCOMPLETE PATH");
	}
		
}
	
	public static Expr bp_smt(Context ctx, a_expression bp, ArrayList<arrayModification> am, HashMap<String, Expr> symbolTable) throws Z3Exception{
		Expr ret = bp.to_smt(ctx, symbolTable );
		return ret;
	}
	
	public static Expr construct(Context ctx, String type, String name) throws Z3Exception{
		if(type.equals("int"))
    		return ctx.MkConst(name,ctx.IntSort());
    	else if(type.equals("double"))
    		return ctx.MkConst(name,ctx.RealSort());
    	else if(type.equals("boolean"))
    		return ctx.MkConst(name,ctx.BoolSort());
    	else if(type.equals("int_array"))
    		return ctx.MkConst(name, ctx.MkArraySort(ctx.IntSort(), ctx.IntSort()));
    	else if(type.equals("double_array"))
    		return ctx.MkConst(name, ctx.MkArraySort(ctx.IntSort(), ctx.RealSort()));
    	else{
    		System.out.println("Does not support the input type!");
    		return null;
    	}
	}
	public static Expr array_bound(Context ctx, String type, String name, Expr f_array_bound) throws Z3Exception{
		if(type.equals("int") || type.equals("double") || type.equals("boolean"))
    		return f_array_bound;
    	else if(type.equals("int_array") || type.equals("double_array")){
    		if(f_array_bound == null)
    			f_array_bound = ctx.MkLe(ctx.MkInt(0), (ArithExpr)ctx.MkConst(name+"_lastIndex", ctx.IntSort()));
    		else
    			f_array_bound = ctx.MkAnd(new BoolExpr[]{(BoolExpr)f_array_bound, 
    									ctx.MkLe(ctx.MkInt(0), (ArithExpr)ctx.MkConst(name+"_lastIndex", ctx.IntSort()))});
    		return f_array_bound;
    	}
    	else{
    		System.out.println("Does not support the input type!");
    		return null;
    	}
	}
	
}
