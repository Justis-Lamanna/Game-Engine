/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameController;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Justis
 */
public class KeyController implements ControlScheme, KeyListener
{
    private final Map<String, Integer> names;
    private final boolean[] keyStates;
    private final char[] keyCache;
    private int current;
    
    private static KeyController singleton = null;
    
    private KeyController()
    {
        names = new HashMap<>();
        keyStates = new boolean[256];
        keyCache = new char[256];
        current = 0;
    }
    
    public static KeyController getInstance()
    {
        if(singleton == null)
        {
            singleton = new KeyController();
        }
        return singleton;
    }
    
    public void addControl(String name, int key)
    {
        names.put(name, key);
    }
    
    @Override
    public int getState(String control)
    {
        Integer key = names.get(control);
        if(key == null){
            return -1;
        }
        else{
            return keyStates[key] ? 1 : 0;
        }
    }

    @Override
    public void update()
    {
        //Nuffin
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
        keyCache[current++] = e.getKeyChar();
        if(current >= 256){current = 0;} //Wraparound.
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        keyStates[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        keyStates[e.getKeyCode()] = false;
    }
    
}
