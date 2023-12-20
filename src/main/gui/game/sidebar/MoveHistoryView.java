package main.gui.game.sidebar;

import java.util.LinkedList;
import java.util.List;

import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import utils.Move;

public class MoveHistoryView extends Pane {

	private SettingsView parentView;

	private VBox wrapper = new VBox(10);
	

	
	private HBox headingWrapper = new HBox();

	private Text heading = new Text("Move History: ");

	private TextFlow textFlow = new TextFlow();
	private ScrollPane scrollPane = new ScrollPane(textFlow);
	private List<Move> moves = new LinkedList<>();

	public MoveHistoryView(SettingsView parent) {

		parentView = parent;
		headingWrapper.getChildren().add(heading);
		wrapper.getChildren().addAll(headingWrapper, scrollPane);
		getChildren().add(wrapper);
		init();
	}

	private void init() {
		this.setTranslateX(parentView.getPrefWidth() / 20);
		this.setTranslateY((parentView.getPrefHeight() / 2) + (parentView.getPrefHeight() / 15));
		textFlow.setPrefWidth((parentView.getPrefWidth() / 5) * 4);
		scrollPane.setPrefHeight(parentView.getPrefHeight() / 3);
	}

	public void addMove(Move move) {
		Text textMove = new Text(move.toString() + " ");
		textMove.setWrappingWidth(0);
		textFlow.getChildren().add(textMove);
		textMove.setUserData(textFlow.getChildren().indexOf(textMove));

		textMove.setOnMouseClicked(event -> {
			Object source = event.getSource();
			if (source instanceof Text) {
				Text text = (Text) source;
				int index = (Integer) text.getUserData();
				parentView.MoveHistoryElementClicked(index);
			}
		});
	}

	public void clearMoves() {
		moves.clear();
	}

	public void setMoveHistory(List<Move> moveHistory) {
		this.moves.clear();
		this.textFlow.getChildren().clear();
		moveHistory.forEach( move -> addMove(move));
	}
	

}
