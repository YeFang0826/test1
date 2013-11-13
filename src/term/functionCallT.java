package term;
import functionCall.*;

public class functionCallT extends term{
	public functionCall call;
	public functionCallT (functionCall c){
		this.type = "functionCall";
		this.call = c;
	}
}
