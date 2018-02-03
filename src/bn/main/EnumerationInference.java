package bn.main;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.algorithm.BayesianFilePath;
import bn.algorithm.ResultOutput;
import bn.algorithm.EnumerationAlgorithm;
import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.RandomVariable;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;

public class EnumerationInference {

	public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
		
		if (args.length < 2 || args.length % 2 != 0) {
			System.out.println("Not correct number of parameter");
			return;
		}
		
		int i = 0;
		String filename = args[i++];
		String []temp_str = filename.split("\\.");
		BayesianNetwork bn;
		
		if(temp_str[temp_str.length-1].equals("bif")) {
			BIFParser parser = new BIFParser(new FileInputStream(BayesianFilePath.bayesian_file_path + filename));
			bn = parser.parseNetwork();
		}else {
			XMLBIFParser xml_file = new XMLBIFParser();
			bn = xml_file.readNetworkFromFile(BayesianFilePath.bayesian_file_path + filename);
		}
		bn.print();

		RandomVariable X = bn.getVariableByName(args[i++]);
		
		String conditionString = "\nCondition: ";
		Assignment assignment = new Assignment();
		while (i < args.length) {
			String variable = args[i];
			RandomVariable randomVariable = bn.getVariableByName(variable);
			i += 1;
			
			String value = args[i];
			i += 1;
			assignment.set(randomVariable, value);
			
			conditionString += variable + "=" + value + " ";
			
		}
		System.out.println(conditionString);

		long startTime = System.currentTimeMillis();
		Distribution distribution = EnumerationAlgorithm.enumeration_ask(
				X, assignment, bn);
		long endtime = System.currentTimeMillis();
		System.out.println("Runtime:" + (endtime-startTime) + "ms");
		ResultOutput.printResult(distribution, X);
	}

}
