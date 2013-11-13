package statement;
import expression.*;
import a_expression.*;

public class assumeStatement extends statement{
	public expression e;
	
	public assumeStatement(expression e){
		this.type = "assumeStatement";
		this.e = e;
	}
	
}
