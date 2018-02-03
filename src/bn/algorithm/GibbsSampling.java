package bn.algorithm;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Domain;
import bn.core.RandomVariable;
import bn.util.ArraySet;

public class GibbsSampling {
	public static Distribution gibbs_ask(RandomVariable X, Assignment e, BayesianNetwork bn, int N) {
		
		Random random = new Random();
		Distribution distribution = new Distribution();
		for(Object object : X.getDomain()) {
			distribution.put(object, 0);
		}
		
		ArrayList<RandomVariable> variables = (ArrayList<RandomVariable>) bn.getVariableListTopologicallySorted();
		ArrayList<RandomVariable> Z = new ArrayList<RandomVariable>(); 
		for(RandomVariable rVariable : variables) {
			if(!e.variableSet().contains(rVariable)) {
				Z.add(rVariable);
			}
		}
		
		Assignment x = e.copy();
		for(RandomVariable rVariable : Z) {
			Domain domain = rVariable.getDomain();
			x.put(rVariable, domain.get(random.nextInt(domain.size())));
		}
		
		for(int i = 0; i < N; i++) {
			for(RandomVariable Zi : Z) {
				//get P(Zi|mb(Zi))
				Distribution temp_dis = new Distribution();
				for(Object object : Zi.getDomain()) {
					x.put(Zi, object);
					Set<RandomVariable> parents = getParents(Zi, bn);
					Assignment temp_ass = getAssignment(parents, x);
					double pi = bn.getProb(Zi, temp_ass);
					for(RandomVariable children : bn.getChildren(Zi)) {
						Set<RandomVariable> parents1 = getParents(children, bn);
						Assignment temp_ass1 = getAssignment(parents1, x);
						pi *= bn.getProb(children, temp_ass1);
					}
					temp_dis.put(object, pi);
				}
				temp_dis.normalize();

				// set the value of Zi in x by sampling from P(Zi|mb(Zi))
				double random_double = random.nextDouble();
				double p = 0;
				for(Object object : Zi.getDomain()) {
					double temp_p = temp_dis.get(object);
					p += temp_p;
					if(random_double <= p) {
						x.put(Zi, object);
						break;
					}
				}
				// N(x) = N(x) + 1 where x is the value of X in x
				Object object = x.get(X);
				distribution.put(object, distribution.get(object) + 1);
			}
		}
		
		distribution.normalize();
		return distribution;
	}
	
	public static Set<RandomVariable> getParents(RandomVariable X,BayesianNetwork bn){
		Set<RandomVariable> parents = new ArraySet<RandomVariable>();
		 for(RandomVariable rv : bn.getVariableList()) {
			 if(bn.getChildren(rv).contains(X)) {
				 parents.add(rv);
			 }
		 }
		 // have to add X !!!
		parents.add(X);
		return parents;
	}
	
	public static Assignment getAssignment(Set<RandomVariable> parents, Assignment e) {
		Assignment assignment = new Assignment();
		for(Map.Entry<RandomVariable, Object> entry : e.entrySet()) {
			for(RandomVariable rVariable : parents) {
				if(entry.getKey().equals(rVariable)) {
					assignment.put(rVariable, entry.getValue());
				}
			}
		}
		return assignment;
	}
}
