package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Certification;
import sailpoint.object.CertificationItem;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Runs when a CertificationItem is refreshed and appears to be complete.
 * This rule determines whether the item is still missing any information. The rule returns a Boolean value: true if
 * the item is complete according to the rule’s evaluation or false if the rule found the item to be still in an
 * incomplete state. The system then marks the item accordingly.
 * <p>
 * This rule was created to permit custom logic around CertificationItem extended attributes. In practice these
 * extended attributes and this rule type are seldom used.
 * <p>
 * Output:
 * Returns true if item is deemed complete and false if it is not
 */
@Slf4j
public abstract class CertificationItemCompletionRule
        extends AbstractJavaRuleExecutor<Boolean, CertificationItemCompletionRule.CertificationItemCompletionRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";
    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM = "item";
    /**
     * Name of entity argument name
     */
    public static final String ARG_ENTITY = "entity";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationItemCompletionRule.ARG_CERTIFICATION,
            CertificationItemCompletionRule.ARG_ITEM,
            CertificationItemCompletionRule.ARG_ENTITY,
            CertificationItemCompletionRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public CertificationItemCompletionRule() {
        super(Rule.Type.CertificationItemCompletion.name(),
                CertificationItemCompletionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationItemCompletionRule.CertificationItemCompletionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationItemCompletionRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCompletionRule.ARG_CERTIFICATION))
                .item((CertificationItem) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCompletionRule.ARG_ITEM))
                .entity((CertificationItem) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCompletionRule.ARG_ENTITY))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationItemCompletionRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - item
     * - entity
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationItemCompletionRuleArguments {
        /**
         * A reference to the Certification object to which the Item (and entity) belong
         */
        @Argument(name = CertificationItemCompletionRule.ARG_CERTIFICATION)
        private final Certification certification;
        /**
         * A reference to the CertificationItem object being completed
         */
        @Argument(name = CertificationItemCompletionRule.ARG_ITEM)
        private final CertificationItem item;
        /**
         * A second reference to the CertificationItem object being completed;
         * exists as a synonym for item
         */
        @Argument(name = CertificationItemCompletionRule.ARG_ENTITY)
        private final CertificationItem entity;
        /**
         * Map in which any data can be stored;
         * shared across multiple rules run in the same completion process
         * (e.g. certificationItemCompletion and CertificationItemCompletion rules can share a state map)
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
