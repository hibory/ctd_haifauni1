package hellobdd;

import java.util.ArrayList;
import java.util.List;

import net.sf.javabdd.BDD;

public class Req implements Comparable<Req> {	
	public BDD Bdd;
	public BDD uncov;
	public BDD excludeVars;
	
	public Req(BDD b){
		Bdd = b;
	}
	
	@Override
	public int compareTo(Req another) {
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
