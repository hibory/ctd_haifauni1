package hellobdd;

import java.util.ArrayList;

/**
 * Holds a single requirement of attributes for a specific model.
 *
 */
public class FocusRequirment {
	ArrayList<String> AttributeNames;
	
	public FocusRequirment(ArrayList<String> attrNames){
		AttributeNames = attrNames;
	}
}
