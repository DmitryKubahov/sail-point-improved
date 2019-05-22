package com.sailpoint.improved.rule.notification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Certification;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.object.WorkItem;

import java.util.Arrays;
import java.util.List;

/**
 * Used to select a fallback owner for a certification work item to prevent
 * self-certification. This runs during certification creation when a predelegation rule in a certification is
 * attempting to assign an item to an owner that will result in self-certification, as well as any time an existing
 * certification work item is forwarded to a different user through automated forwarding (e.g. to the user
 * configured as Forwarding User on the User Preferences page or through execution of the inactive user work
 * item escalation rule or global work item forwarding rule). Of course, this does not apply for users who have been
 * allowed to self-certify (per the allowSelfCertification option in the system configuration).
 * <p>
 * Output:
 * Identity object who should be the new owner of the workItem
 */
@Slf4j
public abstract class FallbackWorkItemForwardRule
        extends AbstractJavaRuleExecutor<Identity, FallbackWorkItemForwardRule.FallbackWorkItemForwardRuleArguments> {

    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM = "item";
    /**
     * Name of owner argument name
     */
    public static final String ARG_OWNER = "owner";
    /**
     * Name of creator argument name
     */
    public static final String ARG_CREATOR = "creator";
    /**
     * Name of certifiers argument name
     */
    public static final String ARG_CERTIFIERS = "certifiers";
    /**
     * Name of certification name argument name
     */
    public static final String ARG_CERTIFICATION_NAME = "name";
    /**
     * Name of certification type argument name
     */
    public static final String ARG_CERTIFICATION_TYPE = "type";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            FallbackWorkItemForwardRule.ARG_ITEM,
            FallbackWorkItemForwardRule.ARG_OWNER,
            FallbackWorkItemForwardRule.ARG_CREATOR,
            FallbackWorkItemForwardRule.ARG_CERTIFIERS,
            FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE
    );

    /**
     * Default constructor
     */
    public FallbackWorkItemForwardRule() {
        super(Rule.Type.FallbackWorkItemForward.name(), FallbackWorkItemForwardRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected FallbackWorkItemForwardRule.FallbackWorkItemForwardRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return FallbackWorkItemForwardRuleArguments.builder()
                .item((WorkItem) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_ITEM))
                .owner((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_OWNER))
                .creator((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_CREATOR))
                .certifiers((List<String>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_CERTIFIERS))
                .certificationName((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                FallbackWorkItemForwardRule.ARG_CERTIFICATION_NAME))
                .certificationType((Certification.Type) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - item
     * - owner
     * - creator
     * - certifiers
     * - certificationName
     * - certificationType
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class FallbackWorkItemForwardRuleArguments {
        /**
         * Reference to the workItem (some workItem arguments may not yet be set)
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_ITEM)
        private final WorkItem item;
        /**
         * Reference to the Identity who currently owns the work item
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_OWNER)
        private final Identity owner;
        /**
         * Name of Identity who created the certification belonging to this workItem
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CREATOR)
        private final String creator;
        /**
         * List of certifier names for the certification belonging to the workItem
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CERTIFIERS)
        private final List<String> certifiers;
        /**
         * Name of the certification belonging to the workItem (may be null if not created yet)
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CERTIFICATION_NAME)
        private final String certificationName;
        /**
         * Type of the certification belonging to the workItem
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE)
        private final Certification.Type certificationType;
    }
}
