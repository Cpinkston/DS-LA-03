/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

TwoOptTour approach to the TSP : 
	starts with some tour (generated through random)
	two opt iterates through all possible combinations of swapping two arcs in a tour
	stops once no improvements are made
*/
import java.util.*;

public class TwoOptTour extends Solver{

    ArrayList<double[]> optCities;	//storage for later use


	public TwoOptTour(ArrayList<double[]> cityList){
		super(cityList);
		optCities = cityList;

		RandomSearch rs = new RandomSearch(cities);
		solution = rs.run();
	}

	// optimizes a solution
	private ArrayList<double[]> twoOpt(ArrayList<double[]> solution){
		Boolean improved = true;
		ArrayList<double[]> existing_route = solution;

		int count = 0;

		ArrayList<double[]> new_route;
		double best_distance;
		double new_distance;
	   	while (improved){
	   		count++;
			System.out.print("completed round : "+count+"\r"); // prints something so you don't think it froze
	   		improved = false;
	      	best_distance = totalDistance(existing_route);
	      	for (int i = 0; i < optCities.size(); i++){
	           	for (int k = i + 1; k < optCities.size() - 1; k++){
	               	new_route = twoOptSwap(existing_route, i, k);
	               	new_distance = totalDistance(new_route);
	               	if (new_distance < best_distance){
	                   	existing_route = new_route;
						improved = true;
	               	}
	           	}
	       	}
	    }
	    System.out.print("\n");
	    return existing_route;
	}

	// nonrestricted 2-opt
	private ArrayList<double[]> twoOptSwap(ArrayList<double[]> route,int i,int k){
		ArrayList<double[]> new_route = new ArrayList<double[]>();
		for (int i_i = 0; i_i<=i; i_i++){
			new_route.add(route.get(i_i));
		}
		for (int k_k = k; k_k>i; k_k--){
			new_route.add(route.get(k_k));
		}
		for (int j_j = k+1; j_j<route.size(); j_j++){
			new_route.add(route.get(j_j));
		}
  	return new_route;
   	}

    private double totalDistance(ArrayList<double[]> path){
		double sum = 0;
		double distance = 0;
		for(int i=0; i<path.size()-1; i++){
		    double[] p1 = path.get(i);
		    double[] p2 = path.get(i+1);
		    distance = Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
		    sum += distance;
		}
		double[] p1 = path.get(0);
		double[] p2 = path.get(path.size()-1);
		sum+=Math.sqrt(Math.pow((p1[1]-p2[1]),2)+Math.pow((p1[2]-p2[2]),2));
		return sum;
    }
	
	public ArrayList<double[]> run(){
		ArrayList<double[]> opt_solution = twoOpt(solution);
		return opt_solution;
	}
}