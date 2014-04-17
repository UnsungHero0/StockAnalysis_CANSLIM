package mathematics;

import java.util.ArrayList;

/**
 * Calculating the correlation coefficient of X and Y data series using data from CorrelationCoefficientParameters Class 
 * @author mingjie.ding
 *
 */
public class CorrelationCoefficientCalculator {
	
	public CorrelationCoefficientCalculator() {
	}
	
	/**
	 * 
	 * @param ccp X and Y data
	 * @return
	 */
	public Double getCorrelationCoefficient(ArrayList<Double> xSeries,
			ArrayList<Double> ySeries) {
		
		CorrelationCoefficientParameters ccp = new CorrelationCoefficientParameters(xSeries, ySeries);
		
		Double sumXYDif = 0.0;
		Double sumXDifSquare = 0.0;
		Double sumYDifSquare = 0.0;
		
		for (int i = 0; i < ccp.getxDif().size()-1; i ++) {
			sumXYDif += ccp.getxDif().get(i) * ccp.getyDif().get(i);
			sumXDifSquare += Math.pow(ccp.getxDif().get(i),2);
			sumYDifSquare += Math.pow(ccp.getyDif().get(i),2);
		}

		Double result = sumXYDif / Math.pow(sumXDifSquare * sumYDifSquare, 0.5);
		
		return result;
	}
}
