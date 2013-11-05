/*******************************************************************************
 * Copyright (c) 2013 Henrik Tramberend, Marc Latoschik.
 * All rights reserved.
 *******************************************************************************/
package ogl.appmain;

import static ogl.vecmathimp.FactoryDefault.vecmath;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glViewport;
import ogl.app.App;
import ogl.app.Input;
import ogl.app.OpenGLApp;
import ogl.nodes.camera.Camera;
import ogl.nodes.shapes.Cube;
import ogl.shader.Shader;
import ogl.vecmath.Matrix;
import ogl.vecmathimp.FactoryDefault;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

//Select the factory we want to use.

// A simple but complete OpenGL 2.0 ES application.
public class RotatingCube implements App {

	private Shader shader;
	private Camera camera;
	private Cube cube;

	static public void main(String[] args) {
		new OpenGLApp("Rotating Cube - OpenGL ES 2.0 (lwjgl)", new RotatingCube())
		.start();
	}

	@Override
	public void init() {
		// Set background color to black.
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		// Enable depth testing.
		glEnable(GL11.GL_DEPTH_TEST);

		// Create shader and cube.
		shader = new Shader();
		cube = new Cube(shader);
		camera = new Camera();
		camera.setLocalTransform(FactoryDefault.vecmath.translationMatrix(0, 0, 3));
		camera.activate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cg2.cube.App#simulate(float, cg2.cube.Input)
	 */
	@Override
	public void simulate(float elapsed, Input input) {
		// Pressing key 'r' toggles the cube animation.
		if (input.isKeyToggled(Keyboard.KEY_R))
			// Increase the angle with a speed of 90 degrees per second.
			angle += 90 * elapsed;
		cube.setLocalTransform(vecmath.rotationMatrix(vecmath.vector(1, 1, 1), angle));
		
		cube.updateWorldTransform();
		camera.updateWorldTransform();		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see cg2.cube.App#display(int, int, javax.media.opengl.GL2ES2)
	 */
	@Override
	public void display(int width, int height) {
		// Adjust the the viewport to the actual window size. This makes the
		// rendered image fill the entire window.
		glViewport(0, 0, width, height);

		// Clear all buffers.
		glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

		shader.activate();

		// Assemble the transformation matrix that will be applied to all
		// vertices in the vertex shader.
		float aspect = (float) width / (float) height;

		// The perspective projection. Camera space to NDC.
		Matrix projectionMatrix = vecmath.perspectiveMatrix(60f, aspect, 0.1f, 100f);
		Shader.setProjectionMatrix(projectionMatrix);

		
		// display objects
		camera.activate();
		cube.display();
	}

	// Initialize the rotation angle of the cube.
	private float angle = 0;
}
