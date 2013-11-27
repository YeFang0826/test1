package statement;
import java.util.ArrayList;
import java.util.HashMap;

import annotation.*;

import expression.expression;
import statement.*;

public class whileStatement extends statement{
	public expression guard;
	public ArrayList<statement> body;
	public loopInvariant invariant;
	public ArrayList<ArrayList<statement>> basicpath;
	
	public whileStatement(){
		this.type = "whileStatement";
		this.basicpath = new ArrayList<ArrayList<statement>>();
	}
	public ArrayList<ArrayList<statement>> bp(HashMap<String, defFunStatement> functions){
		if(this.invariant!=null){
			this.invariant.e.annotation_process();
			//@L
			ArrayList<statement> path = new ArrayList<statement>();
			path.add(new annotationStatement(this.invariant.e, "invariant"));
			path.add(new cut());
			this.basicpath.add(path);
			
			// @L + guard + loop body + @L
			path = new ArrayList<statement>();
			path.add(new annotationStatement(this.invariant.e, "invariant"));
			path.add(new assumeStatement(this.guard));
			statement s;
			ArrayList<ArrayList<statement>> pathRet;
			ArrayList<ArrayList<statement>> pathlist = null;
			boolean startAnotherPath = false;
			boolean hasIf = false;
			
			for(int i=0; i<this.body.size(); i++){
				
				s=this.body.get(i); 
				
				if(s.type.equals("whileStatement")){
					
					pathRet = ((whileStatement)s).bp(functions);
					if(!hasIf){
						if(!startAnotherPath){
							for(int j=0; j<pathRet.size()-1; j++){
								if(j==0){ // add init + loop invariant
									for(int k=0; k<pathRet.get(j).size(); k++)
										path.add(pathRet.get(j).get(k));
									this.basicpath.add(path);
								}
								else
									this.basicpath.add(pathRet.get(j));
							}
						}
						else{ 
							for(int j=0; j<pathRet.size()-1; j++)
								this.basicpath.add(pathRet.get(j));
						}
						path = pathRet.get(pathRet.size()-1);
						startAnotherPath = false;
					}
					else{
						for(int j=0; j<pathlist.size(); j++){
							if(!pathlist.get(j).get(pathlist.get(j).size()-1).type.equals("cut")){
								for(int k=0; k<pathRet.get(0).size(); k++)
									pathlist.get(j).add(pathRet.get(0).get(k));
							}
							this.basicpath.add(pathlist.get(j));
						}
						for(int j=0; j<pathRet.size()-1; j++)
							this.basicpath.add(pathRet.get(j));
						path = pathRet.get(pathRet.size()-1);
						startAnotherPath = false;
						hasIf = false;
					}
					
				}
				else if(s.type.equals("forStatement")){
					
					pathRet = ((forStatement)s).bp(functions);
					if(!hasIf){
						if(!startAnotherPath){
							for(int j=0; j<pathRet.size()-1; j++){
								if(j==0){ // add init + loop invariant
									for(int k=0; k<pathRet.get(j).size(); k++)
										path.add(pathRet.get(j).get(k));
									this.basicpath.add(path);
								}
								else
									this.basicpath.add(pathRet.get(j));
							}
						}
						else{ 
							for(int j=0; j<pathRet.size()-1; j++)
								this.basicpath.add(pathRet.get(j));
						}
						path = pathRet.get(pathRet.size()-1);
						startAnotherPath = false;
					}
					else{
						for(int j=0; j<pathlist.size(); j++){
							if(!pathlist.get(j).get(pathlist.get(j).size()-1).type.equals("cut")){
								for(int k=0; k<pathRet.get(0).size(); k++)
									pathlist.get(j).add(pathRet.get(0).get(k));
							}
							this.basicpath.add(pathlist.get(j));
						}
						for(int j=0; j<pathRet.size()-1; j++)
							this.basicpath.add(pathRet.get(j));
						path = pathRet.get(pathRet.size()-1);
						startAnotherPath = false;
						hasIf = false;
					}
				}
				else if(s.type.equals("ifStatement")){
					pathRet= ((ifStatement)s).bp(functions);
					if(!hasIf){
						hasIf = true;
						ArrayList<statement> temppath;
						pathlist = new ArrayList<ArrayList<statement>>();
						if(!startAnotherPath){
							for(int j=0; j<pathRet.size(); j++){ 
								temppath = new ArrayList<statement>();
								for(int k=0; k<path.size(); k++)
									temppath.add(path.get(k));
								for(int k=0; k<pathRet.get(j).size(); k++)
									temppath.add(pathRet.get(j).get(k));
								pathlist.add(temppath);
							}
						}
						else{
							pathlist = pathRet;
							startAnotherPath = false;
						}
					}
					else{
						ArrayList<statement> temppath;
						pathlist = new ArrayList<ArrayList<statement>>();
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
						}
						pathlist = temppathlist;
						startAnotherPath = false;
					}
				}
				else if(s.type.equals("defVarStatement") || s.type.equals("assignStatement")){
					if(!hasIf){
						if(!startAnotherPath)
							path.add(s);
						else{
							path = new ArrayList<statement>();
							path.add(s);
							startAnotherPath = false;
						}
					}
					else{
						for(int j=0; j<pathlist.size(); j++){
							if(!pathlist.get(j).get(pathlist.get(j).size()-1).type.equals("cut"))
								pathlist.get(j).add(s);
						}
						startAnotherPath = false;
					}
					
				}
				else{
					System.out.println("statement type is not supported");
				}
			}
			
			if(!hasIf || pathlist == null)
				this.basicpath.add(path);
			else{
				for(int i=0; i<pathlist.size(); i++)
					this.basicpath.add(pathlist.get(i));
			}
			for(int i=1; i<this.basicpath.size(); i++){
				if(!this.basicpath.get(i).get(this.basicpath.get(i).size()-1).type.equals("cut")){
					this.basicpath.get(i).add(new annotationStatement(this.invariant.e, "invariant"));
					this.basicpath.get(i).add(new cut());
				}
			}
			
			// @L + not guard = last iteration
			path = new ArrayList<statement>();
			path.add(new annotationStatement(this.invariant.e, "invariant"));
			path.add(new assumeStatement(new expression(this.guard, "not")));
			this.basicpath.add(path);
			
		}
		
		
					
					
		return this.basicpath;
	}
}
