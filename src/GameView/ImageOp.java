/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.InputMismatchException;

/**
 * Operate on images.
 * <p>This class allows you to efficiently transform images. Rather than
 * rebuilding the image after each operation, this class keeps an
 * affine matrix, which is modified after each operation. When the apply()
 * method is called, the image is built using the affine matrix as a guide.
 * <p>Any combination of transformations is valid, although not necessarily
 * commutative. Additionally, any transformations that lie outside the bounding
 * box of the original image are ignored when apply() is called.
 * @author Justis
 */
public class ImageOp
{
    private BufferedImage image;
    private double[][] affineMatrix;
    
    /**
     * Start operating on an image.
     * @param image The image to perform transformations on.
     * @throws NullPointerException the image provided is null.
     */
    public ImageOp(BufferedImage image)
    {
        if(image == null){
            throw new NullPointerException("Image can't be null.");
        }
        this.image = image;
        affineMatrix = getNeutralArray();
    }
    
    /*
    Helper method. Delivers a fresh matrix, with 1's along the diagonal and
    0's everywhere else.
    */
    private static double[][] getNeutralArray()
    {
        double[][] result = new double[3][3];
        result[0][0] = result[1][1] = result[2][2] = 1;
        return result;
    }
    
    /**
     * Return a suitable test string describing the affine matrix.
     * @return The contents of the affine matrix.
     */
    @Override
    public String toString()
    {
        return Arrays.deepToString(affineMatrix);
    }
    
    /**
     * Apply an arbitrary affine matrix.
     * <p>This may be used to apply a custom transformation, if the ones
     * provided here are unsuitable. The matrix provided must be 3x3.
     * <p>All other methods build the matrix required out of their parameters,
     * and ultimately call this method to apply it.
     * @param rsmatrix The custom affine matrix to apply.
     * @return The instance of this ImageOp, for chaining.
     * @throws NullPointerException The matrix supplied is null.
     * @throws InputMismatchException The matrix is not 3x3.
     */
    public ImageOp multiplyMatrix(double[][] rsmatrix)
    {
        if(rsmatrix == null){
            throw new NullPointerException("Matrix can't be null.");
        }
        if(rsmatrix.length != 3 || rsmatrix[0].length != 3){
            throw new InputMismatchException("Matrix must be 3x3.");
        }
        double[][] result = new double[3][3];
        for(int xx = 0; xx < 3; xx++)
            for(int yy = 0; yy < 3; yy++)
            {
                double sum = 0;
                for(int zz = 0; zz < 3; zz++)
                    sum += affineMatrix[xx][zz] * rsmatrix[zz][yy];
                result[xx][yy] = sum;
            }
        affineMatrix = result;
        return this;
    }
    
    /*
    Since we're working with doubles here, this checks that everything
    is finite and happy.
    */
    private static void checkParameters(double... values)
    {
        for(double value : values)
            if(!Double.isFinite(value))
                throw new IllegalArgumentException("Non-finite value provided.");
    }
    
    /**
     * Translates an image.
     * <p>The image is moved by dx pixels in the positive X direction, and dy
     * pixels in the positive Y direction. Negative values for both are
     * permitted; Naturally, this would shift in the negative X and Y direction.
     * @param dx The amount of pixels to shift in positive X.
     * @param dy The amount of pixels to shift in positive Y.
     * @return The instance of this ImageOp, for chaining.
     * @throws IllegalArgumentException Non-finite value provided.
     */
    public ImageOp translate(double dx, double dy)
    {
        checkParameters(dx, dy);
        double[][] transAffine = getNeutralArray();
        transAffine[0][2] = dx;
        transAffine[1][2] = dy;
        return multiplyMatrix(transAffine);
    }
    
