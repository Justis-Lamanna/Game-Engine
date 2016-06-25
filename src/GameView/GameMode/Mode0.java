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
 * <p>This is a basic mode, which should be sufficient for a simple game. To add
 * a paintable, simply use the {@code setPaintable(Paintable, double)} method.
 * <p>Priorities dictate the order of painting, which goes from lowest to 
 * highest priority; i.e., Higher priorities are placed above lower priorities.
 * In the case of identical priorities, the one more recently added has
 * a lower priority than older ones.
 * @author Justis
 */
public class Mode0 implements ViewMode
{  
    private static final int WIDTH = 240;
    private static final int HEIGHT = 160;
    
    protected final SortedList<PaintProperties> sprites;
    protected GameModel game;
    
    /**
     * Creates a Mode0 instance.
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
     * <p>If the GameModel provided is null, game updating is disabled.
     * @param game The game to attach to this mode.
     */
    public void setGame(GameModel game)
    {
        this.game = game;
    }
    
    /**
     * Add a Paintable.
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
    public void removePaintable(Paintable paint)
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
            paint(frame, sprite);
        }
        return frame;
    }
    
    private void paint(BufferedImage bg, PaintProperties p)
    {
        if(p.isVisible())
        {
            Paintable paint = p.getPaintable();
            int drawX = paint.getX();
            int drawY = paint.getY();
            BufferedImage image = paint.getImage();
            for(int xx = 0; xx < image.getWidth(); xx++)
            {
                for(int yy = 0; yy < image.getHeight(); yy++)
                {
                    int thisX = drawX + xx;
                    int thisY = drawY + yy;
                    if(p.isWrapped()){
                        thisX = Math.floorMod(thisX, bg.getWidth());
                        thisY = Math.floorMod(thisY, bg.getHeight());
                    }
                    else if(thisX < 0 || thisX >= bg.getWidth() || thisY < 0 || thisY >= bg.getHeight()){
                            continue;
                    }
                    int paintRGB = image.getRGB(xx, yy);
                    if(paintRGB == p.getTransparentRGB()){
                        paintRGB = 0;
                    }
                    int bgRGB = bg.getRGB(thisX, thisY);
                    bg.setRGB(thisX, thisY, blend(bgRGB, paintRGB));
                }
            }
        }
    }
    
    private int blend(int bottomColor, int topColor)
    {
        double dstA = (bottomColor >>> 24) / 255f, srcA = (topColor >>> 24) / 255f;
        int dstR = (bottomColor >>> 16 & 0xFF), srcR = (topColor >>> 16 & 0xFF);
        int dstG = (bottomColor >>> 8  & 0xFF), srcG = (topColor >>> 8  & 0xFF);
        int dstB = (bottomColor        & 0xFF), srcB = (topColor        & 0xFF);
        double a = (srcA + dstA * (1 - srcA));
        if(a == 0){return 0;}
        int r = (int)((srcR * srcA + dstR * dstA * (1 - srcA)) / a);
        int g = (int)((srcG * srcA + dstG * dstA * (1 - srcA)) / a);
        int b = (int)((srcB * srcA + dstB * dstA * (1 - srcA)) / a);
        return ((int)(a * 255) << 24) | (r << 16) | (g << 8) | b;
    }
}
