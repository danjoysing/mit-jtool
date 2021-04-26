package org.domain.mitjtool.dto;

import lombok.Data;

@Data
public class Req {
	
	private String tableName;
	private String cols;
	private int commentConfig;

}
