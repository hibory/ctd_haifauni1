package hellobdd;

import net.sf.javabdd.BDD;

/**
 * holds computation result: T1, T2, T21.
 *
 */
public class CompositionData {
	public BDD T1;
	public BDD T2;
	public BDD T21;
	public FocusModel M1;
	public FocusModel M2;
	
	public CompositionData(BDD t1, BDD t2, BDD t21, FocusModel m1, FocusModel m2){
		T1 = t1;
		T2 = t2;
		T21 = t21;
		M1 = m1;
		M2 = m2;
	}
	
	
}
