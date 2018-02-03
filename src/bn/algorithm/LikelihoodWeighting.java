package bn.algorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Domain;
import bn.core.RandomVariable;
import bn.util.Pair;

public class LikelihoodWeighting {
	
	public static Distribution likelihood_weighting(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {
		
		Distribution distribution = new Distribution();
		
		for(Object object : X.getDomain()) {
			distribution.put(object, 0.0);
		}
		
		Random random = new Random();
		for(int i = 0; i < N; i++) {
			Pair<HashMap<RandomVariable, Object>, Double> x_w = weighted_sample(bn, e,random);
			HashMap<RandomVariable, Object> hashMap = x_w.getFirst();
			double w = x_w.getSecond();
			
			Object object = hashMap.get(X);
			distribution.put(object, distribution.get(object)+w);
		}
		
		distribution.normalize();
		return distribution;
	}

	public static Pair<HashMap<RandomVariable,Object>,Double> weighted_sample(BayesianNetwork bn, Assignment e, Random random){
		
		double weight = 1.0;
		Assignment temp_ass = e.copy();
		
		HashMap<RandomVariable, Object> x = new HashMap<RandomVariable,Object>();
		// x <- an event with n elements initialized from e
 		Iterator<RandomVariable> iterator = temp_ass.variableSet().iterator();
		while(iterator.hasNext()) {
			RandomVariable rVariable = iterator.next();
			x.put(rVariable, temp_ass.get(rVariable));
		}
		
		for(RandomVariable rv : (ArrayList<RandomVariable>)bn.getVariableListTopologicallySorted()) {
			if(x.containsKey(rv)) {
				weight *= bn.getProb(rv, temp_ass);
			}else {
				Domain domain = rv.getDomain();
				double random_float = random.nextFloat();
				
				double p = 0;
				for (Object object : domain) {
					temp_ass.set(rv, object);
					double pi = bn.getProb(rv, temp_ass);
					p += pi;
					if (random_float <= p) {
						x.put(rv, object);
						break;
					}
				}
			}
		}
		return new Pair<HashMap<RandomVariable,Object>, Double>(x,weight);
	}
}
