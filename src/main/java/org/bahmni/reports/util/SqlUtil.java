package org.bahmni.reports.util;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.StringUtils;
import org.quartz.impl.jdbcjobstore.InvalidConfigurationException;

import java.sql.Connection;
import java.sql.SQLSyntaxErrorException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class SqlUtil {

    public static String toEscapedCommaSeparatedSqlString(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("\\'").append(escapeSql(item, true, null)).append("\\',");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String toCommaSeparatedSqlString(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("'").append(escapeSql(item, true, null)).append("',");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static JasperReportBuilder executeReportWithStoredProc(JasperReportBuilder jasperReport, Connection connection, String formattedSql) throws SQLException {
        Statement stmt;

        try {
            stmt = connection.createStatement();
            boolean hasMoreResultSets = stmt.execute(formattedSql);
            while (hasMoreResultSets ||
                    stmt.getUpdateCount() != -1) { //if there are any more queries to be processed
                if (hasMoreResultSets) {
                    ResultSet rs = stmt.getResultSet();
                    if (rs.isBeforeFirst()) {
                        jasperReport.setDataSource(rs);
                        return jasperReport;
                    }
                }
                hasMoreResultSets = stmt.getMoreResults(); //true if it is a resultset
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return jasperReport;
    }
    public static ResultSet executeSqlWithStoredProc(Connection connection, String formattedSql) throws SQLException, InvalidConfigurationException {
        Statement stmt;
        ResultSet rs;
        try {
            stmt = connection.createStatement();
            boolean hasMoreResultSets = stmt.execute(formattedSql);
            while (hasMoreResultSets ||
                    stmt.getUpdateCount() != -1) { //if there are any more queries to be processed
                if (hasMoreResultSets) {
                    rs = stmt.getResultSet();
                    if (rs.isBeforeFirst()) {
                        return rs;
                    }
                }
                hasMoreResultSets = stmt.getMoreResults(); //true if it is a resultset
            }
        }catch (SQLSyntaxErrorException e){
            throw new InvalidConfigurationException("Column that you have configured in sortBy is either not present in output of the report or it is invalid column");
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return null;
    }

    public static String escapeSql(String str, boolean escapeDoubleQuotes, Character escapeChar) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        char escChar = '\\';
        if (escapeChar != null) {
            escChar = escapeChar.charValue();
        }
        String strToCheck = str.trim().replace("0x", "0X").replace("/*", "\\/*");
        StringBuilder sBuilder = new StringBuilder();
        int stringLength = strToCheck.length();
        for (int i = 0; i < stringLength; ++i) {
            char c = strToCheck.charAt(i);
            switch (c) {
                case 0:
                    sBuilder.append(escChar);
                    sBuilder.append('0');
                    break;
                case ';':
                    sBuilder.append(escChar);
                    sBuilder.append(';');
                    break;
                case '\n': /* Must be escaped for logs */
                    sBuilder.append(escChar);
                    sBuilder.append('n');
                    break;
                case '\r':
                    sBuilder.append(escChar);
                    sBuilder.append('r');
                    break;
                case '\\':
                    sBuilder.append(escChar);
                    sBuilder.append('\\');
                    break;
                case '\'':
                    sBuilder.append(escChar);
                    sBuilder.append('\'');
                    break;
                case '"':
                    if (escapeDoubleQuotes) {
                        sBuilder.append('\\');
                    }
                    sBuilder.append('"');
                    break;
                case '\032':
                    sBuilder.append(escChar);
                    sBuilder.append('Z');
                    break;
                default:
                    sBuilder.append(c);
            }
        }
        return sBuilder.toString();
    }
}

