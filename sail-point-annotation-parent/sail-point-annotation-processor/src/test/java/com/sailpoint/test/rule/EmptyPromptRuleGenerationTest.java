package com.sailpoint.test.rule;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sailpoint.object.Rule;
import sailpoint.server.SimulatedSailPointContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test generation of rule xml from {@link EmptyPromptRuleGenerationTest#PATH_TO_EMPTY_PROMPT_RULE}
 */
@Slf4j
public class EmptyPromptRuleGenerationTest extends AbstractRuleAnnotationProcessorTest {

    /**
     * Path to rule for current test
     */
    private static final String PATH_TO_EMPTY_PROMPT_RULE = "rules/EmptyPromptRuleForTest.java";

    /**
     * Path to xml of current rule after generating
     */
    private static final String PATH_TO_GENERATED_RULE_XML = "Rule/EmptyPromptRuleForTest.xml";

    /**
     * Return current rule file path
     *
     * @return rule path
     */
    @Override
    protected String getRuleFilePathName() {
        return PATH_TO_EMPTY_PROMPT_RULE;
    }

    /**
     * Try to read rule from generated file and check all attributes
     */
    @Test
    public void checkEmptyPromptRuleGenerator() throws IOException {
        String xml = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(PATH_TO_GENERATED_RULE_XML),
                StandardCharsets.UTF_8.name());
        assertNotNull("Xml of rule can not be null", xml);

        Rule rule = (Rule) XML_OBJECT_FACTORY.parseXml(new SimulatedSailPointContext(), xml, false);
        assertNotNull("Rule can not be null", rule);

        assertTrue("Prompt is not null",
                rule.getSignature().getArguments().stream().noneMatch(argument -> argument.getPrompt() != null));
    }
}
