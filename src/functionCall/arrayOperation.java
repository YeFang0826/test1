package functionCall;

import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.z3.*;

import a_expression.*;
import a_term.*;

import expression.expression;

public class arrayOperation extends functionCall{
	public String method;
	
	public arrayOperation(String method){
		this.method = method;
		this.type = "arrayOperation";
		this.inputs = new ArrayList<expression>();
		this.a_inputs = new ArrayList<a_expression>();
	}
	public String tostring(){
		String ret="";
		if(this.method.equals("length")){
			ret = "lastIndex(";
		}
		else
			ret = this.method + "(";
		if(this.a_inputs!=null){
			for(int i=0; i<this.a_inputs.size(); i++){
				if(i==0)
					ret = ret + this.a_inputs.get(i).tostring();
				else 
					ret = ret + ", "+ this.a_inputs.get(i).tostring();
			}
			ret = ret+")"; 
		}
		return ret;
	}
	
	public arrayOperation convert(){
		if(this.inputs!=null && this.inputs.size()>0){
			ArrayList<a_expression> inputs = new ArrayList<a_expression>();
			for(int i=0; i<this.inputs.size(); i++)
				inputs.add(this.inputs.get(i).convert());
			arrayOperation ret = new arrayOperation(this.method);
			ret.a_inputs = inputs;
			return ret;
		}
		else
			return this;
		
	}
	public void annotation_replace(String lhs){
		if(this.a_inputs!=null){
			for(int i=0; i<this.a_inputs.size(); i++)
				this.a_inputs.get(i).annotation_replace(lhs);
		}
	}
	
