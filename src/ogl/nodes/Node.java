package ogl.nodes;

import ogl.vecmath.Matrix;
import ogl.vecmathimp.FactoryDefault;

public abstract class Node {
	private Matrix modelMatrix, worldTrafo;	
	public Matrix getWorldTransform() {
		return worldTrafo;
	}

	protected Node(Matrix modelMatrix){
		setLocalTransform(modelMatrix);
		updateWorldTransform(FactoryDefault.vecmath.identityMatrix());
	}
	
	public Matrix getLocalTransform() {
		return modelMatrix;
	}

	public void setLocalTransform(Matrix modelMatrix) {
		this.modelMatrix = modelMatrix;
	}
		

	public void updateWorldTransform(Matrix previousTrafo){
		worldTrafo = previousTrafo.mult(getLocalTransform());
	}
	
	public void updateWorldTransform(){
		worldTrafo = getLocalTransform();
	}
	
	public abstract void display();
}
