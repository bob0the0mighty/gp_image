package org.dave;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dave.gp.Node;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;

public class GeneticImageOperator implements EvolutionaryOperator<GeneticImage> {

	private EvolutionPipeline<Node> nodePipe;
	
	public GeneticImageOperator(EvolutionPipeline<Node> pipe){
		nodePipe = pipe;
	}
	
	@Override
	public List<GeneticImage> apply(List<GeneticImage> candidates, Random rng) {
		for(GeneticImage gi : candidates){
			Node node = gi.getFunction();
			List<Node> nl = new ArrayList<Node>();
			nl.add(node);
			nl = nodePipe.apply(nl, rng);
			gi.setFunction(nl.get(0));
		}
		return candidates;
	}

}
