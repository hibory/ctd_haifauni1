package hellobdd;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

/**
 * holds the main class which solves the CTD Composition problem.
 *
 */
public class CompositionSolver {
	static boolean isFactoryInit = false;
	private static BDDFactory nextFactory(){
		if(isFactoryInit)
			return bdd;
		
		bdd = BDDFactory.init(1000, 1000);
		bdd.reset();
		isFactoryInit = true;
        return bdd;
	}
	
	public static BDDFactory GetBddFactory(int n){
		BDDFactory bdd = nextFactory();
		if (bdd.varNum() < n) {
			bdd.setVarNum(n);
			int[] nodes = new int[n];
			for(int i=0; i<n; i++)
				nodes[i] = i;
			
			bdd.setVarOrder(nodes);
		}
		
		return bdd;
	}
	
	int sizeThresld = 10000000;
	int NumOfNodesSimple = 4;
	int NumOfNodesPaypal = 8;
	static BDDFactory bdd;
	long MaxNodesSize = 0;
	
	/**
	 * Run CTD computation problem based on given models and requirements XML files
	 * <p>
	 *
	 * @param  model1 Path for XML of the first-model
	 * @param  req1 Path for XML of the second-model's requirements
	 * @param  model2 Path for XML of the second-model
	 * @param  req2 Path for XML of the second-model's requirements 
	 * @return 
	 */
	public CompositionData RunParser(String model1, String req1, String model2, String req2) {
		
		ComputationParser compParser = new ComputationParser(model1, req1, model2, req2);
		FocusModel e1 = compParser.Parser1.Model;
		FocusModel e2 = compParser.Parser2.Model;
		List<Requirement> r1 = compParser.Parser1.Requirements;
		List<Requirement> r2 = compParser.Parser2.Requirements;
		bdd = compParser.BddFactory;
		CompositionData compData = Computation(e1, r1, e2, r2);
		
		PrintSizes(e1,e2,compData);
		//compParser.PrintResult(m2.Valid);
		//compParser.PrintResult(t21); //output the result as MDD
		ComputationParser.ValidateResult(compData.T21, e1.Valid, e2.Valid); // output result validation
		
		return compData;
	}

	
	private void PrintSizes(FocusModel e1, FocusModel e2, CompositionData compData) {
		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		
		System.out.println("E1 and E2 satcount:"+ GetVeryLongNumber(e1.Valid.and(e2.Valid).satCount(allVars)));
		System.out.println("E1 satcount:"+ GetVeryLongNumber(e1.Valid.satCount(e1.IncludedVars)));
		System.out.println("T1 satcount:"+compData.T1.satCount(e1.IncludedVars));
		System.out.println("E2 satcount:"+GetVeryLongNumber(e2.Valid.satCount(e2.IncludedVars)));
		System.out.println("T2 satcount:"+compData.T2.satCount(e2.IncludedVars));
		System.out.println("T21 satcount:"+compData.T21.satCount(e1.IncludedVars.and(e2.IncludedVars)));
	}
	
	/**
	 * Computes the composition test-plan T2oT1 of models E1 and E2, which covers R2oR1. 
	 * @param e1 first model 
	 * @param r1 first model's requirements
	 * @param e2 second model
	 * @param r2 second model's requirements
	 * @return test-plan T2oT1
	 */
	
	public CompositionData Computation(FocusModel e1, List<Requirement> r1, FocusModel e2, List<Requirement> r2){
		
		ValidateInput(e1,r1);
		ValidateInput(e2,r2);
		
		BDD T1 = bdd.zero(),T2 = bdd.zero() ,T21 = bdd.zero();

		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		BDD excludedVars1 = allVars.exist(e1.IncludedVars);
		BDD excludedVars2 = allVars.exist(e2.IncludedVars);
		
		//Init: for t in R1 do uncov(t) = Projt(E1)
		for(Requirement t : r1){
			t.excludeVars = e1.IncludedVars.restrict(t.Bdd); //this all variables except those in t
			t.uncov = e1.Valid.exist(t.excludeVars); //uncov(t) = Projt(E1)
		}
		
		//Init: for t in R2 do uncov(t) = Projt(E2)
		for(Requirement t : r2){
			t.excludeVars = e2.IncludedVars.restrict(t.Bdd); //this all variables except those in t
			t.uncov = e2.Valid.exist(t.excludeVars); //uncov(t) = Projt(E2)
		}
		
		while(!r1.isEmpty() || !r2.isEmpty()){
			
			if(!r1.isEmpty()){
				
				BDD chosen = ChosenTest(r1 ,e1, allVars);
				T1 = T1.or(chosen);		
				
				//inner join
				BDD chosenAc = InnerJoin(chosen,e2.Valid,r2);
				if(chosenAc.satCount()>0){
					// if found: add to T2, clean from R2
					BDD c = chosenAc.exist(excludedVars2);
					T2 = T2.or(c);
					CleanUncov(r2, c);
				}
				else{
					//not found: just take any from e2
					chosenAc = chosen.and(e2.Valid).fullSatOne();
				}
				
				T21 = T21.or(chosenAc);
				
				CleanUncov(r1,chosen);
			}
			
			if(!r2.isEmpty()){
				
				BDD chosen = ChosenTest(r2,e2,allVars);
				T2 = T2.or(chosen);		
				
				BDD chosenAc = InnerJoin(chosen,e1.Valid,r1);
				if(chosenAc.satCount()>0){
					// if found: add to T2, clean from R2
					BDD c = chosenAc.exist(excludedVars1);
					T1 = T1.or(c);
					CleanUncov(r1, c);
				}
				else{
					//not found: just take any from e2
					chosenAc = chosen.and(e1.Valid).fullSatOne();
				}
					
				T21 = T21.or(chosenAc); 
				
				CleanUncov(r2,chosen);
			}
		}
		
		return new CompositionData(T1, T2, T21, e1, e2);
	}
	
