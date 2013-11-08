package org.vicomtech.opener.bratAdaptionTools.model;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;

public class BratAnnotation {

	public static class Reference {
		private String entity;
		private String knowledgeBaseName;
		private String resourceId;

		public Reference(String entity, String knowledgeBaseName,
				String resourceId) {
			this.entity = entity;
			this.knowledgeBaseName = knowledgeBaseName;
			this.resourceId = resourceId;
		}

		public String getEntity() {
			return entity;
		}

		public void setEntity(String entity) {
			this.entity = entity;
		}

		public String getKnowledgeBaseName() {
			return knowledgeBaseName;
		}

		public void setKnowledgeBaseName(String knowledgeBaseName) {
			this.knowledgeBaseName = knowledgeBaseName;
		}

		public String getResourceId() {
			return resourceId;
		}

		public void setResourceId(String resourceId) {
			this.resourceId = resourceId;
		}

		@Override
		public String toString() {
			return "Reference [entity=" + entity + ", knowledgeBaseName="
					+ knowledgeBaseName + ", resourceId=" + resourceId + "]";
		}
	}

	private String id;
	private String type;
	private int[] offsets = new int[] { -1, -1 };
	private List<String> involvedEntities=Lists.newArrayList();
	private String text;
	private int[] kafTokenSpan = new int[] { -1, -1 };;
	private Reference reference;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "BratAnnotation [id=" + id + ", type=" + type + ", offsets="
				+ Arrays.toString(offsets) + ", involvedEntities="
				+ involvedEntities + ", text=" + text + ", kafTokenSpan="
				+ Arrays.toString(kafTokenSpan) + ", reference=" + reference
				+ "]";
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

	public boolean isRelation() {
		return id.startsWith("R") || id.startsWith("*");
	}

	public boolean isEntity() {
		return id.startsWith("T");
	}

	public boolean isNormalization() {
		return id.startsWith("N");
	}

	public void addInvolvedEntity(String id) {
		if (involvedEntities == null) {
			involvedEntities = Lists.newArrayList();
		}
		involvedEntities.add(id);
	}

	public void setInvolvedEntities(List<String> involvedEntities) {
		this.involvedEntities = involvedEntities;
	}

	public List<String> getInvolvedEntities() {
		return involvedEntities;
	}

	public int[] getKafTokenSpan() {
		return kafTokenSpan;
	}

	public void setKafTokenSpan(int[] kafTokenSpan) {
		this.kafTokenSpan = kafTokenSpan;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

}
