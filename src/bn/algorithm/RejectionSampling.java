package bn.algorithm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class RejectionSampling {
	
	public static Distribution rejection_sampling(RandomVariable X, 
			Assignment e, BayesianNetwork bn, int N) {
//		Random random = new Random();
		Distribution distribution = new Distribution();
		
		for (Object obj : X.getDomain()) {
			distribution.put(obj, 0);
		}
		
		int sample_num = 0;
		
		for (int j = 1; j <= N; j++) {
			HashMap<RandomVariable, Object> x = PriorSample.prior_sample(bn);
			
			if (consistent(x, e)) {
				sample_num++;
				Object object = x.get(X);
				distribution.put(object, distribution.get(object) + 1);
			}
			
		}
		
		System.out.println("Consistent Sample: " + String.valueOf(sample_num));
		
		distribution.normalize();
		return distribution;
	}
	
	private static boolean consistent(HashMap<RandomVariable, Object> x, Assignment e) {
		Iterator<Entry<RandomVariable, Object>> iterator = e.entrySet().iterator();
		
		while (iterator.hasNext()) {
			Map.Entry<RandomVariable, Object> temp_map = iterator.next();
			if (!x.get(temp_map.getKey()).equals(temp_map.getValue())) {
				return false;
			}
		}
		
		return true;
	}

}
