/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import GameController.KeyController;
import GameModel.AbstractGame;
import GameModel.GameTask;
import GameModel.SpriteAnimations.AnimateTask;
import GameModel.SpriteAnimations.Animation;
import GameModel.SpriteAnimations.ChangeFrameAnimation;
import GameModel.SpriteAnimations.ChangeImageAnimation;
import GameModel.SpriteAnimations.NoAnimation;
import GameModel.SpriteAnimations.SetPositionAnimation;
import GameModel.SpriteAnimations.ShiftPositionAnimation;
import GameModel.WaitTask;
import GameView.FramedSprite;
import GameView.GameMode.Mode0;
import GameView.Sprite;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.imageio.ImageIO;
import org.ini4j.Ini;

/**
 * An example "game".
 * Currently, this just demonstrates the usage of Tasks.
 * @author Justis
 */
public class ExampleGame extends AbstractGame
{
    private final Mode0 mode;
    private final Map<String, BufferedImage> imageMap;
    private final Map<String, Animation[]> animationMap;
    private final KeyController controls;
    
    //Ini file for all the game images.
    private static final String IMAGE_INI = "Resources/Ini/images.ini";
    
    //Ini file for all the animation scripts.
    private static final String ANIM_INI = "Resources/Ini/animations.ini";
    
    //Updates the controller.
    private final GameTask UPDATE_CONTROLLER = new GameTask(){
        @Override
        public boolean onFrame(AbstractGame game)
        {
            controls.update();
            return false;
        }
    };
    
    /**
     * Create this game.
     * @param mode The Mode0 to connect this game to.
     */
    public ExampleGame(Mode0 mode)
    {
       this.mode = mode;
       imageMap = new HashMap<>();
       animationMap = new HashMap<>();
       controls = KeyController.getInstance();
       controls.addControl("up", KeyEvent.VK_W);
       controls.addControl("down", KeyEvent.VK_S);
    }
    
    /**
     * Start the game.
     */
    @Override
    public void startGame()
    {
        //[Boilerplate Code]
        mode.setGame(this); //If you do this in the constructor, it gets unhappy.
        loadImages();
        loadAnimations();
        //[Controller]
        scheduleTask("Controller", new WaitTask(UPDATE_CONTROLLER, 5), Double.NEGATIVE_INFINITY); //Updating the controller is a task too. It executes every five frames here, and has the highest possible priority so it goes first.
        //[Creating an animated sprite]
        Sprite bg = new Sprite(0, 0, imageMap.get("rayquaza"));
        mode.addPaintable(bg, 0);
        FramedSprite sprite = new FramedSprite(50, 50, imageMap.get("ManectricRunning"), 4);
        mode.addPaintable(sprite, 0.5);
        scheduleTask("ManectricAnim", new AnimateTask(sprite, animationMap.get("ManectricRunning"), 0.33, true), 2); //Animations are tasks too.
        //[The game logic]
        scheduleTask("SpeedUpTask", new ChangeSpeedTask(), 2); //Game logic is a task.
    }
    
    private void loadImages()
    {
        Ini ini;
        try{
            ini = new Ini(new File(IMAGE_INI));
        }
        catch(IOException ex){
            return;
        }
        Set<String> headings = ini.keySet();
        for(String heading : headings)
        {
            try{
                String filename = ini.get(heading, "filename");
                BufferedImage img = ImageIO.read(new File(filename));
                imageMap.put(heading, img);
            } catch (IOException ex) {
            }
        }
    }
    
    /**
     * Load animations through an ini file.
     * The layout of the ini file is explained thusly:
     * <ul>
     * <li>Heading: The name of the animation, which can be used to reference
     * it later.</li>
     * <li>frames: The number of frames of animation. If this value is not
     * a number, or less than zero, the animation is skipped entirely.</li>
     * <li>frame[number]: Specifies the animation for that frame. Counting starts
     * from 0, and works to frames. If any frame number is omitted, of the frames
     * don't match any animations, or the parameters fail in some way, a no-
     * animation is placed instead. The different animations to be used are:
     * <ul>
     * <li>setframe: Sets the frame of the Paintable to some value. Takes one
     * integer as a parameter, which is the frame number to switch to.</li>
     * <li>setpos: Sets the X-Y position of the Paintable to some values. Takes
     * two integers as a parameter, the X followed by the Y position.</li>
     * <li>shiftpos: Moves the Paintable by X number of pixels right, and Y number
     * of pixels down. Takes two integers as a parameter, the X  followed by the Y.</li>
     * <li>setimage: Changes the Paintable's image to a new one. Takes one
     * string as a parameter, which is the name of the image (not the file path!)</li>
     * </li>
     * </ul>
     */
    private void loadAnimations()
    {
        Ini ini;
        try{
            ini = new Ini(new File(ANIM_INI));
        }
        catch(IOException ex){
            return;
        }
        Set<String> headings = ini.keySet();
        for(String heading : headings)
        {
            int qty;
            try{
                qty = Integer.parseInt(ini.get(heading, "frames"));
            }
            catch(NumberFormatException ex){
                continue;
            }
            if(qty < 0){continue;}
            Animation[] script = new Animation[qty];
            for(int frame = 0; frame < qty; frame++)
            {
                String[] command = ini.get(heading, "frame"+frame).split(" ");
                int param1, param2;
                try
                {
                    switch (command[0]) {
                        case "setframe":
                            param1 = Integer.parseInt(command[1]);
                            script[frame] = new ChangeFrameAnimation(param1);
                            break;
                        case "setpos":
                            param1 = Integer.parseInt(command[1]);
                            param2 = Integer.parseInt(command[2]);
                            script[frame] = new SetPositionAnimation(param1, param2);
                            break;
                        case "shiftpos":
                            param1 = Integer.parseInt(command[1]);
                            param2 = Integer.parseInt(command[2]);
                            script[frame] = new ShiftPositionAnimation(param1, param2);
                            break;
                        case "setimage":
                            script[frame] = new ChangeImageAnimation(imageMap.get(command[1]));
                        default:
                            script[frame] = new NoAnimation();
                            break;
                    }
                }
                /*
                If any integers fail to parse, or constructors throw any exceptions,
                it becomes a no-animation instead.
                */
                catch(Exception ex)
                {
                    script[frame] = new NoAnimation();
                }
            }
            animationMap.put(heading, script);
        }
    }
    
    private class ChangeSpeedTask implements GameTask
    {
        private double speed = 0.1;
        
        @Override
        public boolean onFrame(AbstractGame model)
        {
            AnimateTask manectric = (AnimateTask)getTask("ManectricAnim");
            if(controls.getState("up") == 1)
            {
                speed += 0.05;
            }
            else if(controls.getState("down") == 1)
            {
                speed -= 0.05;
            }
            manectric.setAnimationSpeed(speed);
            return false;
        }
    }
}
