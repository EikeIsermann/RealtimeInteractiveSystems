/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik.
 * All rights reserved.
 *******************************************************************************/
package ogl.vecmathimp;

import java.nio.FloatBuffer;

import ogl.vecmath.Matrix;
import ogl.vecmath.Quaternion;
import ogl.vecmath.Vector;

import org.lwjgl.BufferUtils;


//Select the factory we want to use.
import static ogl.vecmathimp.FactoryDefault.vecmath;

/**
 * A simple 4x4 matrix class using float getValues(). Matrices are non-mutable and
 * can be passed around as getValues(). The matrix is stored in one float array in
 * column-major format.
 * 
 * @author henrik, marc
 */
public class MatrixImp implements Matrix {

  private float[] values;

  public float[] getValues(){return values;} 
  
  public void setValues(float[] vals){
    values=vals;
    } 
  /**
   * <code>identity</code> The identity matrix.
   */
  public static final Matrix identity = new MatrixImp();

  private MatrixImp() {
    makeIdent();
  }

  public MatrixImp(float m00, float m01, float m02, float m03, float m10,
      float m11, float m12, float m13, float m20, float m21, float m22,
      float m23, float m30, float m31, float m32, float m33) {
    makeIdent();
    set(0, 0, m00);
    set(0, 1, m01);
    set(0, 2, m02);
    set(0, 3, m03);
    set(1, 0, m10);
    set(1, 1, m11);
    set(1, 2, m12);
    set(1, 3, m13);
    set(2, 0, m20);
    set(2, 1, m21);
    set(2, 2, m22);
    set(2, 3, m23);
    set(3, 0, m30);
    set(3, 1, m31);
    set(3, 2, m32);
    set(3, 3, m33);
  }

  /**
   * Construct a new matrix from three basis vectors.
   * 
   * @param b0
   *          The first basis v.
   * @param b1
   *          The second basis v.
   * @param b2
   *          The third basis v.
   */
  public MatrixImp(Vector b0, Vector b1, Vector b2) {
    makeIdent();
    set(0, 0, b0.x());
    set(1, 0, b0.y());
    set(2, 0, b0.z());
    set(0, 1, b1.x());
    set(1, 1, b1.y());
    set(2, 1, b1.z());
    set(0, 2, b2.x());
    set(1, 2, b2.y());
    set(2, 2, b2.z());
    //System.out.print(this);
  }

  /**
   * Construct a matrix from an array of float getValues().
   * 
   * @param m
   *          An array of 16 float getValues().
   */
  public MatrixImp(float[] m) {
    setValues(m.clone());
  }

  /**
   * Construct a matrix from a quaternion.
   * 
   * @param q
   *          The quaternion.
   */
  public MatrixImp(Quaternion q) {
    float qx2 = q.x() * q.x();
    float qy2 = q.y() * q.y();
    float qz2 = q.z() * q.z();
    // float qw2 = q.w * q.w;

    setValues(new float[] { (1 - 2 * qy2 - 2 * qz2),
        (2 * q.x() * q.y() - 2 * q.z() * q.w()), (2 * q.x() * q.z() + 2 * q.y() * q.w()), 0,
        (2 * q.x() * q.y() + 2 * q.z() * q.w()), (1 - 2 * qx2 - 2 * qz2),
        (2 * q.y() * q.z() - 2 * q.x() * q.w()), 0, (2 * q.x() * q.z() - 2 * q.y() * q.w()),
        (2 * q.y() * q.z() + 2 * q.x() * q.w()), (1 - 2 * qx2 - 2 * qy2), 0, 0, 0, 0, 1 });
  }

  /**
   * Construct a new matrix that represents a translation.
   * 
   * @param t
   *          The translation v.
   * @return The translation matrix.
   */
  public static Matrix translate(Vector t) {
    MatrixImp m = new MatrixImp();
    m.set(3, 0, t.x());
    m.set(3, 1, t.y());
    m.set(3, 2, t.z());
    return m;
  }

