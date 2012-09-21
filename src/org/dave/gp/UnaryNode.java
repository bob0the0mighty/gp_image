//=============================================================================
// Copyright 2006-2010 Daniel W. Dyer
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//=============================================================================
package org.dave.gp;

import java.lang.reflect.Constructor;
import java.util.Random;

import org.dave.TreeFactory;
import org.uncommons.maths.random.Probability;
import org.uncommons.util.reflection.ReflectionUtils;

/**
 * Convenient base class for {@link Node}s that have two sub-trees.
 * @author Daniel Dyer
 */
public abstract class UnaryNode implements Node
{
    protected static final double[] NO_ARGS = new double[0];

    /** The first argument to the binary function. */
    protected final Node arg;
    
    private final String symbol;


    /**
     * @param arg The argument to the unary function.
     * @param symbol A single character that indicates the type of function.
     */
    protected UnaryNode(Node arg, String symbol)
    {
        this.arg = arg;
        this.symbol = symbol;
    }


    /**
     * {@inheritDoc}
     */
    public String getLabel()
    {
        return String.valueOf(symbol);
    }


    /**
     * The arity of a unary node is one.
     * @return 1
     */
    public int getArity()
    {
        return 1;
    }


    /**
     * The depth of a unary node is the depth of its sub-tree plus one.
     * @return The depth of the tree rooted at this node.
     */
    public int getDepth()
    {
        return 1 + arg.getDepth();
    }


    /**
     * The width of a unary node is the width of its sub-tree.
     * @return The width of the tree rooted at this node.
     */
    public int getWidth()
    {
        return arg.getWidth();
    }


    /**
     * {@inheritDoc}
     */
    public int countNodes()
    {
        return 1 + arg.countNodes();
    }


    /**
     * {@inheritDoc}
     */
    public Node getNode(int index)
    {
        if (index == 0)
        {
            return this;
        }
        else
        {
            return arg.getNode(index - 1);
        }
    }


    /**
     * {@inheritDoc}
     */
    public Node getChild(int index)
    {
        switch (index)
        {
            case 0: return arg;
            default: throw new IndexOutOfBoundsException("Invalid child index: " + index);
        }
    }


    /**
     * {@inheritDoc}
     */
    public Node replaceNode(int index, Node newNode)
    {
        if (index == 0)
        {
            return newNode;
        }
        else
        {
            return newInstance(arg.replaceNode(index - 1, newNode));
        }
    }



    /**
     * {@inheritDoc} 
     */
    public String print()
    {
        StringBuilder buffer = new StringBuilder("(");
        buffer.append(symbol);
        buffer.append(' ');
        buffer.append('(');
        buffer.append(arg.print());
        buffer.append(')');
        buffer.append(' ');
        buffer.append(')');
        return buffer.toString();
    }


    /**
     * {@inheritDoc}
     */
    public Node mutate(Random rng, Probability mutationProbability, TreeFactory treeFactory)
    {
        if (mutationProbability.nextEvent(rng))
        {
            return treeFactory.generateRandomCandidate(rng);
        }
        else
        {
            Node newArg = arg.mutate(rng, mutationProbability, treeFactory);
            if (newArg != arg)
            {
                return newInstance(newArg);
            }
            else
            {
                // Tree has not changed.
                return this;
            }
        }
    }


    private Node newInstance(Node newArg)
    {
        Constructor<? extends UnaryNode> constructor = ReflectionUtils.findKnownConstructor(this.getClass(),
                                                                                             Node.class);
        return ReflectionUtils.invokeUnchecked(constructor, newArg);
    }


    @Override
    public String toString()
    {
        return print();
    }
}
