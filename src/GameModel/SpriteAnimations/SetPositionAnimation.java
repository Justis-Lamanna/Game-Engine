/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel.SpriteAnimations;

import GameView.Paintable;

/**
 * An animation where the Paintable is moved to a specific point.
 * @author Justis
 */
public class SetPositionAnimation implements Animation
{
    private final int x;
    private final int y;
    
    /**
     * Create this animation.
     * @param newX The X position the Paintable will be moved to.
     * @param newY The Y position the Paintable will be moved to.
     */
    public SetPositionAnimation(int newX, int newY)
    {
        x = newX;
        y = newY;
    }
    
    /**
     * Moves the Paintable to the specified coordinates.
     * @param p The Paintable to move.
     * @throws NullPointerException Paintable is null.
     */
    @Override
    public void transform(Paintable p)
    {
        if(p == null){
            throw new NullPointerException("Can't transform null.");
        }
        p.setX(x);
        p.setY(y);
    }
}