  public static Matrix translate(float x, float y, float z) {
    MatrixImp m = new MatrixImp();
    m.set(3, 0, x);
    m.set(3, 1, y);
    m.set(3, 2, z);
    return m;
  }

  /**
   * Construct a new matrix that represents a rotation.
   * 
   * @param axis
   *          The rotation axis.
   * @param angle
   *          The angle of rotaion in degree.
   * @return The rotation matrix.
   */
  public static Matrix rotate(Vector axis, float angle) {
    final MatrixImp m = new MatrixImp();
    final float rad = (angle / 180.0f) * ((float) Math.PI);
    final float cosa = (float) Math.cos(rad);
    final float sina = (float) Math.sin(rad);
    final Vector naxis = axis.normalize();
    final float rx = naxis.x();
    final float ry = naxis.y();
    final float rz = naxis.z();
    final float icosa = 1 - cosa;

    m.set(0, 0, (float) (icosa * rx * rx + cosa));
    m.set(0, 1, (float) (icosa * rx * ry + rz * sina));
    m.set(0, 2, (float) (icosa * rx * rz - ry * sina));

    m.set(1, 0, (float) (icosa * rx * ry - rz * sina));
    m.set(1, 1, (float) (icosa * ry * ry + cosa));
    m.set(1, 2, (float) (icosa * ry * rz + rx * sina));

    m.set(2, 0, (float) (icosa * rx * rz + ry * sina));
    m.set(2, 1, (float) (icosa * ry * rz - rx * sina));
    m.set(2, 2, (float) (icosa * rz * rz + cosa));
    return m;
  }

  public static Matrix rotate(float ax, float ay, float az, float angle) {
    return rotate(vecmath.vector(ax, ay, az), angle);
  }

  /**
   * Construct a new matrix that represents a scale transformation.
   * 
   * @param s
   *          The three scale factors.
   * @return The scale matrix.
   */
  public static Matrix scale(Vector s) {
    MatrixImp m = new MatrixImp();
    m.set(0, 0, s.x());
    m.set(1, 1, s.y());
    m.set(2, 2, s.z());
    return m;
  }

  public static Matrix scale(float x, float y, float z) {
    MatrixImp m = new MatrixImp();
    m.set(0, 0, x);
    m.set(1, 1, y);
    m.set(2, 2, z);
    return m;
  }

  /**
   * Construct a new matrix that represents a 'lookat' transformation. The
   * result is consistent with the OpenGL function <code>gluLookAt()</code>. The
   * result ist the <b>inverse</b> of the camera transformation K. It tranforms
   * form world space into camera space.
   * 
   * @param eye
   *          The eye point.
   * @param center
   *          The lookat point.
   * @param up
   *          The up v.
   * @return The rotation matrix.
   */
  public static Matrix lookat(Vector eye, Vector center, Vector up) {
    Vector backwards = eye.sub(center).normalize();
    Vector side = up.cross(backwards).normalize();
    up = backwards.cross(side).normalize();
    MatrixImp r = new MatrixImp(side, up, backwards);
    Matrix t = MatrixImp.translate(eye.mult(-1.0f));
    return r.mult(t);
  }

  public static Matrix frustum(float left, float right, float bottom,
      float top, float zNear, float zFar) {
    if (zNear <= 0.0f || zFar < 0.0f) {
      throw new RuntimeException(
        "Frustum: zNear and zFar must be positive, and zNear>0");
    }
    if (left == right || top == bottom) {
      throw new RuntimeException(
        "Frustum: top,bottom and left,right must not be equal");
    }
    // Frustum matrix:
    // 2*zNear/dx 0 A 0
    // 0 2*zNear/dy B 0
    // 0 0 C D
    // 0 0 −1 0
    float zNear2 = 2.0f * zNear;
    float dx = right - left;
    float dy = top - bottom;
    float dz = zFar - zNear;
    float A = (right + left) / dx;
    float B = (top + bottom) / dy;
    float C = -1.0f * (zFar + zNear) / dz;
    float D = -2.0f * (zFar * zNear) / dz;

    return new MatrixImp(zNear2 / dx, 0, 0, 0, 0, zNear2 / dy, 0, 0, A, B, C, -1,
      0, 0, D, 0);
  }

