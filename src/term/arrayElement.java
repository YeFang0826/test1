package term;

import java.util.ArrayList;

import expression.expression;

public class arrayElement extends term{
	public String aName;
	public ArrayList<expression> index;
	
	public arrayElement(String name, ArrayList<expression> index){
		this.aName = name;
		this.index = index;
		this.type = "arrayElement";
	}
	
}
