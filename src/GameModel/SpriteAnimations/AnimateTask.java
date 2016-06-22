/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel.SpriteAnimations;

import GameModel.AbstractGame;
import GameModel.GameTask;
import GameView.Paintable;

/**
 * Does an actual animation.
 * <p>This class serves as a link between Animation and GameTask. Since it
 * implements GameTask, it can be used through addTask() and removeTask()
 * as specified in the AbstractGame class.
 * <p>Each instance of AnimateTask contains the Paintable to animate, an
 * Animation "script", and a speed of execution. The speed may be
 * somewhat confusing; it refers to the amount added to the current frame, and
 * may be positive, negative, or a fraction. A fractional value would play
 * an animation slower; If 0.25 was provided, for example, it would take four
 * frames between each animation. If 0.75 was provided, the following sequence
 * would occur:
 * <ol>
 * <li>Animation frame 0 = Animation number 0 (0)</li>
 * <li>Animation frame 1 = Animation number 0 (0.75)</li>
 * <li>Animation frame 2 = Animation number 1 (1.5)</li>
 * <li>Animation frame 3 = Animation number 2 (2.25)</li>
 * <li>Animation frame 4 = Animation number 3 (3.0)</li>
 * <li>Animation frame 5 = Animation number 3 (3.75)</li>
 * </ol>
 * Values larger than 1 (or -1) would be even faster, although this would
 * result in animations being skipped.
 * <p>Each animation may also be marked as looping, in which case the animation
 * number will return to zero when it becomes greater than or equal to the 
 * length of the animation script.
 * @author Justis
 */
public class AnimateTask implements GameTask
{
    private Paintable paint;
    private Animation[] script;
    private double currentFrame;
    private double speed;
    private boolean loop;
    
    /**
     * Creates an AnimateTask.
     * This constructor uses an implied speed of 1, meaning each frame
     * will go to the next slot in the Animation script.
     * @param p The Paintable to animate.
     * @param script An array of Animations, to be executed one after the next.
     * @param loop True if this animation should loop.
     * @throws NullPointerException Paintable or Animation[] is null.
     */
    public AnimateTask(Paintable p, Animation[] script, boolean loop)
    {
        this(p, script, 1, loop);
    }
    
    /**
     * Creates an AnimateTask.
     * @param p The Paintable to animate.
     * @param script An array of Animations, to be executed according to the speed.
     * @param speed A value dictating which frame comes next. Next Frame = Current Frame + Speed.
     * @param loop True if this animation should loop.
     * @throws NullPointerException Paintable or Animation[] is null.
     */
    public AnimateTask(Paintable p, Animation[] script, double speed, boolean loop)
    {
        if(p == null){throw new NullPointerException("Null paintable.");}
        if(script == null){throw new NullPointerException("Null script.");}
        this.paint = p;
        this.script = script;
        currentFrame = 0;
        this.speed = speed;
        this.loop = loop;
    }
    
    /**
     * Performs one step of the animation.
     * <p>If this animation is set to loop, the current frame will
     * be reduced such that:<br><br>
     * 0 &lt;= CurrentFrame - i * ScriptLength &lt; ScriptLength<br><br>
     * Where i is an integer.
     * @param model The AbstractGame that called this task. Ignored here.
     * @return True if this animation is finished.
     */
    @Override
    public boolean onFrame(AbstractGame model)
    {
        script[(int)currentFrame].transform(paint);
        currentFrame += speed;
        if(loop)
        {
            while(currentFrame >= script.length){
                currentFrame -= script.length;
            } //Back up until you get back in range.
        }
        return currentFrame >= script.length;
    }
    
    /**
     * Set a new animation speed.
     * @param newSpeed The new speed the animation should occur.
     */
    public void setAnimationSpeed(double newSpeed)
    {
        speed = newSpeed;
    }
    
    /**
     * Set a new animation script.
     * If a null script is provided, nothing changes.
     * @param newScript The new Animation script to use.
     */
    public void setAnimationScript(Animation[] newScript)
    {
        if(newScript != null)
        {
            script = newScript;
        }
    }
}
