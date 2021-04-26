package org.domain.mitjtool.services;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.domain.mitjtool.dto.DomainDTO;
import org.domain.mitjtool.enums.JdbcTypeEnum;
import org.domain.mitjtool.enums.TypeEnum;
import org.springframework.stereotype.Service;

@Service
public class DomainService {

	public String generate(String tableName, String cols) {
		if (StringUtils.isBlank(cols)) return null;
		StringBuilder sb = prepareStringBuilder(tableName);
		String[] rows = cols.replace("\t", TypeEnum.SPACE.getName()).split("\n");
		if (rows != null) {
			Arrays.stream(rows)
				  .map(this::convertRow)
				  .forEach(dto -> {
					  sb.append("    private ")
					    .append(dto.getType())
					    .append(TypeEnum.SPACE.getName())
					    .append(dto.getColName())
					    .append("; ")
					    .append(dto.getComment())
					    .append("\n");
				  });
		}
		sb.append("}");
		return sb.toString();
	}
	
	private DomainDTO convertRow(String row) {
		int firstSpaceIndex = StringUtils.indexOf(row, " ");
		if (firstSpaceIndex == -1) {
			firstSpaceIndex = row.length();
		}
		String col = convertUnderLine(StringUtils.lowerCase(StringUtils.substring(row, 0, firstSpaceIndex)));
		String comment = StringUtils.substring(row, firstSpaceIndex + 1);
		
		DomainDTO dto = new DomainDTO();
		dto.setColName(col);
		dto.setComment("//" + comment);
		dto.setType(TypeEnum.STRING.getName());
		
		String upperedComment = StringUtils.upperCase(comment);
		if (StringUtils.containsAny(upperedComment, JdbcTypeEnum.VARCHAR.getName(),
				                                    JdbcTypeEnum.CHAR.getName(),
				                                    JdbcTypeEnum.CLOB.getName())) {
			dto.setType(TypeEnum.STRING.getName());
    	} else if (StringUtils.contains(upperedComment, JdbcTypeEnum.BLOB.getName())) {
    		dto.setType(TypeEnum.BYTE_ARRAY.getName());
    	} else if (StringUtils.contains(upperedComment, JdbcTypeEnum.INTEGER.getName())) {
    		dto.setType(TypeEnum.INTEGER.getName());
	    } else if (StringUtils.containsAny(upperedComment, JdbcTypeEnum.FLOAT.getName(),
	    		                                    JdbcTypeEnum.NUMBER.getName())) {
			dto.setType(TypeEnum.DOUBLE.getName());
		} else if (StringUtils.contains(upperedComment, JdbcTypeEnum.TIMESTAMP.getName())) {
			dto.setType(TypeEnum.LOCAL_DATE_TIME.getName());
		} else if (StringUtils.contains(upperedComment, JdbcTypeEnum.DATE.getName())) {
			if (StringUtils.contains(upperedComment, "時間")) {
				dto.setType(TypeEnum.LOCAL_DATE_TIME.getName());
			} else if (StringUtils.contains(upperedComment, "日期")) {
				dto.setType(TypeEnum.LOCAL_DATE.getName());
			} else {
				dto.setType(TypeEnum.LOCAL_DATE_TIME.getName());
			}
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
