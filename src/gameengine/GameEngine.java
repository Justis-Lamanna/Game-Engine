/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gameengine;

import GameController.KeyController;
import GameModel.AbstractGame;
import Examples.ExampleGame;
import Examples.Mode7Mode;
import GameView.GameMode.Mode0;
import GameView.GameView;
import java.awt.Dimension;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;

/**
 * How to make a game!
 * 1. Create a GameModel. This is the actual game part of the game.
 * 2. Create a ViewMode. This handles the actual drawing of the game. The
 * ViewMode and the GameModel generally need to be doubly-linked. When the
 * getFrame() method is called, it can then notify the GameModel that a frame
 * has passed. During the GameModel's execution, it needs to add and remove
 * sprites and backgrounds through the ViewMode. An alternative would be
 * to combine these two classes. Your choice.
 * 3. Create a GameView. This is a JComponent that can be put in a JFrame
 * to display the game. It connects to the ViewMode.
 * @author Justis
 */
public class GameEngine {

    private static GameView gameWindow;
    private static JFrame frame;
    
    public static final int DEFAULT_SIZE = 3; //Change the default size at your whim!
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        frame = new JFrame();
        createGame(frame);
        createToolbar(frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setWindowSize(DEFAULT_SIZE);
        frame.setTitle("Test");
        frame.addKeyListener(KeyController.getInstance());
        frame.setVisible(true);
        frame.setResizable(false);
        new Timer(40, i -> gameWindow.repaint()).start();
    }
    
    //Create a toolbar.
    public static void createToolbar(JFrame frame)
    {
        JMenuBar menubar = new JMenuBar();
        JMenu menu = new JMenu("Size");
        ButtonGroup group = new ButtonGroup();
        createButton(menu, group, 1);
        createButton(menu, group, 2);
        createButton(menu, group, 3);
        createButton(menu, group, 4);
        menubar.add(menu);
        frame.setJMenuBar(menubar);
    }
    
    //Change the window size.
    public static void setWindowSize(int scale)
    {
        gameWindow.setPreferredSize(new Dimension(240 * scale, 160 * scale));
        frame.pack();
    }
    
    //Create a zoom button.
    public static void createButton(JMenu menu, ButtonGroup group, int size)
    {
        JRadioButtonMenuItem button = new JRadioButtonMenuItem(String.format("%d%%", size * 100), size == DEFAULT_SIZE);
        button.addActionListener(i -> setWindowSize(size));
        group.add(button); menu.add(button);
    }
    
    //Create a game.
    public static void createGame(JFrame frame)
    {
        Mode0 mode = new Mode0();
        AbstractGame game = new ExampleGame(mode);
        gameWindow = new GameView(mode);
        frame.add(gameWindow);
        game.startGame();
    }
}
