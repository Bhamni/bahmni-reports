package org.bahmni.reports.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PatientAttributesHelperTest {

    private String sqlWithCasteAndEducation = "select person_attribute.person_id,GROUP_CONCAT(DISTINCT (IF(person_attribute_type.name = \"caste\", IFNULL(person_attribute_cn.name, person_attribute.value), NULL))) as \"caste\",GROUP_CONCAT(DISTINCT (IF(person_attribute_type.name = \"education\", IFNULL(person_attribute_cn.name, person_attribute.value), NULL))) as \"education\"\n" +
        "from person_attribute\n" +
        "INNER JOIN person_attribute_type ON person_attribute_type.person_attribute_type_id = person_attribute.person_attribute_type_id\n" +
        "LEFT JOIN concept_name person_attribute_cn ON person_attribute.value = person_attribute_cn.concept_id AND person_attribute_cn.concept_name_type = \"FULLY_SPECIFIED\"\n" +
        "WHERE person_attribute_type.name IN (\"caste\", \"education\")\n" +
        "GROUP BY person_id\n";

    private String sqlWithCaste = "select person_attribute.person_id,GROUP_CONCAT(DISTINCT (IF(person_attribute_type.name = \"caste\", IFNULL(person_attribute_cn.name, person_attribute.value), NULL))) as \"caste\"\n" +
            "from person_attribute\n" +
            "INNER JOIN person_attribute_type ON person_attribute_type.person_attribute_type_id = person_attribute.person_attribute_type_id\n" +
            "LEFT JOIN concept_name person_attribute_cn ON person_attribute.value = person_attribute_cn.concept_id AND person_attribute_cn.concept_name_type = \"FULLY_SPECIFIED\"\n" +
            "WHERE person_attribute_type.name IN (\"caste\")\n" +
            "GROUP BY person_id\n";

    private String patientAttributes = "caste";
    @Test
    public void ensureTwoPatientAttributesAreProperlyConstructed(){
        PatientAttributesHelper helper = new PatientAttributesHelper(Arrays.asList(patientAttributes, "education"));
        Assert.assertEquals("caste,education", helper.getFromClause());
        Assert.assertEquals(normalizeWhitespace(sqlWithCasteAndEducation), normalizeWhitespace(helper.getSql()));
    }

    @Test
    public void ensureOnePatientAttributeIsProperlyConstructed(){
        PatientAttributesHelper helper = new PatientAttributesHelper(Arrays.asList(patientAttributes));
        Assert.assertEquals(patientAttributes, helper.getFromClause());
        Assert.assertEquals(normalizeWhitespace(sqlWithCaste), normalizeWhitespace(helper.getSql()));
    }

    private String normalizeWhitespace(String str) {
        return str.replaceAll("\\s+", " ").trim();
    }
}
