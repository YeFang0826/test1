package a_term;
import functionCall.*;
import expression.*;

public class a_functionCallT extends a_term{
	public functionCall call;
	public a_functionCallT(functionCall c){
		this.call = c;
		this.type = "functionCall";
	}
}
