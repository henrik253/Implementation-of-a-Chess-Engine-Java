package ai.MCTSAgent.Validation.BitboardValidation.Implementations;

import ai.MCTSAgent.Validation.Bitboards.Bitboard;
import ai.MCTSAgent.Validation.Bitboards.MovementBitBoardGenerator;
import ai.MCTSAgent.Validation.BitboardValidation.Validator;

import java.util.ArrayList;

public class RookValidator extends Validator {
    public RookValidator(MovementBitBoardGenerator generator) {
        super(generator);
    }

    @Override
    public ArrayList<int[]> getValidMoves(int start, Bitboard bitboard) {
        ArrayList<int[]> result = new ArrayList<>();
        int row = start/8;
        int col = start%8;
        addRayCastedArmToArrayList(bitboard, result, row, col, -1, 0, start);// upper arm
        addRayCastedArmToArrayList(bitboard, result, row, col, 1, 0, start);// lower arm
        addRayCastedArmToArrayList(bitboard, result, row, col, 0, -1, start);// left arm
        addRayCastedArmToArrayList(bitboard, result, row, col, 0, 1, start);// right arm
        return result;
    }


}
