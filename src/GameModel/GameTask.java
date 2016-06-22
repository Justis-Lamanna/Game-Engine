/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

/**
 * Represents a task for the game to do.
 * <p>This is essentially a usage of the Observer pattern, and can be used to
 * simplify game creation. Rather than hard-coding all aspects of your game
 * into one big class, you can simply divide into some number of tasks.
 * <p>This class is for use with the AbstractGame class and its like. Since
 * GameModels are meant to be used when AbstractGame is not enough, and the
 * AbstractGame is based around these tasks, I figure that is fine.
 * @author Justis
 */
public interface GameTask
{
    /**
     * The code to do on each frame.
     * @param model The AbstractGame that called this method.
     * @return True to delete the task, false if not.
     */
    boolean onFrame(AbstractGame model);
}
