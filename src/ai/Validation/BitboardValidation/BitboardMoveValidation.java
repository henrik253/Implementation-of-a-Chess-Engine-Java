package ai.Validation.BitboardValidation;

import ai.Validation.BitboardValidation.Implementations.*;
import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;
import ai.Validation.Bitboards.MovementBitBoardGenerator;

import java.util.ArrayList;

public class BitboardMoveValidation {
    final MovementBitBoardGenerator generator;
    final boolean checkmateWhite;
    final boolean checkMateBlack;
    public final BitMaskArr arr;
    final Validator[] validators;
    final int kingSafetyRecursionDepth;

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
    public ArrayList<int[]> getValidMovesForPiece(int[][] board, int player, int pieceSquare) {//gets unflipped board
        ArrayList<int[]> validMovesWithoutKingSafety;// holds valid moves without king safety
        ArrayList<int[]> result = new ArrayList<>();// holds valid moves with king safety
        //generate bitboard
        Bitboard bitboard = new Bitboard(board, arr);
        if(player == -1){
            bitboard.flipPlayer();
        }
        //generate validMoves without king safety
        validMovesWithoutKingSafety = getValidMovesWithoutKingSafetyForPiece(bitboard, pieceSquare);

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
    public ArrayList<int[]> getValidMovesForPiece(Bitboard bitboard, int recursionDepth, int pieceSquare) {//gets unflipped board
        ArrayList<int[]> validMovesWithoutKingSafety;// holds valid moves without king safety
        ArrayList<int[]> result = new ArrayList<>();// holds valid moves with king safety

        //generate validMoves without king safety
        validMovesWithoutKingSafety = getValidMovesWithoutKingSafetyForPiece(bitboard, pieceSquare);

        //check for all moves if the king is safe after the move
        for(int[] move : validMovesWithoutKingSafety){
            if(kingIsSafeAfterMove(bitboard, move, recursionDepth - 1)){
                result.add(move);
            }
        }
        return result;
    }

    private ArrayList<int[]> getValidMovesWithoutKingSafetyForPiece(Bitboard bitboard, int pieceSquare) {
        return validators[bitboard.getPieceType(pieceSquare/8, pieceSquare%8)].getValidMoves(pieceSquare, bitboard);
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
