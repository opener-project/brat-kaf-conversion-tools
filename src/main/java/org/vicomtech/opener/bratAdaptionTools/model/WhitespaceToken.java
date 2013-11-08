package org.vicomtech.opener.bratAdaptionTools.model;

import java.util.List;

import com.google.common.collect.Lists;

public class WhitespaceToken {
	private int start;
	private int end;
	private String text;
	
	public WhitespaceToken(int start, int end, String text) {
		super();
		this.start = start;
		this.end = end;
		this.text = text;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getEnd() {
		return end;
	}

	public void setEnd(int end) {
		this.end = end;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "WhitespaceToken [start=" + start + ", end=" + end
				+ ", text=" + text + "]";
	}

	public static List<WhitespaceToken> parseText(String text) {
		String[] whiteSpaceTokens = text.split(" |\n");
		List<WhitespaceToken> whitespaceTokenList = Lists.newArrayList();
		int offsetCount = 0;
		for (String token : whiteSpaceTokens) {
			token = token.trim();
			WhitespaceToken whitespaceToken = new WhitespaceToken(offsetCount,
					offsetCount+token.length(), token);
			offsetCount += token.length() + 1;
			whitespaceTokenList.add(whitespaceToken);
		}
		return whitespaceTokenList;
	}
}
