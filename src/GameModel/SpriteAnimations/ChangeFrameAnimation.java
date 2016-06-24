/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel.SpriteAnimations;

import GameView.FramedSprite;
import GameView.Paintable;

/**
 * An animation where a sprite's frame is changed.
 * @author Justis
 */
public class ChangeFrameAnimation implements Animation
{
    private int frame;
    
    /**
     * Creates this animation.
     * Upper bounds aren't checked here, because each FramedSprite has it's own
     * number of frames.
     * @param newFrame The frame this animation will swap to.
     * @throws ArrayIndexOutOfBoundsException Negative frame was specified.
     */
    public ChangeFrameAnimation(int newFrame)
    {
        if(newFrame < 0){
            throw new ArrayIndexOutOfBoundsException("Negative frame specified.");
        }
        frame = newFrame;
    }
    
    /**
     * Changes this Paintable's frame.
     * Since frames aren't a basic part of the Paintable, this only works on
     * FramedSprites, and its potential derivatives. If something else is
     * specified, this functions as a No-Op.
     * @param p The Paintable to have its frame swapped.
     * @throws NullPointerException Paintable provided is null.
     * @throws ArrayIndexOutOfBoundsException This Paintable doesn't have the
     * specified frame number.
     */
    @Override
    public void transform(Paintable p)
    {
        if(p == null){
            throw new NullPointerException("Can't transform null.");
        }
        if(p instanceof FramedSprite)
        {
            ((FramedSprite)p).setFrame(frame);
        }
    }
}
