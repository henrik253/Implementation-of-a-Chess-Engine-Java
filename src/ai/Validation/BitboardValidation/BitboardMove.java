package ai.Validation.BitboardValidation;

import ai.Validation.Bitboards.Bitboard;

// Applies move to the given bitboard
// Always assumes that the move is a valid move!!!
public class BitboardMove {
    int start;
    int destination;
    int pieceType;
    int startRow;
    int startCol;
    int destinationRow;
    int destinationCol;
    public BitboardMove(int start, int destination, int pieceType){
        this.start = start;
        this.destination = destination;
        this.pieceType = pieceType;
        this.startRow = start/8;
        this.startCol = start % 8;
        this.destinationRow = destination / 8;
        this.destinationCol = destination % 8;
    }

    public void applyMove(Bitboard bitboard){
        switch (this.pieceType) {
            case 1 -> this.applyKingMove(bitboard);
            case 2 -> this.applyRookMove(bitboard);
            case 3 -> this.applyBishopMove(bitboard);
            case 4 -> this.applyQueenMove(bitboard);
            case 5 -> this.applyKnightMove(bitboard);
            case 6 -> this.applyPawnMove(bitboard);
        }
    }
    public void applyMoveToFlippedBoard(Bitboard bitboard){
        bitboard.flipPlayer();
        switch (this.pieceType) {
            case 1 -> this.applyKingMove(bitboard);
            case 2 -> this.applyRookMove(bitboard);
            case 3 -> this.applyBishopMove(bitboard);
            case 4 -> this.applyQueenMove(bitboard);
            case 5 -> this.applyKnightMove(bitboard);
            case 6 -> this.applyPawnMove(bitboard);
        }
        bitboard.flipPlayer();
    }
    public Bitboard applyMoveAndGetCopy(Bitboard bitboard){
        Bitboard copy = new Bitboard(bitboard);
        switch (this.pieceType) {
            case 1 -> this.applyKingMove(copy);
            case 2 -> this.applyRookMove(copy);
            case 3 -> this.applyBishopMove(copy);
            case 4 -> this.applyQueenMove(copy);
            case 5 -> this.applyKnightMove(copy);
            case 6 -> this.applyPawnMove(copy);
        }
        copy.flipPlayer();
        return copy;
    }

    private void applyKingMove(Bitboard bitboard) {
        //apply appropriate move
        if(destinationCol == startCol + 2 && !bitboard.playerKingMoved && !bitboard.rookKingSideMovedPlayer){
            applyCastlingKingSide(bitboard);
        }else if(destinationCol == startCol - 3 && !bitboard.playerKingMoved && !bitboard.rookQueenSideMovedPlayer){
            applyCastlingQueenSide(bitboard);
        }else{
            removePieceFromStartAndSetAtDestination(bitboard);
        }
        bitboard.playerKingMoved = true;
    }

    private void applyRookMove(Bitboard bitboard) {
        setBitBoardRookBooleans(bitboard);
        removePieceFromStartAndSetAtDestination(bitboard);
    }

    private void applyBishopMove(Bitboard bitboard) {
        removePieceFromStartAndSetAtDestination(bitboard);
    }

    private void applyQueenMove(Bitboard bitboard) {
        removePieceFromStartAndSetAtDestination(bitboard);
    }

    private void applyKnightMove(Bitboard bitboard) {
        removePieceFromStartAndSetAtDestination(bitboard);
    }

    private void applyPawnMove(Bitboard bitboard) {
        if(isDoublePawnMove()){
            setEnPassantSquares(bitboard);
        }
        if(!isEnPassantMove(bitboard)){
            removePieceFromStartAndSetAtDestination(bitboard);
        }
        else{
            applyEnPassantMove(bitboard);
        }
        if(lastMoveWasDoubleMove(bitboard)){
            unsetEnPassantSquare(bitboard);
        }
        if(promotingPawn(bitboard)){
            changeUpPawnForQueen(bitboard);
        }
    }



    //--------------------------------------------helper methods--------------------------------------------
    private void applyEnPassantMove(Bitboard bitboard) {
        //remove the piece from start in playerPieces
        bitboard.playerPieces.pieces[0].unset(startRow, startCol);
        bitboard.playerPieces.combinedPieces.unset(startRow, startCol);
        //add the piece to destination in playerPieces
        bitboard.playerPieces.pieces[0].set(destinationRow, destinationCol);
        bitboard.playerPieces.combinedPieces.set(destinationRow, destinationCol);
        if(bitboard.player == 1){
            //remove the pawn enemy pieces
            bitboard.enemyPieces.combinedPieces.unset(destinationRow+1, destinationCol);
            bitboard.enemyPieces.pieces[5].unset(destinationRow+1, destinationCol);
            bitboard.enemyEnPassantSquares.unset(destinationRow, destinationCol);
        }else{
            //remove the pawn enemy pieces
            bitboard.enemyPieces.combinedPieces.unset(destinationRow-1, destinationCol);
            bitboard.enemyPieces.pieces[5].unset(destinationRow-1, destinationCol);
            bitboard.enemyEnPassantSquares.unset(destinationRow, destinationCol);
        }
    }
    private boolean isEnPassantMove(Bitboard bitboard) {
        return bitboard.enemyEnPassantSquares.at(destinationRow, destinationCol);
    }

