public class GraphEdge {
    //instance variables of class
    private final GraphNode u;
    private final GraphNode v;
    private int type;
    private String label;
    //class constructor, The first two parameters are the endpoints of the edge. The last parameters are the type and
    //label of the edge. label could be corridor or door and type indicates the number of coins needed to open the door
    public GraphEdge(GraphNode u, GraphNode v, int type, String label){
        this.type = type;
        this.label = label;
        this.u = u;
        this.v = v;
    }
    //returns the first endpoint of the edge.
    public GraphNode firstEndpoint(){
        return this.u;
    }
    //returns the second endpoint of the edge.
    public GraphNode secondEndpoint(){
        return this.v;
    }
    //returns the type of the edge.
    public int getType(){
        return this.type;
    }
    //sets the type of the edge to the specified value.
    public void setType(int newType){
        this.type = newType;
    }
    //returns the label of the edge.
    public String getLabel(){
        return this.label;
    }
    //sets the label of the edge to the specified value.
    public void setLabel(String newLabel){
        this.label = newLabel;
    }


}
