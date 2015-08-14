package hellobdd;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

public class myJavaBdd {

	private static BDDFactory nextFactory(){
		BDDFactory f = BDDFactory.init(1000, 1000);
        f.reset();
        return f;
	}
	
	public static BDDFactory GetBddFactory(int n){
		BDDFactory bdd = nextFactory();
		if (bdd.varNum() < n) bdd.setVarNum(n);
		
		int[] nodes = new int[n];
		for(int i=0; i<n; i++)
			nodes[i] = i;
		
		bdd.setVarOrder(nodes);
		
		return bdd;
	}
	
	int sizeThresld = 10000000;
	int NumOfNodesSimple = 4;
	int NumOfNodesPaypal = 8;
	BDDFactory bdd;
	
	/**
	 * Run CTD computation problem based on given models and requiremnts
	 * <p>
	 *
	 * @param  model1 Path for XML of the first-model
	 * @param  req1 Path for XML of the second-model's requirements
	 * @param  model2 Path for XML of the second-model
	 * @param  req2 Path for XML of the second-model's requirements 
	 */
	public void RunParser(String model1, String req1, String model2, String req2) {
		
		ComputationParser compParser = new ComputationParser(model1, req1, model2, req2);
		FocusModel m1 = compParser.Parser1.Model;
		FocusModel m2 = compParser.Parser2.Model;
		List<Req> r1 = compParser.Parser1.Requirements;
		List<Req> r2 = compParser.Parser2.Requirements;
		bdd = compParser.BddFactory;
		BDD t21 = Computation(m1, r1, m2, r2);
		//compParser.PrintResult(m2.Valid);
		//compParser.PrintResult(t21); //output the result as MDD
		ComputationParser.ValidateResult(t21, m1.Valid, m2.Valid); // output result validation
	}

	
	public void run(){
		
		bdd = GetBddFactory(NumOfNodesSimple);
		
		BDD d = GetVar('d');
		BDD a = GetVar('a');
		BDD b = GetVar('b');
		BDD c = GetVar('c');
		
		BDD MutualVars = b;
		BDD IncludedVars1 = d.and(a).and(b);	
		BDD e1 =   (  d.not().and(a).and(a))
				.or(  d.and(a).and(b) )
				.or(  d.not().and(a).and(b.not())  )
				.or(  d.and(a.not()).and(b) );
		
		FocusModel m1 = new FocusModel(e1, IncludedVars1, MutualVars);
		List<Req> r1 = new ArrayList<Req>();
		r1.add(new Req(a.and(b)));			
		
		BDD IncludedVars2 =b.and(c);
		BDD e2 = ( b.and(c.not()) )
			  .or( b.not().and(c.not()) );

		FocusModel m2 = new FocusModel(e2, IncludedVars2, MutualVars);
		List<Req> r2 = new ArrayList<Req>();
		r2.add(new Req(b.and(c)));
		
		Computation(m1,r1,m2,r2);
	}
	
