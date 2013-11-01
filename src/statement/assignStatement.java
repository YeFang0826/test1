package statement;

import expression.expression;
import annotation.*;
import term.arrayElement;

public class assignStatement extends statement{
	public String name;
	public arrayElement lhs;
	public expression assignment;
	public annotation a;
	
	public assignStatement(){
		this.type = "assignStatement";
		this.a = null;
	}
}
