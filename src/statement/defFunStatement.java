package statement;

import java.util.ArrayList;
import annotation.*;
import parameter.parameter;
import expression.expression;

public class defFunStatement extends statement{
	public String name;
	public ArrayList<parameter> inputs;
	public ArrayList<statement> body;
	public annotation pre;
	public annotation post;
	
	
	public defFunStatement(){
		this.type = "defFunStatement";
		this.body = new ArrayList<statement>();
		this.pre = null;
		this.post = null;
	}
}
