package Player.AlphaZeroDotFive.MonteCarloTree;

import Player.Player;

public class Node {
    private final MonteCarloTree tree;
    public float value;
    private Node parent;
    private String board;
    private Node[] children;
    private Player other;
    float prior;
    public int visitCount;
    public boolean expanded;
    Node(String board, MonteCarloTree tree, Node parent, Player other, float prior){
        this.board = board;
        this.tree = tree;
        this.parent = parent;
        this.other = other;
        this.prior = prior;
        this.visitCount = 0;
        value = 0; //TODO
        expanded = false;
        System.out.println("Node created!");
    }

    //
    public Node[] expand() {
        expanded = true;
        Move[] validMoves = other.getValidMoves();
        children = new Node[validMoves.length];
        for(int i = 0; i < children.length; i++){
            children[i] = new Node(
                    simulateBoard(validMoves[i]),
                    tree,
                    this,
                    tree.getTheOtherPlayer(other),
                    tree.ai.policyNetwork.getValue(i)
            );
        }
        return children;
    }
    //TODO
    private String simulateBoard(Move move) {
        return "";
    }

    public Node getParent(){
        return this.parent;
    }

    public Node[] getChildren() {
        return this.children;
    }

    public String getBoard() {
        return this.board;
    }
}
