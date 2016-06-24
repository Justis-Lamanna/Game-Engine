/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Examples;

import GameView.ImageOp;
import GameView.MatrixFunction;
import GameView.ViewMode;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Test ViewMode.
 * <p>This ViewMode is used to test out the use of MatrixFunction in conjunction
 * with ImageOp.apply(). This transforms the image provided into something
 * to emulate a simple perspective. The top line of pixels is shrunk by 50%,
 * the middle line of pixels is kept the same size, and the bottom line of
 * pixels is expanded by 50%. The image is rotated before applying this
 * perspective transform, to make it seem as if you're looking around the
 * image from above.
 * <p>Mode 7 is complex stuff. The best solution, rather than the one provided
 * here, would be to develop an equation which would take the viewer's X-Y-Z
 * position, as well as their roll, pitch, and yaw, and convert it into
 * the appropriate positioning. This would allow you to have complete control
 * over the "camera", although it may be out of the scope of your needs.
 * @author Justis
 */
public class Mode7Mode implements ViewMode
{
    private final ViewMode view;
    
    public Mode7Mode(ViewMode view)
    {
        this.view = view;
    }
    
    @Override
    public BufferedImage getFrame()
    {
        BufferedImage timg = new ImageOp(view.getFrame())
                .apply(new Mode7Function());
        return timg;
    }
    
    private class Mode7Function implements MatrixFunction
    {

        @Override
        public double[][] apply(int index)
        {
            double[][] translate = MatrixFunction.translateAffine(80 - index / 2f, 1);
            double[][] scale = MatrixFunction.scaleAffine(0.5 + index / 240f, 1);
            return MatrixFunction.multiplyAffine(translate, scale);
        }
        
    }
}
