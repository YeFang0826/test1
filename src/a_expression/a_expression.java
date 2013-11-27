package a_expression;

import java.util.ArrayList;
import java.util.HashMap;

import term.arrayElement;
import term.bool;
import term.expressionT;
import term.functionCallT;
import term.num;
import term.string;
import term.term;
import expression.expression;
import a_term.*;
import functionCall.*;
import com.microsoft.z3.*;
import parameter.parameter;

public class a_expression {
	public ArrayList<quantifier> q;
	public a_expression l;
	public a_expression r;
	public String op;
	public a_term leaf;
	
	public a_expression(){
		
	}
	public a_expression(a_expression l, a_expression r, String op){
		this.l = l;
		this.r = r;
		this.op = op;
	}
	public a_expression(a_expression r, String op){
		this.r = r;
		this.op = op;
	}
	public a_expression(a_term leaf){
		this.leaf = leaf;
	}
	public void annotation_process(){
		if(this.q!=null && this.q.size()>0){
			for(int i=0; i<this.q.size(); i++){
				for(int j =0; j<this.q.get(i).v.size(); j++){
					this.annotation_replace(this.q.get(i).v.get(j).name);
					//System.out.println("replace " + this.q.get(i).v.get(j).name + " with a_" + this.q.get(i).v.get(j).name);
					this.q.get(i).v.get(j).name = "a_"+this.q.get(i).v.get(j).name;
				}
			}
		}
		if(this.l!=null)
			this.l.annotation_process();
		if(this.r!=null)
			this.r.annotation_process();
		if(this.leaf!=null && this.leaf.type.equals("a_expression"))
			((a_expressionT)this.leaf).e.annotation_process();
			
	}
	
	public void annotation_replace(String lhs){
		a_expression ret = null;
		if(this.leaf!=null){
			if(this.leaf.type.equals("boolean") ||this.leaf.type.equals("number"))
				ret = this;
			else if(this.leaf.type.equals("string")){
				if (((a_string)this.leaf).s.equals(lhs)){
					((a_string)this.leaf).s = "a_"+((a_string)this.leaf).s;
				}
			}
			else if(this.leaf.type.equals("a_expression"))
				((a_expressionT)this.leaf).e.annotation_replace(lhs);
			else if(this.leaf.type.equals("arrayElement"))
				((a_arrayElement)this.leaf).annotation_replace(lhs);
			else if(this.leaf.type.equals("functionCall"))
				((a_functionCallT)this.leaf).call.annotation_replace(lhs);
		}
		else if(this.l!=null && this.r!=null){
			this.l.annotation_replace(lhs);
			this.r.annotation_replace(lhs);
		}
		else if(this.r!=null)
			this.r.annotation_replace(lhs);
	}
	
	
	public a_expression replace(String lhs, expression rhs){
		a_expression ret = null;
		if(this.leaf!=null){
			if(this.leaf.type.equals("boolean") ||this.leaf.type.equals("number"))
				ret = this;
			else if(this.leaf.type.equals("string")){
				if (((a_string)this.leaf).s.equals(lhs)){
					ret = new a_expression(new a_expressionT(rhs.convert()));
					//System.out.println("Replace:" + lhs + " with " + ret.tostring());
				}
				else
					ret = this;
			}
			else if(this.leaf.type.equals("a_expression"))
				ret = new a_expression(new a_expressionT(((a_expressionT)this.leaf).e.replace(lhs,rhs)));
			else if(this.leaf.type.equals("arrayElement"))
				ret = new a_expression(((a_arrayElement)this.leaf).replace(lhs, rhs));
			else if(this.leaf.type.equals("functionCall"))
				ret = new a_expression(new a_functionCallT(((a_functionCallT)this.leaf).call.replace(lhs, rhs)));
		}
		else if(this.l!=null && this.r!=null)
			ret = new a_expression(this.l.replace(lhs,rhs), this.r.replace(lhs,rhs), this.op);
		else if(this.r!=null)
			ret = new a_expression(this.r.replace(lhs,rhs), this.op);
		
		ret.q = this.q;
		return ret;
	}
	
