package org.dave.ops;

import org.dave.gp.Constant;
import org.dave.gp.Node;
import org.dave.gp.UnaryNode;

public class Tangent extends UnaryNode {

	
	public Tangent(Node arg) {
		super(arg, "tan");
	}

	@Override
	public double evaluate(double[] programParameters) {
		return Math.tan(arg.evaluate(programParameters));
	}

	@Override
	public Node simplify() {
		Node simplifiedArg = arg.simplify();
		if (simplifiedArg instanceof Constant)
        {
            return new Constant(Math.tan(simplifiedArg.evaluate(NO_ARGS)));
        }
		
		if (simplifiedArg != arg)
        {
            return new Tangent(simplifiedArg);
        }
        
        return this;
        
	}

}
