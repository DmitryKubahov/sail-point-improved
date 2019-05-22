package com.sailpoint.improved.rule.owner;

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

/**
 * Used to assign group owners for the groups created from a GroupFactory.
 * <p>
 * Output:
 * Identity object of the Identity assigned as the group owner
 */
@Slf4j
public abstract class GroupOwnerRule
        extends AbstractJavaRuleExecutor<Identity, GroupOwnerRule.GroupOwnerRuleArguments> {

    /**
     * Name of factory argument name
     */
    public static final String ARG_FACTORY = "factory";
    /**
     * Name of group argument name
     */
    public static final String ARG_GROUP = "group";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            GroupOwnerRule.ARG_FACTORY,
            GroupOwnerRule.ARG_GROUP
    );

    /**
     * Default constructor
     */
    public GroupOwnerRule() {
        super(Rule.Type.GroupOwner.name(), GroupOwnerRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected GroupOwnerRule.GroupOwnerRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return GroupOwnerRuleArguments.builder()
                .factory((GroupFactory) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, GroupOwnerRule.ARG_FACTORY))
                .group((GroupDefinition) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, GroupOwnerRule.ARG_GROUP))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - factory
     * - group
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class GroupOwnerRuleArguments {
        /**
         * Reference to the groupFactory object from which the groups are generated
         */
        @Argument(name = GroupOwnerRule.ARG_FACTORY)
        private final GroupFactory factory;
        /**
         * Reference to a single GroupDefinition from the factory
         */
        @Argument(name = GroupOwnerRule.ARG_GROUP)
        private final GroupDefinition group;
    }
}
