/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

SwarmIntelligence approach to the TSP : 
	First round -
		Ants randomly choose paths as no pheromones are laid down for influence
		A local optimizer is run on the ants to improve their results
		The ants lay down pheromones based on the fitness of their path
	Future rounds -
		Ants choose paths based on probability distribution of the pheromone levels
		A local optimizer is run on the ants to improve their results
		The ants lay down pheromones based on the fitness of their path

TODO :
	pheromone system - double triple check math

	restrict 2-opt to be more local (currently a huge process)
*/
import java.util.*;



public class SwarmIntelligence extends Solver{

    int popsize;	//number of ants to employ
    int gen;	//number of rounds to run the algorithm
    double default_pheromone; //effects exploration on unused edges

    HashMap<ArrayList<double[]>,Double> trails;	//stores the state of the graph
    ArrayList<Ant> ants;	//stores the ant generation until they're consumed to update the trail pheromones
    ArrayList<Ant> improved_ants;	//stores the improved ant generation
    ArrayList<double[]> antCities;	//storage for later use
    double p_var; //local pheromone decay value
    double t_0;	// t_0 = 1/(n * L_nn)
    double delta_t; // delta_t = 1/L+


	public SwarmIntelligence(ArrayList<double[]> cityList){
		super(cityList);
		
		popsize = 2;
    	gen = 2;	


		antCities = cityList;
		trails = new HashMap<ArrayList<double[]>,Double>();	//pheromones here
		ants = new ArrayList<Ant>();	//ants here

		// p
		p_var = 0.1;
		// t_0
		NearestNeighbor NN = new NearestNeighbor(cities);
		solution = NN.run();
		t_0 = 1/(antCities.size()*totalDistance(solution));

		default_pheromone = 1/totalDistance(solution);
	}


	private ArrayList<Ant> constructAntSolutions(){
		ants.clear();
		for (int i=0; i<popsize; i++){
			//Initialize covered & uncovered nodes
			ArrayList<double[]> chosen_cities = new ArrayList<double[]>();
			chosen_cities.add(antCities.get(0)); //keep city 1 as starting point
			ArrayList<double[]> unchosen_cities = new ArrayList<double[]>(antCities); //copy over all cities
			unchosen_cities.remove(0); //remove city 1 as it's already chosen to be the start

			//Process the path choices
			double[] next_city;
			double[] current_city = antCities.get(0);
			
			while (unchosen_cities.size() > 0){
				next_city = pickPath(current_city,unchosen_cities);
				chosen_cities.add(next_city);
				unchosen_cities.remove(unchosen_cities.indexOf(next_city));

				current_city = next_city;
			}

			//Store the path
			Ant ant = new Ant(chosen_cities);
			ants.add(ant);
		}
		return ants;
	}


	private double[] pickPath(double[] current_city,ArrayList<double[]> unchosen_cities){
		Random rgen = new Random();
		double[] probabilities = new double[unchosen_cities.size()];
		double sum_pheromones = 0;

		// CALCULATE PROBABILITY OF CHOOSING A PATH
		for (int i = 0; i<unchosen_cities.size(); i++){
			ArrayList<double[]> city_pair = new ArrayList<double[]>(2);
			city_pair.add(current_city);
			city_pair.add(unchosen_cities.get(i));

			if (trails.containsKey(city_pair)){
				double pheromone = trails.get(city_pair);
				sum_pheromones += pheromone;

				probabilities[i] = pheromone;
			}
			else{
				double pheromone = default_pheromone;
				sum_pheromones += pheromone;

				probabilities[i] = pheromone;
			}
		}

		//System.out.println(sum_pheromones);
		// PICK THE NEXT CITY USING WEIGHTS
		int i = 0;
		double w = probabilities[0];
		double[] v = unchosen_cities.get(0);
		double x = sum_pheromones * (1 - rgen.nextDouble());
		sum_pheromones -= x;
		while (x > w){
			x -= w;
			i += 1;
			w = probabilities[i];
			v = unchosen_cities.get(i);
		}

		// UPDATE LOCAL PHEROMONE BASED ON CHOSEN PATH
		localPheromoneUpdate(current_city,v);

		return v;
	}

