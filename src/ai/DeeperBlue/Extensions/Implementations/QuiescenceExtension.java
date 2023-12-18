package ai.DeeperBlue.Extensions.Implementations;
import ai.DeeperBlue.Evaluation.BoardEvaluator;
import ai.DeeperBlue.Extensions.Extension;
import ai.DeeperBlue.NormalSearchTree.Nodes.DeeperBlueExtensionNode;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;

public class QuiescenceExtension extends Extension {
    static final int QUIESCENCE_MAX_DEPTH = 2;
    public QuiescenceExtension(){
        this.id = QUIESCENCE_SEARCH;
    }
    @Override
    public void expand(DeeperBlueExtensionNode e) {
        e.value = quiescenceMax(e.intBoard, Float.MIN_VALUE, Float.MAX_VALUE, 0);
    }

    @Override
    public int interest(DeeperBlueExtensionNode e) {
        int numOfPiecesParent = numOfPieces(e.intBoard);
        ArrayList<int[]> validMoves = validation.getValidMoves(e.intBoard, -1);
        ArrayList<int[][]> childBoards = getChildBoards(e.intBoard, validMoves);
        for(int[][] childBoard : childBoards){
            if(numOfPieces(childBoard) != numOfPiecesParent){
                e.interesting = true;
                if(e.currentHighestInterestValue < 55){
                    e.currentHighestInterestValue = 55;
                    e.currentHighestExtensionId = QUIESCENCE_SEARCH;
                }
                return 55;
            }
        }
        return 0;
    }
    private int numOfPieces(int[][] intBoard) {
        int result = 0;
        for(int[] row : intBoard){
            for(int square : row){
                if(square != 0){
                    result++;
                }
            }
        }
        return result;
    }
    private float quiescenceMin(int[][] board, float alpha, float beta, int currentDepth){
        ArrayList<int[]> validMoves = validation.getValidMoves(board, -1);
        ArrayList<int[]> validCaptures = getCaptures(board, validMoves);
        if(validCaptures.isEmpty() || currentDepth == QUIESCENCE_MAX_DEPTH){
            return BoardEvaluator.evaluateSimplePlusBonus(board);
        }
        float value;
        float minValue = beta;
        Bitboard beforeMove = new Bitboard(board, validation.arr);
        for(int[] move : validCaptures){
            value = quiescenceMax(
                    ai.Util.Util.flipBoardHorizontallyAndFLipPlayer(
                            beforeMove.simulateMove(move).toIntBoard()
                    ),
                    alpha,
                    minValue,
                    currentDepth + 1
            );
            if(value < minValue){
                minValue = value;
                if(minValue <= alpha){
                    break;
                }
            }

        }
        return minValue;
    }
    public float quiescenceMax(int[][] board, float alpha, float beta, int currentDepth){
        ArrayList<int[]> validMoves = validation.getValidMoves(board, -1);
        ArrayList<int[]> validCaptures = getCaptures(board, validMoves);
        if(validCaptures.isEmpty() || currentDepth == QUIESCENCE_MAX_DEPTH){
            return BoardEvaluator.evaluateSimplePlusBonus(board);
        }
        float value;
        float maxValue = alpha;
        Bitboard beforeMove = new Bitboard(board, validation.arr);
        for(int[] move : validCaptures){
            value = quiescenceMin(
                    ai.Util.Util.flipBoardHorizontallyAndFLipPlayer(
                            beforeMove.simulateMove(move).toIntBoard()
                    ),
                    maxValue,
                    beta,
                    currentDepth + 1
            );
            if(value > maxValue){
                maxValue = value;
                if(maxValue >= beta){
                    break;
                }
            }

        }
        return maxValue;
    }

    private ArrayList<int[]> getCaptures(int[][] board, ArrayList<int[]> validMoves) {
        ArrayList<int[]> result = new ArrayList<>();
        ArrayList<int[][]> childBoards = getChildBoards(board, validMoves);
        int numOfPiecesParent = numOfPieces(board);
        for (int index = 0; index < childBoards.size(); index++) {
            if(numOfPiecesParent != numOfPieces(childBoards.get(index))){
                result.add(validMoves.get(index));
            }
        }
        return result;
    }
}
