package BitboardValidation.Bitboards;

public class Bitboard {
    public int player;
    public PieceArray playerPieces;
    public PieceArray enemyPieces;
    public PieceArray buffer;
    public ULong playerDoublePawns;
    public ULong enemyDoublePawns;
    public ULong doublePawnBuffer;
    public ULong unmovedPlayerRooks;
    public ULong unmovedEnemyRooks;
    private boolean playerKingNotMoved;
    private boolean enemyKingNotMoved;


    public Bitboard(int[][] intBoard, BitMaskArr arr){
        this.player = 1;
        this.playerPieces = new PieceArray(intBoard, player, arr);
        this.enemyPieces = new PieceArray(intBoard, player * -1,arr);
        this.playerDoublePawns = new ULong(arr);
        this.enemyDoublePawns = new ULong(arr);
        this.unmovedPlayerRooks = new ULong(arr);
        this.unmovedEnemyRooks = new ULong(arr);
        this.setUnmovedRooksAndKings();
    }
    public Bitboard(Bitboard bitboard){
        this.player = bitboard.player;
        this.playerPieces = new PieceArray(bitboard.playerPieces);
        this.enemyPieces = new PieceArray(bitboard.enemyPieces);
        this.playerDoublePawns = new ULong(bitboard.playerDoublePawns);
        this.enemyDoublePawns = new ULong(bitboard.enemyDoublePawns);
        this.unmovedPlayerRooks = new ULong(bitboard.unmovedPlayerRooks);
        this.unmovedEnemyRooks = new ULong(bitboard.unmovedEnemyRooks);
    }

    private void setUnmovedRooksAndKings() {
        this.unmovedEnemyRooks = new ULong(this.enemyPieces.pieces[1].content, this.enemyPieces.combinedPieces.bitmasks);
        this.unmovedPlayerRooks = new ULong(this.playerPieces.pieces[1].content, this.enemyPieces.combinedPieces.bitmasks);
        this.playerKingNotMoved = true;
        this.enemyKingNotMoved = true;
    }

    public void flipPlayer(){
        this.player *= -1;
        this.buffer = enemyPieces;
        this.enemyPieces = this.playerPieces;
        this.playerPieces = this.buffer;
        this.doublePawnBuffer = this.enemyDoublePawns;
        this.enemyDoublePawns = this.playerDoublePawns;
        this.playerDoublePawns = this.doublePawnBuffer;
    }
    public int[] getKingPosition(){
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                if(this.playerPieces.pieces[0].at(row, col)){
                    return new int[]{row, col};
                }
            }
        }
        return new int[]{-1, -1};
    }
    public int playerPieceAt(int row, int col) {
        for(int i = 0; i < this.playerPieces.pieces.length; i++){
            if(this.playerPieces.pieces[i].at(row, col)){
                return i;
            }
        }
        return -1;
    }
}
