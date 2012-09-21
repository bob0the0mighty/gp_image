package org.dave.ops;

import org.dave.gp.Constant;
import org.dave.gp.Node;
import org.dave.gp.UnaryNode;

public class Absolute extends UnaryNode {

	
	public Absolute(Node arg) {
		super(arg, "abs");
	}

	@Override
	public double evaluate(double[] programParameters) {
		return Math.abs(arg.evaluate(programParameters));
	}

	@Override
	public Node simplify() {
		Node simplifiedArg = arg.simplify();
		if (simplifiedArg instanceof Constant)
        {
            return new Constant(Math.abs(simplifiedArg.evaluate(NO_ARGS)));
        }
		
		if (simplifiedArg != arg)
        {
            return new Absolute(simplifiedArg);
        }
        
        return this;
        
	}

}
