/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import GameView.ViewMode;
import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Test ViewMode.
 * This ViewMode was used to try out various types of animation, with timers
 * in the JFrame controlling the clock. The animation simply displays
 * a 256x256 canvas, with each pixel's red and green channels depending on
 * its X/Y position on the plane. The blue channel increases by one on each
 * call to getFrame().
 * @author Justis
 */
public class RainbowMode implements ViewMode
{
    int mod = 0;
    
    private static final int WIDTH = 256;
    private static final int HEIGHT = 256;

    private static final BufferedImage CANVAS = 
            new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
    
    /**
     * Get the image representing this frame.
     * On each call to getFrame, the frame image is calculated, and the blue
     * channel is increased to the next value.
     * @return The image frame.
     */
    @Override
    public BufferedImage getFrame()
    {
        mod = (mod + 1) % 256;
        for(int xx = 0; xx < CANVAS.getWidth(); xx++)
            for(int yy = 0; yy < CANVAS.getHeight(); yy++)
                CANVAS.setRGB(xx, yy, new Color(xx, yy, mod).getRGB());
        return CANVAS;
    }
}
