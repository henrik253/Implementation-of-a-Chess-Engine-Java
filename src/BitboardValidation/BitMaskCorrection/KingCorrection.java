package BitboardValidation.BitMaskCorrection;

import BitboardValidation.Bitboards.Bitboard;
import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;
import BitboardValidation.RayCasters.*;

public class KingCorrection extends BitMaskCorrection{
    public KingCorrection(Bitboard board, MovementBitBoardGenerator generator) {
        super(board, generator);
    }

    @Override
    public ULong correct(ULong inputMask, int row, int col) {
        String[] types = new String[]{"King", "Rook", "Bishop", "Queen", "Knight", "Pawn"};
        ULong result = new ULong(inputMask);
        RayCaster[] localRayCasters;
        BitMaskCorrection[] localCorrections;
        Bitboard flippedBoard = new Bitboard(this.board);
        flippedBoard.flipPlayer();
        if(board.player == 1){
            localRayCasters = new RayCaster[]{
                    new KingRayCaster(flippedBoard, this.generator),
                    new RookRayCaster(flippedBoard, this.generator),
                    new BishopRayCaster(flippedBoard, this.generator),
                    new QueenRayCaster(flippedBoard, this.generator),
                    new KnightRayCaster(flippedBoard, this.generator),
                    new PawnFacingDownRayCaster(flippedBoard, this.generator),
            };
            localCorrections = new BitMaskCorrection[]{
                    new KingCorrection(flippedBoard, this.generator),
                    new RookCorrection(flippedBoard, this.generator),
                    new BishopCorrection(flippedBoard, this.generator),
                    new QueenCorrection(flippedBoard, this.generator),
                    new KnightCorrection(flippedBoard, this.generator),
                    new PawnDownCorrection(flippedBoard, this.generator),
            };
        }else{
            localRayCasters = new RayCaster[]{
                    new KingRayCaster(flippedBoard, this.generator),
                    new RookRayCaster(flippedBoard, this.generator),
                    new BishopRayCaster(flippedBoard, this.generator),
                    new QueenRayCaster(flippedBoard, this.generator),
                    new KnightRayCaster(flippedBoard, this.generator),
                    new PawnFacingUpRayCaster(flippedBoard, this.generator),
            };
            localCorrections = new BitMaskCorrection[]{
                    new KingCorrection(flippedBoard, this.generator),
                    new RookCorrection(flippedBoard, this.generator),
                    new BishopCorrection(flippedBoard, this.generator),
                    new QueenCorrection(flippedBoard, this.generator),
                    new KnightCorrection(flippedBoard, this.generator),
                    new PawnUpCorrection(flippedBoard, this.generator),
            };

        }
        ULong orResult = new ULong(this.generator.bitMaskArr);
        for(int roww = 0; roww < 8; roww++){
            for(int coll = 0; coll < 8; coll++){
                int pieceType = flippedBoard.playerPieceAt(roww, coll);
                if(pieceType != -1 && pieceType != 0){
                        orResult.or(localCorrections[pieceType].correct(localRayCasters[pieceType].castSomeRays(roww, coll), roww, coll));
                }
            }
        }
        orResult.not();
        result.and(orResult);
        return result;
    }
}
