/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import java.awt.image.BufferedImage;

/**
 * Represents a framed sprite.
 * <p>It is often more effective for a sprite to have several different
 * frames, for animation purposes. This sprite adds onto the basic
 * Sprite class by dividing the image into frames, which may be switched
 * between.
 * <p>Sprites created in this manner must have each frame of equal size,
 * and each frame must be placed below the previous.
 * @author Justis
 */
public class FramedSprite extends Sprite
{
    private final BufferedImage[] frames;
    private int currentFrame;
    
    /**
     * Creates a framed sprite.
     * The sprite is divided into some number of frames. If less than
     * one frame is specified, it is ignored, and one frame is produced. If
     * the filename provided does not point to an image, a 10x10 transparent
     * image is used and divided instead.
     * @param x The x coordinate of this sprite.
     * @param y The y coordinate of this sprite.
     * @param filename The filename of this sprite.
     * @param numFrames The number of frames in this image.
     */
    public FramedSprite(int x, int y, String filename, int numFrames)
    {
        super(x, y, filename);
        if(numFrames < 1){
            numFrames = 1;}
        frames = new BufferedImage[numFrames];
        BufferedImage master = super.getImage();
        int frameWidth = master.getWidth();
        int frameHeight = master.getHeight() / numFrames;
        for(int index = 0; index < frames.length; index++)
        {
            frames[index] = master.getSubimage(0, index * frameHeight, frameWidth, frameHeight);
        }
    }
    
    /**
     * Creates a framed sprite.
     * The sprite is divided into some number of frames. If less than
     * one frame is specified, it is ignored, and one frame is produced. If
     * the BufferedImage provided is null, a 10x10 transparent
     * image is used and divided instead.
     * @param x The x coordinate of this sprite.
     * @param y The y coordinate of this sprite.
     * @param img The sprite's image.
     * @param numFrames The number of frames in this image.
     */
    public FramedSprite(int x, int y, BufferedImage img, int numFrames)
    {
        super(x, y, img);
        frames = new BufferedImage[numFrames];
        int frameWidth = img.getWidth();
        int frameHeight = img.getHeight() / numFrames;
        for(int index = 0; index < frames.length; index++)
        {
            frames[index] = img.getSubimage(0, index * frameHeight, frameWidth, frameHeight);
        }
    }
    
    /**
     * Changes the frame to display.
     * Frames count from 0 onward.
     * @param frame The frame number to use.
     * @throws ArrayIndexOutOfBoundsException Frame is less than zero, or greater
     * than the number of frames.
     */
    public void setFrame(int frame)
    {
        if(frame < 0 || frame >= frames.length){
            throw new ArrayIndexOutOfBoundsException("Invalid frame specified");
        }
        currentFrame = frame;
    }
    
    /**
     * Displays the current frame.
     * @return The current frame.
     */
    @Override
    public BufferedImage getImage()
    {
        return frames[currentFrame];
    }
}
