/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GameView;


/**
 * A function which takes an integer and returns an affine matrix.
 * <p>The intended use of this device is to provide Mode 7 type capabilities
 * to the programmer. When calling the apply() method in ImageOp, the programmer
 * may optionally provide a MatrixFunction. Just before each line of pixels is
 * drawn, the MatrixFunction provided will be passed the line number, and will
 * provide the correct affine matrix. That line of pixels will then be
 * transformed according to that matrix.
 * <p>Several static methods are also provided, to assist in creating a
 * MatrixFunction. These functions take input, and provide an affine matrix
 * for all basic transformations.
 * <p>All static methods provided here will throw an IllegalArgumentException
 * if the parameters provided are positive infinity, negative infinity, or NaN.
 * @author Justis
 */
@FunctionalInterface
public interface MatrixFunction
{
    double[][] apply(int index);
    
    /**
     * Creates the neutral affine matrix.
     * The matrix returned is a 3x3 matrix, with one along the diagonal, and
     * zero everywhere else. Applying this matrix to the image will result in
     * no transformation taking place.
     * @return The neutral affine matrix.
     */
    public static double[][] neutralAffine()
    {
        double[][] affine = new double[3][3];
        affine[0][0] = affine[1][1] = affine[2][2] = 1;
        return affine;
    }
    
    /**
     * Creates a translation affine matrix.
     * @param dx The number of pixels to shift in the X direction.
     * @param dy The number of pixels to shift in the Y direction.
     * @return The translation affine matrix.
     */
    public static double[][] translateAffine(double dx, double dy)
    {
        if(!Double.isFinite(dx) || !Double.isFinite(dy)){
            throw new IllegalArgumentException("Non-finite value provided");
        }
        double[][] affine = neutralAffine();
        affine[0][2] = dx;
        affine[1][2] = dy;
        return affine;
    }
    
    /**
     * Creates a scaling affine matrix.
     * @param dx The amount to expand in the X direction, with 1 being no change.
     * @param dy The amount to expand in the Y direction, with 1 being no change.
     * @return The scale affine matrix.
     */
    public static double[][] scaleAffine(double dx, double dy)
    {
        if(!Double.isFinite(dx) || !Double.isFinite(dy)){
            throw new IllegalArgumentException("Non-finite value provided");
        }
        double[][] affine = neutralAffine();
        affine[0][0] = dx;
        affine[1][1] = dy;
        return affine;
    }
    
    /**
     * Creates a rotation affine matrix.
     * @param theta The amount to rotate, in radians.
     * @return The rotation affine matrix.
     */
    public static double[][] rotateAffine(double theta)
    {
        if(!Double.isFinite(theta)){
            throw new IllegalArgumentException("Non-finite value provided");
        }
        double[][] affine = neutralAffine();
        affine[0][0] = Math.cos(theta);
        affine[0][1] = Math.sin(theta);
        affine[1][0] = -Math.sin(theta);
        affine[1][1] = Math.cos(theta);
        return affine;
    }
    
    /**
     * Creates a shear affine matrix.
     * @param thetaX The angle between the Y axis, and the left edge of the image.
     * @param thetaY The angle between the X axis, and the upper edge of the image.
     * @return The shear affine matrix.
     */
    public static double[][] shearAffine(double thetaX, double thetaY)
    {
        if(!Double.isFinite(thetaX) || !Double.isFinite(thetaY)){
            throw new IllegalArgumentException("Non-finite value provided");
        }
        double[][] affine = neutralAffine();
        affine[0][1] = Math.tan(thetaX);
        affine[1][0] = Math.tan(thetaY);
        return affine;
    }
    
    /**
     * Creates a reflection affine matrix.
     * This is technically the same as a scaling operation with a -1 scaling
     * factor. However, use of this method makes it obvious that flipping is
     * occuring.
     * @param xFlip True if a flip along the X-axis should occur.
     * @param yFlip True if a flip along the Y-axis should occur.
     * @return The reflection affine matrix.
     */
    public static double[][] reflectAffine(boolean xFlip, boolean yFlip)
    {
        double[][] affine = neutralAffine();
        if(yFlip){affine[0][0] = -1;}
        if(xFlip){affine[1][1] = -1;}
        return affine;
    }
    
    public static double[][] multiplyAffine(double[][] left, double[][] right)
    {
        if(left == null || right == null){
            throw new NullPointerException("Array provided was null.");
        }
        double[][] result = new double[3][3];
        for(int xx = 0; xx < 3; xx++)
            for(int yy = 0; yy < 3; yy++)
            {
                double sum = 0;
                for(int zz = 0; zz < 3; zz++)
                    sum += left[xx][zz] * right[zz][yy];
                result[xx][yy] = sum;
            }
        return result;
    }
}
