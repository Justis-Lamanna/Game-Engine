/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import java.awt.image.BufferedImage;

/**
 * Represents a paintable item.
 * <p>Any class that implements this can be used on the canvas, either as a
 * background layer or a sprite.
 * @author Justis
 */
public interface Paintable
{
    /**
     * Get the X-coordinate of this Paintable item.
     * @return The X-coordinate of this object, in pixels.
     */
    int getX();
    
    /**
     * Set the X-coordinate of this Paintable item.
     * @param newX The new X-coordinate.
     */
    void setX(int newX);
    
    /**
     * Get the Y-coordinate of this Paintable item.
     * @return The Y-coordinate of this object, in pixels.
     */
    int getY();
    
    /**
     * Set the Y-coordinate of this Paintable item.
     * @param newY The new Y-coordinate.
     */
    void setY(int newY);
    
    /**
     * Get the image of this Paintable item.
     * @return The image that represents this object.
     */
    BufferedImage getImage();
    
    /**
     * Set the image of this Paintable item.
     * @param newImage The image that represents this object.
     */
    void setImage(BufferedImage newImage);
}
