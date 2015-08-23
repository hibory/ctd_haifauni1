package hellobdd;

import net.sf.javabdd.BDD;

/**
 * Represents a specific model’s executable tests
 *
 */
public class FocusModel {
	public BDD Valid;
	public BDD IncludedVars;
	public BDD MutualVars;
	
	public FocusModel(BDD valid, BDD includedVars, BDD mutualVars){
		Valid = valid;
		IncludedVars = includedVars;
		MutualVars = mutualVars;
	}
}
