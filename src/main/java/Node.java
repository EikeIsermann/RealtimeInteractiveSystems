import java.util.UUID;
import java.util.Vector;

public abstract class Node {

	private UUID id = UUID.randomUUID();
	private Vector<Edge> edges = new Vector<Edge>();
	private Vector<Node> childNodes = new Vector<Node>();
	private Vector<Node> parentNodes = new Vector<Node>();

	public UUID getId() {
		return id;
	}

	public void connect(Edge edge) {
		edges.add(edge);
		childNodes.add(edge.getChild());
		parentNodes.add(edge.getParent());
	}

	public Edge[] getEdges() {
		return (Edge[]) edges.toArray();
	}

	public Node[] getChildren() {
		return (Node[]) childNodes.toArray();
	}

	public Node[] getParents() {
		return (Node[]) parentNodes.toArray();
	}

}
