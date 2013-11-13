package a_term;

import java.util.ArrayList;
import java.util.HashMap;

import expression.expression;

import a_expression.*;

public class a_arrayElement extends a_term{
	public String aName;
	public ArrayList<a_expression> index;
	
	public String tostring(){
		String ret="";
		ret = this.aName;
		for(int i=0; i<this.index.size(); i++)
			ret = ret + '['+ this.index.get(i).tostring() + "]";
		return ret;
	}
	public a_arrayElement(String name, ArrayList<a_expression> index){
		this.aName = name;
		this.index = index;
		this.type = "arrayElement";
	}
	
	public a_arrayElement replace(String lhs, expression rhs){
		ArrayList<a_expression> index = new ArrayList<a_expression>();
		for(int i=0; i<this.index.size(); i++)
			index.add(this.index.get(i).replace(lhs,rhs));
		return new a_arrayElement(this.aName, index);
	}
	public a_arrayElement replace(HashMap<String, expression> inputExpr){
		ArrayList<a_expression> index = new ArrayList<a_expression>();
		for(int i=0; i<this.index.size(); i++)
			index.add(this.index.get(i).replace(inputExpr));
		return new a_arrayElement(this.aName, index);
	}
	
}
