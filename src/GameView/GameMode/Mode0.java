/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView.GameMode;

import GameModel.GameModel;
import GameView.Paintable;
import GameView.ViewMode;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Mode 0 View Mode.
 * @author Justis
 */
public class Mode0 implements ViewMode
{  
    //The number of layers in Mode 0 for GBA.
    private static final int DEFAULT_NUMBER_OF_LAYERS = 4;
    private static final int WIDTH = 240;
    private static final int HEIGHT = 160;
    
    protected final SortedList<PaintProperties> sprites;
    protected GameModel game;
    
    /**
     * Creates a Mode0 instance.
     * <p>The number of layers must be specified. In Mode0, layer 0 is
     * the top-most layer, with subsequent layers being underneath it.
     * <p>Sprites are somewhat more complicated. Each sprite has a priority,
     * which represents which layer the sprite is placed above or below.
     * Within each priority, the most recently added sprite is drawn first.
     */
    public Mode0()
    {
        sprites = new SortedList<>();
    }
    
    /**
     * Set the game to be played in this Mode.
     * <p>Unfortunately, this is a necessary evil. When the frame is updated,
     * that needs to propagate into the game itself, so it can time itself
     * correctly.
     * <p>If the GameModel provided is null, nothing happens.
     * @param game The game to attach to this mode.
     */
    public void setGame(GameModel game)
    {
        this.game = game;
    }
    
    /**
     * Add a sprite.
     * <p>The priority specifies both the layer painted on, as well as the
     * relation between all other sprites. The sprite is painted on
     * floor(priority), meaning that priorities of 0, 0.1, and 0.99999 would
     * all be painted on layer 0. 
     * <p>The full value represents the ordering
     * each sprite should be processed. Sprites within a layer are painted so
     * sprites with higher values are painted above those with lower values. In
     * keeping with the example above, priority 0 would be painted below 0.1, and
     * both would be painted below 0.9999.
     * @param paint The Paintable representing the sprite.
     * @param priority The priority of this sprite.
     * @return The PaintProperties of the newly added sprite.
     * @throws NullPointerException The Paintable supplied is null.
     */
    public PaintProperties addPaintable(Paintable paint, double priority)
    {
        if(paint == null){
            throw new NullPointerException("Null Paintable specified.");
        }
        PaintProperties spriteProperties = new PaintProperties(paint, priority);
        sprites.add(spriteProperties);
        return spriteProperties;
    }
    
    /**
     * Remove a sprite.
     * <p>Specifically, this removes the first sprite to which paint.equals()
     * returns true.
     * @param paint The sprite to remove. 
     */
    public void removeSprite(Paintable paint)
    {
        int index = 0;
        for(PaintProperties sprite : sprites)
        {
            if(sprite.getPaintable().equals(paint))
            {
                sprites.remove(index);
                return;
            }
            index++;
        }
    }
    
    /**
     * The doozy, building the frame.
     * <p>The algorithm used for this might not be too fantastic; It essentially
     * takes all the sprites, and one by one pastes them onto each layer.
     * It then pastes each layer together and returns it.
     * Naturally, as the number of layers increase, the slower this algorithm
     * runs. As the number of sprites increase, the algorithm runs slower as
     * well.
     * <p>While all images provided are preserved, the frame will only draw
     * whatever's in the 240x160 pixel window, with the top-left corner at (0, 0).
     * To "move" the camera, you can simply move the backgrounds and sprites
     * in the opposite direction. Plans for the future allow the ability to move
     * the camera explicitly.
     * <p>Everything here is done to ensure no BufferedImages are drawn on, except
     * for the working canvases.
     * <p>Plans for the future: Each pixel should only be drawn once for the
     * fastest speed.
     * @return The finished frame.
     */
    @Override
    public BufferedImage getFrame()
    {
        if(game != null){game.onFrame();}
        BufferedImage frame = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D gfx = frame.createGraphics();
        for(PaintProperties sprite : sprites)
        {
            if(sprite.isVisible()){
                Paintable spriteP = sprite.getPaintable();
                gfx.drawImage(spriteP.getImage(), new RenderOp(sprite), spriteP.getX(), spriteP.getY());
            }
        }
        return frame;
    }
}
