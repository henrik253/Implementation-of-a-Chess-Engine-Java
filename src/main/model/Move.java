package main.model;

public final class Move {

	private final Vector2D oldPos;
	private final Vector2D newPos;

	public Move(Vector2D oldPos, Vector2D newPos) {
		this.oldPos = oldPos;
		this.newPos = newPos;
	}

	public Vector2D getOldPos() {
		return oldPos;
	}

	public Vector2D getNewPos() {
		return newPos;
	}

}
