package com.sailpoint.test.rule;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import sailpoint.object.Rule;
import sailpoint.object.Signature;
import sailpoint.server.SimulatedSailPointContext;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test generation of rule xml from {@link AllAttributesRuleGenerationTest#PATH_TO_ALL_ATTRIBUTES_RULE}
 */
@Slf4j
public class AllAttributesRuleGenerationTest extends AbstractRuleAnnotationProcessorTest {

    /**
     * Path to rule for current test
     */
    private static final String PATH_TO_ALL_ATTRIBUTES_RULE = "rules/AllAttributesRuleForTest.java";
    /**
     * Path to rule for current test
     */
    private static final String PATH_TO_ABSTRACT_ALL_ATTRIBUTES_RULE = "rules/AbstractAllAttributesRuleForTest.java";

    /**
     * Path to xml of current rule after generating
     */
    private static final String PATH_TO_GENERATED_RULE_XML = "Rule/Rule name - simple rule name.xml";

    /**
     * Return current rule file path
     *
     * @return rule path
     */
    @Override
    protected String getJavaClassFilePathName() {
        return PATH_TO_ALL_ATTRIBUTES_RULE;
    }

    /**
     * Override implementation of building compilation to compile 2 files
     *
     * @param compiler - compile instance
     * @return compilation instance from compile
     */
    @Override
    protected Compilation compile(Compiler compiler) {
        return compiler.compile(
                JavaFileObjects.forResource(getClass().getClassLoader().getResource(PATH_TO_ALL_ATTRIBUTES_RULE)),
                JavaFileObjects
                        .forResource(getClass().getClassLoader().getResource(PATH_TO_ABSTRACT_ALL_ATTRIBUTES_RULE)));
    }

    /**
     * Try to read rule from generated file and check all attributes
     */
    @Test
    public void checkAllAttributesRuleGenerator() throws IOException {
        Rule ruleExpected = buildExpectedRule();
        String xml = IOUtils.toString(getClass().getClassLoader().getResourceAsStream(PATH_TO_GENERATED_RULE_XML),
                StandardCharsets.UTF_8.name());
        assertNotNull("Xml of rule can not be null", xml);

        Rule rule = (Rule) XML_OBJECT_FACTORY.parseXml(new SimulatedSailPointContext(), xml, false);
        assertNotNull("Rule can not be null", rule);

        assertEquals("Rule type is not match", ruleExpected.getType(), rule.getType());
        assertEquals("Rule name is not match", ruleExpected.getName(), rule.getName());
        assertEquals("Language name is not match", ruleExpected.getLanguage(), rule.getLanguage());
        assertEquals("Rule java class (source) is not match", ruleExpected.getSource(), rule.getSource());

        assertEquals("Signature return type is not match", ruleExpected.getSignature().getReturnType(),
                rule.getSignature().getReturnType());

        assertEquals("Returns size is not match", ruleExpected.getSignature().getReturns().size(),
                rule.getSignature().getReturns().size());
        assertTrue("Returns contains unknown arguments",
                ruleExpected.getSignature().getReturns().containsAll(rule.getSignature().getReturns()));

        assertEquals("Inputs size is not match", ruleExpected.getSignature().getArguments().size(),
                rule.getSignature().getArguments().size());
        assertTrue("Inputs contains unknown arguments",
                ruleExpected.getSignature().getArguments().containsAll(rule.getSignature().getArguments()));
    }

    /**
     * Creates rule with all expected values
     *
     * @return expected rule
     */
    private Rule buildExpectedRule() {
        Rule expectedRule = new Rule();
        expectedRule.setName("Rule name - simple rule name");
        expectedRule.setType(Rule.Type.AccountGroupRefresh);
        expectedRule.setDescription("Simple java rule for test generation xml with all attributes");

        expectedRule.setLanguage("java");
        expectedRule.setSource("rules.AllAttributesRuleForTest");

        Signature signature = new Signature();
        signature.setReturnType("java.util.List");

        signature.setReturns(Arrays.asList(
                buildArgument("returnArgument", "java.util.Locale", true, "Contains default locale",
                        "prompt for return argument")));

        signature.setArguments(Arrays.asList(
                buildArgument("list", "sailpoint.object.Application", true, "List java doc", "prompt for list"),
                buildArgument("requester", "sailpoint.object.Identity", false, "Identity java doc",
                        "prompt for return reqester"),
                buildArgument("map", "java.util.Map", false, "Map java doc", "prompt for map")
        ));

        expectedRule.setSignature(signature);
        return expectedRule;

    }


}
