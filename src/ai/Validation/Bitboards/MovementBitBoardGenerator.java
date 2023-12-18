package ai.Validation.Bitboards;

import ai.Validation.Boolboards.*;


public class MovementBitBoardGenerator {
    final BoolBoardCalculator[] calculators;
    public final BitMaskArr bitMaskArr;
    public ULong[] rookBoards;
    public ULong[] knightBoards;
    public ULong[] pawnUpBoards;
    public ULong[] pawnDownBoards;
    public ULong[] kingBoards;
    public ULong[] bishopBoards;
    public ULong[] queenBoards;

    public MovementBitBoardGenerator(){
        this.bitMaskArr = new BitMaskArr();
        this.calculators = new BoolBoardCalculator[]{
                new KingBoardCalculator(),
                new RookBoardCalculator(),
                new BishopBoardCalculator(),
                new QueenBoardCalculator(),
                new KnightBoardCalculator(),
                new PawnFacingUpBoardCalculator(),
                new PawnFacingDownBoardCalculator()
        };
        for(BoolBoardCalculator calc : this.calculators){
            calc.calcAllBoards();
        }
        calculateMovementBoards();
    }
    public void calculateMovementBoards(){
        this.bishopBoards = new ULong[64];
        this.kingBoards = new ULong[64];
        this.knightBoards = new ULong[64];
        this.pawnUpBoards = new ULong[64];
        this.pawnDownBoards = new ULong[64];
        this.queenBoards = new ULong[64];
        this.rookBoards = new ULong[64];
        for(int i = 0; i < this.bishopBoards.length; i++){
            this.bishopBoards[i] = new ULong(boolArrToLong(this.calculators[2].allBoards1Dim[i]), this.bitMaskArr);
            this.kingBoards[i] = new ULong(boolArrToLong(this.calculators[0].allBoards1Dim[i]), this.bitMaskArr);
            this.rookBoards[i] = new ULong(boolArrToLong(this.calculators[1].allBoards1Dim[i]), this.bitMaskArr);
            this.pawnDownBoards[i] = new ULong(boolArrToLong(this.calculators[6].allBoards1Dim[i]), this.bitMaskArr);
            this.pawnUpBoards[i] = new ULong(boolArrToLong(this.calculators[5].allBoards1Dim[i]), this.bitMaskArr);
            this.queenBoards[i] = new ULong(boolArrToLong(this.calculators[3].allBoards1Dim[i]), this.bitMaskArr);
            this.knightBoards[i] = new ULong(boolArrToLong(this.calculators[4].allBoards1Dim[i]), this.bitMaskArr);
        }
    }

    private long boolArrToLong(boolean[] board) {
        char[] tempResult = new char[64];
        for(int i = 0; i < tempResult.length; i++){
            if(board[i]){
                tempResult[i] = '1';
            }else{
                tempResult[i] = '0';
            }
        }
        return Long.parseUnsignedLong(new String(tempResult), 2);
    }

}
