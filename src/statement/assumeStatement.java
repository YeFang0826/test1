package statement;
import expression.*;
import a_expression.*;

public class assumeStatement extends statement{
	public expression e;
	public a_expression a_e;
	
	public assumeStatement(expression e){
		this.type = "assumeStatement";
		this.e = e;
	}
	
	public assumeStatement(a_expression a_e){
		this.type = "assumeStatement";
		this.a_e = a_e;
	}
	
}
