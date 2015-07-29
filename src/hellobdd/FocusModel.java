package hellobdd;

import net.sf.javabdd.BDD;

public class FocusModel {
	public BDD Valid;
	public BDD IncludedVars;
	public BDD MutualVars;
	
	public FocusModel(BDD valid, BDD inc, BDD mutual){
		Valid = valid;
		IncludedVars = inc;
		MutualVars = mutual;
	}
}
