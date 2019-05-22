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
import sailpoint.object.Workflow;

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
    public static final String ARG_ITEM_NAME = "item";
    /**
     * Name of owner argument name
     */
    public static final String ARG_OWNER_NAME = "owner";
    /**
     * Name of creator argument name
     */
    public static final String ARG_CREATOR_NAME = "creator";
    /**
     * Name of certifiers argument name
     */
    public static final String ARG_CERTIFIERS_NAME = "certifiers";
    /**
     * Name of name argument name
     */
    public static final String ARG_CERTIFICATION_NAME = "name";
    /**
     * Name of type argument name
     */
    public static final String ARG_CERTIFICATION_TYPE_NAME = "type";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            FallbackWorkItemForwardRule.ARG_ITEM_NAME,
            FallbackWorkItemForwardRule.ARG_OWNER_NAME,
            FallbackWorkItemForwardRule.ARG_CREATOR_NAME,
            FallbackWorkItemForwardRule.ARG_CERTIFIERS_NAME,
            FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE_NAME
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
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_ITEM_NAME))
                .owner((Identity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_OWNER_NAME))
                .creator((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_CREATOR_NAME))
                .certifiers((List<String>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_CERTIFIERS_NAME))
                .certificationName((String) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext,
                                FallbackWorkItemForwardRule.ARG_CERTIFICATION_NAME))
                .certificationType((Certification.Type) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE_NAME))
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
        @Argument(name = FallbackWorkItemForwardRule.ARG_ITEM_NAME)
        private final WorkItem item;
        /**
         * Reference to the Identity who currently owns the work item
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_OWNER_NAME)
        private final Identity owner;
        /**
         * Name of Identity who created the certification belonging to this workItem
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CREATOR_NAME)
        private final String creator;
        /**
         * List of certifier names for the certification belonging to the workItem
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CERTIFIERS_NAME)
        private final List<String> certifiers;
        /**
         * Name of the certification belonging to the workItem (may be null if not created yet)
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CERTIFICATION_NAME)
        private final String certificationName;
        /**
         * Type of the certification belonging to the workItem
         */
        @Argument(name = FallbackWorkItemForwardRule.ARG_CERTIFICATION_TYPE_NAME)
        private final Certification.Type certificationType;
    }
}