	// NON RESTRICTED
	private void applyLocalSearch(Ant ant){
		ant.evaluate();
		Boolean improved = true;
		ArrayList<double[]> existing_route = ant.path;
		double best_distance;
		ArrayList<double[]> new_route;
		double new_distance;
	   	while (improved){
	   		improved = false;
	      	best_distance = totalDistance(existing_route);
	      	for (int i = 0; i < antCities.size(); i++){
	           	for (int k = i + 1; k < antCities.size() - 1; k++){
	               	new_route = twoOptSwap(existing_route, i, k);
	               	new_distance = totalDistance(new_route);
	               	if (new_distance < best_distance){
	                   	existing_route = new_route;
						improved = true;
	               	}
	           	}
	       	}
	    }
	    ant.path = existing_route;
	    ant.evaluate();
	}
	// NONRESTRICTED HUGE SEARCH
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
		// 1. take route[0] to route[i-1] and add them in order to new_route
		// 2. take route[i] to route[k] and add them in reverse order to new_route
		// 3. take route[k+1] to end and add them in order to new_route
  	return new_route;
   	}

	// assumedly working
	private void localPheromoneUpdate(double[] c, double[] v){
		// IDENTIFY PATH CHOSEN
		ArrayList<double[]> edge = new ArrayList<double[]>(2);
		edge.add(c);
		edge.add(v);

		// LOCAL PHEROMONE UPDATE
		// edge_ij(time) <- (1 − p) * edge_ij(t) + ρ * t_0
		// t_0 = 1/(n * L_nn)
		double local_update;
		if (trails.containsKey(edge)){
			local_update = ((1-p_var)*trails.get(edge)) + (p_var*t_0);
		}
		else{
			local_update = ((1-p_var)*default_pheromone) + (p_var*t_0);
		}	
		trails.put(edge,local_update);
	}

	// assumedly working, only the best ant updates
	private void globalPheromoneUpdate(){
		
		Ant ant = FindBestAnt(); 
		//System.out.println(ant.fitness);
		delta_t = ant.fitness;

		// LOOP THROUGH ANT PATH
		for (int i = 0; i<ant.path.size()-1; i++){
			ArrayList<double[]> edge = new ArrayList<double[]>(2);
			edge.add(ant.path.get(i));
			edge.add(ant.path.get(i+1));

			// APPLY GLOBAL UPDATES
			// edge_ij(time) <- (1 − p) * edge_ij(t) + ρ * delta_t
			// delta_t = 1/L+

			double global_update;
			if (trails.containsKey(edge)){
				global_update = ((1-p_var)*trails.get(edge)) + (p_var*delta_t);
			}
			else{
				global_update = ((1-p_var)*default_pheromone) + (p_var*delta_t);
			}	
			trails.put(edge,global_update);		
		}
	}
	
	// returns the best ant based on fitness
	private	Ant FindBestAnt(){
		double min = ants.get(0).fitness;
		Ant best_ant = ants.get(0);
		for (int i = 0; i<ants.size(); i++){
			if (ants.get(i).fitness < min){
				min = ants.get(i).fitness;
				best_ant = ants.get(i);
			}
		}
		return best_ant;
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

	// run function
	public ArrayList<double[]> run(){
		System.out.println("processing... please wait.");
		for(float i=0; i<gen; i++){
			ants = constructAntSolutions();	//generates ants
			for (int k=0; k<ants.size(); k++) applyLocalSearch(ants.get(k)); //improves them
			//for (int j=0; j<ants.size(); j++) ants.get(j).evaluate(); //reevaluates (REDUNDANT?)
		 	globalPheromoneUpdate(); 	//updates the map based on ants
			System.out.printf("completed %.1f\r", (i+1)/gen*100);
		}
		System.out.print("\n");
		Ant solution = FindBestAnt();
		return solution.path;
	}
}