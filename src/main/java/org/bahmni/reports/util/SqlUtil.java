package org.bahmni.reports.util;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
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
            sb.append("\\'").append(escapeSql(item)).append("\\',");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String toCommaSeparatedSqlString(List<String> list) {
        if (CollectionUtils.isEmpty(list)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append("'").append(escapeSql(item)).append("',");
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
        } catch (SQLSyntaxErrorException e) {
            throw new InvalidConfigurationException("Column that you have configured in sortBy is either not present in output of the report or it is invalid column");
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }

        return null;
    }

    public static String escapeSql(String str) {
        if (str == null) {
            return null;
        }
        return StringUtils.replace(str, "'", "''");
    }
}

