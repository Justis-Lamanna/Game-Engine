/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Represents a basic sprite.
 * Since we are using Java, rather than a small CPU, the line between
 * sprite and background is blurred. The only real difference between
 * the two, at this time, is that the Background class provides a constructor
 * that doesn't take a position parameter.
 * @author Justis
 */
public class Sprite implements Paintable
{
    private int x;
    private int y;
    private BufferedImage img = null;
    
    /**
     * Creates a sprite.
     * If the filename provided doesn't point to a sprite, a 10x10 transparent
     * BufferedImage is generated in its stead.
     * @param x The x position of the sprite.
     * @param y The y position of the sprite.
     * @param filename The filename of the image to use.
     */
    public Sprite(int x, int y, String filename)
    {
        this.x = x;
        this.y = y;
        try {
            this.img = ImageIO.read(new File(filename));
        } 
        catch (IOException ex) {
            this.img = new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
        }
    }
    
    /**
     * Creates a sprite.
     * If the BufferedImage provided is null, a 10x10 transparent BufferedImage
     * is generated in its stead.
     * @param x The x position of the sprite.
     * @param y The y position of the sprite.
     * @param img The sprite's image.
     */
    public Sprite(int x, int y, BufferedImage img)
    {
        this.x = x;
        this.y = y;
        this.img = (img != null) ? 
                img : 
                new BufferedImage(10, 10, BufferedImage.TYPE_4BYTE_ABGR);
    }
    
    /**
     * Get the X position of this sprite.
     * @return The X position.
     */
    @Override
    public int getX()               {return x;}
    
    /**
     * Set the X position of this sprite.
     * @param newX The new X position.
     */
    @Override
    public void setX(int newX)      {x = newX;}
    
    /**
     * Get the Y position of this sprite.
     * @return The Y position.
     */
    @Override
    public int getY()               {return y;}
    
    /**
     * Set the Y position of this sprite.
     * @param newY The new Y position.
     */
    @Override
    public void setY(int newY)      {y = newY;}
    
    /**
     * Get this sprite's image.
     * @return The sprite's image.
     */
    @Override
    public BufferedImage getImage() {return img;}
    
    /**
     * Set this sprite's image.
     * If the image provided is null, nothing changes.
     * @param newImage The new image.
     */
    @Override
    public void setImage(BufferedImage newImage)
    {
        if(newImage != null)
            img = newImage;
    }
    
}
