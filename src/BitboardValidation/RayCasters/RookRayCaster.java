package BitboardValidation.RayCasters;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;

public class RookRayCaster extends  RayCaster{
    public RookRayCaster(Bitboard bitBoard, MovementBitBoardGenerator generator) {
        super(bitBoard, generator);
    }

    @Override
    public ULong castSomeRays(int row, int col) {
        return shootVerticalRays(row, col);
    }




}
