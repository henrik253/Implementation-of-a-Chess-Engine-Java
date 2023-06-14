package Player.AlphaZeroDotFive.MonteCarloTree;

import Player.AlphaZeroDotFive.AlphaZeroDotFiveAgent;
import Player.Player;
import java.util.ArrayList;
import Player.AlphaZeroDotFive.MonteCarloTree.Node;
public class MonteCarloTree {
    public AlphaZeroDotFiveAgent ai;
    public Player playerTwo;

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public Player getAi() {
        return ai;
    }

    public Player getTheOtherPlayer(Player player) {
        if(player == ai){
            return playerTwo;
        }else{
            return ai;
        }
    }

    public Node simulate(String board, byte toPlay, int numOfSimulations){
        Node root = new Node(board, this, null, playerTwo, 0.0f);
        Node currentNode;
        ArrayList<Node> searchPath = new ArrayList<>();
        Node[] rootChildren = root.expand();
        Node parent;
        String newBoard;
        float value;
        searchPath.add(root);

        for(int i = 0; i < 5; i++) {
            currentNode = root;
            while (currentNode.expanded) {
                currentNode = getChildrenWithBestUCB(currentNode);
                searchPath.add(currentNode);
            }

            parent = searchPath.get(searchPath.size() - 2);
            board = currentNode.getBoard();
            value = calculateValueForOtherPlayer(board);
            if(value == -10000.0f){
                currentNode.expand();
                currentNode.visitCount++;
                value = ai.valueNetwork.getValue(currentNode.getBoard());
            }
            backtrackToRoot(searchPath, value);
        }
        return root;
    }
    //TODO
    private void backtrackToRoot(ArrayList<Node> searchPath, float value) {

    }

    //TODO
    private float calculateValueForOtherPlayer(String board) {
        return 0.0f;
    }

    private Node getChildrenWithBestUCB(Node currentNode) {
        float bestUCB = 0.0f;
        int index = 0;
        float currentUCB;
        Node[] children = currentNode.getChildren();
        for(int i = 0; i < children.length; i++){
            currentUCB = calculateUCB(children[i]);
            if(currentUCB > bestUCB){
                bestUCB = currentUCB;
                index = i;
            }
        }
        return children[index];
    }
    private float calculateUCB(Node current) {
        float valueScore;
        float priorScore = current.prior * (float)(Math.sqrt(current.getParent().visitCount)) / (current.visitCount + 1);
        if(current.visitCount > 0){
            valueScore = -current.value;
        }else{
            valueScore = 0;
        }
        return valueScore + priorScore;
    }
}
