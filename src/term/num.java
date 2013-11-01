package term;

public class num extends term {
	public double n;
	public num(String n){
		this.type = "number";
		this.n = Double.parseDouble(n);
	}
	
}
