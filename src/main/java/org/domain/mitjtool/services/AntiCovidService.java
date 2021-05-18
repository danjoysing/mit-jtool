package org.domain.mitjtool.services;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.domain.mitjtool.dto.WorkDTO;
import org.springframework.stereotype.Service;

@Service
public class AntiCovidService {

	public List<WorkDTO> cal(LocalDate sDate) {
		LocalDate eDate = sDate.plusMonths(1)
				               .with(TemporalAdjusters.lastDayOfMonth());
		List<WorkDTO> dtos = new ArrayList<>();
		while (sDate.isBefore(eDate)) {
			WorkDTO dto = new WorkDTO();
			dto.setDate(sDate);
			dto.setWeek(sDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.TAIWAN));
			dto.setA(Period.between(WorkDTO.START_DATE_A, sDate).getDays() % 2 == 0);
			dto.setB(Period.between(WorkDTO.START_DATE_B, sDate).getDays() % 2 == 0);
			sDate = sDate.plusDays(1);
			dtos.add(dto);
		}
		return dtos;
	}
}
