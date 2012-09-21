package org.dave;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

import javax.swing.JComponent;

import org.dave.gp.Node;
import org.uncommons.watchmaker.framework.interactive.Renderer;


/**
 * Renders Functions as Swing components.
 * @author Daniel Dyer
 */
public class SwingFunctionRenderer implements Renderer<GeneticImage, JComponent>
{
    /**
     * Renders an evolved function as a component that can be displayed
     * in a Swing GUI.
     * @param function The function to render.
     * @return A component that displays a visual representation of the
     * function.
     */
    public JComponent render(GeneticImage gi)
    {
        return new FunctionView(gi);
    }


    /**
     * A Swing component that can display a visual representation of a
     * function.
     */
    private static final class FunctionView extends JComponent
    {
        private final GeneticImage gi;
        private int WIDTH  = 480;
        private int HEIGHT = 800;

        FunctionView(GeneticImage gi)
        {
            this.gi = gi;
            this.setName(this.gi.toString());
            WIDTH = gi.getWidth();
            HEIGHT = gi.getHeight();
            Dimension size = new Dimension(gi.getWidth(), gi.getHeight());
            setMinimumSize(size);
            setPreferredSize(size);
        }


        @Override
        protected void paintComponent(Graphics graphics)
        {
            super.paintComponent(graphics);
            if (graphics instanceof Graphics2D)
            {
                ((Graphics2D) graphics).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                                         RenderingHints.VALUE_ANTIALIAS_ON);
            }
            
            System.out.println("Function: " + gi);
            graphics.drawImage(gi.getImage(), 0, 0, WIDTH, HEIGHT, Color.BLACK, this);
            
        }

    }
}
