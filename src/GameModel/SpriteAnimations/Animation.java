/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel.SpriteAnimations;

import GameView.Paintable;

/**
 * Animation interface.
 * All animations extend the transform() method, which operates
 * on the specified Paintable in order to move it, or change its appearance.
 * @author Justis
 */
public interface Animation
{
    /**
     * Do something to this Paintable.
     * @param p The Paintable to modify.
     */
    void transform(Paintable p);
}