	public void runPaypal(){
		
		bdd = GetBddFactory(NumOfNodesPaypal);
		
		BDD x1 = GetVar('1');
		BDD x2 = GetVar('2');
		BDD x3 = GetVar('3');
		BDD x4 = GetVar('4');
		BDD x5 = GetVar('5');
		BDD x6 = GetVar('6');
		BDD x7 = GetVar('7');
		BDD x8 = GetVar('8');
		
		BDD IncludedVars1 = x1.and(x2).and(x3).and(x4).and(x5).and(x6);	
		BDD MutualVars = x5.and(x6);
		BDD e1 =   (  x1.not().and(x2.not()).and(x3.not()).and(x4.not()).and(x5.not()).and(x6.not())   )
				.or(  x1.not().and(x2.not()).and(x3.not()).and(x4).and(x5.not()).and(x6.not()) )
				.or(  x1.not().and(x2).and(x3.not()).and(x4).and(x5.not()).and(x6.not())  )
				.or(  x1.not().and(x2).and(x3).and(x4.not()).and(x5.not()).and(x6) )
				.or(  x1.not().and(x2).and(x3.not()).and(x4).and(x5).and(x6.not()) )
				.or(  x1.not().and(x2).and(x3).and(x4.not()).and(x5).and(x6.not()) );
		
		
		FocusModel m1 = new FocusModel(e1, IncludedVars1, MutualVars);
		
		BDD IncludedVars2 = x5.and(x6).and(x7).and(x8);	
		BDD e2 =   (  x5.not().and(x6.not()).and(x7.not()).and(x8.not()))
				.or(  x5.not().and(x6.not()).and(x7).and(x8) )
				.or(  x5.not().and(x6).and(x7.not()).and(x8)  )
				.or(  x5.not().and(x6).and(x7).and(x8) )
				.or(  x5.and(x6.not()).and(x7).and(x8))
				.or(  x5.and(x6.not()).and(x7.not()).and(x8.not()) );
		
		FocusModel m2 = new FocusModel(e2, IncludedVars2, MutualVars);
			
		List<Req> r1 = new ArrayList<Req>();
		r1.add(new Req( x1.and(x2).and(x3).and(x4) ));

		List<Req> r2 = new ArrayList<Req>();
		r2.add(new Req( x5.and(x6).and(x7).and(x8)));
		BDD t21 = Computation(m1,r1,m2,r2);
		ComputationParser.ValidateResult(t21, m1.Valid, m2.Valid);
	}
	
	
	public BDD Computation(FocusModel e1, List<Req> r1, FocusModel e2, List<Req> r2){
		
		if(e1==null || e2==null || r1==null || r2==null)
			throw new IllegalArgumentException("Error: One or more parameter is null. Please check your the passed parameters");
		
		BDD T1 = bdd.zero(),T2 = bdd.zero() ,T21 = bdd.zero();

		BDD allVars = e1.IncludedVars.and(e2.IncludedVars);
		BDD excludedVars1 = allVars.exist(e1.IncludedVars);
		BDD excludedVars2 = allVars.exist(e2.IncludedVars);
		
		//Init: for t in R1 do uncov(t) = Projt(E1)
		for(Req t : r1){
			t.excludeVars = e1.IncludedVars.restrict(t.Bdd); //this all variables except those in t
			t.uncov = e1.Valid.exist(t.excludeVars); //uncov(t) = Projt(E1)
		}
		
		//Init: for t in R2 do uncov(t) = Projt(E2)
		for(Req t : r2){
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

		//T21.printSet();
		
		System.out.println("e1 and e2 satcount:"+ GetVeryLongNumber(e1.Valid.and(e2.Valid).satCount(allVars)));
		
		System.out.println("e1 satcount:"+ GetVeryLongNumber(e1.Valid.satCount(e1.IncludedVars)));
		System.out.println("T1 satcount:"+T1.satCount(e1.IncludedVars));
		System.out.println("E2 satcount:"+GetVeryLongNumber(e2.Valid.satCount(e2.IncludedVars)));
		System.out.println("T2 satcount:"+T2.satCount(e2.IncludedVars));
		System.out.println("T21 satcount:"+T21.satCount(e1.IncludedVars.and(e2.IncludedVars)));
		
		return T21;
	}
	
	private String GetVeryLongNumber(double d){
		NumberFormat formatter = new DecimalFormat("0.00000000000");
		return formatter.format(d);
	}

	private BDD InnerJoin(BDD chosen, BDD valid, List<Req> reqs) {
		//go through all reqs ,and try to join with one with with biggest unvoc
		Collections.sort(reqs);
		Collections.reverse(reqs);
		
		boolean found = false;
		BDD collected = valid.and(chosen);
		for(Req t : reqs){		
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

	private BDD GetVar(char c) {
		int i=0;
		switch(c){
			case 'd': i=0; break;
			case 'a': i=1; break;
			case 'b': i=2; break;
			case 'c': i=3; break;
			
			case '1': i=0; break;
			case '2': i=1; break;
			case '3': i=2; break;
			case '4': i=3; break;
			case '5': i=4; break;
			case '6': i=5; break;
			case '7': i=6; break;
			case '8': i=7; break;
			
			default: i=-1; break;
		}
		
		return bdd.ithVar(i);
	}

	private BDD ChosenTest(List<Req> rin, FocusModel m, BDD all) {
		
		List<Req> r = new ArrayList<Req>(rin);
		
		BDD collected = m.Valid;
		Collections.sort(r); // Sort R in decreasing order of sat(uncov(t))
		Collections.reverse(r);
		
		boolean interrupted = false;
		int max = 0;
		for(Req t : r){		
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
				newCov[i] = newlyCovered(candti[i],r); //newCovi <-newlyCovered(candidatei,R)
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

	//count how many requirements this bdd covers
	private int newlyCovered(BDD path, List<Req> reqs) {
		int covered = 0;
		for(Req t : reqs){
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

	public void CleanUncov(List<Req> r,BDD chosen){
		List<Req> ToBeRemoved = new ArrayList<Req>();
		for(Req t : r){
			
			BDD chosenProj = chosen.exist(t.excludeVars);
			BDD remaining = t.uncov.and(chosenProj.not());
			t.uncov = remaining;
			
			if(t.uncov.pathCount()==0){
				ToBeRemoved.add(t);
			}
		}
		
		for(Req t : ToBeRemoved){
			r.remove(t);
		}
	}
	
	public static void PrintAsDot(String s, BDD b){
		Debugger.log("////////////////////////");
		Debugger.log(s+":");
		if(Debugger.isEnabled()) b.printDot();
		Debugger.log("***********************");
	}
	
	public static void Print(String s){
		System.out.println(s);
	}

	
}