	public arrayOperation replace(String lhs, expression rhs){
		if(this.a_inputs!=null){
			ArrayList<a_expression> a_inputs = new ArrayList<a_expression>();
			a_expression temp;
			for(int i=0; i<this.a_inputs.size(); i++){
				temp = this.a_inputs.get(i).replace(lhs, rhs);
				a_inputs.add(temp);
			}
			arrayOperation ret =  new arrayOperation(this.method);
			ret.a_inputs = a_inputs;
			return ret;
		}
		return this;
	}
	public arrayOperation replace(HashMap<String, expression> inputExpr){
		if(this.a_inputs!=null){
			ArrayList<a_expression> a_inputs = new ArrayList<a_expression>();
			for(int i=0; i<this.a_inputs.size(); i++)
				a_inputs.add(this.a_inputs.get(i).replace(inputExpr));
			arrayOperation ret =  new arrayOperation(this.method);
			ret.a_inputs = a_inputs;
			return ret;
		}
		return this;
	}
	public Expr array_length(Context ctx, HashMap<String, Expr> symbolTable) throws Z3Exception{
		Expr ret = null;
		if(this.a_inputs.get(0).leaf!=null && this.a_inputs.get(0).leaf.type.equals("string") && symbolTable.containsKey(((a_string)this.a_inputs.get(0).leaf).s)){
			ret = ctx.MkConst((((a_string)this.a_inputs.get(0).leaf).s+"_lastIndex"), ctx.IntSort());
		}
			
		else if (this.a_inputs.get(0).leaf!=null && this.a_inputs.get(0).leaf.type.equals("a_expression") &&
				((a_expressionT)this.a_inputs.get(0).leaf).e.leaf!=null && ((a_expressionT)this.a_inputs.get(0).leaf).e.leaf.type.equals("string")
				&& symbolTable.containsKey(((a_string)((a_expressionT)this.a_inputs.get(0).leaf).e.leaf).s)){
			ret = ctx.MkConst((((a_string)((a_expressionT)this.a_inputs.get(0).leaf).e.leaf).s+"_lastIndex"), ctx.IntSort());
		}
		else
			System.out.println("array not defined!");
		return ret;
	}
	public BoolExpr add_sorted_theorem(Context ctx, HashMap<String, Expr> symbolTable) throws Z3Exception{
		// a, begin, end
		
		Expr a = this.a_inputs.get(0).to_smt(ctx, symbolTable );
		Expr a_begin = this.a_inputs.get(1).to_smt(ctx, symbolTable );
		Expr a_end = this.a_inputs.get(2).to_smt(ctx, symbolTable );
		
		
        //g.Assert(ctx.MkLt((ArithExpr) a_begin, (ArithExpr) a_end));
		String array_name="";
		if(this.a_inputs.get(0).leaf!=null && this.a_inputs.get(0).leaf.type.equals("string") && symbolTable.containsKey(((a_string)this.a_inputs.get(0).leaf).s))
			array_name = ((a_string)this.a_inputs.get(0).leaf).s;
		else if (this.a_inputs.get(0).leaf!=null && this.a_inputs.get(0).leaf.type.equals("a_expression") &&
				((a_expressionT)this.a_inputs.get(0).leaf).e.leaf!=null && ((a_expressionT)this.a_inputs.get(0).leaf).e.leaf.type.equals("string")
				&& symbolTable.containsKey(((a_string)((a_expressionT)this.a_inputs.get(0).leaf).e.leaf).s))
			array_name = ((a_string)((a_expressionT)this.a_inputs.get(0).leaf).e.leaf).s;
		else
			System.out.println("array not defined!");
		
		Expr i = ctx.MkConst(array_name+"_i", ctx.IntSort());
        Expr j = ctx.MkConst(array_name+"_j", ctx.IntSort());
        Expr alength = ctx.MkConst(array_name + "_lastIndex", ctx.IntSort());
        
		
		//BoolExpr instance_bound = ctx.MkAnd(new BoolExpr[]{ctx.MkLe(ctx.MkInt(0), (ArithExpr)a_begin), ctx.MkLe((ArithExpr)a_begin, (ArithExpr)alength), 
				 						//ctx.MkLe(ctx.MkInt(0), (ArithExpr)a_end), ctx.MkLe((ArithExpr)a_end, (ArithExpr)alength), ctx.MkLe((ArithExpr)a_begin, (ArithExpr)a_end)});
		
        BoolExpr instance_bound = ctx.MkLe((ArithExpr)a_begin, (ArithExpr)a_end);
        
        BoolExpr sorted_expr1 = ctx.MkAnd(new BoolExpr[]{ctx.MkLe((ArithExpr) a_begin, (ArithExpr) i), ctx.MkLe((ArithExpr) i, (ArithExpr) a_end),
        												  ctx.MkLe((ArithExpr) a_begin, (ArithExpr) j), ctx.MkLe((ArithExpr) j, (ArithExpr) a_end),
        												  ctx.MkLe((ArithExpr)i, (ArithExpr)j)});
        BoolExpr sorted_expr = ctx.MkImplies(sorted_expr1, ctx.MkLe((ArithExpr)ctx.MkSelect((ArrayExpr)a, i), (ArithExpr)ctx.MkSelect((ArrayExpr)a, j)));
       // BoolExpr sorted_expr = ctx.MkAnd(new BoolExpr[]{sorted_expr1, ctx.MkLe((ArithExpr)ctx.MkSelect((ArrayExpr)a, i), (ArithExpr)ctx.MkSelect((ArrayExpr)a, j))});
        
        BoolExpr  sorted= ctx.MkForall(new Expr[]{i, j}, /* names of quantified variables */
                sorted_expr, 1, null, null, null, null);
        
        
        //g.Assert(sorted);
        //return ctx.MkAnd(new BoolExpr[]{instance_bound, sorted});
        return ctx.MkImplies(instance_bound, sorted);
        //return sorted;
        
	}
	public BoolExpr add_partitioned_theorem(Context ctx, HashMap<String, Expr> symbolTable) throws Z3Exception{
		// a, l1, u1, l2, u2
		Expr a = this.a_inputs.get(0).to_smt(ctx, symbolTable );
		Expr l1 = this.a_inputs.get(1).to_smt(ctx, symbolTable );
		Expr u1 = this.a_inputs.get(2).to_smt(ctx, symbolTable );
		Expr l2 = this.a_inputs.get(3).to_smt(ctx, symbolTable );
		Expr u2 = this.a_inputs.get(4).to_smt(ctx, symbolTable );
		
		
		String array_name="";
		if(this.a_inputs.get(0).leaf!=null && this.a_inputs.get(0).leaf.type.equals("string") && symbolTable.containsKey(((a_string)this.a_inputs.get(0).leaf).s))
			array_name = ((a_string)this.a_inputs.get(0).leaf).s;
		else if (this.a_inputs.get(0).leaf!=null && this.a_inputs.get(0).leaf.type.equals("a_expression") &&
				((a_expressionT)this.a_inputs.get(0).leaf).e.leaf!=null && ((a_expressionT)this.a_inputs.get(0).leaf).e.leaf.type.equals("string")
				&& symbolTable.containsKey(((a_string)((a_expressionT)this.a_inputs.get(0).leaf).e.leaf).s))
			array_name = ((a_string)((a_expressionT)this.a_inputs.get(0).leaf).e.leaf).s;
		else
			System.out.println("array not defined!");
		
		Expr alength = ctx.MkConst(array_name + "_lastIndex", ctx.IntSort());
        Expr i = ctx.MkConst(array_name+"_i", ctx.IntSort());
        Expr j = ctx.MkConst(array_name+"_j", ctx.IntSort());
        //ctx.MkLe(ctx.MkInt(-1), (ArithExpr)l1), ctx.MkLe((ArithExpr)u2, (ArithExpr) alength)
        BoolExpr instance_bound = ctx.MkAnd( new BoolExpr[]{ 
				ctx.MkLe((ArithExpr)l1, (ArithExpr)u1), ctx.MkLt((ArithExpr)u1, (ArithExpr)l2), 
				ctx.MkLe((ArithExpr)l2, (ArithExpr)u2)});
        
        BoolExpr partitioned_expr1 = ctx.MkAnd(new BoolExpr[]{ctx.MkLe((ArithExpr)l1, (ArithExpr) i), ctx.MkLe((ArithExpr) i, (ArithExpr) u1),
        												  ctx.MkLe((ArithExpr)l2, (ArithExpr) j), ctx.MkLe((ArithExpr) j, (ArithExpr)u2),
        												  ctx.MkLt((ArithExpr)i, (ArithExpr)j)});
        //BoolExpr partitioned_expr = ctx.MkAnd(new BoolExpr[]{ partitioned_expr1, ctx.MkLe((ArithExpr)ctx.MkSelect((ArrayExpr)a, i), (ArithExpr)ctx.MkSelect((ArrayExpr)a, j))});
        BoolExpr partitioned_expr = ctx.MkImplies(partitioned_expr1, ctx.MkLe((ArithExpr)ctx.MkSelect((ArrayExpr)a, i), (ArithExpr)ctx.MkSelect((ArrayExpr)a, j)));
        
        
        BoolExpr  partitioned= ctx.MkForall(new Expr[]{i, j}, /* names of quantified variables */
        		partitioned_expr, 1, null, null, null, null);
        
        return ctx.MkImplies(instance_bound,partitioned);
	}
}
