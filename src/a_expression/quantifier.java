package a_expression;

import java.util.ArrayList;
import parameter.parameter;

public class quantifier {
	public String q;
	public ArrayList<parameter> v;
	
	public quantifier(String q){
		this.q = q;
		this.v = new ArrayList<parameter>();
	}
	public String tostring(){
		String ret ="";
		ret = ret + this.q+"(";
		for(int i=0; i<this.v.size(); i++){
			if(i==0)
				ret = ret + this.v.get(i).name;
			else 
				ret = ret + "," + this.v.get(i).name;
		}
		ret = ret + "), ";
		return ret;
	}
}
