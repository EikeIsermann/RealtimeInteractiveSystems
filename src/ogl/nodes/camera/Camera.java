package ogl.nodes.camera;

import ogl.nodes.Node;
import ogl.shader.Shader;
import ogl.vecmathimp.FactoryDefault;

public class Camera extends Node {
	public Camera(){
		super(FactoryDefault.vecmath.identityMatrix());
	}
	
	@Override
	public void display() {
	}
	
	public void activate(){
		Shader.setViewMatrix(getWorldTransform().invertFull());
	}
	
}
