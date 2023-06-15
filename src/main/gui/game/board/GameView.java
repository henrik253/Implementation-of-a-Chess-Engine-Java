package main.gui.game.board;

import javafx.scene.layout.Pane;
import main.gui.Component;
import main.model.Vector2D;
import main.model.chessPieces.SimplePiece;

public class GameView extends Pane implements Component {

	private GamePresenter gamePresenter;

	private Board board;

	public GameView() {

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}
	
	public boolean moveRequest(int oldX,int oldY,int newX,int newY) {
		return gamePresenter.moveRequest(oldX,oldY,newX,newY);
	}
	
	public void initSimplePieceBoard(SimplePiece[][] simplePieceBoard) {
		this.board.drawPiecesOnBoard(simplePieceBoard);
	}

	public void setBoard(Board board) {
		this.board = board;
		this.getChildren().add(board);
	}

	public GamePresenter getGamePresenter() {
		return gamePresenter;
	}

	public void setGamePresenter(GamePresenter gamePresenter) {
		this.gamePresenter = gamePresenter;
	}

	public void removePieceFromBoard(Vector2D pos) {
		board.removePiece(pos);
		
	}

}
