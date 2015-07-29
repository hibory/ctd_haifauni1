package hellobdd;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.text.html.HTMLEditorKit.Parser;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

public class ComputationParser {

	public ComputationParser(String model1, String req1, String model2, String req2){
		
		Attributes = new HashMap<String, FocusAttribute>();
		
		Parser1 = new FocusModelParser(model1, req1, Attributes);
		Parser2 = new FocusModelParser(model2, req2, Attributes);
		BddFactory = AssignVariables();
		BDD MutualVars = GetMutualVars(BddFactory);
		
		Parser1.Define(BddFactory, MutualVars);
		Parser2.Define(BddFactory, MutualVars);
	}
	
	public BDDFactory BddFactory;

	private BDD GetMutualVars(BDDFactory bddFactory) {
		BDD b = bddFactory.one();
		for(String key : Parser1.LocalAttributes.keySet()){
			if(!Parser2.LocalAttributes.containsKey(key)) continue;
			
			FocusAttribute attr = Parser1.LocalAttributes.get(key);
			for(int i : attr.Variables){
				BDD v = bddFactory.ithVar(i);
				b.andWith(v);
			}
		}
		return b;
	}

	Map<String, FocusAttribute> Attributes;
	public FocusModelParser Parser1;
	public FocusModelParser Parser2;
	
	private BDDFactory AssignVariables(){
		
	   int varCount = GetVarCount();
	   BDDFactory bddFactory = myJavaBdd.GetBddFactory(varCount);

	   //for each attribute attach it's BDD variables
	   int i=0;
	   for(FocusAttribute attr : Attributes.values()){
		   int count = attr.VarCount();
		   ArrayList<Integer> vars = new ArrayList<Integer>(); 
		   for(int j=0; j<count; j++){
			   vars.add(i+j);
		   }
		   attr.Variables = vars;
		   attr.BddFactory = bddFactory;
		   i += count;
	   }
	   return bddFactory;
	}
	
	private int GetVarCount() {
		int count = 0;
		for(FocusAttribute attr : Attributes.values()){
			count += attr.VarCount();
		}
		return count;
	}

	
}
