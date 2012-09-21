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
package org.uncommons.watchmaker.examples;

import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.PopulationData;
import org.dave.gp.Node;

/**
 * Trivial evolution observer for displaying information at the end
 * of each generation.
 * @param <T> The type of entity being evolved.
 * @author Daniel Dyer
 */
public class EvolutionLogger<T> implements EvolutionObserver<T>
{
    public void populationUpdate(PopulationData<? extends T> data)
    {
        System.out.println("Generation " + data.getGenerationNumber() + ": Best fitness " + data.getBestCandidateFitness());
        System.out.println("Generation " + data.getGenerationNumber() + ": Mean fitness " + data.getMeanFitness());
        System.out.println(data.getBestCandidate().toString());
        System.out.println(((Node) data.getBestCandidate()).getDepth());
    }
}
