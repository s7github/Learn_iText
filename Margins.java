package entities;

public class Margins {
	private float top;
	private float bottom;
	private float left;
	private float right;
	
	public Margins() {
		setTopMargin(-1);
		setRightMargin(-1);
		setBottomMargin(-1);
		setLeftMargin(-1);
	}
	
	public Margins(float left, float right, float top, float bottom) {
		setTopMargin(top);
		setRightMargin(right);
		setBottomMargin(bottom);
		setLeftMargin(left);
	}
	
	public float getTopMargin() {
		return top;
	}
	
	public void setTopMargin(float top) {
		this.top = top;
	}
	
	public float getBottomMargin() {
		return bottom;
	}
	
	public void setBottomMargin(float bottom) {
		this.bottom = bottom;
	}
	
	public float getLeftMargin() {
		return left;
	}
	
	public void setLeftMargin(float left) {
		this.left = left;
	}
	
	public float getRightMargin() {
		return right;
	}
	
	public void setRightMargin(float right) {
		this.right = right;
	}
}
