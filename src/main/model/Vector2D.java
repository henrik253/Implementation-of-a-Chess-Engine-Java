package main.model;

public class Vector2D {

	private int x; 
	private int y; 
	
	public Vector2D(int x,int y) {
		this.x = x; 
		this.y = y; 
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	

	public boolean equals(Vector2D vec) {
		return vec.getX() == this.x && vec.getY() == this.y; 
	}
	
	public Vector2D clone() {
		return new Vector2D(getX(),getY());
	}
	
	public void add(Vector2D vec) {
		this.x += vec.x; 
		this.y += vec.y;
	}
	
	public static Vector2D add(Vector2D v1,Vector2D v2) {
		return new Vector2D(v1.x + v2.x , v1.y + v2.y);
	}
	
	@Override 
	public String toString() {
		return "( "+ x + " | " + y + " )";
	}
	
	
}
