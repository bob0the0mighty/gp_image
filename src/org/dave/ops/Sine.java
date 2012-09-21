package org.dave.ops;

import org.dave.gp.Constant;
import org.dave.gp.Node;
import org.dave.gp.UnaryNode;

public class Sine extends UnaryNode {

	
	public Sine(Node arg) {
		super(arg, "sin");
	}

	@Override
	public double evaluate(double[] programParameters) {
		return Math.sin(arg.evaluate(programParameters));
	}

	@Override
	public Node simplify() {
		Node simplifiedArg = arg.simplify();
		if (simplifiedArg instanceof Constant)
        {
            return new Constant(Math.sin(simplifiedArg.evaluate(NO_ARGS)));
        }
		
		if (simplifiedArg != arg)
        {
            return new Sine(simplifiedArg);
        }
        
        return this;
        
	}

}
