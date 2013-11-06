package org.vicomtech.opener.bratAdaptionTools;

public class BratAnnotation {

	private String id;
	private String type;
	private int[] offsets = new int[] { -1, -1 };
	private String text;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getStart() {
		return offsets[0];
	}

	public int getEnd() {
		return offsets[1];
	}

	public void setOffsets(int start, int end) {
		this.offsets = new int[] { start, end };
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