	public a_expression replace(HashMap<String, expression> inputExpr){
		a_expression ret = null;
		if(this.leaf!=null){
			if(this.leaf.type.equals("boolean") ||this.leaf.type.equals("number"))
				ret = this;
			else if(this.leaf.type.equals("string")){
				if (inputExpr.containsKey(((a_string)this.leaf).s)){
					ret = new a_expression(new a_expressionT(inputExpr.get(((a_string)this.leaf).s).convert()));
					//System.out.println("Replace:" + ((a_string)this.leaf).s);
				}
				else
					ret = this;
			}
			else if(this.leaf.type.equals("a_expression"))
				ret = new a_expression(new a_expressionT(((a_expressionT)this.leaf).e.replace(inputExpr)));
			else if(this.leaf.type.equals("arrayElement"))
				ret = new a_expression(((a_arrayElement)this.leaf).replace(inputExpr));
			else if(this.leaf.type.equals("functionCall"))
				ret = new a_expression(new a_functionCallT(((a_functionCallT)this.leaf).call.replace(inputExpr)));
		}
		else if(this.l!=null && this.r!=null)
			ret = new a_expression(this.l.replace(inputExpr), this.r.replace(inputExpr), this.op);
		else if(this.r!=null)
			ret = new a_expression(this.r.replace(inputExpr), this.op);
		
		ret.q = this.q;
		
		return ret;
	}
	
	public String tostring(){
		String ret = "";
		if(this.leaf!=null){
			if(this.leaf.type.equals("string")){
				ret = ((a_string)this.leaf).s;
			}
			else if(this.leaf.type.equals("number")){
				ret = Double.toString(((a_num)this.leaf).n);
			}
			else if(this.leaf.type.equals("boolean")){
				ret = ((a_bool)this.leaf).b;
			}
			else if(this.leaf.type.equals("functionCall") )
				ret = (((a_functionCallT)this.leaf).call).tostring();
			
			else if(this.leaf.type.equals("arrayElement"))
				ret = ((a_arrayElement)this.leaf).tostring();
			else if(this.leaf.type.equals("a_expression"))
				ret = ((a_expressionT)this.leaf).e.tostring();
		}
		else if(this.l!=null && this.r!=null){
			ret = "("+this.l.tostring() +" "+ this.op +" "+ this.r.tostring()+")"; 
		}
		else if(this.r!=null){
			ret = "("+this.op +" "+ this.r.tostring()+")"; 
		}
		
		
		if(this.q!=null){
			for(int i=0; i<this.q.size(); i++)
				ret = this.q.get(i).tostring() + ret;
		}
		return ret;
	}
	
