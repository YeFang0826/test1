package functionCall;
import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.z3.*;

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
	public void annotation_replace(String lhs){
		if(this.type.equals("arrayOperation"))
			((arrayOperation)this).annotation_replace(lhs);
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
	
	public Expr to_smt(Context ctx, HashMap<String, Expr> symbolTable) throws Z3Exception{
		if(this.type.equals("arrayOperation")){
			if(((arrayOperation)this).method.equals("sorted"))
				return ((arrayOperation)this).add_sorted_theorem(ctx, symbolTable);
			else if(((arrayOperation)this).method.equals("partitioned"))
				return ((arrayOperation)this).add_partitioned_theorem(ctx, symbolTable);
			else if(((arrayOperation)this).method.equals("length"))
				return ((arrayOperation)this).array_length(ctx, symbolTable);
			else{
				System.out.println("array operation not supported");
				return null;
			}
		}
		else{
			System.out.println("array operation not supported");
			return null;
		}
	}
}
