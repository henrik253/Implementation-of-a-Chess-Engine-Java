package ai.Util;

import ai.Validation.BitboardValidation.BitboardMoveValidation;
import ai.Validation.Bitboards.BitMaskArr;
import main.model.chessPieces.concretePieces.Piece;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Util {
    public static final int moveSize = 64 * 64;
    final static BitboardMoveValidation validation = new BitboardMoveValidation(new BitMaskArr(), 1);

    //converts a numberArray that represents an octal number to the decimal system;
    public static int coordinatesToInt(int sx, int sy, int dx, int dy){
        return sx * (8*8*8)+ sy * (8*8) + dx * 8 + dy;
    }
    //converts a number from the decimal to the octal system and returns it as an array;
    public static int[] intToCoordinates(int input){
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

    public boolean endingMove(int player, int[][] board) {
        return validation.getValidMoves(board, player).isEmpty();
    }

    public static List<Integer> getValidMoves(int[][] board, int player) {
        List<Integer> result = new ArrayList<>();
        List<int[]> tempResult = validation.getValidMoves(board, player);
        for (int[] move: tempResult) {
            result.add(coordinatesToInt(
                    move[0]%8,
                    move[0]/8,
                    move[1]%8,
                    move[1]/8
            ));
        }
        return result;
    }
    public static int[] flipCoordinates(int[] ints) {
        return new int[]{ints[0], 7 - ints[1], ints[2], 7 - ints[3]};
    }
    public static int[][] flipBoardHorizontallyAndFLipPlayer(int[][] board) {
        int[][] result = new int[8][8];
        for(int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++) {
                result[7-row][col] = board[row][col] * -1;
            }
        }
        return result;
    }

    public static int[][] translateBoard(Piece[][] board){
        int[][] result = new int[board.length][board[0].length];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[0].length; j++){
                result[i][j] = pieceToNumber(board[i][j]);
            }
        }
        return result;
    }
    //converts a Piece to the ordinal of its name-enum + 1
    //if the Piece is black the number also gets multiplied by -1
    private static int pieceToNumber(Piece piece){
        return piece == null ? 0 :  (-1 + piece.getColor().ordinal() * 2) * (piece.getName().ordinal() + 1);
    }
}
