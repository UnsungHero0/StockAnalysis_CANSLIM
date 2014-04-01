package com.analysis.mathematics;

import java.util.ArrayList;

/**
 * 
 * @author mingjie.ding
 *
 */
public class CorrelationCoefficientParameters {

	/**
	 * 
	 * @param xSeries raw X data series 
	 * @param ySeries raw y data series
	 * @param xAve x average
	 * @param yAve y average
	 * @param xDif (x - xAve) series
	 * @param yDif (y - yAve) series
	 */
	private ArrayList<Double> xSeries = new ArrayList<>();
	private ArrayList<Double> ySeries = new ArrayList<>();
	private Double xAve = 0.0;
	private Double yAve = 0.0;
	private ArrayList<Double> xDif = new ArrayList<>();
	private ArrayList<Double> yDif = new ArrayList<>();

	
	public CorrelationCoefficientParameters(ArrayList<Double> xSeries,
			ArrayList<Double> ySeries) {
		
		setxSeries(xSeries);
		setySeries(ySeries);
		setxAve(getAverage(xSeries));
		setyAve(getAverage(ySeries));
		setxDif(getDif(getxSeries()));
		setyDif(getDif(getySeries()));
		
	}

	public Double getAverage(ArrayList<Double> dataSeries) {

		Double sum = 0.0;
		for (Double element : dataSeries) {
			sum = sum + element;
		}
		return sum / Double.valueOf(dataSeries.size());

	}
	
	public ArrayList<Double> getDif(ArrayList<Double> dataSeries) {
		
		ArrayList<Double> result = new ArrayList<>();
		
		for (Double element : dataSeries) {
			result.add(element - getxAve());
		}
		
		return result;
	}

	public ArrayList<Double> getxSeries() {
		return xSeries;
	}

	public void setxSeries(ArrayList<Double> xSeries) {
		this.xSeries = xSeries;
	}

	public ArrayList<Double> getySeries() {
		return ySeries;
	}

	public void setySeries(ArrayList<Double> ySeries) {
		this.ySeries = ySeries;
	}

	public Double getxAve() {
		return xAve;
	}

	public void setxAve(Double xAve) {
		this.xAve = xAve;
	}

	public Double getyAve() {
		return yAve;
	}

	public void setyAve(Double yAve) {
		this.yAve = yAve;
	}

	public ArrayList<Double> getxDif() {
		return xDif;
	}

	public void setxDif(ArrayList<Double> xDif) {
		this.xDif = xDif;
	}

	public ArrayList<Double> getyDif() {
		return yDif;
	}

	public void setyDif(ArrayList<Double> yDif) {
		this.yDif = yDif;
	}

}
