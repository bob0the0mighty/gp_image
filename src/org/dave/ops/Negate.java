package org.dave.ops;

import org.dave.gp.Constant;
import org.dave.gp.Node;
import org.dave.gp.UnaryNode;

public class Negate extends UnaryNode {

	
	public Negate(Node arg) {
		super(arg, "neg");
	}

	@Override
	public double evaluate(double[] programParameters) {
		return -(arg.evaluate(programParameters));
	}

	@Override
	public Node simplify() {
		Node simplifiedArg = arg.simplify();
		if (simplifiedArg instanceof Constant)
        {
            return new Constant(-(simplifiedArg.evaluate(NO_ARGS)));
        }
		
		if (simplifiedArg != arg)
        {
            return new Negate(simplifiedArg);
        }
        
        return this;
        
	}

}
