package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.AbstractNoneOutputJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.object.Certification;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Can be used to apply custom logic to certifications that have not been
 * finished by a certifier when the automatic closing date arrives (automatic closing date is configurable based on
 * certification end date). The perform maintenance task is responsible for automatically closing certifications for
 * which automatic closing has been enabled. Each certification set for automatic closing on or before the task’s
 * run date is identified and its automatic closing rule is run. Then the remaining auto-closing specifications are
 * applied to any of its items still in an incomplete or unfinished state.
 * <p>
 * Output:
 * None; the rule should update the certification and its entities/items directly (or it may perform actions
 * outside the flow of the certification process, such as sending an email notice to someone about the incomplete
 * items).
 */
@Slf4j
public abstract class CertificationAutomaticClosingRule
        extends AbstractNoneOutputJavaRuleExecutor<CertificationAutomaticClosingRule.CertificationAutomaticClosingRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationAutomaticClosingRule.ARG_CERTIFICATION
    );

    /**
     * Default constructor
     */
    public CertificationAutomaticClosingRule() {
        super(Rule.Type.CertificationAutomaticClosing.name(),
                CertificationAutomaticClosingRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationAutomaticClosingRule.CertificationAutomaticClosingRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationAutomaticClosingRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationAutomaticClosingRule.ARG_CERTIFICATION))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationAutomaticClosingRuleArguments {
        /**
         * A reference to the Certification object being closed
         */
        @Argument(name = CertificationAutomaticClosingRule.ARG_CERTIFICATION)
        private final Certification certification;
    }
}
