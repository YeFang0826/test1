package statement;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.*;
import expression.expression;
import functionCall.*;
import statement.*;
import term.*;
public class ifStatement extends statement{

	public ArrayList<expression> conditions;
	public ArrayList<ArrayList<statement>> exe;
	public annotation a; // assert
	public ArrayList<ArrayList<statement>> basicpath;
	
	public ifStatement(){
		this.conditions = new ArrayList<expression>();
		this.exe = new ArrayList<ArrayList<statement>>();
		this.type = "ifStatement";
		this.basicpath = new ArrayList<ArrayList<statement>>();
	}
	// assume no loop inside ifStatement
	public ArrayList<ArrayList<statement>> bp(HashMap<String, defFunStatement> functions){
		
		expression e1 = new expression(this.conditions.get(0), "not");
		expression e2;
		for(int i=1; i<this.conditions.size(); i++){
			e2 = new expression(e1,new expression(this.conditions.get(i), "not"), "and");
			e1 = e2;
		}
		this.conditions.add(e1);
		
		
		statement s;
		ArrayList<statement> path;
		ArrayList<ArrayList<statement>> pathlist = null;
		ArrayList<ArrayList<statement>> pathRet;
		boolean hasIf;
		
		for(int i=0; i<this.conditions.size(); i++){
			
			path = new ArrayList<statement>();
			path.add(new assumeStatement(this.conditions.get(i)));
			hasIf = false;
			
			
			if(i<this.exe.size()){
				for(int j=0; j<this.exe.get(i).size(); j++){
					s = this.exe.get(i).get(j);
					
					if(s.type.equals("ifStatement")){
						pathRet = ((ifStatement)s).bp(functions);
						ArrayList<statement> temppath;
						if(!hasIf){
							hasIf = true;
							pathlist = new ArrayList<ArrayList<statement>>();
							for(int k=0; k<pathRet.size(); k++){ 
								temppath = new ArrayList<statement>();
								for(int m=0; m<path.size(); m++)
									temppath.add(path.get(m));
								for(int m=0; m<pathRet.get(k).size(); m++)
									temppath.add(pathRet.get(k).get(m));
								pathlist.add(temppath);
							}
						}
						else{
							ArrayList<ArrayList<statement>> temppathlist = new ArrayList<ArrayList<statement>>();
							for(int k=0; k<pathlist.size(); k++){
								if(!pathlist.get(k).get(pathlist.get(k).size()-1).type.equals("cut")){
									for(int l=0; l<pathRet.size(); l++){
										temppath = new ArrayList<statement>();
										for(int m=0; m<pathlist.get(k).size(); m++)
											temppath.add(pathlist.get(k).get(m));
										for(int m=0; m<pathRet.get(l).size(); m++)
											temppath.add(pathlist.get(l).get(m));
										temppathlist.add(temppath);
									}
								}
								else{
									temppathlist.add(pathlist.get(k));
								}
							}
							pathlist = temppathlist;
						}
					}
					else if(s.type.equals("defVarStatement") || s.type.equals("assignStatement")){
						if(!hasIf){
							path.add(s);
						}
						else{
							for(int k=0; k<pathlist.size();k++){
								if(!pathlist.get(k).get(pathlist.get(k).size()-1).type.equals("cut"))
									pathlist.get(k).add(s);
							}
						}
					}
					else if(s.type.equals("returnStatement")){
						
						if(((returnStatement)s).toreturn.leaf!= null && ((returnStatement)s).toreturn.leaf.type.equals("functionCall")
								&& ((functionCallT)(((returnStatement)s).toreturn.leaf)).call.type.equals("user-defined")
								&& functions.containsKey(((ud_FunctionCall)((functionCallT)(((returnStatement)s).toreturn.leaf)).call).method)){
							
							functionCall call = ((functionCallT)(((returnStatement)s).toreturn.leaf)).call;
							defFunStatement f = functions.get(((ud_FunctionCall)call).method);
							ArrayList<statement> temppath;
							
							if(!hasIf){
								hasIf = true;
								pathlist = new ArrayList<ArrayList<statement>>();
								temppath = new ArrayList<statement>();
								for(int k=0; k<path.size(); k++)
									temppath.add(path.get(k));
								temppath.add(new annotationStatement(((ud_FunctionCall)call).prepost_gen(f, true), "pre"));
								temppath.add(new cut());
								pathlist.add(temppath);
								
								temppath = new ArrayList<statement>();
								for(int k=0; k<path.size(); k++)
									temppath.add(path.get(k));
								temppath.add(new assumeStatement(((ud_FunctionCall)call).prepost_gen(f, false)));
								pathlist.add(temppath);
							}
							else{
								ArrayList<ArrayList<statement>> temppathlist = new ArrayList<ArrayList<statement>>();
								for(int k=0; k<pathlist.size(); k++){
									if(!pathlist.get(k).get(pathlist.get(k).size()-1).type.equals("cut")){
										temppath = new ArrayList<statement>();
										for(int m=0; m<pathlist.get(k).size(); m++)
											temppath.add(pathlist.get(k).get(m));
										temppath.add(new annotationStatement(((ud_FunctionCall)call).prepost_gen(f, true), "pre"));
										temppath.add(new cut());
										temppathlist.add(temppath);
										
										temppath = new ArrayList<statement>();
										for(int m=0; m<pathlist.get(k).size(); m++)
											temppath.add(pathlist.get(k).get(m));
										temppath.add(new assumeStatement(((ud_FunctionCall)call).prepost_gen(f, false)));
										temppathlist.add(temppath);
									}
									else{
										temppathlist.add(pathlist.get(k));
									}
								}
								pathlist = temppathlist;
							}
							
						}
						
						else{
							if(!hasIf){
								path.add(s);
								path.add(new cut()); // return a value
							}
							else{
								for(int k=0; k<pathlist.size();k++){
									if(!pathlist.get(k).get(pathlist.get(k).size()-1).type.equals("cut")){
										pathlist.get(k).add(s);
										pathlist.get(k).add(new cut()); // return a value
										
									}
								}
							}
						}
					}
					else{
						System.out.println("statement type is not supported"); // no while/for loop inside ifstatement
					}
				}
			}
			
			if(!hasIf)
				this.basicpath.add(path);
			else{
				for(int j=0; j<pathlist.size(); j++)
					this.basicpath.add(pathlist.get(j));
			}
		}
		return this.basicpath;
	}
}	
	
