package org.domain.mitjtool.enums;

public enum TypeEnum {
	STRING("String"),
	INTEGER("Integer"),
	DOUBLE("Double"),
	BYTE_ARRAY("byte[]"),
	LOCAL_DATE("LocalDate"),
	LOCAL_DATE_TIME("LocalDateTime"),
	SPACE(" ");

	private String name;
	private TypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
