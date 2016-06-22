/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameModel;

/**
 * Executes a task less often.
 * <p>It is typically useful to have a task that runs often, yet not at one
 * frame per second. This allows you to specify a task to run one every
 * set number of frames.
 * @author Justis
 */
public class WaitTask implements GameTask
{
    private final GameTask task;
    private final long timeToWait;
    private long current;
    
    /**
     * Creates a WaitTask.
     * If the time to wait is less than zero, the task begins immediately.
     * @param task The task to execute after a certain time.
     * @param timeToWait The number of frames to wait before execution.
     * @throws NullPointerException GameTask provided is null.
     */
    public WaitTask(GameTask task, long timeToWait)
    {
        if(task == null){
            throw new NullPointerException("Task can't be null.");
        }
        this.task = task;
        this.timeToWait = timeToWait;
        current = 0;
    }
    
    /**
     * Does the code.
     * @param game The AbstractGame that called this task. Ignored.
     * @return True if the task should be deleted after running, false if
     * it shouldn't be deleted.
     */
    @Override
    public boolean onFrame(AbstractGame game)
    {
        if(current++ > timeToWait)
        {
            current = 0; 
            return task.onFrame(game);
        }
        return false;
    }
}
