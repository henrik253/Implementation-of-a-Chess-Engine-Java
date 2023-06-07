package main.model.gameStates;

public enum ChessMove {
	VALID,NOT_VALID;
	
	public boolean isValid() {
		return VALID == this;
	}

}