    private void setEnPassantSquares(Bitboard bitboard) {
        if(bitboard.player == 1 && destinationRow+1 < 8){
            bitboard.playerEnPassantSquares.set(destinationRow+1, destinationCol);
        }else if (destinationRow-1 >= 0){
            bitboard.playerEnPassantSquares.set(destinationRow-1, destinationCol);
        }
    }
    private boolean isDoublePawnMove() {
        return startRow - destinationRow == 2 || startRow - destinationRow == -2;
    }
    private void setBitBoardRookBooleans(Bitboard bitboard) {
        if(bitboard.player == -1){// if its blacks turn
            if(start == 0){// if its the left rook
                bitboard.rookQueenSideMovedPlayer = true;
            }else if(start == 7){// if its the right rook
                bitboard.rookKingSideMovedPlayer = true;
            }
        }else{// if its whites turn
            if(start == 56){// if its the left rook
                bitboard.rookQueenSideMovedPlayer = true;
            }else if(start == 63){// if its the right rook
                bitboard.rookKingSideMovedPlayer = true;
            }
        }
    }
    private void removePieceFromStartAndSetAtDestination(Bitboard bitboard) {
        int pieceType = bitboard.getPieceType(startRow, startCol);
        //remove the piece from start in playerPieces
        bitboard.playerPieces.pieces[pieceType].unset(startRow, startCol);
        bitboard.playerPieces.combinedPieces.unset(startRow, startCol);
        //if there is a enemy piece at the destination
        if(bitboard.enemyPieces.combinedPieces.at(destinationRow, destinationCol)){
            //remove it from enemy pieces
            bitboard.enemyPieces.combinedPieces.unset(destinationRow, destinationCol);
            bitboard.enemyPieces.pieces[bitboard.getPieceType(destinationRow, destinationCol)].unset(destinationRow, destinationCol);
            //add the piece to destination in playerPieces
            bitboard.playerPieces.pieces[pieceType].set(destinationRow, destinationCol);
            bitboard.playerPieces.combinedPieces.set(destinationRow, destinationCol);
        }else{
            //add the piece to destination in playerPieces
            bitboard.playerPieces.pieces[pieceType].set(destinationRow, destinationCol);
            bitboard.playerPieces.combinedPieces.set(destinationRow, destinationCol);
        }
    }

    private void applyCastlingKingSide(Bitboard bitboard) {
        //remove the king
        bitboard.playerPieces.pieces[0].unset(startRow, startCol);
        bitboard.playerPieces.combinedPieces.unset(startRow, startCol);
        //remove the rook
        if(bitboard.player == 1) {
            bitboard.playerPieces.pieces[1].unset(7, 7);
            bitboard.playerPieces.combinedPieces.unset(7, 7);
        }else{
            bitboard.playerPieces.pieces[1].unset(0, 7);
            bitboard.playerPieces.combinedPieces.unset(0, 7);
        }
        //add the king
        bitboard.playerPieces.pieces[0].set(destinationRow, destinationCol);
        bitboard.playerPieces.combinedPieces.set(destinationRow, destinationCol);
        //add the rook
        bitboard.playerPieces.pieces[1].set(destinationRow, destinationCol-1);
        bitboard.playerPieces.combinedPieces.set(destinationRow, destinationCol-1);
    }

    private void applyCastlingQueenSide(Bitboard bitboard) {
        //remove the king
        bitboard.playerPieces.pieces[0].unset(startRow, startCol);
        bitboard.playerPieces.combinedPieces.unset(startRow, startCol);
        //remove the rook
        if(bitboard.player == 1) {
            bitboard.playerPieces.pieces[1].unset(7, 0);
            bitboard.playerPieces.combinedPieces.unset(7, 0);
        }else{
            bitboard.playerPieces.pieces[1].unset(0, 0);
            bitboard.playerPieces.combinedPieces.unset(0, 0);
        }
        //add the king
        bitboard.playerPieces.pieces[0].set(destinationRow, destinationCol);
        bitboard.playerPieces.combinedPieces.set(destinationRow, destinationCol);
        //add the rook
        bitboard.playerPieces.pieces[1].set(destinationRow, destinationCol+1);
        bitboard.playerPieces.combinedPieces.set(destinationRow, destinationCol+1);
    }
    private void changeUpPawnForQueen(Bitboard bitboard) {//gets called after the move was made!!!!!!
        bitboard.playerPieces.pieces[5].unset(destinationRow, destinationCol);
        bitboard.playerPieces.pieces[3].set(destinationRow, destinationCol);
    }

    private boolean promotingPawn(Bitboard bitboard) {
        if(bitboard.player == 1){
            return this.destinationRow == 0;
        }else{
            return this.destinationRow == 7;
        }
    }

    private void unsetEnPassantSquare(Bitboard bitboard) {
        if(bitboard.player == 1 && startRow < 8){
            bitboard.playerEnPassantSquares.unset(startRow+1, startCol);
        }else if(startRow >= 0){
            bitboard.playerEnPassantSquares.unset(startRow-1, startCol);
        }
    }

    private boolean lastMoveWasDoubleMove(Bitboard bitboard) {
        if(bitboard.player == 1 && startRow < 8){
            return bitboard.playerEnPassantSquares.at(startRow+1, startCol);
        }else if(startRow >= 0){
            return bitboard.playerEnPassantSquares.at(startRow-1, startCol);
        }
        return false;
    }
}
