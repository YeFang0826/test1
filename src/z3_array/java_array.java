package z3_array;


import java.util.HashMap;

import com.microsoft.z3.*;

public class java_array {
	public static void main(String[] args) throws Z3Exception{
		
		HashMap<String, String> cfg = new HashMap<String, String>();
        cfg.put("proof", "true");
        Context ctx = new Context(cfg);
        Solver s = ctx.MkSolver();
        Goal g = ctx.MkGoal(true, false, false);
        ArraySort array_sort = ctx.MkArraySort(ctx.IntSort(), ctx.RealSort());
        Expr a = ctx.MkConst("a", array_sort);
        
	}
	
	public static Solver add_sorted_theorem(Context ctx, Solver s, Goal g, Expr a, String array_name, String begin, String end) throws Z3Exception{
		
		Expr a_begin = null;
		if(begin.equals("0"))
			a_begin = ctx.MkIntConst("0");
		else
			a_begin= ctx.MkConst(begin, ctx.IntSort());
		
		Expr a_end = ctx.MkConst(end, ctx.IntSort());
        s.Assert(ctx.MkLt((ArithExpr) a_begin, (ArithExpr) a_end));
        Expr i = ctx.MkConst(array_name+"_i", ctx.IntSort());
        Expr j = ctx.MkConst(array_name+"_j", ctx.IntSort());
        
        BoolExpr sorted_expr1 = ctx.MkAnd(new BoolExpr[]{ ctx.MkLe((ArithExpr) a_begin, (ArithExpr) i), ctx.MkLt((ArithExpr) i, (ArithExpr) a_end),
        												  ctx.MkLe((ArithExpr) a_begin, (ArithExpr) j), ctx.MkLt((ArithExpr) j, (ArithExpr) a_end),
        												  ctx.MkLe((ArithExpr)i, (ArithExpr)j)});
        BoolExpr sorted_expr = ctx.MkImplies(sorted_expr1, ctx.MkLe((ArithExpr)ctx.MkSelect((ArrayExpr) a, i), (ArithExpr)ctx.MkSelect((ArrayExpr) a, j)));
        
        BoolExpr  sorted= ctx.MkForall(new Expr[]{i, j}, /* names of quantified variables */
                sorted_expr, 1, null, null, null, null);
        
        
        s.Assert(sorted);
        g.Assert(sorted);
        
        return s;
	}
	public static Solver add_partitioned_theorem(Context ctx, Solver s, Goal g, Expr a, String array_name, ArithExpr l1,ArithExpr u1, ArithExpr l2, ArithExpr u2) throws Z3Exception{
		
		
		s.Assert(ctx.MkAnd(new BoolExpr[]{ctx.MkLt(ctx.MkInt(-1), l1), ctx.MkLe(l1, u1), ctx.MkLt(u1, l2), ctx.MkLe(l2, u2)}));
      
        Expr i = ctx.MkConst(array_name+"_i", ctx.IntSort());
        Expr j = ctx.MkConst(array_name+"_j", ctx.IntSort());
        
        BoolExpr partitioned_expr1 = ctx.MkAnd(new BoolExpr[]{ ctx.MkLe(l1, (ArithExpr) i), ctx.MkLe((ArithExpr) i, u1),
        												  ctx.MkLe(l2, (ArithExpr) j), ctx.MkLe((ArithExpr) j, u2),
        												  ctx.MkLt((ArithExpr)i, (ArithExpr)j)});
        BoolExpr partitioned_expr = ctx.MkImplies(partitioned_expr1, ctx.MkLe((ArithExpr)ctx.MkSelect((ArrayExpr) a, i), (ArithExpr)ctx.MkSelect((ArrayExpr) a, j)));
        
        BoolExpr  partitioned= ctx.MkForall(new Expr[]{i, j}, /* names of quantified variables */
        		partitioned_expr, 1, null, null, null, null);
        
        
        s.Assert(partitioned);
        g.Assert(partitioned);
        
        return s;
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
