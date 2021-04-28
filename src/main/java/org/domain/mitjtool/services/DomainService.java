package org.domain.mitjtool.services;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.domain.mitjtool.dto.DomainDTO;
import org.domain.mitjtool.dto.Req;
import org.domain.mitjtool.enums.JdbcTypeEnum;
import org.domain.mitjtool.enums.TypeEnum;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

	public String generate(Req req) {
		if (StringUtils.isBlank(req.getCols())) return null;
		StringBuilder sb = prepareStringBuilder(req.getTableName());
		String[] rows = req.getCols()
				           .replace("\t", TypeEnum.SPACE.getName()) //取代tab
				           .replace(String.valueOf((char)13), TypeEnum.SPACE.getName()) //取代enter符
				           .split("\n");
		if (rows != null) {
			Arrays.stream(rows)
			      .filter(StringUtils::isNotBlank)
				  .map(row -> convertRow(row, req.getCommentConfig()))
				  .forEach(dto ->
					  sb.append("    private ")
					    .append(dto.getType())
					    .append(TypeEnum.SPACE.getName())
					    .append(dto.getColName())
					    .append("; ")
					    .append(dto.getComment())
					    .append("\n")
				  );
		}
		sb.append("}");
		return sb.toString();
	}
	
	private DomainDTO convertRow(String row, int commentConfig) {
		row = row.trim();
		int firstSpaceIndex = StringUtils.indexOf(row, " ");
		if (firstSpaceIndex == -1) {
			firstSpaceIndex = row.length();
		}
		String firstCol = convertUnderLine(StringUtils.lowerCase(StringUtils.substring(row, 0, firstSpaceIndex)));
		String otherCols = StringUtils.substring(row, firstSpaceIndex + 1);
		
		String comment = otherCols;
		if (commentConfig == 1) {
			int lastSpaceIndex = StringUtils.lastIndexOf(row, " ");
			if (lastSpaceIndex == -1) {
				lastSpaceIndex = row.length();
			}
			comment = StringUtils.substring(row, lastSpaceIndex + 1);
		}
		//從firstCol取出'.'以後的欄位名稱
		int lastIndexOfDotInFirstCol = StringUtils.lastIndexOf(firstCol, ".");
		if (lastIndexOfDotInFirstCol != -1) {
			firstCol = StringUtils.substring(firstCol, lastIndexOfDotInFirstCol + 1);
		}
		
		DomainDTO dto = new DomainDTO();
		dto.setColName(firstCol);
		dto.setComment("//" + comment);
		dto.setType(TypeEnum.STRING.getName());
		
		String upperedOtherCols = StringUtils.upperCase(otherCols);
		if (StringUtils.containsAny(upperedOtherCols, JdbcTypeEnum.VARCHAR.getName(),
				                                      JdbcTypeEnum.CHAR.getName(),
				                                      JdbcTypeEnum.CLOB.getName())) {
			dto.setType(TypeEnum.STRING.getName());
    	} else if (StringUtils.contains(upperedOtherCols, JdbcTypeEnum.BLOB.getName())) {
    		dto.setType(TypeEnum.BYTE_ARRAY.getName());
    	} else if (StringUtils.contains(upperedOtherCols, JdbcTypeEnum.INTEGER.getName())) {
    		dto.setType(TypeEnum.INTEGER.getName());
	    } else if (StringUtils.contains(upperedOtherCols, JdbcTypeEnum.FLOAT.getName())) {
			dto.setType(TypeEnum.FLOAT.getName());
		} else if (StringUtils.contains(upperedOtherCols, JdbcTypeEnum.NUMBER.getName())){
			dto.setType(TypeEnum.DOUBLE.getName());
		} else if (StringUtils.contains(upperedOtherCols, JdbcTypeEnum.TIMESTAMP.getName())) {
			dto.setType(TypeEnum.LOCAL_DATE_TIME.getName());
		} else if (StringUtils.contains(upperedOtherCols, JdbcTypeEnum.DATE.getName())) {
			if (StringUtils.contains(upperedOtherCols, "時間")) {
				dto.setType(TypeEnum.LOCAL_DATE_TIME.getName());
			} else if (StringUtils.contains(upperedOtherCols, "日期")) {
				dto.setType(TypeEnum.LOCAL_DATE.getName());
			} else {
				dto.setType(TypeEnum.LOCAL_DATE_TIME.getName());
			}
		} else if (StringUtils.containsAny(upperedOtherCols, "數字", "價", "金", "額", "合計", "小計", "總計")) {
			dto.setType(TypeEnum.DOUBLE.getName());
		} else {
			dto.setType(TypeEnum.STRING.getName());
		}
		return dto;
	}
	
	private StringBuilder prepareStringBuilder(String tableName) {
		String camelTableName = convertUnderLine(
				                    StringUtils.upperCase(StringUtils.substring(tableName, 0, 1))
				                  + StringUtils.lowerCase(StringUtils.substring(tableName, 1)));
		
		StringBuilder sb = new StringBuilder();
		sb
		  .append("@Data\n")
		  .append("@NoArgsConstructor\n")
		  .append("@AllArgsConstructor\n")
		  .append("@Alias(\"" + camelTableName + "\")\n")
		  .append("@Table(\"" + StringUtils.upperCase(tableName) + "\")\n")
		  .append("public class ")
		  .append(camelTableName)
		  .append(" {\n");
		return sb;
	}
	
	private String convertUnderLine(String s) {
		if (StringUtils.isBlank(s)) return ""; 
		char[] carr = s.toCharArray();
		for (int i = 0, max = carr.length;  i < max; i++) {
			if (carr[i] == '_' && carr.length > i + 1) {
				carr[i + 1] = StringUtils.upperCase(String.valueOf(carr[i + 1])).charAt(0);
			}
		}
		return String.valueOf(carr).replace("_", "");
	}
}
