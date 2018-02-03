package bn.algorithm;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import bn.core.Distribution;
import bn.core.RandomVariable;

public class ResultOutput {
	
	public static void printResult(Distribution d, RandomVariable rv) {
		System.out.println("Query Variable: " + rv.toString());
		
		Set<Map.Entry<Object, Double>> set = d.entrySet();
		Iterator<Entry<Object, Double>> iterator = set.iterator();
		
		String proString = "< ";
		
		while (iterator.hasNext()) {
			Map.Entry<Object, Double> temp_map = iterator.next();
			String keyString = (String)temp_map.getKey();
			String valueString = String.format("%.3f", temp_map.getValue());
			proString += keyString + ":" + valueString + " ";
		}
		
		proString += ">";
		
		System.out.println(proString);
	}

}
