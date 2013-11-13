package statement;
import java.util.ArrayList;

import annotation.*;
import expression.expression;

public class defVarStatement extends statement{
	public String vtype;
	public String name;
	public expression assignment;
	public annotation a;
	public ArrayList<statement> basicpath;
	
	public defVarStatement(){
		this.type = "defVarStatement";
		this.a = null;
		this.basicpath = new ArrayList<statement>();
	}
	
}
