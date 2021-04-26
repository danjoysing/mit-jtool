package org.domain.mitjtool.enums;

public enum JdbcTypeEnum {
	CHAR("CHAR"),
	VARCHAR("VARCHAR"),
	CLOB("CLOB"),
	BLOB("BLOB"),
	NUMBER("NUMBER"),
	FLOAT("FLOAT"),
	INTEGER("INTEGER"),
	LONG("LONG"),
	TIMESTAMP("TIMESTAMP"),
	DATE("DATE"),
	OTHER("_");

	private String name;
	private JdbcTypeEnum(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
