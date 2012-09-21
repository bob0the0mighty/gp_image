package org.dave;

import java.util.Random;

import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

public class GeneticImageFactory extends AbstractCandidateFactory<GeneticImage> {
	
	private final int width, height;
	
	// The number of program parameters that each program tree will be provided.
    private final int parameterCount;

    // The maximum depth of a program tree.  No function nodes will be created below
    // this depth (branches will be terminated with parameters or constants).
    private final int maximumDepth;

    // Probability that a created node is a function node rather
    // than a value node.
    private final Probability functionProbability;

    // Probability that a value (non-function) node is a parameter
    // node rather than a constant node.
    private final Probability parameterProbability;
    
    private final TreeFactory nodeMaker;
	
    /**
     * @param parameterCount The number of program parameters that each
     * generated program tree can will be provided when executed.
     * @param maxDepth The maximum depth of generated trees.
     * @param functionProbability The probability (between 0 and 1) that a
     * randomly-generated node will be a function node rather than a value
     * (parameter or constant) node.
     * @param parameterProbability The probability that a randomly-generated
     * non-function node will be a parameter node rather than a constant node.
     */
    public GeneticImageFactory(int width,
    						int height,
						   	int maxDepth,
						   	Probability funcProb,
						   	Probability paramProb)
    { 
        if (maxDepth < 1)
        {
            throw new IllegalArgumentException("Max depth must be at least 1.");
        }

        this.width = width;
        this.height = height;
        this.parameterCount = 2;
        this.maximumDepth = maxDepth;
        this.functionProbability = funcProb;
        this.parameterProbability = paramProb;
        this.nodeMaker = new TreeFactory(parameterCount, maximumDepth, functionProbability, parameterProbability);
    }

    
	@Override
	public GeneticImage generateRandomCandidate(Random rng) {
		return new GeneticImage(nodeMaker.generateRandomCandidate(rng), height, width);
	}

}
