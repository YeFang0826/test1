package term;
import expression.expression;
public class expressionT extends term{
	public expression e;
	public expressionT(expression e){
		this.type = "expression";
		this.e = e;
	}
}
