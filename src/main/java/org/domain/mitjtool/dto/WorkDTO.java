package org.domain.mitjtool.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class WorkDTO {

	public static final LocalDate START_DATE_A = LocalDate.of(2021, 5, 17);
	public static final LocalDate START_DATE_B = LocalDate.of(2021, 5, 18);
	
	private LocalDate date;
	private String week;
	private boolean a;
	private boolean b;
}
