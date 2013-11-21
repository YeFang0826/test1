package a_expression;

import java.util.ArrayList;

import expression.expression;

import a_expression.*;

public class arrayModification{
	public static String type = "arrayModification";
	public String a; // array ID
	public ArrayList<a_expression> index;
	public a_expression newvalue;
	
	public arrayModification(String a, ArrayList<expression> index, expression newvalue){
		this.a = a;
		this.index = new ArrayList<a_expression>();
		for(int i=0; i<index.size(); i++)
			this.index.add(index.get(i).convert());
		this.newvalue = newvalue.convert();
	}
	public arrayModification(String a, ArrayList<a_expression> index, a_expression newvalue){
		this.a = a;
		this.index= index;
		this.newvalue = newvalue;
	}
	public void replace(String lhs, expression rhs){
		ArrayList<a_expression> index = new ArrayList<a_expression>();
		for(int i=0; i<this.index.size(); i++)
			index.add(this.index.get(i).replace(lhs, rhs));
		this.index = index;
		this.newvalue = this.newvalue.replace(lhs, rhs);
	}
	public String toString(){
		String ret = this.a + "--> <";
		if(this.index!=null){
			for(int i=0; i<this.index.size(); i++)
				if(i==0)
					ret = ret + this.index.get(i).tostring();
				else
					ret = ret +","+ this.index.get(i).tostring();
		}
		ret = ret + " = "+ this.newvalue.tostring()+">";
		return ret;
	}
	
}
