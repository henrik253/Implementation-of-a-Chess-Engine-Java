package utils;

public enum ChessMove {
	VALID,NOT_VALID,NONE;
	
	public boolean isValid() {
		return VALID == this;
	}

}
