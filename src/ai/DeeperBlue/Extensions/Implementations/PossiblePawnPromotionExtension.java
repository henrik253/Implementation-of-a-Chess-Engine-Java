package ai.DeeperBlue.Extensions.Implementations;

import ai.DeeperBlue.DeeperBlueException;
import ai.DeeperBlue.Extensions.Extension;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueExtensionNode;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueNode;

import java.util.ArrayList;

public class PossiblePawnPromotionExtension extends Extension {
    private static final int DISTANCE_THRESHOLD = 4;

    @Override
    public void expand(DeeperBlueExtensionNode leafNode) throws DeeperBlueException {
        this.id = POSSIBLE_PAWN_PROMOTION;
        fillChildrenWithMaxNodes(leafNode);
        expandChildrensPawnMoves(leafNode);
    }

    private void expandChildrensPawnMoves(DeeperBlueExtensionNode leafNode) throws DeeperBlueException {
        ArrayList<int[]> currentValidMoves;
        for(DeeperBlueNode child : leafNode.children){
            currentValidMoves = validation.getValidMoves(child.intBoard, 1);
            for(int[] move : currentValidMoves){
                if(potentialToPromotePawn(move, child.intBoard)){
                    child.expand();
                }
            }
        }
    }

    private boolean potentialToPromotePawn(int[] move, int[][] intBoard) {
        //return if it is a passedPawn, but first check if it even is a pawn
        return intBoard[move[0]/8][move[0]%8] == 6 && passedPawn(intBoard, move);
    }

    private boolean passedPawn(int[][] intBoard, int[] move) {
        int piecesInWay;
        int row = move[0]/8;
        int col = move[0]%8;
        int currentCol;
        int currentRow;
        for(int colOff = -1; colOff < 2; colOff++){
            currentCol = col + colOff;
            currentRow = row;
            if(currentCol < 8 && currentCol >= 0){//bound checking
                while(currentRow >= 0){
                    //if there is a friendly pawn or an enemy piece in the way it isnt a passed pawn
                    if(intBoard[currentRow][currentCol] < 0 || (colOff == 0 && intBoard[currentRow][currentCol] == 6)){
                        return false;
                    }
                    currentRow--;
                }
            }
        }
        return true;
    }

    // returns interest based on the distance between the closest pawn,
    // and the other end of the board.
    // While reading keep in mind that the board is still flipped!
    @Override
    public int interest(DeeperBlueExtensionNode leafNode) {
        int bestDistance = DISTANCE_THRESHOLD + 1;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if(leafNode.intBoard[row][col] == -6){
                    if(row <= DISTANCE_THRESHOLD){
                        bestDistance = Math.min(bestDistance, row);
                    }
                }
            }
        }
        int result = 100 - (bestDistance * 10);

        if(bestDistance < DISTANCE_THRESHOLD + 1){
            if(result > leafNode.currentHighestInterestValue){
                leafNode.currentHighestExtensionId = POSSIBLE_PAWN_PROMOTION;
                leafNode.currentHighestInterestValue = result;
                leafNode.interesting = true;
            }
            return result;
        }
        return 0;
    }
}
