/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import GameView.ImageOp;
import GameView.MatrixFunction;
import GameView.ViewMode;
import java.awt.image.BufferedImage;

/**
 * Test ViewMode.
 * <p>This ViewMode was used to test out the ImageOp class, which performs
 * various operations on an image. Currently, this acts in a manner similar
 * to a decorator; It takes an existing ViewMode, modifies its frame, and
 * returns it.
 * <p>As for the class itself, it rotates whatever ViewMode it's given around
 * the center point.
 * @author Justis
 */
public class SpinMode implements ViewMode
{
    int mod = 0;
    ViewMode toSpin;
    
    /**
     * Create a SpinMode.
     * @param mode The ViewMode to spin around.
     * @throws NullPointerException ViewMode provided is null.
     */
    public SpinMode(ViewMode mode)
    {
        if(mode == null){
            throw new NullPointerException("Null ViewMode");
        }
        toSpin = mode;
    }
    
    /**
     * Get the image representing this frame.
     * <p>On each call to getFrame(), this calls the decorated ViewMode's
     * getFrame() method, and rotates it by some amount. The amount is then
     * incremented.
     * <p>Notice how I kept track of a degree amount, and applied it to the
     * frame each time, rather than calling a rotation of 1 degree each time.
     * Due to the inaccuracies of Math.sin() and Math.cos(), this may yield to
     * crazy behavior. Always use as few rotations as possible.
     * @return The image frame.
     */
    @Override
    public BufferedImage getFrame()
    {
        BufferedImage img = new ImageOp(toSpin.getFrame())
                .rotateDegrees(512, 512, mod++)
                        .apply();
        mod++;
        return img;
    }
}
