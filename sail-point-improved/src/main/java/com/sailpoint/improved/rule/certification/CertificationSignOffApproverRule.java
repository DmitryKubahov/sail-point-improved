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
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Rule is used to specify one or more additional levels of approval for a
 * certification. When the certification is signed off, this rule runs (if one is specified for the certification) to
 * identify the next approver to whom the certification should be forwarded for review and approval. This rule
 * runs every time a certification is signed off, including second-level signoffs. As long as the rule returns an
 * Identity, the certification will be forwarded to that Identity for review and signoff; when it returns null, the
 * forwarding process terminates for the certification.
 * <p>
 * NOTE: If the logic in this rule could potentially reroute the certification to the same Identity who just signed off
 * on it, the rule must check for this condition and return null when the new certifier matches the existing one.
 * Otherwise, an endless loop could be created where the certification is repeatedly returned to the same certifier
 * for another signoff, and the certification would never successfully complete.
 * <p>
 * Output:
 * Map containing either an Identity or Identity name with
 * the key “identity” or “identityName”, respectively.
 * e.g.: “identity”, identityObject or “identityName”, “Adam.Kennedy”
 */
@Slf4j
public abstract class CertificationSignOffApproverRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, CertificationSignOffApproverRule.CertificationSignOffApproverRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";
    /**
     * Name of certifier argument name
     */
    public static final String ARG_CERTIFIER = "certifier";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationSignOffApproverRule.ARG_CERTIFICATION,
            CertificationSignOffApproverRule.ARG_CERTIFIER,
            CertificationSignOffApproverRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public CertificationSignOffApproverRule() {
        super(Rule.Type.CertificationSignOffApprover.name(),
                CertificationSignOffApproverRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationSignOffApproverRule.CertificationSignOffApproverRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationSignOffApproverRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationSignOffApproverRule.ARG_CERTIFICATION))
                .certifier((Identity) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationSignOffApproverRule.ARG_CERTIFIER))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationSignOffApproverRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - certifier
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationSignOffApproverRuleArguments {
        /**
         * A reference to the Certification object being closed
         */
        @Argument(name = CertificationSignOffApproverRule.ARG_CERTIFICATION)
        private final Certification certification;
        /**
         * Reference to the Identity who was assigned as the certifier for this certification
         */
        @Argument(name = CertificationSignOffApproverRule.ARG_CERTIFIER)
        private final Identity certifier;
        /**
         * A map of values that can be shared between rules; allows passing of data between rules
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
