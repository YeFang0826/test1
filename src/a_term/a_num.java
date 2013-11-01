package a_term;

public class a_num extends a_term{
	public double n;
	public a_num(String n){
		this.n = Double.parseDouble(n);
		this.type = "number";
	}
}
