/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

Helper class for SwarmIntelligence.java :
	Ants store their path and the fitness of the path, useful for knowing the pheromones to lay down.
*/
import java.util.*;

public class Ant{
    ArrayList<double[]> path;
    double fitness;

    public Ant(ArrayList<double[]> cityList){
    	//initialize path to be duplicate of city list
		path = new ArrayList<double[]>(cityList);
		//fitness here is synomymous with total distance
		fitness = 0;
	}


    //calculates the length of the Ant
    public double evaluate(){
		double sum = 0;
		double distance = 0;
		for(int i=0; i<path.size()-1; i++){
		    double[] p1 = path.get(i);
		    double[] p2 = path.get(i+1);
		    distance = Math.sqrt(Math.pow(Math.abs(p1[1]-p2[1]),2)+Math.pow(Math.abs(p1[2]-p2[2]),2));
		    sum += distance;
		}
		double[] p1 = path.get(0);
		double[] p2 = path.get(path.size()-1);
		sum+=Math.sqrt(Math.pow(Math.abs(p1[1]-p2[1]),2)+Math.pow(Math.abs(p1[2]-p2[2]),2));
		fitness = sum;
		return sum;
    }
	
	
}