package application;

public class Token {
	String string;
	int id;
	int lineNum;

	public Token(String string, int id, int lineNum) {
		this.string = string;
		this.id = id;
		this.lineNum = lineNum;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLineNum() {
		return lineNum;
	}

	public void setLineNum(int lineNum) {
		this.lineNum = lineNum;
	}

	@Override
	public String toString() {
		return "Token [string=" + string + ", id=" + id + ", lineNum=" + lineNum + "]";
	}

//	@Override
//	public String toString() {
//		return "Token [string=" + string + ", id=" + id + "]";
//	}
	

}
