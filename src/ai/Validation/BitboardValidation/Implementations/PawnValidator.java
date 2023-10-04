package ai.Validation.BitboardValidation.Implementations;

import ai.Validation.BitboardValidation.Validator;
import ai.Validation.Bitboards.Bitboard;
import ai.Validation.Bitboards.MovementBitBoardGenerator;

import java.util.ArrayList;

public class PawnValidator extends Validator {
    public PawnValidator(MovementBitBoardGenerator generator) {
        super(generator);
    }

    @Override
    public ArrayList<int[]> getValidMoves(int start, Bitboard bitboard) {
        if(bitboard.player == 1){
            return getValidMovesForUpFacingPawn(start, bitboard);
        }else{
            return getValidMovesForDownFacingPawn(start, bitboard);
        }
    }

    private ArrayList<int[]> getValidMovesForUpFacingPawn(int start, Bitboard bitboard) {
        ArrayList<int[]> result = new ArrayList<>();
        int startRow = start/8;
        int startCol = start%8;

        //if it is in the initial row and there is no piece in the way, add the double move
        if(startRow == 6 && !bitboard.enemyPieces.combinedPieces.at(startRow - 2, startCol) && !bitboard.playerPieces.combinedPieces.at(startRow - 2, startCol)){
            result.add(new int[]{start, (startRow - 2) * 8 + startCol});
        }
        if(startRow != 0){
            //add diagonal destinations if there is an enemy or en enPassantSquare
            if(//left
                     startCol - 1 >= 0 &&
                    (bitboard.enemyPieces.combinedPieces.at(startRow - 1, startCol - 1) ||
                     bitboard.enemyEnPassantSquares.at(startRow - 1, startCol - 1)))
            {
                result.add(new int[]{start, (startRow - 1) * 8 + startCol - 1});
            }

            if(//right
                    startCol + 1 < 8 &&
                   (bitboard.enemyPieces.combinedPieces.at(startRow - 1, startCol + 1) ||
                    bitboard.enemyEnPassantSquares.at(startRow - 1, startCol + 1)))
            {
                result.add(new int[]{start, (startRow - 1) * 8 + startCol + 1});
            }
            //add 1 Square move if there is no piece in the way
            if(
                    !bitboard.enemyPieces.combinedPieces.at(startRow - 1, startCol) &&
                    !bitboard.playerPieces.combinedPieces.at(startRow - 1, startCol))
            {
                result.add(new int[]{start, (startRow - 1) * 8 + startCol});
            }
        }
        return result;
    }

    private ArrayList<int[]> getValidMovesForDownFacingPawn(int start, Bitboard bitboard) {
        ArrayList<int[]> result = new ArrayList<>();
        int startRow = start/8;
        int startCol = start%8;

        //if it is in the initial row and there is no piece in the way, add the double move
        if(startRow == 1 && !bitboard.enemyPieces.combinedPieces.at(startRow + 2, startCol)&& !bitboard.playerPieces.combinedPieces.at(startRow + 2, startCol)){
            result.add(new int[]{start, (startRow + 2) * 8 + startCol});
        }
        if(startRow != 7){
            //add diagonal destinations if there is an enemy or en enPassantSquare
            if(//left
                    startCol - 1 >= 0 &&
                   (bitboard.enemyPieces.combinedPieces.at(startRow + 1, startCol - 1) ||
                    bitboard.enemyEnPassantSquares.at(startRow + 1, startCol - 1)))
            {
                result.add(new int[]{start, (startRow + 1) * 8 + startCol - 1});
            }

            if(//right
                    startCol + 1 < 8 &&
                   (bitboard.enemyPieces.combinedPieces.at(startRow + 1, startCol + 1) ||
                    bitboard.enemyEnPassantSquares.at(startRow + 1, startCol + 1)))
            {
                result.add(new int[]{start, (startRow + 1) * 8 + startCol + 1});
            }
            //add 1 Square move if there is no piece in the way
            if(
                    !bitboard.enemyPieces.combinedPieces.at(startRow + 1, startCol) &&
                    !bitboard.playerPieces.combinedPieces.at(startRow + 1, startCol))
            {
                result.add(new int[]{start, (startRow + 1) * 8 + startCol});
            }
        }
        return result;
    }
}
