/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

import GameView.GameMode.SortedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import java.util.Map;

/**
 * A skeleton to facilitate game creation.
 * <p>Most importantly, this class utilizes GameTasks to create game logic.
 * On each call to onFrame(), which is typically done by the GameMode, each
 * task is iterated through, one after the next. Tasks can be assigned a name, 
 * which can then be used in place of the task
 * when methods require a task parameter.
 * <p>Like in the Mode0 class, this internally gives each task its own
 * priority and disable flag. GameTasks with lower priorities are executed
 * before tasks of higher priority. Additionally, a priority range may be
 * set; This would execute all tasks lower than or equal to some priority,
 * and ignore all others. Between tasks of equal priority, the most recent
 * addition will be executed before later ones. Due to the nature of comparing
 * doubles, NaN is the highest value possible.
 * <p>Utilizing organized priorities can help game creation run smoothly. Generally,
 * things like playing sound would have a very high priority, so sound will
 * never cease playing due to a priority bound. Animations may have a high
 * priority as well; oftentimes animations need to play and finish before other
 * tasks are taken care of, so the priority bound could be used to halt everything
 * except animation, sound, and any other important tasks.
 * @author Justis
 */
public abstract class AbstractGame implements GameModel
{
    private class GameTaskPrio implements GameTask, Comparable
    {
        private final GameTask task;
        private final double priority;
        private boolean disable;
        
        private GameTaskPrio(GameTask task, double priority)
        {
            this.task = task;
            this.priority = priority;
            disable = false;
        }
        
        @Override
        public int compareTo(Object o)
        {
            GameTaskPrio other = (GameTaskPrio)o;
            return Double.compare(priority, other.priority);
        }
        
        @Override
        public boolean onFrame(AbstractGame game)
        {
            return disable ? false : task.onFrame(game);
        }
        
        @Override
        public String toString()
        {
            return String.format("%s%s (%f)", disable ? "-" : "", task.getClass(), priority);
        }
    }
    
    private final List<GameTaskPrio> tasks = new SortedList<>();
    private final List<GameTaskPrio> scheduledTasks = new ArrayList<>();
    private final Map<String, GameTaskPrio> taskMap = new HashMap<>();
    private double priorityRange = Double.NaN;
    
    /**
     * Add a task to the task pool.
     * <p>If the task provided is null, nothing happens.
     * @param task The task to add to the pool.
     * @param priority The priority of the task.
     */
    public void addTask(GameTask task, double priority)
    {
        if(task != null){
            tasks.add(new GameTaskPrio(task, priority));
        }
    }
    
    /**
     * Add a task to the task pool.
     * <p>If the task or the name provided is null, nothing happens.
     * @param name The name of the task.
     * @param task The task to add to the pool.
     * @param priority The priority of the task.
     */
    public void addTask(String name, GameTask task, double priority)
    {
        if(task != null && name != null){
            GameTaskPrio pTask = new GameTaskPrio(task, priority);
            tasks.add(pTask);
            taskMap.put(name, pTask);
        }
    }
    
    /**
     * Remove a task from the task pool.
     * @param task The task to remove from the pool.
     */
    public void removeTask(GameTask task)
    {
        Iterator<GameTaskPrio> iter = tasks.iterator();
        while(iter.hasNext())
        {
            GameTaskPrio gt = iter.next();
            if(gt.task.equals(task)){iter.remove(); break;}
        }
    }
    
    /**
     * Remove a task from the task pool.
     * @param taskName The name of the task to remove.
     */
    public void removeTask(String taskName)
    {
        tasks.remove(taskMap.get(taskName));
    }
    
    /**
     * Schedules a task to be added to the task pool.
     * <p>When tasks are being executed, it can be a little iffy adding
     * tasks to the pool through addTask. When a task is scheduled through
     * this method, it is added to the pool after all the tasks in the pool
     * have been run through.
     * <p>If a null task is provided, nothing happens.
     * @param task The task to schedule.
     * @param priority The priority of the task.
     */
    public void scheduleTask(GameTask task, double priority)
    {
        if(task != null){
            scheduledTasks.add(new GameTaskPrio(task, priority));
        }
    }
    
    /**
     * Schedules a task to be added to the task pool.
     * <p>When tasks are being executed, it can be a little iffy adding
     * tasks to the pool through addTask. When a task is scheduled through
     * this method, it is added to the pool after all the tasks in the pool
     * have been run through.
     * <p>If a null task is provided, nothing happens.
     * @param name The name of the task.
     * @param task The task to schedule.
     * @param priority The priority of the task.
     */
    public void scheduleTask(String name, GameTask task, double priority)
    {
        if(task != null && name != null){
            GameTaskPrio pTask = new GameTaskPrio(task, priority);
            scheduledTasks.add(pTask);
            taskMap.put(name, pTask);
        }
    }
    
    /**
     * Enable or disable a task.
     * @param task The task to enable/disable.
     * @param enable True to enable the task, false if to disable.
     */
    public void setTaskEnabled(GameTask task, boolean enable)
    {
        Iterator<GameTaskPrio> iter = tasks.iterator();
        while(iter.hasNext())
        {
            GameTaskPrio gt = iter.next();
            if(gt.task.equals(task))
            {
                gt.disable = !enable;
                return;
            }
        }
    }
    
    /**
     * Enable or disable a task.
     * @param name The name of the task to enable/disable.
     * @param enable True to enable the task, false to disable.
     */
    public void setTaskEnabled(String name, boolean enable)
    {
        GameTaskPrio task = taskMap.get(name);
        if(task != null)
        {
            task.disable = !enable;
        }
    }
    
    /**
     * Get the task associated with a name.
     * @param name The name of the task.
     * @return The task itself, or null if there is no such task.
     */
    public GameTask getTask(String name)
    {
        return taskMap.get(name).task;
    }
    
    /**
     * Set the priority bound on the task pool.
     * All tasks with priority less than or equal to this bound will be
     * executed, and all tasks greater than this bound are ignored. A bound
     * of Double.NaN would allow all tasks
     * to execute, and a bound of Double.NEGATIVE_INFINITY
     * would disable all tasks.
     * @param bound The new priority bound of the task pool.
     */
    public void setTaskBound(double bound)
    {
        priorityRange = bound;
    }
    
    /**
     * Runs through all the tasks in the pool.
     * The controller's state is updated through its update() method, and
     * the tasks are then run through, from lowest to highest priority, until
     * hitting the priority range.
     */
    @Override
    public void onFrame()
    {
        Iterator<GameTaskPrio> iter = tasks.iterator();
        while(iter.hasNext())
        {
            GameTaskPrio task = iter.next();
            if(task.priority > priorityRange){return;}
            if(task.onFrame(this)){
                iter.remove();
            }
        }
        scheduledTasks.forEach(tasks::add);
        scheduledTasks.clear();
    }
    
    /**
     * Shows the list of tasks in the pool.
     * @return All the tasks in the pool.
     */
    @Override
    public String toString()
    {
        return tasks.toString();
    }
}