    /**
     * Scales an image.
     * <p>The image is increased by dx times in the positive X direction, and by
     * dy times in the positive Y direction. If |scale factor| is greater than 1,
     * the image is expanded, and if |scale factor| is less than 1, but greater
     * than zero, the image is shrunk. Naturally, scale factors of 1 will
     * not change the image, and scale factors of 0 will make it disappear.
     * <p>Negative values for both are permitted; this would have the effect
     * of reflecting, then scaling by
     * the absolute value.
     * @param dx The amount to expand the image in the X direction.
     * @param dy The amount to expand the image in the Y direction.
     * @return The instance of this ImageOp, for chaining.
     * @throws IllegalArgumentException Non-finite value provided.
     */
    public ImageOp scale(double dx, double dy)
    {
        checkParameters(dx, dy);
        double[][] scaleAffine = getNeutralArray();
        scaleAffine[0][0] = dx;
        scaleAffine[1][1] = dy;
        return multiplyMatrix(scaleAffine);
    }
    
    /**
     * Scales an image.
     * <p>This is simply a convenience method, as many scaling applications
     * require uniform scaling in both directions. This is identical to calling
     * scale(sf, sf).
     * @param sf The amount to expand the image in both X and Y directions.
     * @return The instance of this ImageOp, for chaining.
     * @throws IllegalArgumentException Non-finite value provided.
     */
    public ImageOp scale(double sf)
    {
        return scale(sf, sf);
    }
    
    /**
     * Rotates an image.
     * <p>The image is rotated by theta radians around the origin of the image,
     * which is the top-left corner of the image. It is recommended that 
     * the programmer uses as few rotates as possible, as the functions for
     * finding sine and cosine are not fully precise, and repeated
     * multiplication may exacerbate these errors.
     * @param theta The angle between the X-axis and the upper edge of the
     * image, in radians.
     * @return The instance of this ImageOp, for chaining.
     * @throws IllegalArgumentException Non-finite value provided.
     */
    public ImageOp rotate(double theta)
    {
        checkParameters(theta);
        double[][] rotateAffine = getNeutralArray();
        rotateAffine[0][0] = Math.cos(theta);
        rotateAffine[0][1] = Math.sin(theta);
        rotateAffine[1][0] = -Math.sin(theta);
        rotateAffine[1][1] = Math.cos(theta);
        return multiplyMatrix(rotateAffine);
    }
    
    /**
     * Rotates an image.
     * <p>A convenience method, this allows you to specify a degree measure
     * to rotate, instead of a radian measure. Despite radian-to-degree
     * conversion being available in the Math class, it can be frustrating
     * to constantly call that method for each rotation.
     * <p>It is recommended that 
     * the programmer uses as few rotates as possible, as the functions for
     * finding sine and cosine are not fully precise, and repeated
     * multiplication may exacerbate these errors.
     * @param theta The angle between the X-axis and the upper edge of
     * the image, in degrees.
     * @return The instance of this ImageOp, for chaining.
     * @throws IllegalArgumentException Non-finite value provided.
     */
    public ImageOp rotateDegrees(double theta)
    {
        return rotate(Math.toRadians(theta));
    }
    
    /**
     * Rotates an image.
     * <p>Unlike the simple rotateDegrees(double) method, this method allows you to
     * specify an origin for which to rotate the image. This is somewhat of
     * a convenience method; the same effect can be achieved by translating
     * the image to the new origin, rotating, and then translating it back.
     * However, this method can be considered safer, as all three parameters
     * are checked before applying all transformations, while a chain only
     * checks the relevant parameters.
     * @param x The X value of the center of rotation.
     * @param y The Y value of the center of rotation.
     * @param theta The angle between the X-axis and the upper edge of the
     * image, in degrees.
     * @return The instance of this ImageOp, for chaining.
     * @throws IllegalArgumentException Non-finite value provided.
     */
    public ImageOp rotateDegrees(double x, double y, double theta)
    {
        checkParameters(x, y, theta);
        return translate(x, y).rotateDegrees(theta).translate(-x, -y);
    }
    
