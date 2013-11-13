package functionCall;

import java.util.ArrayList;
import java.util.HashMap;

import a_expression.*;

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
	public arrayOperation replace(String lhs, expression rhs){
		if(this.a_inputs!=null){
			ArrayList<a_expression> a_inputs = new ArrayList<a_expression>();
			for(int i=0; i<this.a_inputs.size(); i++)
				a_inputs.add(this.a_inputs.get(i).replace(lhs, rhs));
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
}
