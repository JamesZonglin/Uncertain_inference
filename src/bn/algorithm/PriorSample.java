package bn.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Domain;
import bn.core.RandomVariable;

public class PriorSample {
	
	public static HashMap<RandomVariable, Object> prior_sample(BayesianNetwork bn) {
		
		Random random = new Random();
		
		HashMap<RandomVariable, Object> hashMap = new HashMap<RandomVariable, Object>();
		
		Assignment assignment = new Assignment();
		
		for (RandomVariable rv : (ArrayList<RandomVariable>) bn.getVariableListTopologicallySorted()) {
			Domain domain = rv.getDomain();
			double random_float = random.nextFloat();
			
			double p = 0;
			for (Object object : domain) {
				Assignment temp_ass = assignment.copy();
				temp_ass.set(rv, object);
				double pi = bn.getProb(rv, temp_ass);
				p += pi;
				if (random_float <= p) {
					hashMap.put(rv, object);
					assignment.set(rv, object);
					break;
				}
			}
		}
		return hashMap;
	}
}
