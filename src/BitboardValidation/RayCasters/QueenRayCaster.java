package BitboardValidation.RayCasters;

import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;
import BitboardValidation.Bitboards.Bitboard;

public class QueenRayCaster extends RayCaster{

    public QueenRayCaster(Bitboard bitBoard, MovementBitBoardGenerator generator) {
        super(bitBoard, generator);
    }

    @Override
    public ULong castSomeRays(int row, int col) {
        ULong result = shootVerticalRays(row, col);
        result.or(shootDiagonalRays(row, col));
        return result;
    }
}
