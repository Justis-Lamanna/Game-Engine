/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel.SpriteAnimations;

import GameView.Paintable;

/**
 * An animation where the paintable is shifted some amount.
 * This, of course, difffers from SetPositionAnimation in that
 * this uses a relative distance to move, rather than a fixed coordinate
 * to move to.
 * @author Justis
 */
public class ShiftPositionAnimation implements Animation
{
    private int dx;
    private int dy;
    
    /**
     * Creates this animation.
     * @param dx The number of pixels in the positive X direction to move.
     * @param dy The number of pixels in the positive Y direction to move.
     */
    public ShiftPositionAnimation(int dx, int dy)
    {
        this.dx = dx;
        this.dy = dy;
    }
    
    /**
     * Shifts the Paintable in the specified manner.
     * Deep down, this functions by getting the Paintable's coordinates,
     * adding the values specified in the constructor, and setting the
     * Paintable's coordinates to that new value.
     * @param p The Paintable to shift.
     * @throws NullPointerException The Paintable provided is null.
     */
    @Override
    public void transform(Paintable p)
    {
        if(p == null){
            throw new NullPointerException("Can't transform null.");
        }
        p.setX(p.getX() + dx);
        p.setY(p.getY() + dy);
    }
}
