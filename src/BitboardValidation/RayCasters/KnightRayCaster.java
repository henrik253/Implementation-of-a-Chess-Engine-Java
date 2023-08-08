package BitboardValidation.RayCasters;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;

public class KnightRayCaster extends RayCaster{

    public KnightRayCaster(Bitboard bitBoard, MovementBitBoardGenerator generator){
        super(bitBoard, generator);

    }
    @Override
    public ULong castSomeRays(int row, int col) {
        return andPositions(row, col, this.generator.knightBoards);
    }
}