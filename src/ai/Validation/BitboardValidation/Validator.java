package ai.Validation.BitboardValidation;

import ai.Validation.Bitboards.Bitboard;
import ai.Validation.Bitboards.MovementBitBoardGenerator;

import java.util.ArrayList;

public abstract class Validator {

    private final MovementBitBoardGenerator generator;

    /*
        Always calculates the valid moves from the perspective of the player that's stored
        in the Bitboard struct, important for pawns. It doesn't check for king safety
        which gets done in the BitboardMoveValidation class after all validators ran.
    
        */
    public Validator(MovementBitBoardGenerator generator){
        this.generator = generator;
    }
    public abstract ArrayList<int[]> getValidMoves(
            int start,
            Bitboard board
    );
    public void addRayCastedArmToArrayList(Bitboard bitboard, ArrayList<int[]> result, int row, int col, int rowOffSet, int colOffSet, int start) {
        row += rowOffSet;
        col += colOffSet;
        while(row >= 0 && row < 8 && col >= 0 && col < 8){// while coordinates are in bounds
            if(bitboard.playerPieces.combinedPieces.at(row, col)){// if it's an playerPiece stop raycasting
                break;
            }else if(bitboard.enemyPieces.combinedPieces.at(row, col)){// if it's an enemyPiece add the move to the result and then stop raycasting
                result.add(new int[]{start, row * 8 + col});
                break;
            }
            //if nothing of the above, add the move to the result and update row and col
            result.add(new int[]{start, row * 8 + col});
            row += rowOffSet;
            col += colOffSet;
        }
    }
}
