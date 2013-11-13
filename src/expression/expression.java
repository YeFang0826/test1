package expression;

import term.*;
import a_expression.*;
import a_term.*;

public class expression {
	public expression l;
	public expression r;
	public String op;
	public term leaf;
	
	public expression(){
		
	}
	public expression(expression l, expression r, String op){
		this.l = l;
		this.r = r;
		this.op = op;
	}
	public expression(expression r, String op){
		this.r = r;
		this.op = op;
	}
	public expression(term leaf){
		this.leaf = leaf;
	}
	public a_expression convert(){
		a_expression ret = null;
		if(this.leaf!=null){
			if(this.leaf.type.equals("boolean"))
				ret = new a_expression(new a_bool(((bool)this.leaf).b));
			else if(this.leaf.type.equals("string"))
				ret = new a_expression(new a_string(((string)this.leaf).s));
			else if(this.leaf.type.equals("number"))
				ret = new a_expression(new a_num(Double.toString(((num)this.leaf).n)));
			else if(this.leaf.type.equals("expression"))
				ret = new a_expression(new a_expressionT(((expressionT)this.leaf).e.convert()));
			else if(this.leaf.type.equals("arrayElement"))
				ret = new a_expression(((arrayElement)this.leaf).convert());
			else if(this.leaf.type.equals("functionCall"))
				ret = new a_expression(new a_functionCallT(((functionCallT)this.leaf).call.convert()));
		}
		else if(this.l!=null && this.r!=null)
			ret = new a_expression(this.l.convert(), this.r.convert(), this.op);
		else if(this.r!=null)
			ret = new a_expression(this.r.convert(), this.op);
		
		return ret;
	}
}
