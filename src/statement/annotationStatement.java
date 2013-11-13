package statement;
import a_expression.*;
public class annotationStatement extends statement{
	public a_expression a;
	public String a_type;
	
	public annotationStatement(a_expression a, String a_type){
		this.a = a;
		this.type = "annotationStatement";
		this.a_type = a_type;
	}
}
