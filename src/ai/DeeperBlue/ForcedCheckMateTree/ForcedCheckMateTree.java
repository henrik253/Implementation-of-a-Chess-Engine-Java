package ai.DeeperBlue.ForcedCheckMateTree;
import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;

import java.util.ArrayList;

public class ForcedCheckMateTree {
    ArrayList<Boolean> moveValues;
    ArrayList<int[]> moveSquares;
    final BitboardMoveValidation validation;
    boolean checkMateNow;
    int[] mateInOne;
    public ForcedCheckMateTree(){
        this.validation = new BitboardMoveValidation(new BitMaskArr(), 1);
        checkMateNow = false;
    }
    public void search(int[][] board) {
        moveSquares = this.validation.getValidMoves(board, -1);
        if(searchForCheckMates(board)[0] != -1){
            checkMateNow = true;
            this.mateInOne = searchForCheckMates(board);
        }
        this.moveValues = new ArrayList<>();
        Bitboard beforeMove = new Bitboard(board, this.validation.arr);
        for(int[] intMove : moveSquares){
            beforeMove.flipPlayer();
            moveValues.add(
                min(
                        ai.Util.Util.flipBoardHorizontallyAndFLipPlayer(beforeMove.simulateMove(intMove).toIntBoard())
                )
            );
            beforeMove.flipPlayer();
        }
    }

    private int[] searchForCheckMates(int[][] board) {
        Bitboard beforeMove = new Bitboard(board, this.validation.arr);
        beforeMove.flipPlayer();
        for(int[] intMove : moveSquares){
            if(
                    this.validation.getValidMoves(
                                    beforeMove.simulateMove(intMove).toIntBoard(),
                                    1)
                            .isEmpty())
            {
                return intMove;
            }
        }
        return new int[]{-1, -1};
    }

    public int[] getMove() {
        if(this.checkMateNow){
            return this.mateInOne;
        }
        for (int index = 0; index < moveValues.size(); index++) {
            if(moveValues.get(index)){
                return moveSquares.get(index);
            }
        }
        return new int[]{-1, -1};
    }
    private boolean min(int[][] board){//returns false if a move is found where the player cant checkmate
        ArrayList<int[]> validMoves = this.validation.getValidMoves(board, -1);
        Bitboard beforeMove = new Bitboard(board, this.validation.arr);
        beforeMove.flipPlayer();
        for(int[] intMove : validMoves){
            if(
                    !max( ai.Util.Util.flipBoardHorizontallyAndFLipPlayer(beforeMove.simulateMove(intMove).toIntBoard()))
            ){
                return false;
            }
        }
        beforeMove.flipPlayer();
        return true;
    }
    private boolean max(int[][] board){// returns true if a checkmating move is found
        ArrayList<int[]> validMoves = this.validation.getValidMoves(board, -1);
        Bitboard beforeMove = new Bitboard(board, this.validation.arr);
        beforeMove.flipPlayer();
        for(int[] intMove : validMoves){
            if(
                    this.validation.getValidMoves(
                                    beforeMove.simulateMove(intMove).toIntBoard(),
                                    1)
                            .isEmpty())
            {
                return true;
            }
        }
        beforeMove.flipPlayer();
        return false;
    }
    public boolean foundForcedCheckMate() {
        for(boolean i : moveValues){
            if(i){
                return true;
            }
        }
        return false;
    }
}
