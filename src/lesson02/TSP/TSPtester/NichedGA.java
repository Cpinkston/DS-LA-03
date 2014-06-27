/*
CS 361 Evolutionary Computing and Artificial Intelligence
Final Project
Prof. Sherri Goings
Naozumi Hiranuma, Yasin Dara, and Evan Albright 

Comparative Study of the Application of Evolutionary Computing Strategies to the Traveling Salesman Problem

NichedGA approach to the TSP : 
	TALK ABOUT ALGORITHM HERE
*/
import java.util.*;

public class NichedGA extends Solver{
	
    //popsize*gen=100000
    int popsize = 500;
    int gen = 250;
    ArrayList<PathGene> population;
    
    //Randomly initiate a population upop instantiation.
    public NichedGA(ArrayList<double[]> cityList){
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
    public ArrayList<PathGene> FPS(double scale, int nicheradius){
	ArrayList<PathGene> newpop = new ArrayList<PathGene>(popsize);
	double worst = findWorst();
	
	//find the sum of the fitness
	//the worst individual has a fintess of 0 and everythign else has worst-pathlength.
	double sumfit = 0;
	for(int i=0; i<popsize; i++){
	    sumfit += adjustFitness(worst-population.get(i).fitness, scale, nicheradius, population.get(i));
	}
	
	//If sumfit is trivial, just return as it is.
	if((sumfit ==0 && scale<=1) || (scale>1 && sumfit==popsize)) return population;
	
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
		curSumFit += adjustFitness(worst-population.get(curPopIndex).fitness, scale, nicheradius, population.get(curPopIndex));
	    }
	}
	return newpop;
    }
    
    //Finds Levenshtein distance between two ArrayLists.
    //I modified the code found in here http://rosettacode.org/wiki/Levenshtein_distance#Java
    //so that it can deal with lists no strings.
    public int computeDistance(PathGene p1, PathGene p2) {
 
	int[] costs = new int[p2.genome.size() + 1];

	for (int i = 0; i <= p1.genome.size(); i++) {
	    int lastValue = i;
	    for (int j = 0; j <= p2.genome.size(); j++) {
		if (i == 0)
		    costs[j] = j;
		else {
		    if (j > 0) {
			int newValue = costs[j - 1];
			if (p1.genome.get(i - 1)[0] != p2.genome.get(j - 1)[0])
			    newValue = Math.min(Math.min(newValue, lastValue), costs[j]) + 1;
			costs[j - 1] = lastValue;
			lastValue = newValue;
		    }
		}
	    }
	    if (i > 0)
		costs[p2.genome.size()] = lastValue;
	}
	//System.out.println(costs[p2.genome.size()]);
	return costs[p2.genome.size()];
    }
    //Finds hamming distance between two ArrayLists.
    public int computeDistance2(PathGene p1, PathGene p2) {
 	int dif = 0;
	/*
	for(int i=0; i<p1.genome.size(); i++){
	    System.out.print((int)p1.genome.get(i)[0]+",");
	}
	System.out.println();
	
	for(int i=0; i<p1.genome.size(); i++){
	    System.out.print((int)p2.genome.get(i)[0]+",");
	    }*/
	for(int i=0; i<p1.genome.size(); i++){
	    if(p1.genome.get(i)[0]!=p2.genome.get(i)[0]){
		dif+=1;
	    }
	}
	//System.out.print(dif);
	return dif;
    }

    public double adjustFitness(double fitness, double scale, int nicheradius, PathGene self){
	if(scale>1) fitness = Math.pow(scale,fitness);
	if(nicheradius>0){
	    double nicheCount = 0;
	    for(int i=0; i<popsize; i++){
		int dif = computeDistance2(self, population.get(i));
		//System.out.println(dif);
		if (dif < nicheradius){
		    nicheCount += 1-(double)dif/nicheradius;
		}
	    }
	    if(nicheCount<1) nicheCount = 1;
	    fitness = fitness/nicheCount;
	}
	return fitness;
    }

    public ArrayList<PathGene> crossover(int th){
	// shuffuling the population
	Collections.shuffle(population);
	for(int i=0; i<popsize; i+=2){
	    for(int j=i+1; j<popsize; j++){
		if(computeDistance2(population.get(i),population.get(j))<th){
		    PathGene temp = population.get(j);
		    population.set(j,population.get(i+1));
		    population.set(i,temp);
		}
	    }
	}
	//Crossover
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
	return newpop;
    }

    public void analyze(){
	int sum1=0;
	int sum2=0;
	int sum3=0;
	int sum4=0;
	for(int i=0; i<population.size();i++){
	    int dif = computeDistance(population.get(0),population.get(i));
	    if(dif>50) sum1+=1;
	    else if(dif>25) sum2+=1;
	    else if(dif>14) sum3+=1;
	    else sum4+=1;
	}
	System.out.println(sum1+" "+sum2+" "+sum3+" "+sum4);
    }

    public ArrayList<double[]> run(){
	
	for(int i=0; i<gen; i++){
	    //evaluation
	    evaluate();
	    //selection (a scaling factor of 1.7 seemed to perform the best.)
	    population = FPS(1.3, 15);
	    //crossover (restricted)
	    population = crossover(15);
	    //mutation
	    for(int j=0; j<popsize; j++){
		population.get(j).mutate();
		population.get(j).mutate();
		population.get(j).mutate();
	    }
	    evaluate();
		System.out.print("completed percent : "+(float)i/gen*100+"\r"); // prints something so you don't think it froze

	    //System.out.println("gen "+ i + " the best is: " + findBest().fitness);
	    //analyze();
	}
	System.out.print("\n");
	/*
	System.out.println("\nBestgene is....");
	
	for(int i=0; i<findBest().genome.size(); i++){
	    System.out.println((int)findBest().genome.get(i)[0]);
	    }*/
	evaluate();
	PathGene sol = findBest();
	return sol.genome;
    }
}