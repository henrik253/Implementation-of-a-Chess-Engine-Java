package BitboardValidation.RayCasters;

import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;
import BitboardValidation.Bitboards.Bitboard;

public class BishopRayCaster extends RayCaster{
    public BishopRayCaster(Bitboard bitBoard, MovementBitBoardGenerator generator) {
        super(bitBoard, generator);
    }

    @Override
    public ULong castSomeRays(int row, int col) {
        return shootDiagonalRays(row, col);
    }




}
