/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView.GameMode;

import GameView.Paintable;
import java.awt.Color;

/**
 * Keeps track of each Paintable's properties.
 * <p>The basics of a Paintable implementation are good enough for
 * general painting, but unsuitable for more specialized applications.
 * While the Paintable methods keep track of a Paintable object's X-Y position
 * and appearance, this class adds more methods, which are more directly
 * tied to the ViewMode, such as priority and display flags.
 * <p>All setter methods in this class return the instance of this class, in
 * order to "build" the sprite. This permits you to configure a sprite like this:
 * {@code mode.addSprite(0, 0, "Resources/spr0.png").setWrapped(true).setTransparentRGB(Color.BLACK);}
 * <p>Fields are detailed here:
 * <ul>
 * <li>Paintable - The Paintable this PaintProperties wraps. It includes what
 * the sprite looks like, as well as its X-Y position.</li>
 * <li>Priority - The height of the Paintable. This value, floored, represents
 * the sprite layer to be painted on. Within other Paintables of this layer,
 * Paintables are painted from lower priority to higher priority, with higher
 * priorities overwriting lower priorities.</li>
 * <li>Visibility - Whether this Paintable should be shown or hidden from view.
 * By default, this is true.</li>
 * <li>Wrap - Whether this Paintable should be clipped to the screen size, or
 * wrap around the screen to the other side. By default, this is false.
 * Due to the change to Graphics2D, wrapping only works for backgrounds; if
 * used on a sprite, weird things happen.</li>
 * <li>Background Color - The color of the Paintable's background. It is easier
 * to create Paintables, and designate some color as the "transparent" color.
 * This color is essentially ignored when it is encountered during screen render.
 * By default, this is the color of the top-left pixel.</li>
 * <li>Semitransparency - A flag, saying this Paintable is semitransparent. The
 * process of blending two images is somewhat complicated, and may be more
 * time-consuming than absolutely necessary. Setting this flag to false removes
 * the calculation, and does a straight painting operation. By default, this is
 * true.</li>
 * </ul>
 * @author Justis
 */
public class PaintProperties implements Comparable
{
    private final Paintable value;
    private double priority;
    private boolean shown = true;
    private boolean wrap = false;
    private int bgColor;
    private boolean semitransparent = true;
    
    /**
     * Initializes the PaintProperties.
     * In addition to the explicitly-provided Paintable and priority,
     * the background color is implicitly found here, by using the
     * color in the top-left corner as the transparent value.
     * @param value
     * @param priority 
     */
    public PaintProperties(Paintable value, double priority)
    {
        this.value = value;
        this.priority = priority;
        setTransparentRGB(null);
    }
    
    /**
     * Gets the Paintable wrapped in this PaintProperties.
     * @return The wrapped Paintable.
     */
    public Paintable getPaintable(){return value;}
    
    /**
     * Get the priority of this Paintable.
     * @return The priority.
     */
    public double getPriority(){return priority;}
    
    /**
     * Set the priority of this Paintable.
     * <p>If the priority provided is non-finite, nothing happens.
     * @param newPriority The new priority of this Paintable.
     * @return The instance of this PaintProperties.
     */
    public PaintProperties setPriority(double newPriority)
    {
        if(Double.isFinite(newPriority))
        {
            priority = newPriority;
        }
        return this;
    }
    
    /**
     * Get the wrapped flag of this Paintable.
     * @return The wrapped flag.
     */
    public boolean isWrapped(){return wrap;}
    
    /**
     * Set the wrapped flag of this Paintable.
     * @param newWrap True if this Paintable should wrap around the screen.
     * @return The instance of this PaintProperties.
     */
    public PaintProperties setWrapped(boolean newWrap){wrap = newWrap; return this;}
    
    /**
     * Checks if this sprite is set as visible.
     * @return True if it is visible, false if hidden.
     */
    public boolean isVisible(){return shown;}
    
    /**
     * Sets the visibility of this sprite.
     * @param visible True if the sprite should be visible, false if not.
     * @return The instance of this PaintProperties.
     */
    public PaintProperties setVisible(boolean visible){shown = visible; return this;}
    
    /**
     * Gets the background color of this sprite.
     * @return The current background transparency.
     */
    public int getTransparentRGB(){return bgColor;}
    
    /**
     * Sets the background color of this sprite.
     * <p>If a Color of null is provided, transparency is determined by using the
     * top-left corner of the image.
     * @param newColor The new transparency color. 
     * @return The instance of this PaintProperties.
     */
    public PaintProperties setTransparentRGB(Color newColor)
    {
        bgColor = (newColor == null) ? 
                value.getImage().getRGB(0, 0) : 
                newColor.getRGB();
        return this;
    }
    
    /**
     * Gets the semitransparency of this sprite.
     * @return True if the image is semitransparent.
     * @deprecated Semitransparency is now handled by the Graphics2D library.
     */
    @Deprecated
    public boolean isSemiTransparent(){return semitransparent;}
    
    /**
     * Sets the semitransparency of this sprite.
     * @param st True if the image should be semitransparent.
     * @return The instance of this PaintProperties.
     * @deprecated Semitransparency is now handled by the Graphics2D library.
     */
    @Deprecated
    public PaintProperties isSemiTransparent(boolean st){semitransparent = st; return this;}

    //This is so the SortedList can handle it.
    @Override
    public int compareTo(Object o)
    {
        PaintProperties other = (PaintProperties)o;
        return (int)Math.signum(priority - other.priority);
    }

    //This is to assist in finding and deleteing. Takes either PaintProperties
    //or Paintable, as theyre synonymous here.
    @Override
    public boolean equals(Object o)
    {
        if(o instanceof PaintProperties)
        {
            PaintProperties other = (PaintProperties)o;
            return value.equals(other.value);
        }
        else if(o instanceof Paintable)
        {
            Paintable other = (Paintable)o;
            return value.equals(other);
        }
        return false;
    }

    //This is because the yellow line in NetBeans bothered me.
    @Override
    public int hashCode(){return value.hashCode();}

    //Debugging
    @Override
    public String toString(){
        return String.format("%s (%f)", value.toString(), priority);}
}
