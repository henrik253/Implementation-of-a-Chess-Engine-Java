package ai.Validation.BitboardValidation.Implementations;

import ai.Validation.BitboardValidation.Validator;
import ai.Validation.Bitboards.Bitboard;
import ai.Validation.Bitboards.MovementBitBoardGenerator;

import java.util.ArrayList;

public class KingValidator extends Validator {
    public KingValidator(MovementBitBoardGenerator generator) {
        super(generator);
    }

    @Override
    public ArrayList<int[]> getValidMoves(int start, Bitboard bitboard) {
        ArrayList<int[]> result = new ArrayList<>();
        int rowStart = start/8;
        int colStart = start%8;
        // for all squares in moore neighborhood
        for(int rowOffset = -1; rowOffset < 2; rowOffset++){
            for (int colOffset = -1; colOffset < 2; colOffset++) {
                // if there is no player piece at the square with offsets
                // and the square with offsets is in bounds,
                // add it as a destination
                if(
                        rowStart + rowOffset >= 0 &&
                        rowStart + rowOffset < 8 &&
                        colStart + colOffset >= 0 &&
                        colStart + colOffset < 8 &&
                        !bitboard.playerPieces.combinedPieces.at(rowStart + rowOffset, colStart + colOffset)
                ){
                    result.add(new int[]{start, (rowStart + rowOffset) * 8 + colStart + colOffset});
                }
            }
        }
        //castling
        /*if(!bitboard.playerKingMoved){
            if(!bitboard.rookKingSideMovedPlayer &&
                    !bitboard.playerPieces.combinedPieces.at(rowStart, colStart + 1) &&
                    !bitboard.playerPieces.combinedPieces.at(rowStart, colStart + 2) &&
                    colStart + 2 < 8){
                result.add(new int[]{start, (rowStart) * 8 + colStart + 2});
            }
            if(!bitboard.rookQueenSideMovedPlayer &&
                    !bitboard.playerPieces.combinedPieces.at(rowStart, colStart - 1) &&
                    !bitboard.playerPieces.combinedPieces.at(rowStart, colStart - 2) &&
                    !bitboard.playerPieces.combinedPieces.at(rowStart, colStart - 3)&&
                    colStart - 3 > 0){
                result.add(new int[]{start, (rowStart) * 8 + colStart - 3});
            }
        }*/
        return result;
    }
}
