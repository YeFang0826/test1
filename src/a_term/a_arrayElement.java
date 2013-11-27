package a_term;

import java.util.ArrayList;
import java.util.HashMap;

import com.microsoft.z3.*;

import expression.expression;

import a_expression.*;

public class a_arrayElement extends a_term{
	//only support one dimensional array;
	public String aName;
	public ArrayList<a_expression> index;
	
	public String tostring(){
		String ret="";
		ret = this.aName;
		for(int i=0; i<this.index.size(); i++)
			ret = ret + '['+ this.index.get(i).tostring() + "]";
		return ret;
	}
	public a_arrayElement(String name, ArrayList<a_expression> index){
		this.aName = name;
		this.index = index;
		this.type = "arrayElement";
	}
	public void annotation_replace(String lhs){
		for(int i=0; i<this.index.size(); i++)
			this.index.get(i).annotation_replace(lhs);
	}
	public a_arrayElement replace(String lhs, expression rhs){
		ArrayList<a_expression> index = new ArrayList<a_expression>();
		for(int i=0; i<this.index.size(); i++)
			index.add(this.index.get(i).replace(lhs,rhs));
		return new a_arrayElement(this.aName, index);
	}
	public a_arrayElement replace(HashMap<String, expression> inputExpr){
		ArrayList<a_expression> index = new ArrayList<a_expression>();
		for(int i=0; i<this.index.size(); i++)
			index.add(this.index.get(i).replace(inputExpr));
		return new a_arrayElement(this.aName, index);
	}
	public Expr to_smt(Context ctx, HashMap<String, Expr> symbolTable) throws Z3Exception{
		Expr a = null;
		Expr ret = null;
		Expr index = this.index.get(0).to_smt(ctx, symbolTable);
		
		if(symbolTable.containsKey(this.aName))
			a = symbolTable.get(this.aName);
		if(a!= null)	
			ret = ctx.MkSelect((ArrayExpr)a, index); 
		return ret;
	}
	
}
