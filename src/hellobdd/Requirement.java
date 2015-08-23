package hellobdd;

import net.sf.javabdd.BDD;

/**
 * holds a BDD for a single requirements, and its uncovered tests (called uncov)
 *
 */
public class Requirement implements Comparable<Requirement> {	
	public BDD Bdd;
	public BDD uncov;
	public BDD excludeVars;
	
	public Requirement(BDD b){
		Bdd = b;
	}
	
	@Override
	public int compareTo(Requirement another) {
		if (this.uncov.pathCount() < another.uncov.pathCount()){
            return -1;
        }
		else if (this.uncov.pathCount() > another.uncov.pathCount()){
            return 1;
        }
		
		return 0;
	}

	public BDD Projection(FocusModel m) {
		BDD excludeVars = m.IncludedVars.restrict(Bdd); //this all variables except those in t
		BDD proj = m.Valid.exist(excludeVars); //uncov(t) = Projt(E)
		proj.andWith(m.Valid);
		
		return proj;
	}
}
