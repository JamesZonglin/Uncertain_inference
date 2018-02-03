package bn.algorithm;

import java.util.ArrayList;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;

public class EnumerationAlgorithm {
	
	public static Distribution enumeration_ask(RandomVariable X, Assignment e, BayesianNetwork bn) {
		Distribution QX = new Distribution(X);
		
		// condition contains the query variable
		if (e.variableSet().contains(X)) {
			for (Object xi : X.getDomain()) {
				if (xi.equals(e.get(X))) {
					QX.put(xi, 1.0);
				} else {
					QX.put(xi, 0.0);
				}
			}
			return QX;
		}
		
		for (Object xi : X.getDomain()) {
			
			// temp_e is e extended with X=xi
			Assignment temp_e = e.copy();
			temp_e.set(X, xi);
			
			QX.put(xi, enumerate_all((ArrayList<RandomVariable>)bn.getVariableListTopologicallySorted(),
					temp_e, bn));
		}
		
		QX.normalize();
		return QX;
	}
	
	private static double enumerate_all(ArrayList<RandomVariable> vars, Assignment e, BayesianNetwork bn) {
		if (vars.isEmpty()) return 1.0;
		
		// first variable
		RandomVariable Y = vars.get(0);
		// other variables
		@SuppressWarnings("unchecked")
		ArrayList<RandomVariable> rest_vars = (ArrayList<RandomVariable>) vars.clone();
		rest_vars.remove(0);
		
		if (e.variableSet().contains(Y)) {
			Assignment temp_e =e.copy();
			double pi = bn.getProb(Y, temp_e);
			return pi * enumerate_all(rest_vars, temp_e, bn);
		} else {
			double p = 0;
			for (Object yi : Y.getDomain()) {
				// temp_e is e extended with Y=yi
				Assignment temp_e =e.copy();
				temp_e.set(Y, yi);
				
				double pi = bn.getProb(Y, temp_e);
				p += pi * enumerate_all(rest_vars, temp_e, bn);
			}
			return p;
		}
	}
}
