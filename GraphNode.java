public class GraphNode {
    //instance variables of class
    private final int name;
    private boolean marked;
    //class constructor creates a node with the given name and mark default as false
    public GraphNode(int name){
        this.name = name;
        this.marked = false;
    }
    //marks the node with the specified value
    public void mark(boolean mark){
        this.marked = mark;
    }
    //returns the value with which the node has been marked.
    public boolean isMarked(){
        return this.marked;
    }
    //returns the name of the node.
    public int getName(){
        return this.name;
    }

}
