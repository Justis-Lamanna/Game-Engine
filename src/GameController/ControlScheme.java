/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameController;

/**
 * Interface for a control scheme.
 * <p>Many different control schemes can be used for a game, either having
 * different types of keyboard controls, or utilizing some other peripheral.
 * This allows you to swap between them easily, something I didn't do for my
 * Mystery Dungeon game.
 * @author Justis
 */
public interface ControlScheme
{
    /**
     * Get the state of a specific control.
     * <p>Control names are non-specific, and may be anything the programmer
     * wants. The programmer is charged with connecting the name of the 
     * control to the physical control.
     * <p>The integer value returned through this method is also at the
     * programmer's discretion. It can be a simple 0/1 value, depending on
     * if a button was pressed or released. It could also represent a
     * pressure reading, if the button can sense how hard it is being pressed,
     * a direction in the case of a D-pad, an X-Y pair if using some sort
     * of touch device, or anything else that may come in the future.
     * @param control The name of the control to get.
     * @return Some integer, representing the controller's state.
     */
    int getState(String control);
    
    /**
     * Updates the control state.
     * When this control scheme is assigned to a game, this method should
     * be called repeatedly in order to update the controller's internal state.
     */
    void update();
}
