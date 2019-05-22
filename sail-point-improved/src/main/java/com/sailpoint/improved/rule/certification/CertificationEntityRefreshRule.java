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
import sailpoint.object.CertificationEntity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;

/**
 * Runs when any certificationEntity is refreshed. Refresh of a certificationEntity
 * occurs when decisions made for that entity or any of its certificationItems is saved. The rule’s logic could, for
 * example, be used to copy a custom field value from one item to another or from the CertificationEntity down to
 * its certificationItems.
 * <p>
 * This rule was created to permit custom logic around CertificationItem extended attributes. In practice these
 * extended attributes and this rule type are seldom used.
 * <p>
 * Output:
 * None. The rule can directly modify the CertificationEntity object passed to it as a parameter.
 */
@Slf4j
public abstract class CertificationEntityRefreshRule
        extends AbstractJavaRuleExecutor<Object, CertificationEntityRefreshRule.CertificationEntityRefreshRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";
    /**
     * Name of certificationEntity argument name
     */
    public static final String ARG_CERTIFICATION_ENTITY = "certificationEntity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationEntityRefreshRule.ARG_CERTIFICATION,
            CertificationEntityRefreshRule.ARG_CERTIFICATION_ENTITY
    );

    /**
     * Default constructor
     */
    public CertificationEntityRefreshRule() {
        super(Rule.Type.CertificationEntityRefresh.name(),
                CertificationEntityRefreshRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationEntityRefreshRule.CertificationEntityRefreshRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationEntityRefreshRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationEntityRefreshRule.ARG_CERTIFICATION))
                .certificationEntity((CertificationEntity) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationEntityRefreshRule.ARG_CERTIFICATION_ENTITY))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - certificationEntity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationEntityRefreshRuleArguments {
        /**
         * Reference to the certification object to which this entity belongs
         */
        @Argument(name = CertificationEntityRefreshRule.ARG_CERTIFICATION)
        private final Certification certification;
        /**
         * Reference to the certificationEntity object that was refreshed, causing launch of this rule
         */
        @Argument(name = CertificationEntityRefreshRule.ARG_CERTIFICATION_ENTITY)
        private final CertificationEntity certificationEntity;
    }
}
