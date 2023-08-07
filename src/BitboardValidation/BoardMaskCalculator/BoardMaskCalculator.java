package BitboardValidation.BoardMaskCalculator;

import BitboardValidation.Bitboards.MovementBitBoardGenerator;
import BitboardValidation.Bitboards.ULong;

public class BoardMaskCalculator {
    ULong[] boardMasksEnemy;
    public ULong combinedBoardMaskEnemy;
    ULong[] boardMasksPlayer;
    public ULong combinedBoardMaskPlayer;
    public MovementBitBoardGenerator generator;
    public ULong playerPieces;
    public ULong enemyPieces;

    public BoardMaskCalculator(){
        generator = new MovementBitBoardGenerator();
        generator.calculateMovementBoards();
        this.boardMasksEnemy = new ULong[64];
        this.combinedBoardMaskEnemy = new ULong(generator.bitMaskArr);
        this.boardMasksPlayer = new ULong[64];
        this.combinedBoardMaskPlayer = new ULong(generator.bitMaskArr);
        for(int i = 0; i < 64; i++){
            this.boardMasksPlayer[i] = new ULong(generator.bitMaskArr);
            this.boardMasksEnemy[i] = new ULong(generator.bitMaskArr);
        }
        playerPieces = new ULong(generator.bitMaskArr);
        enemyPieces = new ULong(generator.bitMaskArr);
    }

    public void calculateBoardMasks(int[][] board){
        zeroAllMasks();
        fillMovementMasks(board);
    }

    private void fillMovementMasks(int[][] board) {
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[0].length; col++){
                fillMovementMask(board, row, col);
            }
        }
    }

    private void fillMovementMask(int[][] board, int row, int col) {
        if(board[row][col] < 0){
            setBoardMaskEnemy(row, col, board[row][col]);
            enemyPieces.set(row, col);
        }else if(board[row][col] > 0){
            setBoardMaskPlayer(row, col, board[row][col]);
            playerPieces.set(row, col);
        }
    }

    private void zeroAllMasks() {
        combinedBoardMaskEnemy.content = 0;
        combinedBoardMaskPlayer.content = 0;
        playerPieces.content = 0;
        enemyPieces.content = 0;
        for (ULong uLong : this.boardMasksEnemy) {
            uLong.content = 0;
        }
        for (ULong uLong : this.boardMasksPlayer) {
            uLong.content = 0;
        }
    }

    private void setBoardMaskEnemy(int row, int col, int type) {
        switch (type) {
            case -1 -> {
                boardMasksEnemy[row * 8 + col].content = generator.kingBoards[row * 8 + col].content;
                combinedBoardMaskEnemy.or(generator.kingBoards[row * 8 + col]);
            }
            case -2 -> {
                boardMasksEnemy[row * 8 + col].content = generator.rookBoards[row * 8 + col].content;
                combinedBoardMaskEnemy.or(generator.rookBoards[row * 8 + col]);
            }
            case -3 -> {
                boardMasksEnemy[row * 8 + col].content = generator.bishopBoards[row * 8 + col].content;
                combinedBoardMaskEnemy.or(generator.bishopBoards[row * 8 + col]);
            }
            case -4 -> {
                boardMasksEnemy[row * 8 + col].content = generator.queenBoards[row * 8 + col].content;
                combinedBoardMaskEnemy.or(generator.queenBoards[row * 8 + col]);
            }
            case -5 -> {
                boardMasksEnemy[row * 8 + col].content = generator.knightBoards[row * 8 + col].content;
                combinedBoardMaskEnemy.or(generator.knightBoards[row * 8 + col]);
            }
            case -6 -> {
                boardMasksEnemy[row * 8 + col].content = generator.pawnDownBoards[row * 8 + col].content;
                combinedBoardMaskEnemy.or(generator.pawnDownBoards[row * 8 + col]);
            }
        }
    }
    private void setBoardMaskPlayer(int row, int col, int type) {
        switch (type) {
            case 1 -> {
                boardMasksPlayer[row * 8 + col].content = generator.kingBoards[row * 8 + col].content;
                combinedBoardMaskPlayer.or(generator.kingBoards[row * 8 + col]);
            }
            case 2 -> {
                boardMasksPlayer[row * 8 + col].content = generator.rookBoards[row * 8 + col].content;
                combinedBoardMaskPlayer.or(generator.rookBoards[row * 8 + col]);
            }
            case 3 -> {
                boardMasksPlayer[row * 8 + col].content = generator.bishopBoards[row * 8 + col].content;
                combinedBoardMaskPlayer.or(generator.bishopBoards[row * 8 + col]);
            }
            case 4 -> {
                boardMasksPlayer[row * 8 + col].content = generator.queenBoards[row * 8 + col].content;
                combinedBoardMaskPlayer.or(generator.queenBoards[row * 8 + col]);
            }
            case 5 -> {
                boardMasksPlayer[row * 8 + col].content = generator.knightBoards[row * 8 + col].content;
                combinedBoardMaskPlayer.or(generator.knightBoards[row * 8 + col]);
            }
            case 6 -> {
                boardMasksPlayer[row * 8 + col].content = generator.pawnUpBoards[row * 8 + col].content;
                combinedBoardMaskPlayer.or(generator.pawnUpBoards[row * 8 + col]);
            }
        }
    }
}
