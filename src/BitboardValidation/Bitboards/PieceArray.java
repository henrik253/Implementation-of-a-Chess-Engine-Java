package BitboardValidation.Bitboards;

public class PieceArray {
    public ULong[] pieces;
    public ULong combinedPieces;
    public PieceArray(int[][] intBoard, int player, BitMaskArr arr) {
        initPieces(arr);
        for(int row = 0; row < intBoard.length; row++){
            for (int col = 0; col < intBoard[0].length; col++) {
                if((intBoard[row][col] > 0 && player == 1)){
                    pieces[intBoard[row][col] - 1].set(row, col);
                    this.combinedPieces.set(row, col);
                }else if(intBoard[row][col] < 0 && player == -1){
                    pieces[-1*intBoard[row][col] - 1].set(row, col);
                    this.combinedPieces.set(row, col);
                }
            }
        }
    }
    public PieceArray(PieceArray toCopy) {
        this.pieces = new ULong[toCopy.pieces.length];
        for(int i = 0; i < this.pieces.length; i++){
            this.pieces[i] = new ULong(toCopy.pieces[i]);
        }
        this.combinedPieces = new ULong(toCopy.combinedPieces.bitmasks);
        this.combinedPieces.content = toCopy.combinedPieces.content;
    }



    private static void flipBoardToPlayer(int[][] intBoard, int player) {
        if(player == -1){
            for(int row = 0; row < intBoard.length; row++){
                for (int col = 0; col < intBoard[0].length; col++) {
                    intBoard[row][col] *= player;
                }
            }
        }
    }

    private void initPieces(BitMaskArr arr) {
        this.combinedPieces = new ULong(arr);
        this.pieces = new ULong[6];
        for(int i = 0; i < this.pieces.length; i++){
            this.pieces[i] = new ULong(arr);
        }
    }

    public void print(){
        String[] names = new String[]{"King", "Rook", "Bishop", "Queen", "Knight", "Pawn"};
        for(int i = 0; i < pieces.length; i++){
            System.out.println(names[i] + "´s");
            pieces[i].print2D();
        }
    }
}