	public Expr to_smt(Context ctx, HashMap<String, Expr> symbolTable) throws Z3Exception{
		
		if(this.q!=null && this.q.size()>0){ // new variables in annotation a_variablename
			parameter p;
			for(int i=0; i<this.q.size(); i++){
				for(int j=0; j<this.q.get(i).v.size(); j++){
					p = this.q.get(i).v.get(j);
					
					if(p.type.equals("int"))
		        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.IntSort()));
		        	else if(p.type.equals("double"))
		        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.RealSort()));
		        	else if(p.type.equals("boolean"))
		        		symbolTable.put(p.name, ctx.MkConst(p.name,ctx.BoolSort()));
		        	else
		        		System.out.println("Does not support the type inside quantifier!");
				}
			}
		}

		Expr ret = null;
		if(this.leaf!=null){
			if(this.leaf.type.equals("string")){
				if(symbolTable.containsKey(((a_string)this.leaf).s))
					ret = symbolTable.get(((a_string)this.leaf).s);
				else
					System.out.println("variable" + ((a_string)this.leaf).s + " not defined");
			}
			else if(this.leaf.type.equals("number")){
				ret = ctx.MkReal(Double.toString(((a_num)this.leaf).n));
			}
			else if(this.leaf.type.equals("boolean")){
				if(((a_bool)this.leaf).b.equals("true"))
						ret = ctx.MkBool(true);
				else if(((a_bool)this.leaf).b.equals("false"))
						ret = ctx.MkBool(false);
			}
			
			else if(this.leaf.type.equals("functionCall") )
				ret = (((a_functionCallT)this.leaf).call).to_smt(ctx, symbolTable );
			
			else if(this.leaf.type.equals("arrayElement"))
				ret = ((a_arrayElement)this.leaf).to_smt(ctx, symbolTable );
				
			else if(this.leaf.type.equals("a_expression"))
				ret = ((a_expressionT)this.leaf).e.to_smt(ctx, symbolTable );
			//System.out.println(ret.toString());
		}
		else if(this.l!=null && this.r!=null){
			
			if(this.op.equals("and")){
				BoolExpr[] to_and = {(BoolExpr) this.l.to_smt(ctx, symbolTable ), (BoolExpr) this.r.to_smt(ctx, symbolTable )};
				ret = ctx.MkAnd(to_and);
			}
			else if(this.op.equals("or")){
				BoolExpr[] to_or = {(BoolExpr) this.l.to_smt(ctx, symbolTable ), (BoolExpr) this.r.to_smt(ctx, symbolTable )};
				ret = ctx.MkOr(to_or);
			}
			else if(this.op.equals("implies") || this.op.equals("=>")){
				ret = ctx.MkImplies((BoolExpr)this.l.to_smt(ctx, symbolTable ), (BoolExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else if(this.op.equals(">=")){
				ret = ctx.MkGe((ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else if(this.op.equals(">")){
				ret = ctx.MkGt((ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else if(this.op.equals("<=")){
				ret = ctx.MkLe((ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else if(this.op.equals("<")){
				ret = ctx.MkLt((ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else if(this.op.equals("==")){
				ret = ctx.MkEq((ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else if(this.op.equals("!=")){
				ret = ctx.MkNot(ctx.MkEq((ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable )));
			}
			else if(this.op.equals("+")){
				ArithExpr[] to_add = {(ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable )};
				ret = ctx.MkAdd(to_add);
			}
			else if(this.op.equals("*")){
				ArithExpr[] to_mult = {(ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable )};
				ret = ctx.MkMul(to_mult);
			}
			else if(this.op.equals("-")){
				ArithExpr[] to_sub = {(ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable )};
				ret = ctx.MkSub(to_sub);
			}
			else if(this.op.equals("/")){
				ret = ctx.MkDiv((ArithExpr)this.l.to_smt(ctx, symbolTable ), (ArithExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else if(this.op.equals("<=>")){
				ret = ctx.MkIff((BoolExpr)this.l.to_smt(ctx, symbolTable ), (BoolExpr)this.r.to_smt(ctx, symbolTable ));
			}
			else{
				System.out.println("Operator: "+this.op + " is not supported.");
			}
			
				
		}
		else if(this.r!=null){
			if(this.op.equals("not"))
				ret = ctx.MkNot((BoolExpr)this.r.to_smt(ctx, symbolTable ));
			else if(this.op.equals("-"))
				ret = ctx.MkUnaryMinus((ArithExpr)this.r.to_smt(ctx, symbolTable ));
		}
		
		
		if(this.q!=null && this.q.size()>0){
			
			parameter p;
			Sort[] sorts;
			Symbol[] name;
			
			for(int i=0; i<this.q.size(); i++){
				sorts = new Sort[this.q.get(i).v.size()];
				name = new Symbol[this.q.get(i).v.size()];
				for(int j=0; j<this.q.get(i).v.size(); j++){
					p = this.q.get(i).v.get(j);
					name[j] = ctx.MkSymbol(p.name);
					
					if(p.type.equals("int")){
						sorts[j] = ctx.IntSort();
					}
		        	else if(p.type.equals("double")){
		        		sorts[j] = ctx.RealSort();
		        	}
		        	else if(p.type.equals("boolean")){
		        		sorts[j] = ctx.RealSort();
		        	}
		        	else
		        		System.out.println("Does not support the type inside quantifier!");
				}
				if(this.q.get(i).q.equals("exist"))
					ret = ctx.MkExists(sorts, name, ret, 0, null, null, null, null);
				
				else if(this.q.get(i).q.equals("forall"))
					ret = ctx.MkForall(sorts, name, ret, 0, null, null, null, null);
				
			}	
		}
		return ret;
	}
	
}
