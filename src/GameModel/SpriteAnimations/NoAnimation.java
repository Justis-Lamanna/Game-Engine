/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel.SpriteAnimations;

import GameView.Paintable;

/**
 * The null animation.
 * This "animation" is simply a No-Op. It can be used to pad an
 * animation script, and that's basically it.
 * @author Justis
 */
public class NoAnimation implements Animation
{
    /**
     * Does nothing.
     * @param p The Paintable to do nothing on.
     */
    @Override
    public void transform(Paintable p)
    {
        //Nothing.
    }
}
