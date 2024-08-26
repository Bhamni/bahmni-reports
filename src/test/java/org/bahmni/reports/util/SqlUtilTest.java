package org.bahmni.reports.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SqlUtilTest {

    @Test
    public void shouldEscapeSQLInjection() {
        assertEquals("0X3", SqlUtil.escapeSql("0x3", true, null));
        assertEquals("DROP sampletable\\;--", SqlUtil.escapeSql("DROP sampletable;--", true, null));
        assertEquals("admin\\'--", SqlUtil.escapeSql("admin'--", true, null));
        assertEquals("admin\\'\\\\/*", SqlUtil.escapeSql("admin'/*", true, null));
    }
}
