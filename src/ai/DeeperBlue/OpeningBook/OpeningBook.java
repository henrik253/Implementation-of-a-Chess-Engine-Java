package ai.DeeperBlue.OpeningBook;

import ai.Validation.Bitboards.BitMaskArr;
import ai.Validation.Bitboards.Bitboard;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Hashtable;
import java.util.List;

public class OpeningBook {
    Hashtable<Long, int[]> content;
    BitMaskArr arr;
    public OpeningBook(){
        this.arr = new BitMaskArr();
        this.content = new Hashtable<>();
        this.fillContent();

    }

    private void fillContent() {
        //readlines
        String[] currentLine;
        int[][] currentIntBoard;
        try {
            List<String> lines = Files.readAllLines(Paths.get("./resources/openingBook.csv"));
            for(String line : lines){
                //convert to move/hash
                line = line.replace(" ", "");
                currentLine = line.split(",");
                currentIntBoard = new int[8][8];
                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        currentIntBoard[row][col] = Integer.parseInt(currentLine[row * 8 + col + 2]);
                    }
                }
                this.content.put(
                        new Bitboard(currentIntBoard, this.arr).getSimpleHash(),
                        new int[]{Integer.parseInt(currentLine[0]), Integer.parseInt(currentLine[1])}
                );
            }



        } catch (IOException e) {
            System.out.println("ticktack");
            throw new RuntimeException(e);
        }

    }
    public boolean contains(int[][] board){
        return this.content.contains(new Bitboard(board, this.arr).getSimpleHash());
    }
}
