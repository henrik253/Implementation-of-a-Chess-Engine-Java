package main.model.gameStates;

public enum ChessMove {
	VALID,NOT_VALID,NONE;
	
	public boolean isValid() {
		return VALID == this;
	}

}
