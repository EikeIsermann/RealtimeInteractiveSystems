/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik.
 * All rights reserved.
 *******************************************************************************/
package ogl.vecmath;

import java.nio.FloatBuffer;

/**
 * Simple 3 component v class. Vectors are non mutable and can be passed
 * around by value.
 * 
 * @author henrik, marc
 */
public interface Vector {

  /**
   * Accessors.
   * 
   * @return The components value.
   */
  public abstract float x();

  /**
   * Accessors.
   * 
   * @return The components value.
   */
  public abstract float y();

  /**
   * Accessors.
   * 
   * @return The components value.
   */
  public abstract float z();

  /**
   * Component-wise addition of two vectors.
   * 
   * @param v
   *          The second v.
   * @return The sum.
   */
  public abstract Vector add(Vector v);

  /**
   * Subtract a v from this v.
   * 
   * @param v
   *          The second v.
   * @return The difference.
   */
  public abstract Vector sub(Vector v);

  /**
   * Multiply this v by a scalar.
   * 
   * @param s
   *          The scalar.
   * @return The scaled v.
   */
  public abstract Vector mult(float s);

  /**
   * Componentwise multiplication of two vectors. This is not the dot product!
   * 
   * @param v
   *          The second v.
   * @return The product.
   */
  public abstract Vector mult(Vector v);

  /**
   * Compute the length of this v.
   * 
   * @return The lenght of this v.
   */
  public abstract float length();

  /**
   * Normalize this v.
   * 
   * @return The normalized v.
   */
  public abstract Vector normalize();

  /**
   * Calculate the dot product of two vectors.
   * 
   * @param v
   *          The second v.
   * @return The dot product.
   */
  public abstract float dot(Vector v);

  /**
   * Calculate the cross product of two vectors.
   * 
   * @param v
   *          The second v.
   * @return The cross product.
   */
  public abstract Vector cross(Vector v);

  public abstract float[] asArray();

  public abstract FloatBuffer asBuffer();

  public abstract void fillBuffer(FloatBuffer buf);

  /*
   * @see java.lang.Object#toString()
   */
  public abstract String toString();

  public abstract boolean equals(final Object o);

  public abstract int compareTo(Vector o);

  public abstract int size();

}