package statement;
import java.util.ArrayList;
import annotation.*;
import expression.expression;

public class ifStatement extends statement{

	public ArrayList<expression> conditions;
	public ArrayList<statement> exe;
	public annotation a;
	
	public ifStatement(){
		this.conditions = new ArrayList<expression>();
		this.exe = new ArrayList<statement>();
		this.type = "ifStatement";
	}
	
}
