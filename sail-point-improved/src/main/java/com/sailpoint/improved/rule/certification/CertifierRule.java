package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.GroupDefinition;
import sailpoint.object.GroupFactory;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Used only with Advanced certifications that are certifying members of GroupFactory-
 * generated Groups. It identifies the certifier for each Group. This rule runs once for each Group generated from
 * the specified GroupFactory; if the certification includes more than one GroupFactory, a separate rule can be
 * specified for each GroupFactory
 * <p>
 * Output:
 * Identifies the Certifier(s) for each group created from the groupFactory; can return
 * - an identity name, a CSV list of identity names
 * - an Identity object, a List of Identity objects
 * - a List of Identity names
 *
 * <T> - return type of rule:
 * - {@link String}
 * - {@link Identity}
 * - {@link List<String>}
 * - {@link List<Identity>}
 */
@Slf4j
public abstract class CertifierRule<T extends Object>
        extends AbstractJavaRuleExecutor<T, CertifierRule.CertifierRuleArguments> {

    /**
     * Name of factory argument name
     */
    public static final String ARG_FACTORY = "factory";
    /**
     * Name of group argument name
     */
    public static final String ARG_GROUP = "group";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertifierRule.ARG_FACTORY,
            CertifierRule.ARG_GROUP,
            CertifierRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public CertifierRule() {
        super(Rule.Type.Certifier.name(), CertifierRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertifierRule.CertifierRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertifierRuleArguments.builder()
                .factory((GroupFactory) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertifierRule.ARG_FACTORY))
                .group((GroupDefinition) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertifierRule.ARG_GROUP))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertifierRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - factory
     * - group
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertifierRuleArguments {
        /**
         * The groupFactory object that created the group(s) being certified
         */
        @Argument(name = CertifierRule.ARG_FACTORY)
        private final GroupFactory factory;
        /**
         * The group object whose members are being assigned a certifier by the rule
         */
        @Argument(name = CertifierRule.ARG_GROUP)
        private final GroupDefinition group;
        /**
         * Map in which any data can be stored; shared across multiple rules in the certification generation process
         */
        @Argument(name = CertifierRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
