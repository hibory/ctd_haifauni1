package hellobdd;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class main {

	public static void main(String[] args) {
	
		TestingJavaBDD();
		//TestingJDD();
	}
	
	public static void TestingJavaBDD() {
		
		RunLsGrepStory1();
		//RunLsGrepStory2();
		//RunLsGrepStory3();
		//RunLsGrepStory4();
	}
	
	private static void RunLsGrepStory4() {
		System.out.println("Story4");
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story4\\";
		String model1 = dir + "ls.allvalid.model";
		String req1 = dir + "ls.req.xml";
		
		String model2 = dir + "grep.allvalid.model";
		String req2 = dir + "grep.req.xml";
		
		myJavaBdd javaBdd = new myJavaBdd();
		javaBdd.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunLsGrepStory3() {
		System.out.println("Story3");
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story3\\";
				
		String model1 = dir + "ls.dirTypes.model";
		String req1 = dir + "ls.req.xml";
		
		String model2 = dir + "grep.RestrictedCooler.model";
		String req2 = dir + "grep.req.xml";
		
		myJavaBdd javaBdd = new myJavaBdd();
		javaBdd.RunParser(model1,req1,model2,req2);
	}

	private static void RunLsGrepStory2() {
		System.out.println("Story2");
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story2\\";
				
		String model1 = dir + "ls.timeInputs.model";
		String req1 = dir + "ls.req.xml";
		
		String model2 = dir + "grep.RestrictedFilesTypes.model";
		String req2 = dir + "grep.req.xml";
		
		myJavaBdd javaBdd = new myJavaBdd();
		javaBdd.RunParser(model1,req1,model2,req2);
	}

	private static void RunLsGrepStory1() {
		System.out.println("Story1");
		String dir = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\unixshell\\story1\\";
		String model1 = dir + "ls.model";
		String req1 = dir + "ls.req.xml";
		
		String model2 = dir + "grep.model";
		String req2 = dir + "grep.req.xml";
		
		myJavaBdd javaBdd = new myJavaBdd();
		javaBdd.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunShoppingShipping() {
		System.out.println("ShoppingShipping");
		String model1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping.model";
		String req1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping.ctd.req.xml";
		
		String model2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping_2.model";
		String req2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\ShoppingShipping_2.ctd.req.xml";
		
		myJavaBdd javaBdd = new myJavaBdd();
		javaBdd.RunParser(model1,req1,model2,req2);
	}
	
	private static void RunZoo() {
		System.out.println("Zoo");
		String model1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo.model";
		String model2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo_2.model";
		String req1 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo.ctd.req.xml";
		String req2 = "C:\\Users\\amirshwa\\workspace1\\hellobdd\\xmls\\zoo_2.ctd.req.xml";
		
		myJavaBdd javaBdd = new myJavaBdd();
		javaBdd.RunParser(model1,req1,model2,req2);
	}

}
