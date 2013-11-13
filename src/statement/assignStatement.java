package statement;

import java.util.ArrayList;

import expression.expression;
import annotation.*;
import term.arrayElement;

public class assignStatement extends statement{
	public String name;
	public arrayElement lhs;
	public expression assignment;
	public annotation a; // assert
	public ArrayList<statement> basicpath;
	
	public assignStatement(){
		this.type = "assignStatement";
		this.a = null;
	}
	
}
