package ai.MCTSAgent.Validation.BitboardValidation;

import ai.MCTSAgent.Validation.Bitboards.BitMaskArr;
import ai.MCTSAgent.Validation.Bitboards.Bitboard;
import ai.MCTSAgent.Validation.Bitboards.MovementBitBoardGenerator;
import ai.MCTSAgent.Validation.BitboardValidation.Implementations.*;

import java.util.ArrayList;

public class BitboardMoveValidation {
    MovementBitBoardGenerator generator;
    boolean checkmateWhite;
    boolean checkMateBlack;
    public BitMaskArr arr;
    Validator[] validators;
    int kingSafetyRecursionDepth;

    public BitboardMoveValidation(BitMaskArr arr, int kingSafetyRecursionDepth) {
        this.generator = new MovementBitBoardGenerator();
        this.checkmateWhite = false;
        this.checkMateBlack = false;
        this.arr = arr;
        this.validators = new Validator[]{
                new KingValidator(generator),
                new RookValidator(generator),
                new BishopValidator(generator),
                new QueenValidator(generator),
                new KnightValidator(generator),
                new PawnValidator(generator),
        };
        this.kingSafetyRecursionDepth = kingSafetyRecursionDepth;
    }
    public boolean checkmate(int[][] internalBoard, int player) {//gets unflipped board
        return getValidMoves(internalBoard,player).size() == 0;
    }
    public ArrayList<int[]> getValidMoves(int[][] board, int player) {//gets unflipped board
        ArrayList<int[]> validMovesWithoutKingSafety;// holds valid moves without king safety
        ArrayList<int[]> result = new ArrayList<>();// holds valid moves with king safety
        //generate bitboard
        Bitboard bitboard = new Bitboard(board, arr);
        if(player == -1){
            bitboard.flipPlayer();
        }
        //generate validMoves without king safety
        validMovesWithoutKingSafety = getValidMovesWithoutKingSafety(bitboard);

        for(int[] move : validMovesWithoutKingSafety){
            if(kingIsSafeAfterMove(bitboard, move, kingSafetyRecursionDepth) && move[0] != move[1]){
                result.add(move);
            }
        }
        return result;
    }
    public ArrayList<int[]> getValidMoves(Bitboard bitboard, int recursionDepth) {//gets unflipped board
        ArrayList<int[]> validMovesWithoutKingSafety;// holds valid moves without king safety
        ArrayList<int[]> result = new ArrayList<>();// holds valid moves with king safety

        //generate validMoves without king safety
        validMovesWithoutKingSafety = getValidMovesWithoutKingSafety(bitboard);

        //check for all moves if the king is safe after the move
        for(int[] move : validMovesWithoutKingSafety){
            if(kingIsSafeAfterMove(bitboard, move, recursionDepth - 1)){
                result.add(move);
            }
        }
        return result;
    }

    //-------------------------------------helper methods-------------------------------------
    private ArrayList<int[]> getValidMovesWithoutKingSafety(Bitboard bitboard) {
        ArrayList<int[]> result = new ArrayList<>();
        int currentPieceType;
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                currentPieceType = bitboard.getPieceType(row, col);
                if(currentPieceType != -1 && bitboard.playerPieces.combinedPieces.at(row, col)){
                    result.addAll(validators[currentPieceType].getValidMoves(row * 8 + col, bitboard));
                }
            }
        }
        return result;
    }
    private boolean kingIsSafeAfterMove(Bitboard bitboard, int[] move, int recursionDepth) {
        if(recursionDepth > 0){// stops endless king safety recursion
            Bitboard afterMove = bitboard.simulateMove(move);
            int[] kingPosition = afterMove.getKingPosition();
            //flip the board and calculate validMoves
            afterMove.flipPlayer();
            ArrayList<int[]> validMoves = getValidMoves(afterMove, recursionDepth - 1);
            //check if there is a move which ends on the king position
            for(int[] validMove : validMoves){
                if(validMove[1] == kingPosition[0] * 8 + kingPosition[1]){
                    return false;
                }
            }
        }
        return true;
    }
}
