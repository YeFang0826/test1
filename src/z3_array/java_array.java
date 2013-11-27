package z3_array;


import java.util.ArrayList;
import java.util.HashMap;

import parameter.parameter;

import a_expression.a_expression;
import a_expression.arrayModification;

import com.microsoft.z3.*;

public class java_array {
	public static void main(String[] args) throws Z3Exception{
		
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("proof", "true");
        Context ctx = new Context(cfg);
        Solver s = ctx.MkSolver();
        Goal g = ctx.MkGoal(true, false, false);
        ArraySort array_sort = ctx.MkArraySort(ctx.IntSort(), ctx.RealSort());
       
	}
	public Expr bp_smt(Context ctx, a_expression bp, ArrayList<arrayModification> am, ArrayList<parameter> functionInputs) throws Z3Exception{
		// bp is the a_expression 
		// am is a list of array modification
		// functionInputs is declared const
		HashMap<String,Expr> symbolTable = new HashMap<String, Expr>();
		parameter p;
		for(int i=0; i<functionInputs.size(); i++){ // input type can be int, double, boolean, int[], double[]
			p = functionInputs.get(i);
			if(p.type.equals("int"))
        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.IntSort()));
        	else if(p.type.equals("double"))
        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.RealSort()));
        	else if(p.type.equals("boolean"))
        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.BoolSort()));
        	else if(p.type.equals("int_array"))
        		symbolTable.put(p.name, ctx.MkConst(p.name, ctx.MkArraySort(ctx.IntSort(), ctx.IntSort())));
        	else if(p.type.equals("double_array"))
        		symbolTable.put(p.name, ctx.MkConst(p.name, ctx.MkArraySort(ctx.IntSort(), ctx.RealSort())));
        	else
        		System.out.println("Does not support the input type!");
        	
		}
		
		if(am.size()>0){
			arrayModification x = null;
			for(int i =0; i<am.size(); i++){
				x = am.get(i);
				if(symbolTable.containsKey(x.a)){
					//symbolTable.put(x.a, x.to_smt(ctx, symbolTable));
				}
			}
		}
		
		//Expr ret = bp.to_smt(ctx, symbolTable);
		return null;
	}
	
	
	
}
/*
        // test partitioned theorem
        Expr l1 = ctx.MkIntConst("0");
        Expr u1 = ctx.MkConst("cut", ctx.IntSort());
        Expr l2 = ctx.MkAdd(new ArithExpr[]{ (ArithExpr) u1, ctx.MkIntConst("1")});
        Expr u2 = ctx.MkIntConst("a_length");
        s = add_partitioned_theorem(ctx, s, g, a, "a",(ArithExpr)l1,(ArithExpr)u1,(ArithExpr)l2, (ArithExpr)u2);
        Expr i = ctx.MkConst("i", ctx.IntSort());
        Expr j = ctx.MkConst("j", ctx.IntSort());
        BoolExpr p1 = ctx.MkAnd(new BoolExpr[]{ ctx.MkLe((ArithExpr) l1, (ArithExpr) i), ctx.MkLe((ArithExpr) i, (ArithExpr) u1),
				  ctx.MkLe((ArithExpr) l2, (ArithExpr) j), ctx.MkLe((ArithExpr) j, (ArithExpr) u2),
				  ctx.MkLt((ArithExpr)i, (ArithExpr)j), ctx.MkGt((ArithExpr)ctx.MkSelect((ArrayExpr) a, i), (ArithExpr)ctx.MkSelect((ArrayExpr) a, j))});
        s.Assert(p1);
        g.Assert(p1);
        
        for (BoolExpr f : g.Formulas())
        	System.out.println(f.toString());
        Status q = s.Check();
        System.out.println(q);
        
*/