    /**
     * Shears an image.
     * <p>If the image is sheared in X only, the upper edge of the image
     * remains in place, while the remainder of the image is pulled. If the
     * image is sheared in Y only, the left-most edge of the image remains
     * in place, while the remainder of the image is pulled.
     * @param thetaX The angle between the Y axis and its corresponding image
     * edge.
     * @param thetaY The angle between the X axis and its corresponding image
     * edge.
     * @return The instance of this ImageOp, for chaining.
     * @throws IllegalArgumentException Non-finite value provided.
     */
    public ImageOp shear(double thetaX, double thetaY)
    {
        checkParameters(thetaX, thetaY);
        double[][] shearAffine = getNeutralArray();
        shearAffine[0][1] = Math.tan(thetaX);
        shearAffine[1][0] = Math.tan(thetaY);
        return multiplyMatrix(shearAffine);
    }
    
    /**
     * Reflects an image.
     * <p>If a reflection on the X-axis takes place, this results in a vertical
     * flip. If a reflection on the Y-axis takes place, this results in a horizontal
     * flip. A reflection on both axes is identical to a 180-degree rotation.
     * @param xAxis True if a flip along the x-axis should occur.
     * @param yAxis True if a flip along the y-axis should occur.
     * @return The instance of this ImageOp, for chaining.
     */
    public ImageOp reflect(boolean xAxis, boolean yAxis)
    {
        double[][] flipAffine = getNeutralArray();
        if(yAxis){flipAffine[0][0] = -1;}
        if(xAxis){flipAffine[1][1] = -1;}
        return multiplyMatrix(flipAffine);
    }
    
