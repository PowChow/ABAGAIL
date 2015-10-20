package opt.test;

import java.util.Arrays;

import dist.DiscreteDependencyTree;
import dist.DiscreteUniformDistribution;
import dist.Distribution;

import opt.DiscreteChangeOneNeighbor;
import opt.EvaluationFunction;
import opt.GenericHillClimbingProblem;
import opt.HillClimbingProblem;
import opt.NeighborFunction;
import opt.RandomizedHillClimbing;
import opt.SimulatedAnnealing;
import opt.example.*;
import opt.ga.CrossoverFunction;
import opt.ga.DiscreteChangeOneMutation;
import opt.ga.GenericGeneticAlgorithmProblem;
import opt.ga.GeneticAlgorithmProblem;
import opt.ga.MutationFunction;
import opt.ga.StandardGeneticAlgorithm;
import opt.ga.UniformCrossOver;
import opt.prob.GenericProbabilisticOptimizationProblem;
import opt.prob.MIMIC;
import opt.prob.ProbabilisticOptimizationProblem;
import shared.FixedIterationTrainer;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CountOnesTest {
    /** The n value */
    private static final int N = 80;
    
    public static void main(String[] args) {
    	int it = args.length > 0 ? Integer.parseInt(args[0]): 1000;
    	double start, end, trainingTime, accuracy; 
    	
        int[] ranges = new int[N];
        Arrays.fill(ranges, 2);
        
        EvaluationFunction ef = new CountOnesEvaluationFunction();
        Distribution odd = new DiscreteUniformDistribution(ranges);
        NeighborFunction nf = new DiscreteChangeOneNeighbor(ranges);
        MutationFunction mf = new DiscreteChangeOneMutation(ranges);
        CrossoverFunction cf = new UniformCrossOver();
        Distribution df = new DiscreteDependencyTree(.1, ranges); 
        HillClimbingProblem hcp = new GenericHillClimbingProblem(ef, odd, nf);
        GeneticAlgorithmProblem gap = new GenericGeneticAlgorithmProblem(ef, odd, mf, cf);
        ProbabilisticOptimizationProblem pop = new GenericProbabilisticOptimizationProblem(ef, odd, df);
        
        //RANDOMIZED HILL CLIMBING
        RandomizedHillClimbing rhc = new RandomizedHillClimbing(hcp);      
        FixedIterationTrainer fit = new FixedIterationTrainer(rhc, it);
        //System.out.println(ef.value(rhc.getOptimal()));
        
        start = System.nanoTime();
        fit.train();
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10, 9);
        System.out.printf("%s,%d,%.2f,%.2f\n","RHC",it, trainingTime, ef.value(rhc.getOptimal()));
        
        //SIMULATED ANNEALING
        SimulatedAnnealing sa = new SimulatedAnnealing(100, .95, hcp);
        fit = new FixedIterationTrainer(sa, it);
        //System.out.println(ef.value(sa.getOptimal()));
        
        start = System.nanoTime();
        fit.train();
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10, 9);
        System.out.printf("%s,%d,%.2f,%.2f\n","SA",it, trainingTime, ef.value(sa.getOptimal()));
        
        //GENETIC ALGORITHM
        StandardGeneticAlgorithm ga = new StandardGeneticAlgorithm(20, 20, 0, gap);
        fit = new FixedIterationTrainer(ga, it);
        //System.out.println(ef.value(ga.getOptimal()));
        
        start = System.nanoTime();
        fit.train();
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10, 9);
        System.out.printf("%s,%d,%.2f,%.2f\n","GA",it, trainingTime, ef.value(ga.getOptimal()));
        
        //MIMIC
        MIMIC mimic = new MIMIC(50, 10, pop);
        fit = new FixedIterationTrainer(mimic, it);
        //System.out.println(ef.value(mimic.getOptimal()));
        
        start = System.nanoTime();
        fit.train();
        end = System.nanoTime();
        trainingTime = end - start;
        trainingTime /= Math.pow(10, 9);
        System.out.printf("%s,%d,%.2f,%.2f\n","MIMC",it, trainingTime, ef.value(mimic.getOptimal()));
    }
}