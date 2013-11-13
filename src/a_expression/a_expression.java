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
	
	public a_expression replace(String lhs, expression rhs){
		a_expression ret = null;
		if(this.leaf!=null){
			if(this.leaf.type.equals("boolean") ||this.leaf.type.equals("number"))
				ret = this;
			else if(this.leaf.type.equals("string")){
				if (((a_string)this.leaf).s.equals(lhs)){
					ret = new a_expression(new a_expressionT(rhs.convert()));
					System.out.println("Replace:" + lhs);
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
					System.out.println("Replace:" + ((a_string)this.leaf).s);
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
		return ret;
	}
}