    /**
     * Applies all transformations to the image.
     * <p>The transformations are applied in the order they were called. Rather
     * than reading through all pixels in the old image, transforming them, and
     * placing them in the new image, the reverse process occurs, ensuring there
     * are no gaps and providing a smooth image.
     * <p>The final image has the same dimensions as the original; thus, any
     * pixels that end up outside these dimensions are ignored.
     * @return The transformed image.
     */
    public BufferedImage apply()
    {
        /*
        |X'|   |A B C|   |X| -> X' = AX + BY + C
        |y'| = |D E F| * |Y| -> Y' = DX + EY + F
        |1 |   |0 0 1|   |1|
        While simply applying this to points would work, it ends up
        resulting in patchy images, since pixels can only be at integer
        coordinates. Instead of plugging in the old coordinate to find
        the new, we need to plug in the new coordinate and get the old.
        
        After reversing the above system, you get:
        X = (EX' - BY' + BF - EC) / (AE - BD)
        Y = (AY' - DX' + DC - FA) / (AE - BD)
        */
        
        //These constants are used just to make everything less confusing.
        final double a = affineMatrix[0][0];
        final double b = affineMatrix[0][1];
        final double c = affineMatrix[0][2];
        final double d = affineMatrix[1][0];
        final double e = affineMatrix[1][1];
        final double f = affineMatrix[1][2];
        
        final double xNumConst = (b * f) - (e * c);
        final double yNumConst = (d * c) - (a * f);
        final double denomConst = 1 / ((e * a) - (b * d)); //Curiously, this is identical between both equations.
        BufferedImage newImage = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_4BYTE_ABGR);
        for(int xx = 0; xx < image.getWidth(); xx++)
            for(int yy = 0; yy < image.getHeight(); yy++)
            {
                double readX = ((e * xx) - (b * yy) + xNumConst) * denomConst;
                double readY = ((a * yy) - (d * xx) + yNumConst) * denomConst;
                if(readX < 0 || readY < 0 
                    || readX >= image.getWidth() || readY >= image.getHeight()){continue;}
                int rgb = image.getRGB((int)readX, (int)readY);
                newImage.setRGB(xx, yy, rgb);
            }
        return newImage;
    }
    
    /**
     * Applies all transformations to the image.
     * <p>The transformations are applied in the order they were called. Rather
     * than reading through all pixels in the old image, transforming them, and
     * placing them in the new image, the reverse process occurs, ensuring there
     * are no gaps and providing a smooth image.
     * <p>Unlike the apply() method, the image returned from this method returns
     * the full image. This comes with the expense of an extra pass through the
     * image, to determine the ultimate bounds of the new image. The returned
     * image size is equal the smallest it can be, while still containing the
     * original image.
     * @return The transformed image, unclipped.
     */
    public BufferedImage applyUnclipped()
    {
        final double a = affineMatrix[0][0];
        final double b = affineMatrix[0][1];
        final double c = affineMatrix[0][2];
        final double d = affineMatrix[1][0];
        final double e = affineMatrix[1][1];
        final double f = affineMatrix[1][2];
        
        final double xNumConst = (b * f) - (e * c);
        final double yNumConst = (d * c) - (a * f);
        final double denomConst = 1 / ((e * a) - (b * d)); //Curiously, this is identical between both equations.
        
        //First, apply the transform to the original image. Determine the bounding box.
        int minX = Integer.MAX_VALUE; int maxX = Integer.MIN_VALUE;
        int minY = Integer.MAX_VALUE; int maxY = Integer.MIN_VALUE;
        for(int xx = 0; xx < image.getWidth(); xx++)
            for(int yy = 0; yy < image.getHeight(); yy++)
            {
                double readX = (a * xx) + (b * yy) + c;
                minX = Math.min(minX, (int)readX);
                maxX = Math.max(maxX, (int)readX);
                double readY = (d * xx) + (e * yy) + f;
                minY = Math.min(minY, (int)readY);
                maxY = Math.max(maxY, (int)readY);
            }
        
        //Create the new image, based on the bounds found. Put everything in place.
        int newWidth = maxX - minX + 1;
        int newHeight = maxY - minY + 1;
        BufferedImage newImage = new BufferedImage(
            newWidth,
            newHeight,
            BufferedImage.TYPE_4BYTE_ABGR);
        for(int xx = minX; xx <= maxX; xx++)
            for(int yy = minY; yy <= maxY; yy++)
            {
                double readX = ((e * xx) - (b * yy) + xNumConst) * denomConst;
                double readY = ((a * yy) - (d * xx) + yNumConst) * denomConst;
                if(readX < 0 || readY < 0 
                    || readX >= image.getWidth() || readY >= image.getHeight()){continue;}
                int rgb = image.getRGB((int)readX, (int)readY);
                newImage.setRGB(xx - minX, yy - minY, rgb);
            }
        return newImage;
    }
    
    /**
     * Applies all transformations to an image.
     * <p>This method allows the programmer to provide a MatrixFunction, which
     * is used to further transform an image. The MatrixFunction provided is
     * called, with the current scanline passed as a parameter. The matrix
     * stored in this ImageOp is multiplied with the matrix provided by the
     * AffineMatrix, and the line is drawn according to the result.
     * <p>The main intention of this is to emulate Mode 7 graphics, which is
     * done on the GBA by modifying the matrix on each scanline.
     * @param fn The MatrixFunction which will provide the matrix modifier
     * for each scanline.
     * @return The transformed image.
     */
    public BufferedImage apply(MatrixFunction fn)
    {
        BufferedImage newImage = new BufferedImage(
            image.getWidth(),
            image.getHeight(),
            BufferedImage.TYPE_4BYTE_ABGR);
        for(int yy = 0; yy < image.getHeight(); yy++)
        {
            double[][] affine = MatrixFunction.multiplyAffine(affineMatrix, fn.apply(yy));
            double a = affine[0][0];
            double b = affine[0][1];
            double c = affine[0][2];
            double d = affine[1][0];
            double e = affine[1][1];
            double f = affine[1][2];

            double xNumConst = (b * f) - (e * c);
            double yNumConst = (d * c) - (a * f);
            double denomConst = 1 / ((e * a) - (b * d)); //Curiously, this is identical between both equations.
            for(int xx = 0; xx < image.getWidth(); xx++)
            {
                double readX = ((e * xx) - (b * yy) + xNumConst) * denomConst;
                double readY = ((a * yy) - (d * xx) + yNumConst) * denomConst;
                if(readX < 0 || readY < 0 
                    || readX >= image.getWidth() || readY >= image.getHeight()){continue;}
                int rgb = image.getRGB((int)readX, (int)readY);
                newImage.setRGB(xx, yy, rgb);
            }
        }
        return newImage;
    }
}
