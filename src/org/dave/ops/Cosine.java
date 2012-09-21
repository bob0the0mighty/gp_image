package org.dave.ops;

import org.dave.gp.Constant;
import org.dave.gp.Node;
import org.dave.gp.UnaryNode;

public class Cosine extends UnaryNode {

	
	public Cosine(Node arg) {
		super(arg, "cos");
	}

	@Override
	public double evaluate(double[] programParameters) {
		return Math.cos(arg.evaluate(programParameters));
	}

	@Override
	public Node simplify() {
		Node simplifiedArg = arg.simplify();
		if (simplifiedArg instanceof Constant)
        {
            return new Constant(Math.cos(simplifiedArg.evaluate(NO_ARGS)));
        }
		
		if (simplifiedArg != arg)
        {
            return new Cosine(simplifiedArg);
        }
        
        return this;
        
	}

}
