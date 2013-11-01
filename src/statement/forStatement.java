package statement;
import java.util.ArrayList;

import annotation.*;
import expression.*;

public class forStatement extends statement{
	
	public String iterator;
	public expression begin;
	public expression increment;
	public expression guard;
	public ArrayList<statement> body;
	public annotation invariant;
	
	public forStatement(){
		this.type = "forStatement";
		this.invariant = null;
	}
}
