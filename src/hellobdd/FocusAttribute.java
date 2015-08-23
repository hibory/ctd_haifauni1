package hellobdd;

import java.util.ArrayList;
import java.util.Collections;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

/**
 * Represents an attribute within a model, and its possible values.
  It also holds the BDD variables attached to each value, to allow multi-value support with BDD.
 *
 */
public class FocusAttribute {
	public String Name;
	public ArrayList<String> Values;
	public ArrayList<Integer> Variables;
	public BDDFactory BddFactory;
	
	public FocusAttribute(String name){
		Name = name;
		Values = new ArrayList<String>();
	}
	
	public int VarCount(){
		double u = log(Values.size(),2);
		return (int) Math.ceil(u);
	}
	
	public void CovertToBinary(){
		
		for(int i=0; i<Values.size(); i++){
			
			String binary = Integer.toBinaryString(i);
			
			int mm = 33;
		}
	
	}
	
	static double log(int x, int base)
	{
	    return (double) (Math.log(x) / Math.log(base));
	}

	public BDD GetBdd(String value) {
		int index = Values.indexOf(value);
		BDD bdd = IndexToBdd(index);
		return bdd;
	}

	private BDD IndexToBdd(int index) {
		String binary = Integer.toBinaryString(index);
		
		int j=0;
		BDD bdd = BddFactory.one();
		
		//assign true/false values to variables 
		//according to binary value
		for (int i = (binary.length()-1); i >= 0; i--) {
		    char character = binary.charAt(i); 
		    
		    BDD v = BddFactory.ithVar(Variables.get(j));
		    if(character  == '1'){
		    	bdd = bdd.and(v);
		    }
		    else
		    {
		    	bdd = bdd.and(v.not());
		    }
		    j++;
		}
		
		//assign remaining variables to false
		for(int i=j; i<Variables.size(); i++){
			BDD v = BddFactory.ithVar(Variables.get(i));
			bdd = bdd.and(v.not());
		}
		
		return bdd;
	}

	public BDD GetVarSet() {
		BDD b = BddFactory.one();
		for(int i : Variables){
			BDD v = BddFactory.ithVar(i);
			b.andWith(v);
		}
		return b;
	}

	public String GetValue(BDD bdd) {
		
		String binary="";
		for(int i : Variables){
			BDD v = bdd.getFactory().ithVar(i);
			if(bdd.and(v).isZero()){
				binary="0" + binary;
			}
			else
				binary="1" + binary;
			
		}
		
		int inttt = Integer.parseInt(binary, 2);
		return Values.get(inttt);
	}
	
	public BDD GetIlligalBdd(){
		int n = VarCount();
		int max = (int) Math.pow(2, n);
		
		BDD res = BddFactory.zero();
		for(int i=Values.size(); i<max; i++){
			BDD b = IndexToBdd(i);
			res.orWith(b);
		}
		return res;
	}

}
