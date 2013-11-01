package a_term;

import java.util.ArrayList;

import expression.expression;

import a_expression.*;

public class a_arrayElement extends a_term{
	public String aName;
	public ArrayList<a_expression> index;
	
	public a_arrayElement(String name, ArrayList<a_expression> index){
		this.aName = name;
		this.index = index;
		this.index = new ArrayList<a_expression>();
	}
	
}
