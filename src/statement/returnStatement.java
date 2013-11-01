package statement;

import expression.expression;

public class returnStatement extends statement{
	public expression toreturn;
	
	public returnStatement(){
		this.type = "returnStatement";
	}
	
}