  public static Matrix perspective(float fovy, float aspect, float zNear,
      float zFar) {
    float top = (float) Math.tan(fovy * ((float) Math.PI) / 360.0f) * zNear;
    float bottom = -1.0f * top;
    float left = aspect * bottom;
    float right = aspect * top;
    return frustum(left, right, bottom, top, zNear, zFar);
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#get(int, int)
   */
  @Override
  public float get(int c, int r) {
    return values[4 * c + r];
  }

  /**
   * Set the value of one matrix element.
   * 
   * @param c
   *          The column in which to set the value.
   * @param r
   *          The row in which to set the value.
   * @param v
   *          The new value for the matrix element at position (c, r)
   */
  private void set(int c, int r, float v) {
    values[4 * c + r] = v;
  }

  /**
   * Calculate the product of two matrices.
   * 
   * @param m
   *          The second matrix.
   * @return The product.
   */

  @Override
  public Matrix mult(Matrix m) {
    // Optimized version.
    MatrixImp n = new MatrixImp();
    {
      {
        float v = 0;
        v += values[4 * 0 + 0] * m.getValues()[4 * 0 + 0];
        v += values[4 * 1 + 0] * m.getValues()[4 * 0 + 1];
        v += values[4 * 2 + 0] * m.getValues()[4 * 0 + 2];
        v += values[4 * 3 + 0] * m.getValues()[4 * 0 + 3];
        n.getValues()[4 * 0 + 0] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 1] * m.getValues()[4 * 0 + 0];
        v += getValues()[4 * 1 + 1] * m.getValues()[4 * 0 + 1];
        v += getValues()[4 * 2 + 1] * m.getValues()[4 * 0 + 2];
        v += getValues()[4 * 3 + 1] * m.getValues()[4 * 0 + 3];
        n.getValues()[4 * 0 + 1] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 2] * m.getValues()[4 * 0 + 0];
        v += getValues()[4 * 1 + 2] * m.getValues()[4 * 0 + 1];
        v += getValues()[4 * 2 + 2] * m.getValues()[4 * 0 + 2];
        v += getValues()[4 * 3 + 2] * m.getValues()[4 * 0 + 3];
        n.getValues()[4 * 0 + 2] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 3] * m.getValues()[4 * 0 + 0];
        v += getValues()[4 * 1 + 3] * m.getValues()[4 * 0 + 1];
        v += getValues()[4 * 2 + 3] * m.getValues()[4 * 0 + 2];
        v += getValues()[4 * 3 + 3] * m.getValues()[4 * 0 + 3];
        n.getValues()[4 * 0 + 3] = v;
      }
    }
    {
      {
        float v = 0;
        v += getValues()[4 * 0 + 0] * m.getValues()[4 * 1 + 0];
        v += getValues()[4 * 1 + 0] * m.getValues()[4 * 1 + 1];
        v += getValues()[4 * 2 + 0] * m.getValues()[4 * 1 + 2];
        v += getValues()[4 * 3 + 0] * m.getValues()[4 * 1 + 3];
        n.getValues()[4 * 1 + 0] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 1] * m.getValues()[4 * 1 + 0];
        v += getValues()[4 * 1 + 1] * m.getValues()[4 * 1 + 1];
        v += getValues()[4 * 2 + 1] * m.getValues()[4 * 1 + 2];
        v += getValues()[4 * 3 + 1] * m.getValues()[4 * 1 + 3];
        n.getValues()[4 * 1 + 1] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 2] * m.getValues()[4 * 1 + 0];
        v += getValues()[4 * 1 + 2] * m.getValues()[4 * 1 + 1];
        v += getValues()[4 * 2 + 2] * m.getValues()[4 * 1 + 2];
        v += getValues()[4 * 3 + 2] * m.getValues()[4 * 1 + 3];
        n.getValues()[4 * 1 + 2] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 3] * m.getValues()[4 * 1 + 0];
        v += getValues()[4 * 1 + 3] * m.getValues()[4 * 1 + 1];
        v += getValues()[4 * 2 + 3] * m.getValues()[4 * 1 + 2];
        v += getValues()[4 * 3 + 3] * m.getValues()[4 * 1 + 3];
        n.getValues()[4 * 1 + 3] = v;
      }
    }
    {
      {
        float v = 0;
        v += getValues()[4 * 0 + 0] * m.getValues()[4 * 2 + 0];
        v += getValues()[4 * 1 + 0] * m.getValues()[4 * 2 + 1];
        v += getValues()[4 * 2 + 0] * m.getValues()[4 * 2 + 2];
        v += getValues()[4 * 3 + 0] * m.getValues()[4 * 2 + 3];
        n.getValues()[4 * 2 + 0] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 1] * m.getValues()[4 * 2 + 0];
        v += getValues()[4 * 1 + 1] * m.getValues()[4 * 2 + 1];
        v += getValues()[4 * 2 + 1] * m.getValues()[4 * 2 + 2];
        v += getValues()[4 * 3 + 1] * m.getValues()[4 * 2 + 3];
        n.getValues()[4 * 2 + 1] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 2] * m.getValues()[4 * 2 + 0];
        v += getValues()[4 * 1 + 2] * m.getValues()[4 * 2 + 1];
        v += getValues()[4 * 2 + 2] * m.getValues()[4 * 2 + 2];
        v += getValues()[4 * 3 + 2] * m.getValues()[4 * 2 + 3];
        n.getValues()[4 * 2 + 2] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 3] * m.getValues()[4 * 2 + 0];
        v += getValues()[4 * 1 + 3] * m.getValues()[4 * 2 + 1];
        v += getValues()[4 * 2 + 3] * m.getValues()[4 * 2 + 2];
        v += getValues()[4 * 3 + 3] * m.getValues()[4 * 2 + 3];
        n.getValues()[4 * 2 + 3] = v;
      }
    }
    {
      {
        float v = 0;
        v += getValues()[4 * 0 + 0] * m.getValues()[4 * 3 + 0];
        v += getValues()[4 * 1 + 0] * m.getValues()[4 * 3 + 1];
        v += getValues()[4 * 2 + 0] * m.getValues()[4 * 3 + 2];
        v += getValues()[4 * 3 + 0] * m.getValues()[4 * 3 + 3];
        n.getValues()[4 * 3 + 0] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 1] * m.getValues()[4 * 3 + 0];
        v += getValues()[4 * 1 + 1] * m.getValues()[4 * 3 + 1];
        v += getValues()[4 * 2 + 1] * m.getValues()[4 * 3 + 2];
        v += getValues()[4 * 3 + 1] * m.getValues()[4 * 3 + 3];
        n.getValues()[4 * 3 + 1] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 2] * m.getValues()[4 * 3 + 0];
        v += getValues()[4 * 1 + 2] * m.getValues()[4 * 3 + 1];
        v += getValues()[4 * 2 + 2] * m.getValues()[4 * 3 + 2];
        v += getValues()[4 * 3 + 2] * m.getValues()[4 * 3 + 3];
        n.getValues()[4 * 3 + 2] = v;
      }
      {
        float v = 0;
        v += getValues()[4 * 0 + 3] * m.getValues()[4 * 3 + 0];
        v += getValues()[4 * 1 + 3] * m.getValues()[4 * 3 + 1];
        v += getValues()[4 * 2 + 3] * m.getValues()[4 * 3 + 2];
        v += getValues()[4 * 3 + 3] * m.getValues()[4 * 3 + 3];
        n.getValues()[4 * 3 + 3] = v;
      }
    }
    return n;
  }

  @Override
  public Matrix multSlow(Matrix m) {
    MatrixImp n = new MatrixImp();
    for (int c = 0; c != 4; c++) {
      for (int r = 0; r != 4; r++) {
        float v = 0;
        for (int k = 0; k != 4; k++)
          v += get(k, r) * m.get(c, k);
        n.set(c, r, v);
      }
    }
    return n;
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#transformPoint(cg2.vecmath.Vector)
   */
  @Override
  public Vector transformPoint(Vector v) {
    final float x = get(0, 0) * v.x() + get(1, 0) * v.y() + get(2, 0) * v.z()
        + get(3, 0);
    final float y = get(0, 1) * v.x() + get(1, 1) * v.y() + get(2, 1) * v.z()
        + get(3, 1);
    final float z = get(0, 2) * v.x() + get(1, 2) * v.y() + get(2, 2) * v.z()
        + get(3, 2);
    return vecmath.vector(x, y, z);
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#transformDirection(cg2.vecmath.Vector)
   */
  @Override
  public Vector transformDirection(Vector v) {
    float x = get(0, 0) * v.x() + get(1, 0) * v.y() + get(2, 0) * v.z();
    float y = get(0, 1) * v.x() + get(1, 1) * v.y() + get(2, 1) * v.z();
    float z = get(0, 2) * v.x() + get(1, 2) * v.y() + get(2, 2) * v.z();
    return vecmath.vector(x, y, z); 
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#transformNormal(cg2.vecmath.Vector)
   */
  @Override
  public Vector transformNormal(Vector v) {
    return transformDirection(v);
  }

  @Override
  public Matrix transpose() {
    MatrixImp n = new MatrixImp();
    for (int c = 0; c != 4; c++) {
      for (int r = 0; r != 4; r++) {
        n.set(c, r, get(r, c));
      }
    }
    return n;
  }

  /**
   * Calculate the inverse matrix. The matrix is assumed to be orthonormal.
   * 
   * @return The inverse matrix.
   */
  @Override
  public Matrix invertRigid() {
    /*
     * this only works for rigid body transformations!
     */
    /*
     * calculate the inverse rotation by transposing the upper 3x3 submatrix.
     */
    MatrixImp ri = new MatrixImp();
    for (int c = 0; c != 3; c++)
      for (int r = 0; r != 3; r++)
        ri.set(c, r, get(r, c));
    /*
     * calculate the inverse translation
     */
    MatrixImp ti = new MatrixImp();
    ti.set(3, 0, -get(3, 0));
    ti.set(3, 1, -get(3, 1));
    ti.set(3, 2, -get(3, 2));
    return ri.mult(ti);
  }

  private Matrix makeIdent() {
    setValues(new float[16]);
    set(0, 0, 1.0f);
    set(1, 1, 1.0f);
    set(2, 2, 1.0f);
    set(3, 3, 1.0f);
    return this;
  }

  /**
   * Calculate the full inverse of this matrix. This takes some time.
   * 
   * @return The inverse matrix.
   */
  @Override
  public Matrix invertFull() {
    Matrix ret = new MatrixImp();
    float[] mat = getValues();
    float[] dst = ret.getValues();
    float[] tmp = new float[12];

    /* temparray for pairs */
    float src[] = new float[16];

    /* array of transpose source matrix */
    float det;

    /* determinant */
    /*
     * transpose matrix
     */
    for (int i = 0; i < 4; i++) {
      src[i] = mat[i * 4];
      src[i + 4] = mat[i * 4 + 1];
      src[i + 8] = mat[i * 4 + 2];
      src[i + 12] = mat[i * 4 + 3];
    }

    /* calculate pairs for first 8 elements (cofactors) */
    tmp[0] = src[10] * src[15];
    tmp[1] = src[11] * src[14];
    tmp[2] = src[9] * src[15];
    tmp[3] = src[11] * src[13];
    tmp[4] = src[9] * src[14];
    tmp[5] = src[10] * src[13];
    tmp[6] = src[8] * src[15];
    tmp[7] = src[11] * src[12];
    tmp[8] = src[8] * src[14];
    tmp[9] = src[10] * src[12];
    tmp[10] = src[8] * src[13];
    tmp[11] = src[9] * src[12];

    /* calculate first 8 elements (cofactors) */
    dst[0] = tmp[0] * src[5] + tmp[3] * src[6] + tmp[4] * src[7];
    dst[0] -= tmp[1] * src[5] + tmp[2] * src[6] + tmp[5] * src[7];
    dst[1] = tmp[1] * src[4] + tmp[6] * src[6] + tmp[9] * src[7];
    dst[1] -= tmp[0] * src[4] + tmp[7] * src[6] + tmp[8] * src[7];
    dst[2] = tmp[2] * src[4] + tmp[7] * src[5] + tmp[10] * src[7];
    dst[2] -= tmp[3] * src[4] + tmp[6] * src[5] + tmp[11] * src[7];
    dst[3] = tmp[5] * src[4] + tmp[8] * src[5] + tmp[11] * src[6];
    dst[3] -= tmp[4] * src[4] + tmp[9] * src[5] + tmp[10] * src[6];
    dst[4] = tmp[1] * src[1] + tmp[2] * src[2] + tmp[5] * src[3];
    dst[4] -= tmp[0] * src[1] + tmp[3] * src[2] + tmp[4] * src[3];
    dst[5] = tmp[0] * src[0] + tmp[7] * src[2] + tmp[8] * src[3];
    dst[5] -= tmp[1] * src[0] + tmp[6] * src[2] + tmp[9] * src[3];
    dst[6] = tmp[3] * src[0] + tmp[6] * src[1] + tmp[11] * src[3];
    dst[6] -= tmp[2] * src[0] + tmp[7] * src[1] + tmp[10] * src[3];
    dst[7] = tmp[4] * src[0] + tmp[9] * src[1] + tmp[10] * src[2];
    dst[7] -= tmp[5] * src[0] + tmp[8] * src[1] + tmp[11] * src[2];

    /* calculate pairs for second 8 elements (cofactors) */
    tmp[0] = src[2] * src[7];
    tmp[1] = src[3] * src[6];
    tmp[2] = src[1] * src[7];
    tmp[3] = src[3] * src[5];
    tmp[4] = src[1] * src[6];
    tmp[5] = src[2] * src[5];
    tmp[6] = src[0] * src[7];
    tmp[7] = src[3] * src[4];
    tmp[8] = src[0] * src[6];
    tmp[9] = src[2] * src[4];
    tmp[10] = src[0] * src[5];
    tmp[11] = src[1] * src[4];

    /* calculate second 8 elements (cofactors) */
    dst[8] = tmp[0] * src[13] + tmp[3] * src[14] + tmp[4] * src[15];
    dst[8] -= tmp[1] * src[13] + tmp[2] * src[14] + tmp[5] * src[15];
    dst[9] = tmp[1] * src[12] + tmp[6] * src[14] + tmp[9] * src[15];
    dst[9] -= tmp[0] * src[12] + tmp[7] * src[14] + tmp[8] * src[15];
    dst[10] = tmp[2] * src[12] + tmp[7] * src[13] + tmp[10] * src[15];
    dst[10] -= tmp[3] * src[12] + tmp[6] * src[13] + tmp[11] * src[15];
    dst[11] = tmp[5] * src[12] + tmp[8] * src[13] + tmp[11] * src[14];
    dst[11] -= tmp[4] * src[12] + tmp[9] * src[13] + tmp[10] * src[14];
    dst[12] = tmp[2] * src[10] + tmp[5] * src[11] + tmp[1] * src[9];
    dst[12] -= tmp[4] * src[11] + tmp[0] * src[9] + tmp[3] * src[10];
    dst[13] = tmp[8] * src[11] + tmp[0] * src[8] + tmp[7] * src[10];
    dst[13] -= tmp[6] * src[10] + tmp[9] * src[11] + tmp[1] * src[8];
    dst[14] = tmp[6] * src[9] + tmp[11] * src[11] + tmp[3] * src[8];
    dst[14] -= tmp[10] * src[11] + tmp[2] * src[8] + tmp[7] * src[9];
    dst[15] = tmp[10] * src[10] + tmp[4] * src[8] + tmp[9] * src[9];
    dst[15] -= tmp[8] * src[9] + tmp[11] * src[10] + tmp[5] * src[8];

    /* calculate determinant */
    det = src[0] * dst[0] + src[1] * dst[1] + src[2] * dst[2] + src[3] * dst[3];

    if (det == 0.0f) {
      throw new RuntimeException("singular matrix is not invertible");
    }

    /* calculate matrix inverse */
    det = 1 / det;

    for (int j = 0; j < 16; j++) {
      dst[j] *= det;
    }

    return ret;

  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#asArray()
   */
  @Override
  public float[] asArray() {
    return getValues().clone();
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#asBuffer()
   */
  @Override
  public FloatBuffer asBuffer() {
    FloatBuffer b = BufferUtils.createFloatBuffer(getValues().length);
    b.put(getValues());
    b.rewind();
    return b;
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#fillBuffer(java.nio.FloatBuffer)
   */
  @Override
  public void fillBuffer(FloatBuffer buf) {
    buf.rewind();
    buf.put(getValues()).rewind();
  }

  /**
   * Construct a new Matrix containing only the rotation.
   * 
   * @return The newly constructed matrix.
   */
  @Override
  public Matrix getRotation() {
    MatrixImp r = new MatrixImp();
    r.set(0, 0, get(0, 0));
    r.set(1, 0, get(1, 0));
    r.set(2, 0, get(2, 0));
    r.set(0, 1, get(0, 1));
    r.set(1, 1, get(1, 1));
    r.set(2, 1, get(2, 1));
    r.set(0, 2, get(0, 2));
    r.set(1, 2, get(1, 2));
    r.set(2, 2, get(2, 2));
    return r;
  }

  /**
   * Construct a new matrix containing only the translation.
   * 
   * @return The newly constructed matrix.
   */
  @Override
  public MatrixImp getTranslation() {
    MatrixImp t = new MatrixImp();
    t.set(3, 0, get(3, 0));
    t.set(3, 1, get(3, 1));
    t.set(3, 2, get(3, 2));
    return t;
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#getPosition()
   */
  @Override
  public Vector getPosition() {
    return vecmath.vector(get(3, 0), get(3, 1), get(3, 2));
  }

  /*
   * @see java.lang.Object#toString()
   */
  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#toString()
   */
  @Override
  public String toString() {
    String s = "";
    for (int r = 0; r < 4; r++) {
      s += "[ ";
      for (int c = 0; c < 4; c++) {
        s += get(c, r) + " ";
      }
      s += "]\n";
    }
    return s;
  }

  /* (non-Javadoc)
   * @see cg2.vecmath.Matrix#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object o) {
    if (!(o instanceof Matrix))
      return false;
    Matrix m = (Matrix) o;
    for (int i = 0; i != 16; i++)
      if (getValues()[i] != m.getValues()[i])
        return false;
    return true;
  }

  @Override
  public boolean equals(Matrix m, float epsilon) {
    for (int i = 0; i != 16; i++)
      if (Math.abs(getValues()[i] - m.getValues()[i]) > epsilon)
        return false;
    return true;
  }
}
