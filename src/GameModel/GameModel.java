/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

/**
 * The common class for all games.
 * <p>The recommended practice for using the GameModel class is to initialize
 * all resources in the constructor, and when the startGame() method is called,
 * the game itself will begin. A slightly simpler way to create a game is
 * to utilize the AbstractGameModel abstract class, which allows you to work
 * with GameTasks.
 * @author Justis
 */
public interface GameModel
{
    /**
     * Start the game!
     */
    void startGame();
    
    /**
     * Calls any code which should execute on each frame.
     */
    void onFrame();
}
