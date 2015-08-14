/**
 * 
 */
package hellobdd;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import org.junit.Test;

/**
 * @author amirshwa
 *
 */
public class TestComputation {

	/*@Test
	public void test() {
		fail("Not yet implemented");
	}*/

	/**
	 * E1,E2 = null
	 */
	@Test(expected = IllegalArgumentException.class)  
	public void TestInvalid1(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		
		FocusModel e1 = null , e2 = null;
		List<Req> r1 = ValidReq(1,bddFactory), r2 = ValidReq(2,bddFactory);
		
		javaBdd.Computation(e1, r1, e2, r2);
	}

	@Test(expected = IllegalArgumentException.class)  
	public void TestInvalid2(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = null, r2 = null;
		
		javaBdd.Computation(e1, r1, e2, r2);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void TestInvalid3(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = null, e2 = ValidModel(2,bddFactory);
		List<Req> r1 = null, r2 = ValidReq(2,bddFactory);
		
		javaBdd.Computation(e1, r1, e2, r2);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void TestInvalid4(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = null;
		List<Req> r1 = ValidReq(1,bddFactory), r2 = null;
		
		javaBdd.Computation(e1, r1, e2, r2);
	}
	
	@SuppressWarnings("serial")
	private List<Req> ValidReq(int id, BDDFactory bddFactory) {
		final BDD a1 = bddFactory.ithVar(0);
		final BDD a2 = bddFactory.ithVar(1);
		final BDD b1 = bddFactory.ithVar(2);
		final BDD b2 = bddFactory.ithVar(3);
		final BDD c1 = bddFactory.ithVar(4);
		final BDD c2 = bddFactory.ithVar(5);
		
		switch(id){
		case 1:
			return new ArrayList<Req>(){{ 
				add(new Req(a1.and(b1)));
				add(new Req(a1.and(b2)));
				}};
		case 2:
			return new ArrayList<Req>(){{
				add(new Req(b1.and(c1)));
				add(new Req(b2.and(c1)));
			}};
		}
		
		return new ArrayList<Req>();
	}
	
	private FocusModel ValidModel(int id, BDDFactory bddFactory) {
		
		BDD a1 = bddFactory.ithVar(0);
		BDD a2 = bddFactory.ithVar(1);
		BDD b1 = bddFactory.ithVar(2);
		BDD b2 = bddFactory.ithVar(3);
		BDD c1 = bddFactory.ithVar(4);
		BDD c2 = bddFactory.ithVar(5);
		BDD Valid,Included,Mutual;
		
		switch(id){
		case 1:
			Valid = a1.and(b1);
			Included = a1.and(a2).and(b1).and(b2);
			Mutual = b1.and(b2);
			return new FocusModel(Valid, Included, Mutual);
		case 2:
			Valid = b1.and(c1);
			Included = b1.and(b2).and(c1).and(c2);
			Mutual = b1.and(b2);
			return new FocusModel(Valid, Included, Mutual);
		}
		
		return null;
	}
	
	/**
	 * In dummy BDD we use 6 variables : {a1,a2,b1,b2,c1,c2}
	 * model-1 contains {a1,a2,b1,b2} and model-2 contains {b1,b2,c1,c2}
	 * {b1,b2} are the mutual variables between two models
	 * working with 4 variables in each model allows to specify Requirements 
	 * such that some variables can be ignored.
	 * For Example: if Requirements = {a1 AND b1} , so requirements ignores variables a2,b2
	 *   
	 * @param javaBdd CTD Composition class 
	 * @return BDD factory which defines 6 variables
	 */
	private BDDFactory DummyBdd(myJavaBdd javaBdd) {
		int varCount = 6;
	    BDDFactory bddFactory = myJavaBdd.GetBddFactory(varCount);

	    return bddFactory;
	}
	
}
