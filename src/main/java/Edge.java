public abstract class Edge {

	private Node childNode;
	private Node parentNode;

	public Edge(Node parentNode, Node childNode) {
		this.parentNode = parentNode;
		this.childNode = childNode;
		this.parentNode.connect(this);
		this.childNode.connect(this);
	}

	public Node getChild() {
		return childNode;
	}

	public Node getParent() {
		return parentNode;
	}

}
