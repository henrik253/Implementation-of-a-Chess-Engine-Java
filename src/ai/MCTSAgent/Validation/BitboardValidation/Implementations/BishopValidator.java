package ai.MCTSAgent.Validation.BitboardValidation.Implementations;

import ai.MCTSAgent.Validation.Bitboards.Bitboard;
import ai.MCTSAgent.Validation.Bitboards.MovementBitBoardGenerator;
import ai.MCTSAgent.Validation.BitboardValidation.Validator;

import java.util.ArrayList;

public class BishopValidator extends Validator {
    public BishopValidator(MovementBitBoardGenerator generator) {
        super(generator);
    }

    @Override
    public ArrayList<int[]> getValidMoves(int start, Bitboard bitboard) {
        ArrayList<int[]> result = new ArrayList<>();
        int row = start/8;
        int col = start%8;
        addRayCastedArmToArrayList(bitboard, result, row, col, -1, -1, start);// left upper arm
        addRayCastedArmToArrayList(bitboard, result, row, col, -1, 1, start);// right upper arm
        addRayCastedArmToArrayList(bitboard, result, row, col, 1, -1, start);// left lower arm
        addRayCastedArmToArrayList(bitboard, result, row, col, 1, 1, start);// right lower arm
        return result;
    }
}
