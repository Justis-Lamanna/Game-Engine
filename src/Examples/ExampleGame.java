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
import GameModel.SpriteAnimations.NoAnimation;
import GameModel.SpriteAnimations.SetPositionAnimation;
import GameModel.SpriteAnimations.ShiftPositionAnimation;
import GameModel.WaitTask;
import GameView.FramedSprite;
import GameView.GameMode.Mode0;
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
    private final Map<String, GameTask> taskMap;
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
       taskMap = new HashMap<>();
       imageMap = new HashMap<>();
       animationMap = new HashMap<>();
       controls = KeyController.getInstance();
       controls.addControl("up", KeyEvent.VK_W);
       controls.addControl("down", KeyEvent.VK_S);
    }
    
    /**
     * Add a task.
     * In order to make it easier for me to describe tasks, I introduced
     * a mapping system, allowing me to use Strings to describe tasks. If
     * either the name or the task is null, nothing happens.
     * @param name The name of the task.
     * @param task The task to execute each frame.
     * @param priority The priority of the task.
     */
    public void addTask(String name, GameTask task, double priority)
    {
        if(name != null && task != null)
        {
            taskMap.put(name, task);
            addTask(task, priority);
        }
    }
    
    /**
     * Remove a task.
     * This lets you use the tasks name to remove it, instead of the
     * actual task.
     * @param name The name of the task to remove.
     */
    public void removeTask(String name)
    {
        removeTask(taskMap.remove(name));
    }
    
    /**
     * Schedule a task.
     * See the scheduleTask(GameTask, double) method for an explanation
     * on scheduling vs. adding.
     * @param name The name of the task.
     * @param task The task to schedule.
     * @param priority The priority of the task.
     */
    public void scheduleTask(String name, GameTask task, double priority)
    {
        if(name != null && task != null)
        {
            taskMap.put(name, task);
            scheduleTask(task, priority);
        }
    }
    
    /**
     * Enable or disable a task.
     * This lets you use the tasks name to enable or disable it, instead
     * of the actual task.
     * @param name The name of the task.
     * @param enabled True if the task should be enabled, false if not.
     */
    public void setTaskEnabled(String name, boolean enabled)
    {
        setTaskEnabled(taskMap.get(name), enabled);
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
        FramedSprite sprite = new FramedSprite(50, 50, imageMap.get("ManectricRunning"), 4);
        mode.addSprite(sprite, 0);
        scheduleTask("ManectricAnim", new AnimateTask(sprite, animationMap.get("ManectricRunning"), 0.33, true), 2); //Animations are tasks too.
        scheduleTask("ManectricShiftAnim", new AnimateTask(sprite, animationMap.get("ManectricSliding"), 0.33, true), 2); //Animations can be stacked.
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
            int qty = Integer.parseInt(ini.get(heading, "frames"));
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
                        default:
                            script[frame] = new NoAnimation();
                            break;
                    }
                }
                //If the integers fail to parse, or no parameters are supplied,
                //just automatically go to a no-anim.
                catch(Exception ex)
                {
                    script[frame] = new NoAnimation();
                }
                System.out.println(script[frame]);
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
            AnimateTask manectric = (AnimateTask)taskMap.get("ManectricAnim");
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
