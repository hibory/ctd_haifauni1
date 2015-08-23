package hellobdd;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Hold list of rules combination that is not valid within a given model.
   Combining a list of restriction allows defining a certain model.
 *
 */
public class FocusRestriction {
	public String Name;
	public ArrayList<FocusRules> Rules;
	
	public FocusRestriction(String name, String expression){
		Name=name;	
		ParseExpression(expression);
	}
	
	private void ParseExpression(String expression){
		Rules = new ArrayList<FocusRules>();
		String[] parts = expression.split(" ");
		for(String p : parts){
			if(p.indexOf(".equals") < 0) continue;
			
			FocusRules rule = GetKeyValue(p);	
			Rules.add(rule);	
		}
	}

	private FocusRules GetKeyValue(String p1) {
		String attr = p1.substring(0, p1.indexOf('.'));
		String equals = p1.substring(p1.indexOf('.')+1);
				
		Pattern p = Pattern.compile("equals\\(\\\"(.*?)\\\"\\)");
		Matcher m = p.matcher(equals);
		String value = null;
		while(m.find()) {
		   value = m.group(1);
		}
		return new FocusRules(attr,value);
	}
}
