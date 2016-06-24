/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel.SpriteAnimations;

import GameView.Paintable;
import java.awt.image.BufferedImage;

/**
 * An animation where a sprite's image has changed.
 * I recommend that a ChangeFrameAnimation be used instead of this method. A
 * ChangeFrameAnimation, in conjunction with a FramedSprite, can be somewhat
 * more efficient, rather than loading a bunch of images from memory. Additionally,
 * framed sprites allow more generic animations; the same animations can be applied
 * to any number of sprite sheets, whereas an animation of this type would
 * require different animations for each sprite sets.
 * @author Justis
 */
public class ChangeImageAnimation implements Animation
{
    private BufferedImage image;
    
    /**
     * Creates this animation.
     * @param img The image to change into.
     * @throws NullPointerException the image is null.
     */
    public ChangeImageAnimation(BufferedImage img)
    {
        if(img == null){
            throw new NullPointerException("Null image provided.");
        }
        image = img;
    }
    
    /**
     * Transforms this sprite's image.
     * @param p The Paintable to transform.
     */
    @Override
    public void transform(Paintable p)
    {
        if(p == null){
            throw new NullPointerException("Can't transform null.");
        }
        p.setImage(image);
    }
}
