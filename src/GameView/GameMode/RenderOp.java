/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView.GameMode;

import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorModel;

/**
 *
 * @author Justis
 */
public class RenderOp implements BufferedImageOp
{
    private final PaintProperties paint;
    
    public RenderOp(PaintProperties p)
    {
        paint = p;
    }
    
    @Override
    public BufferedImage filter(BufferedImage src, BufferedImage dest) {
        BufferedImage image = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
        int imageX = paint.getPaintable().getX();
        int imageY = paint.getPaintable().getY();
        for(int xx = 0; xx < image.getWidth(); xx++){
            for(int yy = 0; yy < image.getHeight(); yy++){
                int paintX = xx;
                int paintY = yy;
                int color = src.getRGB(xx, yy);
                if(color == paint.getTransparentRGB()){
                    color = 0;
                }
                image.setRGB(paintX, paintY, color);
            }
        }
        return image;
    }

    @Override
    public Rectangle2D getBounds2D(BufferedImage src){return null;}
    
    @Override
    public BufferedImage createCompatibleDestImage(BufferedImage src, ColorModel destCM) {return null;}

    @Override
    public Point2D getPoint2D(Point2D srcPt, Point2D dstPt) {return null;}

    @Override
    public RenderingHints getRenderingHints() {return null;}
    
}
