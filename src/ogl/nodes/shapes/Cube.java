package ogl.nodes.shapes;

import ogl.shader.Shader;
import ogl.vecmath.Color;
import ogl.vecmath.Vector;

import static ogl.nodes.shapes.Vertex.*;

public class Cube extends Shape{	
	public Cube(Shader shader){
		super(vertices, shader);
	}

	
	// Width, depth and height of the cube divided by 2.
	private static float w2 = 0.5f;
	private static float h2 = 0.5f;
	private static float d2 = 0.5f;


	//
	//     6 ------- 7 
	//   / |       / | 
	//  3 ------- 2  | 
	//  |  |      |  | 
	//  |  5 -----|- 4 
	//  | /       | / 
	//  0 ------- 1
	//

	// The positions of the cube vertices.
	private static Vector[] p = { 
			vec(-w2, -h2, -d2), 
			vec(w2, -h2, -d2),
			vec(w2, h2, -d2), 
			vec(-w2, h2, -d2), 
			vec(w2, -h2, d2), 
			vec(-w2, -h2, d2),
			vec(-w2, h2, d2), 
			vec(w2, h2, d2) 
	};

	// The colors of the cube vertices.
	private static Color[] c = { 
			col(0, 0, 0), 
			col(1, 0, 0), 
			col(1, 1, 0), 
			col(0, 1, 0),
			col(1, 0, 1), 
			col(0, 0, 1), 
			col(0, 1, 1), 
			col(1, 1, 1) 
	};

	// Vertices combine position and color information. Every four vertices define
	// one side of the cube.
	private static Vertex[] vertices = {
			// front
			v(p[0], c[0]), v(p[1], c[1]), v(p[2], c[2]), v(p[3], c[3]),
			// back
			v(p[4], c[4]), v(p[5], c[5]), v(p[6], c[6]), v(p[7], c[7]),
			// right
			v(p[1], c[1]), v(p[4], c[4]), v(p[7], c[7]), v(p[2], c[2]),
			// top
			v(p[3], c[3]), v(p[2], c[2]), v(p[7], c[7]), v(p[6], c[6]),
			// left
			v(p[5], c[5]), v(p[0], c[0]), v(p[3], c[3]), v(p[6], c[6]),
			// bottom
			v(p[5], c[5]), v(p[4], c[4]), v(p[1], c[1]), v(p[0], c[0]) 
	};
}
