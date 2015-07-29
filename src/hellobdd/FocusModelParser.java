package hellobdd;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.javabdd.BDD;
import net.sf.javabdd.BDDFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FocusModelParser {
	

	public FocusModelParser(String modelXml, String reqXml, Map<String, FocusAttribute> attributes){
		AllAttributes = attributes;
		ReqXml = reqXml;
		
		LocalAttributes = new HashMap<String, FocusAttribute>();
		//Requirements = new ArrayList<Req>();
		LoadModelData(modelXml);
	}
	
	Map<String, FocusAttribute> AllAttributes;
	Map<String, FocusAttribute> LocalAttributes;
	ArrayList<FocusRestriction> rests;
	private String ReqXml;
	
	BDD Valid;
	List<Req> Requirements;	
	public FocusModel Model;
   
   // read data from xml
   private void LoadModelData(String xml) {
	   Document document = ReadXmlDoc(xml);
		   
	   FillAttributes(document);
	   rests = GetAllRestriction(document); 
	   
	   //test that it outputs correctly:
	   for(FocusAttribute attr : AllAttributes.values()){
		   System.out.println(attr.Name+":");
		   for(String v : attr.Values){
			   System.out.println(v+":");
		   }
		   System.out.println("=====");
	   }
	   
	   for(FocusRestriction rest : rests){
		   System.out.println(rest.Name+":");
		   System.out.println(rest.Expression+":");
		   
		   System.out.println("=====");
	   }
	   
	   
	   int m = 1;
	   int m1 = m-1;
   }


   private void DefineValid(BDDFactory bddFactory, BDD MutualVars) {
	   
	   //assign all requirements & valid-space
	   Valid = bddFactory.one();
	   
	   for(FocusRestriction rest : rests){
		   
		   BDD rulesBdd = bddFactory.one();
		   for(KeyValuePair rule : rest.Rules){
			   FocusAttribute attr = AllAttributes.get(rule.Key);
			   BDD b = attr.GetBdd(rule.Value);
			   rulesBdd = rulesBdd.and(b);
			   
			   Print("b",b);
			   Print("rulesBdd",rulesBdd);
			   attr.PrintVariables();
		   }
		   
		   Valid = Valid.and(rulesBdd.not());
		   Print("Valid",Valid);
	   }
	   
	   BDD IncludedVars = bddFactory.one();
	   for(FocusAttribute attr : LocalAttributes.values()){
		   for(int i : attr.Variables){
			   BDD v = bddFactory.ithVar(i);
			   IncludedVars.andWith(v);
		   }
	   }
	   
	   
	   Model = new FocusModel(Valid, IncludedVars,MutualVars);
   }
   
   // read attributes from xml
   private ArrayList<FocusRestriction> GetAllRestriction(Document document) {
	   NodeList nl = document.getElementsByTagName("restriction");
	   ArrayList<FocusRestriction> rests = new ArrayList<FocusRestriction>();
	   
	   //loop through rests
	   for (int i = 0; i < nl.getLength(); i++) {
		   
		   Element attrEl =  (Element)nl.item(i);
           String name = attrEl.getAttribute("name");
           String expression = attrEl.getAttribute("expression");
           
           rests.add(new FocusRestriction(name,expression));
	   }
	   
	   return rests;
   }

   //Map<String, String> map = new HashMap<String, String>();
   // read restrictions from xml
   private void FillAttributes(Document document) {
	   NodeList nl = document.getElementsByTagName("attribute");
	   
	   //Map<String, FocusAttribute> attributes = new HashMap<String, FocusAttribute>();
	   
	   //loop through attributes
	   for (int i = 0; i < nl.getLength(); i++) {
		   Element attrEl =  (Element)nl.item(i);
           String name = attrEl.getAttribute("name");
           
                    
           FocusAttribute attr;
           if(AllAttributes.containsKey(name)){
        	   attr = AllAttributes.get(name);
           }
           else
           {
        	   attr = new FocusAttribute(name);
               
               //loop through values
               NodeList values = attrEl.getElementsByTagName("value"); 
               for (int j = 0; j < values.getLength(); j++) {
            	   Element valueEl = (Element)values.item(j);
            	   String valueName = valueEl.getAttribute("name");
            	   
            	   attr.Values.add(valueName);
               }
               AllAttributes.put(attr.Name, attr);
           }
           
           LocalAttributes.put(attr.Name, attr);           
	   }
	}

   private Document ReadXmlDoc(String xml) {
		File file = new File(xml);
		   DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
		           .newInstance();
		   DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   Document document = null;
		try {
			document = documentBuilder.parse(file);
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return document;
	}

   
   private void Print(String s, BDD b){
	   myJavaBdd.PrintAsDot(s, b);
   }


   
   private void DefineRequirements(BDDFactory bddFactory) {
	   Requirements = new ArrayList<Req>();
	   
	   ArrayList<FocusRequirment> reqs = GetRequirements(ReqXml);
	   for(FocusRequirment focusReq : reqs){
		   BDD b = bddFactory.one();
		   for(String name : focusReq.AttributeNames){
			   FocusAttribute attr = LocalAttributes.get(name);
			   BDD varSet = attr.GetVarSet();
			   b.andWith(varSet);
		   }
		   
		   Req req = new Req(b);
		   Requirements.add(req);
	   }
   }


	private ArrayList<FocusRequirment> GetRequirements(String xml) {
		Document document = ReadXmlDoc(xml);
		
		ArrayList<FocusRequirment> focusReqs = new ArrayList<FocusRequirment>();
		NodeList nl = document.getElementsByTagName("requirement");
			
		   //loop through requirements
		   for (int i = 0; i < nl.getLength(); i++) {
			   Element reqEl =  (Element)nl.item(i);
			   ArrayList<String> attrNames = new ArrayList<String>();
	           
	           //loop through attributes
               NodeList attributes = reqEl.getElementsByTagName("attribute"); 
               for (int j = 0; j < attributes.getLength(); j++) {
            	   Element attrEl = (Element)attributes.item(j);
            	   String name = attrEl.getAttribute("name");
            	   
            	   attrNames.add(name);
               }
               
               FocusRequirment req = new FocusRequirment(attrNames);
               focusReqs.add(req);
		   }
		
		return focusReqs;
	}


	public void Define(BDDFactory bddFactory, BDD mutualVars) {
		DefineValid(bddFactory,mutualVars);
		DefineRequirements(bddFactory);
	}
   
	
}
