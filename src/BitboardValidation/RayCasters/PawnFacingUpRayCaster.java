package BitboardValidation.RayCasters;

import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;
import BitboardValidation.Bitboards.Bitboard;

public class PawnFacingUpRayCaster extends RayCaster{
    public PawnFacingUpRayCaster(Bitboard bitBoard, MovementBitBoardGenerator generator) {
        super(bitBoard, generator);
    }

    @Override
    public ULong castSomeRays(int row, int col) {
        return andPositions(row, col, this.generator.pawnUpBoards);
    }
}
