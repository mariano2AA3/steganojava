package tmi.steganojava.utils;

public class Pair<L,R> {
	private L left;
	private R right;
	
	public Pair(L left,R right){
		this.left = left;
		this.right = right;
	}
	
	public Pair(){}

	public L getLeft() {
		return left;
	}

	public void setLeft(L left) {
		this.left = left;
	}

	public R getRight() {
		return right;
	}

	public void setRight(R right) {
		this.right = right;
	}
	
	public void setPair(Pair<L,R> p){
		this.left = p.getLeft();
		this.right = p.getRight();
	}

}
