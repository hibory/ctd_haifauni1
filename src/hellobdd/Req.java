package hellobdd;

import java.util.ArrayList;
import java.util.List;

import net.sf.javabdd.BDD;

public class Req implements Comparable<Req> {	
	public BDD Bdd;
	public BDD uncov;
	
	public Req(BDD b){
		Bdd = b;
	}
	
	@Override
	public int compareTo(Req another) {
		if (this.uncov.pathCount() < another.uncov.pathCount()){
            return -1;
        }else{
            return 1;
        }
	}
}
