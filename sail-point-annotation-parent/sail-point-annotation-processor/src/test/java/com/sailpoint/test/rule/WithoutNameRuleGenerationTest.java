package com.sailpoint.test.rule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sailpoint.object.Rule;
import sailpoint.server.SimulatedSailPointContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test generation of rule xml from {@link WithoutNameRuleGenerationTest#PATH_TO_WITHOUT_NAME_RULE}
 */
@Slf4j
public class WithoutNameRuleGenerationTest extends AbstractRuleAnnotationProcessorTest {

    /**
     * Path to rule for current test
     */
    private static final String PATH_TO_WITHOUT_NAME_RULE = "rules/WithoutNameRuleForTest.java";

    /**
     * Path to xml of current rule after generating
     */
    private static final String PATH_TO_GENERATED_RULE_XML = "Rule/WithoutNameRuleForTest.xml";

    /**
     * Expected rule name
     */
    private static final String EXPECTED_RULE_NAME = "WithoutNameRuleForTest";

    /**
     * Return current rule file path
     *
     * @return rule path
     */
    @Override
    protected String getJavaClassFilePathName() {
        return PATH_TO_WITHOUT_NAME_RULE;
    }

    /**
     * Try to read rule from generated file and check all attributes
     */
    @Test
    public void checkWithoutNameRuleGenerator() throws IOException {
        String xml = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(PATH_TO_GENERATED_RULE_XML),
                StandardCharsets.UTF_8.name());
        assertNotNull("Xml of rule can not be null", xml);

        Rule rule = (Rule) XML_OBJECT_FACTORY.parseXml(new SimulatedSailPointContext(), xml, false);
        assertNotNull("Rule can not be null", rule);

        assertEquals("Name is not match class simple name", EXPECTED_RULE_NAME, rule.getName());
    }
}
