package org.uncommons.watchmaker.examples.geneticprogramming;

public class Division extends BinaryNode {

	public Division(Node left, Node right)
    {
        super(left, right, '/');
    }
	
	@Override
	public double evaluate(double[] programParameters) {
		try {
			return left.evaluate(programParameters) / right.evaluate(programParameters);
		} catch(Exception ex) {
			return 0;
		}
	}

	@Override
	public Node simplify() {
//		Node simplifiedLeft = left.simplify();
//        Node simplifiedRight = right.simplify();
//        
//        // If the two arguments are constants, we can simplify by calculating the result, it won't
//        // ever change.
//        if (simplifiedLeft instanceof Constant && simplifiedRight instanceof Constant)
//        {
//            try {
//        	return new Constant(simplifiedLeft.evaluate(NO_ARGS) / simplifiedRight.evaluate(NO_ARGS));
//            } catch(Exception ex) {
//            	return new Constant(0);
//            }
//        } 
//        else if (simplifiedRight instanceof Constant)
//        {
//            double constant = simplifiedRight.evaluate(NO_ARGS);
//            if (constant == 1)
//            {
//                return simplifiedLeft;
//            }
//            else if (constant == 0)
//            {
//                return new Constant(0);
//            }
//        }
//        else if (simplifiedLeft instanceof Constant)
//        {
//            double constant = simplifiedLeft.evaluate(NO_ARGS);
//            if (constant == 0)
//            {
//                return new Constant(0);
//            }
//        }
//        return simplifiedLeft != left || simplifiedRight != right
//                ? new Division(simplifiedLeft, simplifiedRight)
//                : this;
		return this;
	}

}
