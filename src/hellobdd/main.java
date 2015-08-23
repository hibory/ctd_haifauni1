package hellobdd;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class main {

	public static void main(String[] args) {
		TestingJavaBDD();
		//TestInvalid1();
	}
	
	public static void TestingJavaBDD() {
		
		//RunLsGrepStory1();
		//RunLsGrepStory2();
		//RunLsGrepStory3();
		//RunLsGrepStory4();
		RunLsGrepStory5("M");
	}

	private static void RunLsGrepStory1() {
		System.out.println("Story1");
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story1\\";
		String model1 = dir + "ls.model";
		String req1 = dir + "ls.req.xml";
		
		String model2 = dir + "grep.model";
		String req2 = dir + "grep.req.xml";
		
		CompositionSolver solver = new CompositionSolver();
		solver.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunLsGrepStory3() {
		System.out.println("Story3");
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story3\\";
				
		String model1 = dir + "ls.dirTypes.model";
		String req1 = dir + "ls.req.xml";
		
		String model2 = dir + "grep.RestrictedCooler.model";
		String req2 = dir + "grep.req.xml";
		
		CompositionSolver solver = new CompositionSolver();
		solver.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunLsGrepStory4() {
		System.out.println("Story4");
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story4\\";
		String model1 = dir + "ls.allvalid.model";
		String req1 = dir + "ls.req.xml";
		
		String model2 = dir + "grep.allvalid.model";
		String req2 = dir + "grep.req.xml";
		
		CompositionSolver solver = new CompositionSolver();
		solver.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunLsGrepStory5(String suffix) {
		System.out.println("Story5-" + suffix);
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story5\\";
		String allReq = dir + "all.req.xml";
		
		String model1 = dir + "ls.model"+suffix +".model";
		String req1 = allReq;
		
		String model2 = dir + "grep.model"+suffix +".model";
		String req2 = allReq;
		
		CompositionSolver solver = new CompositionSolver();
		solver.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunShoppingShipping() {
		System.out.println("ShoppingShipping");
		String model1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping.model";
		String req1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping.ctd.req.xml";
		
		String model2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping_2.model";
		String req2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping_2.ctd.req.xml";
		
		CompositionSolver solver = new CompositionSolver();
		solver.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunZoo() {
		System.out.println("Zoo");
		String model1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo.model";
		String model2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo_2.model";
		String req1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo.ctd.req.xml";
		String req2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo_2.ctd.req.xml";
		
		CompositionSolver solver = new CompositionSolver();
		solver.RunParser(model1,req1,model2,req2);
	}
}
