package org.domain.mitjtool.services;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
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
		int i = 0;
		while (sDate.plusDays(i).isBefore(eDate)) {
			LocalDate t = sDate.plusDays(i);
			WorkDTO dto = new WorkDTO();
			dto.setDate(t);
			dto.setWeek(t.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.TAIWAN));
			dto.setA(ChronoUnit.DAYS.between(WorkDTO.START_DATE_A, t) % 2 == 0);
			dto.setB(ChronoUnit.DAYS.between(WorkDTO.START_DATE_B, t) % 2 == 0);
			dtos.add(dto);
			i++;
		}
		return dtos;
	}
}
