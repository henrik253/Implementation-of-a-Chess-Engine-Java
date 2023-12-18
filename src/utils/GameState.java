package utils;

public enum GameState {
	NO_GAME,IN_GAME,GAME_OVER; 
	
	public boolean inGame() {
		return this == IN_GAME;
	}
	
	public boolean noGame() {
		return this == NO_GAME; 
	}
	
	public boolean isGameOver() {
		return this == GAME_OVER; 
	}
	

	
	
}
