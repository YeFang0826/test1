package term;

import java.util.ArrayList;

import expression.expression;
import a_expression.*;
import a_term.*;

public class arrayElement extends term{
	public String aName;
	public ArrayList<expression> index;
	
	public arrayElement(String name, ArrayList<expression> index){
		this.aName = name;
		this.index = index;
		this.type = "arrayElement";
	}
	public a_arrayElement convert(){
		ArrayList<a_expression> index = new ArrayList<a_expression>();
		for(int i=0; i<this.index.size(); i++){
			index.add(this.index.get(i).convert());
		}
		return new a_arrayElement(this.aName, index);
	}
	
}
