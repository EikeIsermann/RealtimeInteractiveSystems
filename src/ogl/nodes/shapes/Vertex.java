package ogl.nodes.shapes;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import ogl.vecmath.Color;
import ogl.vecmath.Vector;

public class Vertex {
	Vector position;
	Color color;

	Vertex(Vector p, Color c) {
		position = p;
		color = c;
	}
	
	// Make construction of vertices easy on the eyes.
	public static Vertex v(Vector p, Color c) {
		return new Vertex(p, c);
	}

	// Make construction of vectors easy on the eyes.
	public static Vector vec(float x, float y, float z) {
		return vecmath.vector(x, y, z);
	}

	// Make construction of colors easy on the eyes.
	public static Color col(float r, float g, float b) {
		return vecmath.color(r, g, b);
	}
}
