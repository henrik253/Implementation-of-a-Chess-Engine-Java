package ai.Validation.BitboardValidation.Implementations;

import ai.Validation.BitboardValidation.Validator;
import ai.Validation.Bitboards.Bitboard;
import ai.Validation.Bitboards.MovementBitBoardGenerator;

import java.util.ArrayList;

public class QueenValidator extends Validator {
    public QueenValidator(MovementBitBoardGenerator generator) {
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
        addRayCastedArmToArrayList(bitboard, result, row, col, -1, -1, start);// left upper arm
        addRayCastedArmToArrayList(bitboard, result, row, col, -1, 1, start);// right upper arm
        addRayCastedArmToArrayList(bitboard, result, row, col, 1, -1, start);// left lower arm
        addRayCastedArmToArrayList(bitboard, result, row, col, 1, 1, start);// right lower arm
        return result;
    }
}
