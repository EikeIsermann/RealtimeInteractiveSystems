package ogl.nodes.shapes;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

import java.nio.FloatBuffer;

import ogl.nodes.shapes.Vertex;
import ogl.shader.Shader;
import ogl.nodes.Node;
import ogl.vecmathimp.FactoryDefault;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public abstract class Shape extends Node{
	protected Vertex[] vertices;
	protected FloatBuffer positionData;
	protected FloatBuffer colorData;
	protected Shader shader;
		
	public Shape(Vertex[] vertices, Shader shader){
		super(FactoryDefault.vecmath.identityMatrix());
		this.vertices = vertices;
		this.shader = shader;		
		// Prepare the vertex data arrays.
		// Compile vertex data into a Java Buffer data structures that can be
		// passed to the OpenGL API efficently.
		positionData = BufferUtils.createFloatBuffer(vertices.length
				* vecmath.vectorSize());
		colorData = BufferUtils.createFloatBuffer(vertices.length
				* vecmath.colorSize());

		for (Vertex v : vertices) {
			positionData.put(v.position.asArray());
			colorData.put(v.color.asArray());
		}
		positionData.rewind();
		colorData.rewind();
	}	
	
	public void display(){
		shader.activate();
		
		shader.setModelMatrix(getWorldTransform());
		// Enable the vertex data arrays (with indices 0 and 1). We use a vertex
		// position and a vertex color.
		glVertexAttribPointer(Shader.vertexAttribIdx, 3, false, 0, positionData);
		glEnableVertexAttribArray(Shader.vertexAttribIdx);
		glVertexAttribPointer(Shader.colorAttribIdx, 3, false, 0, colorData);
		glEnableVertexAttribArray(Shader.colorAttribIdx);

		// Draw the triangles that form the cube from the vertex data arrays.
		glDrawArrays(GL11.GL_QUADS, 0, vertices.length);
	}
}
