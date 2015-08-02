package hellobdd;

import java.io.IOException;
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
	
	int sizeThresld = 100;
	int NumOfNodesSimple = 4;
	int NumOfNodesPaypal = 8;
	BDDFactory bdd;
	
	public void Test(){
		// TODO Auto-generated method stub
		System.out.println("Hello, World");
		
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
		
		//r1.add(new Req( x1.not().and(x2.not())));
		//r1.add(new Req( x1.not().and(x2)));
		//r1.add(new Req( x1.and(x2).and(x3).and(x4) ));
		//r1.add(new Req( x1.and(x2).and(x5).and(x6)));
		//r1.add(new Req( x3.and(x4).and(x5).and(x6)));
		//r1.add(new Req( x1.not().and(x2.not()).and(x3.not()).and(x4.not()) , IncludedVars1 ));
		//r1.add(new Req( x1.not().and(x2.not()).and(x5.not()).and(x6.not() ), IncludedVars1 ));
		//r1.add(new Req( x3.not().and(x4).and(x5).and(x6.not()) , IncludedVars1 ));
			
		List<Req> r2 = new ArrayList<Req>();
		r2.add(new Req( x5.and(x6).and(x7).and(x8)));
		
		//r2.add(new Req( x5.and(x6.not()).and(x7.not()).and(x8.not()) ));
		//r2.add(new Req( x5.not().and(x6.not()).and(x7).and(x8) ));
		
		BDD t21 = Computation(m1,r1,m2,r2);
		ComputationParser.ValidateResult(t21, m1.Valid, m2.Valid);
	}
	
	public BDD Computation(FocusModel m1, List<Req> r1, FocusModel m2, List<Req> r2){
		
		BDD T1 = bdd.zero(),T2 = bdd.zero() ,T21 = bdd.zero();
		BDD e1 = m1.Valid;
		BDD e2 = m2.Valid;
		BDD mutual = m2.MutualVars;
		
		//Init: for t in R1 do uncov(t) = Projt(E1)
		for(Req t : r1){
			BDD excludeVars = m1.IncludedVars.restrict(t.Bdd); //this all variables except those in t
			t.uncov = e1.exist(excludeVars); //uncov(t) = Projt(E1)
			t.excludeVars = excludeVars;
			
			System.out.println("T.uncov:");
			t.uncov.printSet();
			
			System.out.println("E1:");
			e1.printSet();
			
			//t.uncov = t.uncov.and(e1);
			
			//t.uncov = e1.and(t.Bdd); //old version
		}
		
		//Init: for t in R2 do uncov(t) = Projt(E2)
		for(Req t : r2){
			BDD excludeVars = m2.IncludedVars.restrict(t.Bdd); //this all variables except those in t
			
			t.excludeVars = excludeVars;
			t.uncov = e2.exist(excludeVars); //uncov(t) = Projt(E2)
			
			System.out.println("T.uncov:");
			t.uncov.printSet();
			
			System.out.println("E2:");
			e2.printSet();
			
			//t.uncov = t.uncov.and(e2);
			
			//t.uncov = e2.and(t.Bdd); //old version
		}
		
		while(!r1.isEmpty() || !r2.isEmpty()){
			
			if(!r1.isEmpty()){
				
				BDD chosen = ChosenTest(r1,m1);
				T1 = T1.or(chosen);		
				
				//inner join
				BDD chosenAc = chosen.and(e2).fullSatOne();
				//chosenAc = chosenAc.exist(mutual);//WithoutB Version
				T21 = T21.or(chosenAc);
				
				CleanUncov(r1,chosen);
			}
			
			if(!r2.isEmpty()){
				
				BDD chosen = ChosenTest(r2,m2);
				T2 = T2.or(chosen);		
				
				//inner join
				BDD chosenAc = chosen.and(e1).fullSatOne();
				//chosenAc = chosenAc.exist(mutual);//WithoutB Version				
				PrintAsDot("ChosenAC:",chosenAc);				
				T21 = T21.or(chosenAc); 
				
				CleanUncov(r2,chosen);
			}
		}
		
		T21.printSet();
		
		System.out.println("");
		System.out.println("e1:");
		e1.printSet();
		System.out.println("satcount:"+e1.satCount(m1.IncludedVars));
		
		System.out.println("");
		System.out.println("T1:");
		T1.printSet();
		System.out.println("satcount:"+T1.satCount(m1.IncludedVars));
				
		System.out.println("");
		System.out.println("e2:");
		e2.printSet();
		System.out.println("satcount:"+e2.satCount(m2.IncludedVars));
		
		System.out.println("");
		System.out.println("T2:");
		T2.printSet();
		System.out.println("satcount:"+T2.satCount(m2.IncludedVars));
		
		System.out.println("");
		System.out.println("T21:");
		T21.printSet();
		System.out.println("satcount:"+T21.satCount(m1.IncludedVars.and(m2.IncludedVars)));
		
		System.out.println("");
		System.out.println("E21:");
		e1.and(e2).printSet();
		System.out.println("satcount:"+e1.and(e2).satCount(m1.IncludedVars.and(m2.IncludedVars)));
		
		
		return T21;
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

	private BDD ChosenTest(List<Req> rin, FocusModel m) {
		
		List<Req> r = new ArrayList<Req>(rin);
		
		BDD collected = m.Valid;
		Collections.sort(r); // Sort R in decreasing order of sat(uncov(t))
		Collections.reverse(r);
		
		boolean interrupted = false;
		for(Req t : r){

			PrintAsDot("t.bdd",t.Bdd);
			PrintAsDot("t.uncov",t.uncov);
			PrintAsDot("collected ^ unvoct",collected.and(t.uncov));
			PrintAsDot("collected",collected);
			
			if(collected.and(t.uncov).pathCount()>0) //if (Collected ^ uncov(t)) != FALSE then
				collected = collected.and(t.uncov);  //  Collected <- Collected ^ uncov(t)
			
			if(collected.nodeCount()>sizeThresld){ 
				interrupted = true;
				break;                              
			}
		}
		
		BDD chosen;
		int n = (int) collected.pathCount();
		BDD[] candti = new BDD[n];
		int[] newCov = new int[n];
		
		if(interrupted){
			for(int i=0; i<n; i++){
				candti[i] =collected.fullSatOne(); //candti <-randSat(Collected)
				newCov[i] = newlyCovered(candti[i],r); //newCovi <-newlyCovered(candidatei,R)
			}
			int maxValueIndex = GetMaxValueIndex(newCov);
			chosen = candti[maxValueIndex].satOne(m.IncludedVars, true); //chosen <- {candti|ForEach j:newCovi >=newCcovj}
		}
		else{
			
			//chosen = collected.fullSatOne();
			chosen = collected.satOne(m.IncludedVars, true); //chosen <-randSat(Collected)
			
			PrintAsDot("chosen",chosen);
			PrintAsDot("collected",collected);
		}
		
		return chosen;
	}

	//todo: implement this method
	private int newlyCovered(BDD bdd, List<Req> r) {
		// TODO Auto-generated method stub
		return 0;
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
		PrintAsDot("chosen",chosen);
		PrintAsDot("not chosen",chosen.not());
		List<Req> ToBeRemoved = new ArrayList<Req>();
		for(Req t : r){
			
			BDD chosenProj = chosen.exist(t.excludeVars);
			BDD remaining = t.uncov.and(chosenProj.not());
			PrintAsDot("t.uncov",t.uncov);
			PrintAsDot("t.uncov ^ chosen.not",remaining);			
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

	public void RunParser(String model1, String req1, String model2, String req2) {

		ComputationParser compParser = new ComputationParser(model1, req1, model2, req2);
		FocusModel m1 = compParser.Parser1.Model;
		FocusModel m2 = compParser.Parser2.Model;
		List<Req> r1 = compParser.Parser1.Requirements;
		List<Req> r2 = compParser.Parser2.Requirements;
		bdd = compParser.BddFactory;
		BDD t21 = Computation(m1, r1, m2, r2);
		
		compParser.PrintResult(t21); //output the result as MDD
		ComputationParser.ValidateResult(t21, m1.Valid, m2.Valid); // output result validation
	}

	public void TestDifferentBdd() {
		BDDFactory bddFactory1 = myJavaBdd.GetBddFactory(5);
		//BDDFactory bddFactory2 = myJavaBdd.GetBddFactory(10);
		
		BDD x1 = bddFactory1.ithVar(1);
		BDD x2 = bddFactory1.ithVar(2);
		BDD x4 = bddFactory1.ithVar(4);
		
		BDD res = x1.and(x2).and(x4);
		
		try {
			bddFactory1.save("C:\\temp\\bdd.txt", res);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		res.printDot();
		return;
	}
	
	public void TestUnique() {
		BDDFactory bddFactory1 = myJavaBdd.GetBddFactory(5);
		//BDDFactory bddFactory2 = myJavaBdd.GetBddFactory(10);
		
		BDD x1 = bddFactory1.ithVar(1);
		BDD x2 = bddFactory1.ithVar(2);
		BDD x3 = bddFactory1.ithVar(3);
		BDD x4 = bddFactory1.ithVar(4);
		
		BDD res = (x1.and(x2).and(x3).and(x4))
			   .or(x1.and(x2.not()).and(x3.not()).and(x4))
			   .or(x1.and(x2.not()).and(x3).and(x4))
			   //.or(x1.and(x2.and(x3.not()).and(x4)))
			   ;
		
		BDD u = res.unique(x1.and(x4));
		
		res.printDot();
		u.printDot();
		
		return;
		
	
	}

}