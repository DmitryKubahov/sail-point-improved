package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractNoneOutputJavaRuleExecutor;
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

/**
 * Allow custom processing to occur at the beginning of any new certification
 * phase. They are connected to the certification definition as the Period Enter Rule for any certification phase
 * (e.g. Active Period Enter Rule, Challenge Period Enter Rule, etc.). Continuous certifications provide the rules
 * with both the Certification and the CertificationItem, since individual items are phased separately for continuous
 * certifications. Normal certifications only provide the Certification, since the whole certification is phased as a
 * unit.
 * CertificationPhaseChange rules are commonly used for actions like:
 * •  creating a data snapshot to send to an external system sending an update or report to a certification monitoring team
 * •  (Active Period Enter Rule) reporting on how long it took a certification to generate by comparing rule-fire time to certification start timestamp)
 * •  (Active Period Enter Rule) pre-deciding certain line items in the certification (can be overridden during review)
 * •  (Challenge Period Enter Rule) emailing managers that they should be expecting challenges to revocations
 * <p>
 * Output:
 * None. This rule is expected to act directly on the certification or certificationItem passed to the rule.
 */
@Slf4j
public abstract class CertificationPhaseChangeRule
        extends AbstractNoneOutputJavaRuleExecutor<CertificationPhaseChangeRule.CertificationPhaseChangeRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";
    /**
     * Name of certificationItem argument name
     */
    public static final String ARG_CERTIFICATION_ITEM = "certificationItem";
    /**
     * Name of previousPhase argument name
     */
    public static final String ARG_PREVIOUS_PHASE = "previousPhase";
    /**
     * Name of nextPhase argument name
     */
    public static final String ARG_NEXT_PHASE = "nextPhase";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationPhaseChangeRule.ARG_CERTIFICATION,
            CertificationPhaseChangeRule.ARG_CERTIFICATION_ITEM,
            CertificationPhaseChangeRule.ARG_NEXT_PHASE
    );

    /**
     * Default constructor
     */
    public CertificationPhaseChangeRule() {
        super(Rule.Type.CertificationPhaseChange.name(),
                CertificationPhaseChangeRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationPhaseChangeRule.CertificationPhaseChangeRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationPhaseChangeRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationPhaseChangeRule.ARG_CERTIFICATION))
                .certificationItem((CertificationItem) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationPhaseChangeRule.ARG_CERTIFICATION_ITEM))
                .previousPhase((Certification.Phase) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationPhaseChangeRule.ARG_PREVIOUS_PHASE))
                .nextPhase((Certification.Phase) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationPhaseChangeRule.ARG_NEXT_PHASE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - certificationItem
     * - previousPhase
     * - nextPhase
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationPhaseChangeRuleArguments {
        /**
         * Certification object undergoing phase transition
         */
        @Argument(name = CertificationPhaseChangeRule.ARG_CERTIFICATION)
        private final Certification certification;
        /**
         * CertificationItem undergoing phase transition; only passed in for transitions of continuous certifications,
         * where certificationItems are phased individually
         */
        @Argument(name = CertificationPhaseChangeRule.ARG_CERTIFICATION_ITEM)
        private final CertificationItem certificationItem;
        /**
         * Phase being exited (may be null)
         */
        @Argument(name = CertificationPhaseChangeRule.ARG_PREVIOUS_PHASE)
        private final Certification.Phase previousPhase;
        /**
         * Phase to which the certification is being transitioned
         */
        @Argument(name = CertificationPhaseChangeRule.ARG_NEXT_PHASE)
        private final Certification.Phase nextPhase;
    }
}
