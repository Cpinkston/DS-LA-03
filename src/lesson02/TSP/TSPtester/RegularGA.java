/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

RegularGA approach to the TSP : 
	TALK ABOUT ALGORITHM HERE
*/
import java.util.*;

public class RegularGA extends Solver{
    //popsize*gen=100000
    int popsize = 400;
    int gen = 250;
    ArrayList<PathGene> population;
    
    //Randomly initiate a population upop instantiation.
    public RegularGA(ArrayList<double[]> cityList){
	super(cityList);
	population = new ArrayList<PathGene>();
	for(int i=0; i<popsize; i++){
	    PathGene temp = new PathGene(cityList);
	    population.add(temp);
	}
    }
    
    //Finds an individual with the worst fitness and returns the fitness value.
    public double findWorst(){
	PathGene max = population.get(0);
	for(int i=1; i<popsize; i++){
	    if(population.get(i).fitness>max.fitness){
		max = population.get(i);
	    }
	}
	return max.fitness;
    }

    public PathGene findBest(){
	PathGene min = population.get(0);
	for(int i=1; i<popsize; i++){
	    if(population.get(i).fitness<min.fitness){
		min = population.get(i);
	    }
	}
	return min;
    }

    //Evaluate a whole population and assign fitness values.
    public void evaluate(){
	for(int i=0; i<popsize; i++){
	    PathGene temp = population.get(i);
	    temp.fitness = temp.evaluate();
	}
    }
    
    //Conduct a fitness proportional selection of the current population.
    //Inspired by the code written by Sherri Goings. 
    public ArrayList<PathGene> FPS(double scale){
	ArrayList<PathGene> newpop = new ArrayList<PathGene>(popsize);
	double worst = findWorst();
	
	//find the sum of the fitness
	//the worst individual has a fintess of 0 and everythign else has worst-pathlength.
	double sumfit = 0;
	for(int i=0; i<popsize; i++){
	    sumfit += adjustFitness(worst-population.get(i).fitness, scale);
	}
	
	//If sumfit = 0, just return as it is.
	if(sumfit ==0) return population;
	
	//Conduct FPS selection
	double space = sumfit/popsize;
	double curChoicePoint = space/2;
	double curSumFit = 0;
	int curPopIndex = -1;
	int newPopIndex = 0;

	while(newPopIndex < popsize){
	    if (curSumFit >= curChoicePoint){
		newpop.add(new PathGene(population.get(curPopIndex)));
		newPopIndex ++;
		curChoicePoint += space;
	    }else{
		curPopIndex ++;
		curSumFit += adjustFitness(worst-population.get(curPopIndex).fitness, scale);
	    }
	}
	return newpop;
    }

    public double adjustFitness(double fitness, double scale){
	if(scale>1) fitness = Math.pow(scale,fitness);
	return fitness;
    }

    public ArrayList<double[]> run(){
	
	for(int i=0; i<gen; i++){
	    //evaluation
	    evaluate();
	    //selection (a scaling factor of 1.7 seemed to perform the best.)
	    population = FPS(1.7);
	    //crossover
	    Collections.shuffle(population);
	    ArrayList<PathGene> newpop = new ArrayList<PathGene>();
	    for(int j=0; j<popsize; j+=2){
		PathGene parent1 = population.get(j);
		PathGene parent2 = population.get(j+1);
		PathGene child1 = new PathGene(cities);
		PathGene child2 = new PathGene(cities);
		child1.genome = parent1.crossover(parent2);
		child2.genome = parent2.crossover(parent1);
		newpop.add(child1);
		newpop.add(child2);
	    }
	    //System.out.println(newpop.size());
	    population = newpop;
	    //mutation
	    for(int j=0; j<popsize; j++){
		population.get(j).mutate();
	    }
	    //evaluate();
	    //System.out.println("gen "+ i + " the best is: " + findBest().fitness);
	}
	/*
	System.out.println("\nBestgene is....");
	
	for(int i=0; i<findBest().genome.size(); i++){
	    System.out.println((int)findBest().genome.get(i)[0]);
	    }*/
	evaluate();
	PathGene sol = findBest();
	System.out.println("The Minimum travel distance is " + sol.fitness);
	return sol.genome;
    }
}