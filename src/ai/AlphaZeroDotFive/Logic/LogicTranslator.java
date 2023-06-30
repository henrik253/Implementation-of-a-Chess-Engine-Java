package ai.AlphaZeroDotFive.Logic;

import main.model.Model;
import main.model.Vector2D;
import main.model.chessPieces.ChessPieceColor;
import main.model.chessPieces.concretePieces.*;
import main.model.gameLogic.BoardRepresentation;
import main.model.gameLogic.MoveValidation;

import java.util.LinkedList;

public class LogicTranslator {
    private int[][][] boardBuffer;
    boolean[] inCheckMoves;
    int rows;
    int cols;
    public int moveSize;
    Model model;
    public LogicTranslator(){
        this.rows = 8;
        this.cols = 8;
        this.moveSize = 64 * 64;
        this.model = new Model();
        this.boardBuffer = new int[4096][8][8];
        inCheckMoves = new boolean[4096];
    }
    public boolean[] getValidMoves(int[][] board, int playerOnMove){
        Piece[][] currentBoard = translateBoard(board);
        boolean[] result = new boolean[board.length * board[0].length * board.length * board[0].length];
        int[] coords;
        int index = 0;
        for(int sx = 0; sx < 8; sx++){
            for(int sy = 0; sy < 8; sy++){
                for(int dx = 0; dx < 8; dx++){
                    for(int dy = 0; dy < 8; dy++){
                        result[index] = validateMove(
                                sx, sy, dx, dy,
                                currentBoard,
                                index,
                                playerOnMove);
                        index++;
                    }
                }
            }
        }
        return result;
    }


    public boolean validateMove(int sx, int sy, int dx, int dy, Piece[][] currentBoard, int index, int playerOnMove){
        this.model = new Model();
        model.setBoardRepresentation(new BoardRepresentation(currentBoard));
        model.setMoveValidation(new MoveValidation());
        model.getMoveValidation().setBoard(model.getBoardRepresentation());
        model.getMoveValidation().setOnMove(playerOnMove == 1 ? ChessPieceColor.WHITE : ChessPieceColor.BLACK);
        if(model.getMoveValidation().makeMove(new Vector2D(sx, sy), new Vector2D(dx, dy))){
            this.boardBuffer[index] = translateBoard(model.getBoard());
            inCheckMoves[index] = model.getMoveValidation().enemyInCheck();
            return true;
        }
        return false;
    }
    //TODO
    //Only returns if the enemy is in check!
    public boolean endingMove(int move){
        //return inCheckMoves[move];
        return false;
    }

    //converts a numberArray that represents an octal number to the decimal system;
    public int coordinatesToInt(int sx, int sy, int dx, int dy){
        return sx * (8*8*8)+ sy * (8*8) + dx * 8 + dy;
    }
    //converts a number from the decimal to the octal system and returns it as an array;
    public int[] intToCoordinates(int input){
        LinkedList<Integer> result = new LinkedList<>();
        while(input != 0){
            result.push(input % 8);
            input = Math.floorDiv(input, 8);
        }
        while(result.size() < 4){
            result.push(0);
        }
        return new int[]{
                result.get(0),
                result.get(1),
                result.get(2),
                result.get(3),
        };
    }

    //translates the Board from the Number-representation to the Piece-Representation thats used by the game
    public Piece[][] translateBoard(int[][] board){
        Piece[][] result = new Piece[board.length][board[0].length];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                result[i][j] = this.numberToPiece(board[i][j], i, j);
            }
        }
        return result;
    }
    //translates the Board from the Piece-Representation to the Number-representation thats used by the ai
    public int[][] translateBoard(Piece[][] board){
        int[][] result = new int[board.length][board[0].length];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                result[i][j] = this.pieceToNumber(board[i][j]);
            }
        }
        return result;
    }

    //converts a number to a Piece with the same ordinal as the name enum + 1
    //if the number is negative the Piece is black, else its white
    private Piece numberToPiece(int num, int row, int column) {
        ChessPieceColor color = num < 0 ? ChessPieceColor.BLACK : ChessPieceColor.WHITE;
        return switch (Math.abs(num)) {
            case 1 -> new King(color, row, column);
            case 2 -> new Rook(color, row, column);
            case 3 -> new Bishop(color, row, column);
            case 4 -> new Queen(color, row, column);
            case 5 -> new Knight(color, row, column);
            case 6 -> new Pawn(color, row, column);
            default -> null;
        };
    }
    //converts a Piece to the ordinal of its name-enum + 1
    //if the Piece is black the number also gets multiplied by -1
    private int pieceToNumber(Piece piece){
        return piece == null ? 0 :  (-1 + piece.getColor().ordinal() * 2) * (piece.getName().ordinal() + 1);
    }
    public void printBoard(Piece[][] board){
        int number;
        for(Piece[] pieces : board){
            for(Piece piece : pieces){
                if(pieceToNumber(piece) < 0){
                    System.out.print("|" + pieceToNumber(piece));
                }else{
                    System.out.print("| " + pieceToNumber(piece));
                }
            }
            System.out.print("|\n");
        }
    }

    public void printBoard(int[][] intBoard){
        Piece[][] board = this.translateBoard(intBoard);
        int number;
        for(Piece[] pieces : board){
            for(Piece piece : pieces){
                if(pieceToNumber(piece) < 0){
                    System.out.print("|" + pieceToNumber(piece));
                }else{
                    System.out.print("| " + pieceToNumber(piece));
                }
            }
            System.out.print("|\n");
        }
        System.out.print("\n");
    }

    public int[][][] getBoardBuffer() {
        return boardBuffer;
    }
}
