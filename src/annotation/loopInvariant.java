package annotation;
import a_expression.*;
public class loopInvariant extends annotation{
	
	public loopInvariant(a_expression e){
		this.type = "loopInvariant";
		this.e = e;
	}
}
