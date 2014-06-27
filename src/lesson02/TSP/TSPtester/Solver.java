/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

Helper class for all algorithms :
	allows the run(), solution and cities to be accessed and graphed more readily
*/
import java.util.*;

public abstract class Solver{
	protected ArrayList<double[]> cities;
	protected ArrayList<double[]> solution;
	
	public Solver(ArrayList<double[]> cityList){
		this.cities = cityList;
		this.solution =  new ArrayList<double[]>();
	}
	
	abstract public ArrayList<double[]> run();
}