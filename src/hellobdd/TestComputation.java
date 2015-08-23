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
		List<Req> r1 = ValidReqTwo(1,bddFactory), r2 = ValidReqTwo(2,bddFactory);
		
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
		List<Req> r1 = null, r2 = ValidReqTwo(2,bddFactory);
		
		javaBdd.Computation(e1, r1, e2, r2);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void TestInvalid4(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = null;
		List<Req> r1 = ValidReqTwo(1,bddFactory), r2 = null;
		
		javaBdd.Computation(e1, r1, e2, r2);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void TestInvalid5(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = ValidReqTwo(2,bddFactory), r2 = ValidReqTwo(2,bddFactory);
		
		javaBdd.Computation(e1, r1, e2, r2);
	}
	
	@Test(expected = IllegalArgumentException.class)  
	public void TestInvalid6(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = ValidReqTwo(1,bddFactory), r2 = ValidReqTwo(1,bddFactory);
		
		javaBdd.Computation(e1, r1, e2, r2);
	}
	
	@Test  
	public void TestBasic1(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = ValidReqTwo(1,bddFactory), r2 = ValidReqTwo(2,bddFactory);
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		
		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);


		assertEquals(2, t21Sat);
		assertEquals(2, t1Sat);
		assertEquals(2, t2Sat);
	}
	
	@Test  
	public void TestBasic2(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = ValidReqAll(1,bddFactory), r2 = ValidReqAll(2,bddFactory);
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		
		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);


		assertEquals(4, t21Sat);
		assertEquals(4, t1Sat);
		assertEquals(4, t2Sat);
	}
	
	@Test
	public void TestBasic3(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = ValidReqTwo(1,bddFactory), r2 = ValidReqAll(2,bddFactory);
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);

		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);

		assertEquals(4, t21Sat);
		assertEquals(2, t1Sat);
		assertEquals(4, t2Sat);
	}
	
	@Test
	public void TestBasic4(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = ValidReqTwo(1,bddFactory), r2 = ValidReqAll(2,bddFactory);
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);

		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);

		assertEquals(4, t21Sat);
		assertEquals(2, t1Sat);
		assertEquals(4, t2Sat);
	}
	
	@Test
	public void TestEdge1(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = new ArrayList<Req>(), r2 = new ArrayList<Req>();
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		
		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);

		assertEquals(0, t21Sat);
		assertEquals(0, t1Sat);
		assertEquals(0, t2Sat);
	}
	
	@Test
	public void TestEdge2(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = new ArrayList<Req>(), r2 = ValidReqAll(2,bddFactory);
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		
		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);

		assertEquals(4, t21Sat);
		assertEquals(0, t1Sat);
		assertEquals(4, t2Sat);
	}
	
	@Test
	public void TestEdge3(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(2,bddFactory);
		List<Req> r1 = ValidReqAll(1,bddFactory), r2 = ValidReqAll(2,bddFactory);
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		e1.Valid = bddFactory.zero();
		e2.Valid = bddFactory.zero();
		
		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);

		assertEquals(0, t21Sat);
		assertEquals(0, t1Sat);
		assertEquals(0, t2Sat);
	}
	
	@Test
	public void TestEdge4(){
		myJavaBdd javaBdd = new myJavaBdd();
		BDDFactory bddFactory = DummyBdd(javaBdd);
		FocusModel e1 = ValidModel(1,bddFactory), e2 = ValidModel(1,bddFactory);
		List<Req> r1 = ValidReqAll(1,bddFactory), r2 = ValidReqAll(1,bddFactory);
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		
		CompositionData compData = javaBdd.Computation(e1, r1, e2, r2);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(e1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(e2.IncludedVars);

		assertEquals(3, t21Sat);
		assertEquals(4, t1Sat);
		assertEquals(4, t2Sat);
	}
	
	@Test
	public void TestHuge1(){
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story5\\";
		String allReq = dir + "all.req.xml";
		
		String model1 = dir + "ls.modelM.model";
		String req1 = allReq;
		
		String model2 = dir + "grep.modelM.model";
		String req2 = allReq;
		
		myJavaBdd javaBdd = new myJavaBdd();
		CompositionData compData = javaBdd.RunParser(model1,req1,model2,req2);
		
		BDD allVars = compData.M1.IncludedVars.and(compData.M2.IncludedVars);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(compData.M1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(compData.M2.IncludedVars);

		assertTrue(t21Sat > 55);
		assertTrue(t1Sat > 55);
		assertTrue(t2Sat > 55);
	}
	
	@Test
	public void TestHuge2(){
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story5\\";
		String allReq = dir + "all.req.xml";
		
		String model1 = dir + "ls.modelB.model";
		String req1 = allReq;
		
		String model2 = dir + "grep.modelB.model";
		String req2 = allReq;
		
		myJavaBdd javaBdd = new myJavaBdd();
		CompositionData compData = javaBdd.RunParser(model1,req1,model2,req2);
		
		BDD allVars = compData.M1.IncludedVars.and(compData.M2.IncludedVars);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(compData.M1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(compData.M2.IncludedVars);
		
		assertTrue(t21Sat > 120);
		assertTrue(t1Sat > 120);
		assertTrue(t2Sat > 120);
	}
	
	@Test
	public void TestHuge3(){
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story5\\";
		String allReq = dir + "all.req.xml";
		
		String model1 = dir + "ls.modelBB.model";
		String req1 = allReq;
		
		String model2 = dir + "grep.modelBB.model";
		String req2 = allReq;
		
		myJavaBdd javaBdd = new myJavaBdd();
		CompositionData compData = javaBdd.RunParser(model1,req1,model2,req2);
		
		BDD allVars = compData.M1.IncludedVars.and(compData.M2.IncludedVars);
		long t21Sat = (long) compData.T21.satCount(allVars);
		long t1Sat = (long) compData.T1.satCount(compData.M1.IncludedVars);
		long t2Sat = (long) compData.T2.satCount(compData.M2.IncludedVars);

		assertTrue(t21Sat > 200);
		assertTrue(t1Sat > 115);
		assertTrue(t2Sat > 210);
	}
	
	private List<Req> ValidReqAll(int id, BDDFactory bddFactory) {
		return ValidReq(id, bddFactory, true);
	}
	
	private List<Req> ValidReqTwo(int id, BDDFactory bddFactory) {
		return ValidReq(id, bddFactory, false);
	}
	
	@SuppressWarnings("serial")
	private List<Req> ValidReq(int id, BDDFactory bddFactory, boolean allPairs) {
		final BDD a1 = bddFactory.ithVar(0);
		final BDD a2 = bddFactory.ithVar(1);
		final BDD b1 = bddFactory.ithVar(2);
		final BDD b2 = bddFactory.ithVar(3);
		final BDD c1 = bddFactory.ithVar(4);
		final BDD c2 = bddFactory.ithVar(5);
		
		switch(id){
		case 1:
			if(allPairs)
				return new ArrayList<Req>(){{ 
					add(new Req(a1.and(b1)));
					add(new Req(a1.and(b2)));
					add(new Req(a2.and(b1)));
					add(new Req(a2.and(b2)));
					}};
				
			return new ArrayList<Req>(){{ 
				add(new Req(a1.and(b1)));
				add(new Req(a1.and(b2)));
				}};
		case 2:
			if(allPairs)
				return new ArrayList<Req>(){{ 
					add(new Req(b1.and(c1)));
					add(new Req(b1.and(c2)));
					add(new Req(b2.and(c1)));
					add(new Req(b2.and(c2)));
					}};
					
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
