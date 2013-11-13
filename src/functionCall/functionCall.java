package functionCall;
import java.util.ArrayList;
import java.util.HashMap;

import expression.expression;
import a_expression.*;

public class functionCall {
	public String type;
	public ArrayList<expression> inputs;
	public ArrayList<a_expression> a_inputs;
	
	public String tostring(){
		if(this.type.equals("arrayOperation"))
			return ((arrayOperation)this).tostring();
		else
			return ((ud_FunctionCall)this).tostring();
	}
	
	public functionCall convert(){
		if(this.type.equals("arrayOperation"))
			return ((arrayOperation)this).convert();
		else
			return ((ud_FunctionCall)this).convert();
	}
	public functionCall replace(String lhs, expression rhs){
		if(this.type.equals("arrayOperation"))
			return ((arrayOperation)this).replace(lhs, rhs);
		else
			return ((ud_FunctionCall)this).replace(lhs, rhs);
	}
	public functionCall replace(HashMap<String, expression> inputExpr){
		if(this.type.equals("arrayOperation"))
			return ((arrayOperation)this).replace(inputExpr);
		else
			return ((ud_FunctionCall)this).replace(inputExpr);
	}
}
