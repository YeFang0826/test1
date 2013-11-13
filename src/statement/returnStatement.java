package statement;

import expression.expression;
import annotation.*;

public class returnStatement extends statement{
	public expression toreturn;
	public annotation a; // function call return 
	
	public returnStatement(){
		this.type = "returnStatement";
	}
	
}
