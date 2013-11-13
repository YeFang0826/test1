package functionCall;
import java.util.ArrayList;
import java.util.HashMap;

import a_expression.*;
import statement.*;
import expression.*;

public class ud_FunctionCall extends functionCall{
	public String method;
	
	public ud_FunctionCall(String method){
		this.type = "user-defined";
		this.method = method;
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
	
	public ud_FunctionCall convert(){
		if(this.inputs!=null && this.inputs.size()>0){
			ArrayList<a_expression> inputs = new ArrayList<a_expression>();
			for(int i=0; i<this.inputs.size(); i++)
				inputs.add(this.inputs.get(i).convert());
			ud_FunctionCall ret = new ud_FunctionCall(this.method);
			ret.a_inputs = inputs;
			return ret;
		}
		else
			return this;
		
	}
	public ud_FunctionCall replace(String lhs, expression rhs){
		if(this.a_inputs!=null){
			ArrayList<a_expression> a_inputs = new ArrayList<a_expression>();
			for(int i=0; i<this.a_inputs.size(); i++)
				a_inputs.add(this.a_inputs.get(i).replace(lhs, rhs));
			ud_FunctionCall ret =  new ud_FunctionCall(this.method);
			ret.a_inputs = a_inputs;
			return ret;
		}
		return this;
	}
	public ud_FunctionCall replace(HashMap<String, expression> inputExpr){
		if(this.a_inputs!=null){
			ArrayList<a_expression> a_inputs = new ArrayList<a_expression>();
			for(int i=0; i<this.a_inputs.size(); i++)
				a_inputs.add(this.a_inputs.get(i).replace(inputExpr));
			ud_FunctionCall ret =  new ud_FunctionCall(this.method);
			ret.a_inputs = a_inputs;
			return ret;
		}
		return this;
	}
	
	public a_expression prepost_gen(defFunStatement f, boolean isPre){
		a_expression ret = null;
		HashMap<String, expression> inputExpr = new HashMap<String, expression>();
		for(int i=0; i<f.inputs.size(); i++)
			inputExpr.put(f.inputs.get(i).name, this.inputs.get(i));
		if(isPre)
			ret = f.pre.e.replace(inputExpr);
		else
			ret = f.post.e.replace(inputExpr);
		return ret;
	}
}
