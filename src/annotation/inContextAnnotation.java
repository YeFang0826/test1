package annotation;
import a_expression.*;

public class inContextAnnotation extends annotation{
	public inContextAnnotation(a_expression e){
		this.type = "inContext";
		this.e = e;
	}
}
