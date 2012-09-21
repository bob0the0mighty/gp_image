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
package org.dave;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.swing.SpringUtilities;
import org.uncommons.swing.SwingBackgroundTask;
import org.dave.EvolutionLogger;
import org.dave.gp.Node;
import org.dave.gp.ops.Simplification;
import org.dave.gp.ops.TreeCrossover;
import org.dave.gp.ops.TreeMutation;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.interactive.InteractiveSelection;
import org.uncommons.watchmaker.framework.interactive.Renderer;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import org.dave.SwingConsole;

/**
 * Watchmaker Framework implementation of Dawkin's biomorph program. 
 * @author Daniel Dyer
 */
@SuppressWarnings("serial")
public class GPImageApplet extends AbstractExampleApplet
{
	private static int WIDTH = 600;
	private static int HEIGHT = 800;
    private Renderer<GeneticImage, JComponent> renderer;
    private SwingConsole console;
    private JDialog selectionDialog;
    private JPanel GeneticImageHolder;
    private GeneticImage last;


    /**
     * Initialize and layout the GUI.
     * @param container The Swing component that will contain the GUI controls.
     */
    @Override
    protected void prepareGUI(Container container)
    {
        renderer = new SwingFunctionRenderer();
        console = new SwingConsole(5);
        selectionDialog = new JDialog((JFrame) null, "Image Selection", true);
        GeneticImageHolder = new JPanel(new GridLayout(1, 1));

        container.add(new ControlPanel(), BorderLayout.WEST);
        container.add(GeneticImageHolder, BorderLayout.CENTER);
        GeneticImageHolder.setBorder(BorderFactory.createTitledBorder("Last Evolved Image"));
        GeneticImageHolder.add(new JLabel("Nothing generated yet.", JLabel.CENTER));
        selectionDialog.add(console, BorderLayout.CENTER);
        selectionDialog.setSize(HEIGHT, WIDTH);
        selectionDialog.validate();
    }


    /**
     * Helper method to create a background task for running the interactive evolutionary
     * algorithm.
     * @param populationSize How big the population used by the created evolution engine
     * should be.
     * @param generationCount How many generations to use when the evolution engine is
     * invoked.
     * @param random If true use random mutation, otherwise use Dawkins mutation.
     * @return A Swing task that will execute on a background thread and update
     * the GUI when it is done.
     */
    private SwingBackgroundTask<GeneticImage> createTask(final int populationSize,
                                                     final int generationCount)
    {
        return new SwingBackgroundTask<GeneticImage>()
        {
            @Override
            protected GeneticImage performTask()
            {
            	GeneticImageFactory factory = new GeneticImageFactory(WIDTH,
            			HEIGHT,
                        5, // Maximum depth of generated trees.
                        Probability.EVENS, // Probability that a GeneticImage is a function GeneticImage.
                        new Probability(0.5d)); // Probability that other GeneticImages are params rather than constants.
				List<EvolutionaryOperator<GeneticImage>> operators = new ArrayList<EvolutionaryOperator<GeneticImage>>(3);
				operators.add(new TreeMutation(factory, new Probability(0.2d)));
				operators.add(new TreeCrossover());
				operators.add(new Simplification());
				InteractiveSelection<GeneticImage> selection = new InteractiveSelection<GeneticImage>(console,
                                                                                      renderer,
                                                                                      populationSize,
                                                                                      1);
				EvolutionEngine<GeneticImage> engine = new GenerationalEvolutionEngine<GeneticImage>(factory,
                                                       new EvolutionPipeline<GeneticImage>(operators),
                                                       selection,
                                                       new MersenneTwisterRNG());
				engine.addEvolutionObserver(new EvolutionLogger<GeneticImage>());
            	
                engine.addEvolutionObserver(new GenerationTracker());
                return engine.evolve(populationSize,
                                     1,
                                     new GenerationCount(generationCount));
            }

            @Override
            protected void postProcessing(GeneticImage result)
            {
                selectionDialog.setVisible(false);
                last = result;
                System.out.println("Result Func: " + last.toString());
                System.out.println("Result HASH: " + last.hashCode());
                GeneticImageHolder.removeAll();
                GeneticImageHolder.add(renderer.render(result));
                GeneticImageHolder.revalidate();
                System.out.println(result);
                
            }
        };
    }


    /**
     * Entry point for running this example as an application rather than an applet.
     * @param args Program arguments (ignored).
     */
    public static void main(String[] args)
    {
        new GPImageApplet().displayInFrame("Watchmaker Framework - Biomporphs Example");
    }


    /**
     * Simple observer to update the dialog title every time the evolution advances
     * to a new generation.
     */
    private final class GenerationTracker implements EvolutionObserver<GeneticImage>
    {
        public void populationUpdate(final PopulationData<? extends GeneticImage> populationData)
        {
            System.out.println("elite count: " + populationData.getEliteCount());
            System.out.println("elite : " + populationData.getBestCandidate());
        	SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    selectionDialog.setTitle("Image Selection - Generation "
                                             + (populationData.getGenerationNumber() + 1));
                }
            });
        }
    }


    /**
     * Panel for controlling the evolutionary algorithm parameters.
     */
    private final class ControlPanel extends JPanel
    {
        private JSpinner populationSpinner;
        private JSpinner generationsSpinner;

        ControlPanel()
        {
            super(new BorderLayout());
            add(createInputPanel(), BorderLayout.NORTH);
            add(createButtonPanel(), BorderLayout.SOUTH);
            setBorder(BorderFactory.createTitledBorder("Evolution Controls"));
        }


        private JComponent createInputPanel()
        {
            JPanel inputPanel = new JPanel(new SpringLayout());
            JLabel populationLabel = new JLabel("Population Size: ");
            populationSpinner = new JSpinner(new SpinnerNumberModel(2, 2, 25, 1));
            populationLabel.setLabelFor(populationSpinner);
            inputPanel.add(populationLabel);
            inputPanel.add(populationSpinner);
            JLabel generationsLabel = new JLabel("Number of Generations: ");
            generationsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 1));
            generationsLabel.setLabelFor(generationsSpinner);
            inputPanel.add(generationsLabel);
            inputPanel.add(generationsSpinner);
            
            
            SpringUtilities.makeCompactGrid(inputPanel, 2, 2, 30, 6, 6, 6);

            return inputPanel;
        }


        private JComponent createButtonPanel()
        {
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton startButton = new JButton("Start");
            startButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent actionEvent)
                {
                    createTask((Integer) populationSpinner.getValue(),
                               (Integer) generationsSpinner.getValue()).execute();
                    selectionDialog.setVisible(true);
                }
            });
            buttonPanel.add(startButton);
            
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					
				}
			});
            buttonPanel.add(saveButton);
            
            return buttonPanel;
        }
    }
}