	private void ValidateInput(FocusModel e, List<Requirement> r) {
		if(e==null || r==null)
			throw new IllegalArgumentException("Error: One or more parameter is null. Please check your the passed parameters");
		
		for(Requirement t : r){
			if(!t.Bdd.and(e.IncludedVars).equals(e.IncludedVars))
				throw new IllegalArgumentException("Error: Requirements contains variables not from respected Valid");
		}
	}

	private String GetVeryLongNumber(double d){
		NumberFormat formatter = new DecimalFormat("0.00000000000");
		return formatter.format(d);
	}
	
	/**
	 * InnerJoin considers an input test from one model, and tries to matching test from the second model
	 * @param chosen BDD of a single valid test from one model.
	 * @param valid BDD of all valid tests from second model.
	 * @param reqs list of coverage requirements from the second model 
	 * @return matching test which covers the most requirements from the second model
	 */
	private BDD InnerJoin(BDD chosen, BDD valid, List<Requirement> reqs) {
		Collections.sort(reqs);
		Collections.reverse(reqs);
		
		boolean found = false;
		BDD collected = valid.and(chosen);
		for(Requirement t : reqs){		
			if(collected.and(t.uncov).pathCount()>0){ //if (Collected ^ uncov(t)) != FALSE then
				collected = collected.and(t.uncov);  //  Collected <- Collected ^ uncov(t)
				found = true;
			}
		}
		
		if(found){
			return collected.fullSatOne();
		}
			
		return bdd.zero();
	}

	/**
	 * ChosenTest considers an input set R of tuples of parameters coverage is required
	 * and returns a single test that covers as much requirements as possible.
	 * @param r list of coverage requirements
	 * @param m model which includes valid tests
	 * @param all list of all BDD variables
	 * @return a single test that covers as much requirements as possible.
	 */
	private BDD ChosenTest(List<Requirement> r, FocusModel m, BDD all) {
		
		List<Requirement> reqs = new ArrayList<Requirement>(r);
		
		BDD collected = m.Valid;
		Collections.sort(reqs); // Sort R in decreasing order of sat(uncov(t))
		Collections.reverse(reqs);
		
		boolean interrupted = false;
		int max = 0;
		for(Requirement t : reqs){		
			if(collected.and(t.uncov).pathCount()>0) //if (Collected ^ uncov(t)) != FALSE then
				collected = collected.and(t.uncov);  //  Collected <- Collected ^ uncov(t)
			
			if(collected.nodeCount()>sizeThresld){ 
				interrupted = true;
				break;                              
			}
			max = collected.nodeCount() > max ? collected.nodeCount() : max; 
		}
		
		BDD chosen;
		
		if(interrupted){
			int n = sizeThresld;
			BDD[] candti = new BDD[n];
			int[] newCov = new int[n];
			
			Iterator<BDD> iterator = collected.iterator(all);
			
			int i=0;
			while (iterator.hasNext() && i<sizeThresld) {
				candti[i] = iterator.next();
				newCov[i] = newlyCovered(candti[i],reqs); //newCovi <-newlyCovered(candidatei,R)
				i++;
			}
			
			int maxValueIndex = GetMaxValueIndex(newCov);
			chosen = candti[maxValueIndex].satOne(m.IncludedVars, true); //chosen <- {candti|ForEach j:newCovi >=newCcovj}
		}
		else{
			chosen = collected.satOne(m.IncludedVars, false); //chosen <-randSat(Collected)			
		}
		
		return chosen;
	}

	/**
	 * Count how many requirements given BDD covers
	 * @param path single satisfying path as BDD
	 * @param reqs list of requirements
	 * @return number of new requirements this path covers
	 */
	private int newlyCovered(BDD path, List<Requirement> reqs) {
		int covered = 0;
		for(Requirement t : reqs){
			BDD bddProj = path.exist(t.excludeVars);
			if(t.uncov.and(bddProj).satCount()>0)
				covered++;
		}
		return covered;
	}
	
	private int GetMaxValueIndex(int[] array) {
		if(array.length<1) return -1;
		
		int largest = array[0], index = 0;
		for (int i = 1; i < array.length; i++) {
		  if ( array[i] >= largest ) {
		      largest = array[i];
		      index = i;
		   }
		}
		return index;
	}

	/**
	 * Given a single test and list of coverage-requirements
	 * Check for requirements that are fully covered by this test and clean them from the list 
	 * @param r list of coverage-requirements
	 * @param chosen a single test BDD
	 */
	private void CleanUncov(List<Requirement> r,BDD chosen){
		List<Requirement> ToBeRemoved = new ArrayList<Requirement>();
		for(Requirement t : r){
			
			BDD chosenProj = chosen.exist(t.excludeVars);
			BDD remaining = t.uncov.and(chosenProj.not());
			t.uncov = remaining;
			
			if(t.uncov.pathCount()==0){
				ToBeRemoved.add(t);
			}
		}
		
		for(Requirement t : ToBeRemoved){
			r.remove(t);
		}
	}

}