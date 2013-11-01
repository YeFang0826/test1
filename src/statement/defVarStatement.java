package statement;
import annotation.*;
import expression.expression;

public class defVarStatement extends statement{
	public String vtype;
	public String name;
	public expression assignment;
	public annotation a;
	
	public defVarStatement(){
		this.type = "defVarStatement";
		this.a = null;
	}
}
