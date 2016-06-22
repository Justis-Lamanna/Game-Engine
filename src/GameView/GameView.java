/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;

/**
 * The current view of the game.
 * <p>Specifically, this represents the "view" part of the Model-View-Controller
 * pattern, by turning the ViewMode into something that can be placed in a
 * window.
 * <p>This class does not handle the frame rate; the programmer may decide
 * how they want the frame rate to be controlled. Timers may be placed, either
 * on the frame to repaint the GameView repeatedly, or inside the GameModel to
 * allow the game to control its frame rate. A Timer is not a necessity; the
 * GameModel may instead call a repaint at times when the screen changes.
 * @author Justis
 */
public class GameView extends JComponent
{
    /**
     * Utilized as the default View Mode for this game engine. Simply
     * displays a black screen, 240 pixels wide and 160 pixels tall.
     */
    private static class NullViewMode implements ViewMode
    {
        private static final int WIDTH = 240;
        private static final int HEIGHT = 160;
        private static final int COLOR = Color.BLACK.getRGB();
        
        private static final BufferedImage BLANK = 
                new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        private static NullViewMode instance = null;
        
        private NullViewMode(int color)
        {
            for(int xx = 0; xx < BLANK.getWidth(); xx++)
                for(int yy = 0; yy < BLANK.getHeight(); yy++)
                    BLANK.setRGB(xx, yy, color);
        }
        
        /*
        Working a little Singleton action here. No sense recreating the
        black screen each time.
        */
        public static NullViewMode getInstance()
        {
            if(instance == null)
            {
                instance = new NullViewMode(COLOR);
            }
            return instance;
        }
        
        @Override
        public BufferedImage getFrame()
        {
            return BLANK;
        }
    }
    
    private ViewMode mode;
    
    /**
     * Creates a GameView using a default ViewMode.
     * <p>By itself, this GameView isn't particularly useful. The default
     * ViewMode is a 240x160 pixel black screen.
     */
    public GameView()
    {
        this(null);
    }
    
    /**
     * Creates a GameView with a specific ViewMode.
     * <p>This will be the GameView you'll want to use. The ViewMode provided
     * provides the interface between the GameModel and the GameView.
     * <p>If null is provided for the ViewMode, the ViewMode used is the
     * default ViewMode, with a 240x160 pixel black screen.
     * @param startMode The ViewMode to use for this GameView.
     */
    public GameView(ViewMode startMode)
    {
        mode = (startMode == null) ? 
                NullViewMode.getInstance() : 
                startMode;
    }
    
    /**
     * Changes this GameView to use a new ViewMode.
     * <p>This can be used to swap to a new ViewMode, if necessary. If null is 
     * provided for the ViewMode, the ViewMode used is the
     * default ViewMode, with a 240x160 pixel black screen.
     * @param newMode The ViewMode this GameView should use now.
     */
    public void changeMode(ViewMode newMode)
    {
        mode = (newMode == null) ? 
                NullViewMode.getInstance() : 
                newMode;
    }
    
    /**
     * Paints this GameView.
     * <p>Naturally, this is the method called when the frame is repainted. This
     * essentially gets the frame from the ViewMode, and scales it to fit the
     * GameView component.
     * @param g The Graphics object from the JFrame.
     */
    @Override
    public void paint(Graphics g)
    {
        BufferedImage frame = mode.getFrame();
        g.drawImage(frame, 0, 0, getWidth(), getHeight(), null);
    }
}
