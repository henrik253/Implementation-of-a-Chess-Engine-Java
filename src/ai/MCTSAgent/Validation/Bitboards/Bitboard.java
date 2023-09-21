package ai.MCTSAgent.Validation.Bitboards;

import ai.MCTSAgent.Validation.BitboardValidation.Move;

public class Bitboard {
    public int player;
    public PieceArray playerPieces;
    public PieceArray enemyPieces;
    public PieceArray buffer;
    public ULong playerEnPassantSquares;
    public ULong enemyEnPassantSquares;
    public boolean
            playerKingMoved,
            enemyKingMoved,
            rookKingSideMovedPlayer,
            rookQueenSideMovedPlayer,
            rookKingSideMovedEnemy,
            rookQueenSideMovedEnemy;
    private boolean boolBuffer;
    private ULong uLongBuffer;


    //standard Constructor
    public Bitboard(int[][] intBoard, BitMaskArr arr){
        this.player = 1;
        this.playerPieces = new PieceArray(intBoard, player, arr);
        this.enemyPieces = new PieceArray(intBoard, player * -1,arr);
        this.playerKingMoved = false;
        this.enemyKingMoved = false;
        this.rookKingSideMovedPlayer = false;
        this.rookKingSideMovedEnemy = false;
        this.rookQueenSideMovedPlayer = false;
        this.rookQueenSideMovedEnemy = false;
        this.playerEnPassantSquares = new ULong(arr);
        this.enemyEnPassantSquares = new ULong(arr);
    }
    //copy constructor
    public Bitboard(Bitboard bitboard){
        this.player = bitboard.player;
        this.playerPieces = new PieceArray(bitboard.playerPieces);
        this.enemyPieces = new PieceArray(bitboard.enemyPieces);
        this.buffer = bitboard.buffer;
        this.playerKingMoved = bitboard.playerKingMoved;
        this.enemyKingMoved = bitboard.enemyKingMoved;
        this.rookKingSideMovedPlayer = bitboard.rookKingSideMovedPlayer;
        this.rookKingSideMovedEnemy = bitboard.rookKingSideMovedEnemy;
        this.rookQueenSideMovedPlayer = bitboard.rookQueenSideMovedPlayer;
        this.rookQueenSideMovedEnemy = bitboard.rookQueenSideMovedEnemy;
        this.playerEnPassantSquares = bitboard.playerEnPassantSquares;
        this.enemyEnPassantSquares = bitboard.enemyEnPassantSquares;
        this.uLongBuffer = bitboard.uLongBuffer;
        this.boolBuffer = bitboard.boolBuffer;
    }

    //swaps all aspects of the bitboard and changes the player
    public void flipPlayer(){
        this.player *= -1;

        this.buffer = this.playerPieces;
        this.playerPieces = this.enemyPieces;
        this.enemyPieces = this.buffer;

        this.boolBuffer = this.playerKingMoved;
        this.playerKingMoved = this.enemyKingMoved;
        this.enemyKingMoved = this.boolBuffer;

        this.boolBuffer = this.rookKingSideMovedPlayer;
        this.rookKingSideMovedPlayer = this.rookKingSideMovedEnemy;
        this.rookKingSideMovedEnemy = this.boolBuffer;

        this.boolBuffer = this.rookQueenSideMovedPlayer;
        this.rookQueenSideMovedPlayer = this.rookQueenSideMovedEnemy;
        this.rookQueenSideMovedEnemy = this.boolBuffer;

        this.uLongBuffer = this.playerEnPassantSquares;
        this.playerEnPassantSquares = this.enemyEnPassantSquares;
        this.enemyEnPassantSquares = this.uLongBuffer;
    }

    //returns the position of the king of the player
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

    //returns the int which describes the type of the piece
    public int playerPieceAt(int row, int col) {
        for(int i = 0; i < this.playerPieces.pieces.length; i++){
            if(this.playerPieces.pieces[i].at(row, col)){
                return i;
            }
        }
        return -1;
    }

    //returns an intBoard where the enemyPieces are multiplied by -1
    public int[][]toIntBoard() {
        int[][] result = new int[8][8];
        Bitboard buffer = new Bitboard(this);
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                int pieceType = buffer.playerPieceAt(row, col);
                if(pieceType != -1){
                    result[row][col] = (pieceType+1) * buffer.player;
                }
            }
        }
        buffer.flipPlayer();
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                int pieceType = buffer.playerPieceAt(row, col);
                if(pieceType != -1){
                    result[row][col] = (pieceType+1) * buffer.player;
                }
            }
        }
        return result;
    }
    public int getPieceType(int row, int col){
        if(this.playerPieces.combinedPieces.at(row, col)){
            for(int i = 0; i < 6; i++){
                if(this.playerPieces.pieces[i].at(row, col)){
                    return i;
                }
            }
        }else{
            for(int i = 0; i < 6; i++){
                if(this.enemyPieces.pieces[i].at(row, col)){
                    return i;
                }
            }
        }
        return -1;
    }
    // Returns a copy of the bitboard where the given move is made
    public Bitboard simulateMove(int[] move) {
        Bitboard copy = new Bitboard(this);
        Move moveStruct = new Move(move[0], move[1], this.getPieceType(move[0]/8, move[0]%8)+1);
        moveStruct.applyMove(copy);
        return copy;
    }
}
