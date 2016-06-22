/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView.GameMode;

import GameModel.GameModel;
import GameView.Paintable;
import GameView.ViewMode;
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
    
    private final PaintProperties[] layers;
    private final SortedList<PaintProperties> sprites;
    private GameModel game;
    
    /**
     * Creates a Mode0 instance.
     * <p>The number of layers must be specified. In Mode0, layer 0 is
     * the top-most layer, with subsequent layers being underneath it.
     * <p>Sprites are somewhat more complicated. Each sprite has a priority,
     * which represents which layer the sprite is placed above or below.
     * Within each priority, the most recently added sprite is drawn first.
     * @param numLayers Number of layers this instance should have.
     */
    public Mode0(int numLayers)
    {
        if(numLayers < 0){
            throw new NegativeArraySizeException(
                    "Negative number of layers provided.");
        }
        layers = new PaintProperties[numLayers];
        sprites = new SortedList<>();
    }
    
    /**
     * Creates a Mode0 instance.
     * <p>The number of layers is the default, which is 4. In Mode0, layer 0 is
     * the top-most layer, with subsequent layers being underneath it.
     * <p>Sprites are somewhat more complicated. Each sprite has a priority,
     * which represents which layer the sprite is placed above or below.
     * Within each priority, the most recently added sprite is drawn first.
     */
    public Mode0()
    {
        this(DEFAULT_NUMBER_OF_LAYERS);
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
     * Set a new layer up.
     * <p>The classical way this mode works is by having a set number of labels.
     * The lower the layer number, the more covered the layer is.
     * @param paint
     * @param priority 
     * @return The PaintProperties of this newly-added layer.
     */
    public PaintProperties setLayer(Paintable paint, int priority)
    {
        if(paint == null){
            throw new NullPointerException("Paintable is null.");
        }
        if(priority < 0 || priority >= layers.length){
            throw new ArrayIndexOutOfBoundsException(
                    String.format("Priority must be from 0 to %d.", layers.length-1));
        }
        layers[priority] = new PaintProperties(paint, priority);
        return layers[priority];
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
     * @throws ArithmeticException Priority isn't between 0 and the number of layers, or is non-finite.
     */
    public PaintProperties addSprite(Paintable paint, double priority)
    {
        if(paint == null){
            throw new NullPointerException("Null Paintable specified.");
        }
        if(!Double.isFinite(priority) || priority < 0 || priority >= layers.length){
            throw new ArithmeticException("Invalid priority specified.");
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
        BufferedImage[] fLayers = new BufferedImage[layers.length];
        for(int layer = 0; layer < fLayers.length; layer++){
            fLayers[layer] = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);
            paint(fLayers[layer], layers[layer]); //Paint the background.
        }
        for(PaintProperties sprite : sprites)
        {
            paint(fLayers[(int)sprite.getPriority()], sprite);
        }
        //We have some BufferedImages that need to be combined.
        for(int xx = 0; xx < frame.getWidth(); xx++)
        {
            for(int yy = 0; yy < frame.getHeight(); yy++)
            {
                int rgb = -1; //Transparent.
                for(int layer = 0; layer < fLayers.length; layer++)
                {
                    rgb = fLayers[layer].getRGB(xx, yy);
                    if(rgb != -1){break;}
                }
                frame.setRGB(xx, yy, rgb);
            }
        }
        return frame;
    }
    
    //Paints a Paintable onto a background based on its PaintProperties.
    private void paint(BufferedImage bg, PaintProperties prop)
    {
        if(bg == null || prop == null || !prop.isVisible()){return;}
        Paintable img = prop.getPaintable();
        int x = img.getX();
        int y = img.getY();
        BufferedImage image = img.getImage();
        for(int xx = 0; xx < image.getWidth(); xx++)
            for(int yy = 0; yy < image.getHeight(); yy++)
            {
                if(image.getRGB(xx, yy) == prop.getTransparentRGB()){continue;}
                if(prop.isWrapped())
                {
                    blend(bg, Math.floorMod(x+xx, bg.getWidth()), Math.floorMod(y+yy, bg.getHeight()), prop, xx, yy);
                }
                else if(x+xx >= 0 && x+xx < bg.getWidth() && y+yy >= 0 && y+yy < bg.getHeight()){
                    blend(bg, x+xx, y+yy, prop, xx, yy);
                }
            }
    }
    
    private void blend(BufferedImage bg, int bgx, int bgy, PaintProperties prop, int px, int py)
    {
        if(prop.isSemiTransparent())
        {
            int bgRGB = bg.getRGB(bgx, bgy);
            double bgA = ((bgRGB & 0xFF000000) >>> 24) / 255f;
            int bgR = (bgRGB & 0xFF0000) >>> 16;
            int bgG = (bgRGB & 0xFF00) >>> 8;
            int bgB = (bgRGB & 0xFF);
            //
            int pRGB = prop.getPaintable().getImage().getRGB(px, py);
            double pA = ((pRGB & 0xFF000000) >>> 24) / 255f;
            int pR = (pRGB & 0xFF0000) >>> 16;
            int pG = (pRGB & 0xFF00) >>> 8;
            int pB = (pRGB & 0xFF);
            //
            double newA = pA + bgA * (1 - pA);
            int newR = (int)((pR * pA + bgR * bgA * (1 - pA)) / newA);
            int newG = (int)((pG * pA + bgG * bgA * (1 - pA)) / newA);
            int newB = (int)((pB * pA + bgB * bgA * (1 - pA)) / newA);
            int newRGB = ((int)(newA * 255) << 24) | (newR << 16) | (newG << 8) | newB;
            bg.setRGB(bgx, bgy, newRGB);
        }
        else
        {
            bg.setRGB(bgx, bgy, prop.getPaintable().getImage().getRGB(px, py));
        }
    }
}
