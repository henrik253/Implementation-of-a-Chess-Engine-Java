package ai.DeeperBlue.OpeningBook;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class OpeningBook{

    HashMap<Integer, int[]> content;

    public OpeningBook(){
        this.initContent();
    }

    private void initContent(){
        this.content = new HashMap<>();
        try(BufferedReader input = new BufferedReader(new FileReader("./resources/openingBook.csv"))){
            String buffer;
            while(true){
                buffer = input.readLine();
                if(buffer == null){
                    break;
                }
                this.addConvertedLineToContent(buffer);
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

    }

    private void addConvertedLineToContent(String buffer) {
        int[] intArr = this.convertToIntArray(buffer);
        int[] move = new int[2];
        int[] board = new int[64];
        System.arraycopy(intArr, 0, move, 0, 2);
        System.arraycopy(intArr, 2, board, 0, 64);
        this.content.put(Arrays.hashCode(board), move);
    }

    private int[] convertToIntArray(String buffer) {
        String[] stringRepresentation = buffer.split(",");
        int[] intRepresentation = new int[stringRepresentation.length];
        for(int i = 0; i < stringRepresentation.length; i++){
            intRepresentation[i] = Integer.parseInt(stringRepresentation[i].replace(" ", ""));
        }
        return intRepresentation;
    }

    public int[] getMove(int[][] board){
        int[] flattenedBoard = this.flatten(board);
        return this.content.get(Arrays.hashCode(flattenedBoard));
    }

    private int[] flatten(int[][] board) {
        int[] result = new int[64];
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(board[row], 0, result, row * 8, board[0].length);
        }
        return result;
    }
}