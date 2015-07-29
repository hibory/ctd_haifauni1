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
		myJavaBdd javaBdd = new myJavaBdd();
		//javaBdd.runPaypal();
		
		String model1 = "C:\\Users\\amirshwa\\workspace1\\ShoppingShipping.model";
		String model2 = "C:\\Users\\amirshwa\\workspace1\\ShoppingShipping_2.model";
		
		String req1 = "C:\\Users\\amirshwa\\workspace1\\ShoppingShipping.ctd.req.xml";
		String req2 = "C:\\Users\\amirshwa\\workspace1\\ShoppingShipping_2.ctd.req.xml";
		
		javaBdd.RunParser(model1,req1,model2,req2);
		
	}
	
	public static void TestingJDD(){
		//myJdd jdd = new myJdd();
		//jdd.run();
		
	}

}
