package ai.Validation.BitboardValidation.Implementations;

import ai.Validation.BitboardValidation.Validator;
import ai.Validation.Bitboards.Bitboard;
import ai.Validation.Bitboards.MovementBitBoardGenerator;

import java.util.ArrayList;

public class KnightValidator extends Validator {
    final int[][] offsets;
    public KnightValidator(MovementBitBoardGenerator generator){
        super(generator);
        this.offsets = new int[][]{
                {-1, -2},
                {-1, 2},
                {1, -2},
                {1, 2},
                {-2, -1},
                {-2, 1},
                {2, -1},
                {2, 1}
        };
    }
    @Override
    public ArrayList<int[]> getValidMoves(int start, Bitboard board) {
        ArrayList<int[]> result = new ArrayList<>();
        int startRow = start/8;
        int startCol = start%8;
        for(int[] offset: this.offsets){
            if(
                    startRow + offset[0] >= 0 &&
                    startRow + offset[0] < 8 &&
                    startCol + offset[1] >= 0 &&
                    startCol + offset[1] < 8 &&
                    !board.playerPieces.combinedPieces.at(startRow + offset[0], startCol + offset[1])
            ){
                result.add(
                        new int[]{start,
                                (startRow+offset[0]) * 8 +
                                        startCol+offset[1]});
            }
        }
        return result;
    }
}
