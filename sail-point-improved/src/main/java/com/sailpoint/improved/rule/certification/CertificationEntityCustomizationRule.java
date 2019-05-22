package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.CertificationContext;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Certification;
import sailpoint.object.CertificationEntity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Runs when a certification is generated. It allows the CertificationEntity to
 * be customized; for example, default values can be calculated for the custom fields. This rule is generally used
 * only when custom fields have been added to CertificationEntity for the installation. It runs for every
 * CertificationEntity in a certification.
 * <p>
 * NOTE: The {@link CertificationItemCustomizationRule} rule runs for each certifiable item attached at a
 * certificationEntity before that entity’s CertificationEntityCustomization rule runs.
 * <p>
 * Output:
 * None. The CertificationEntity object passed as parameter to the rule should be edited directly by the rule.
 */
@Slf4j
public abstract class CertificationEntityCustomizationRule
        extends AbstractJavaRuleExecutor<Object, CertificationEntityCustomizationRule.CertificationEntityCustomizationRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";
    /**
     * Name of certifiableEntity argument name
     */
    public static final String ARG_CERTIFIABLE_ENTITY = "certifiableEntity";
    /**
     * Name of entity argument name
     */
    public static final String ARG_ENTITY = "entity";
    /**
     * Name of certContext argument name
     */
    public static final String ARG_CERT_CONTEXT = "certContext";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationEntityCustomizationRule.ARG_CERTIFICATION,
            CertificationEntityCustomizationRule.ARG_CERTIFIABLE_ENTITY,
            CertificationEntityCustomizationRule.ARG_ENTITY,
            CertificationEntityCustomizationRule.ARG_CERT_CONTEXT,
            CertificationEntityCustomizationRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public CertificationEntityCustomizationRule() {
        super(Rule.Type.CertificationEntityCustomization.name(),
                CertificationEntityCustomizationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationEntityCustomizationRule.CertificationEntityCustomizationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationEntityCustomizationRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationEntityCustomizationRule.ARG_CERTIFICATION))
                .certifiableEntity(
                        (AbstractCertifiableEntity) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                                CertificationEntityCustomizationRule.ARG_CERTIFIABLE_ENTITY))
                .entity((CertificationEntity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationEntityCustomizationRule.ARG_ENTITY))
                .certContext((CertificationContext) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationEntityCustomizationRule.ARG_CERT_CONTEXT))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationEntityCustomizationRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - certifiableEntity
     * - entity
     * - certContext
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationEntityCustomizationRuleArguments {
        /**
         * Reference to the Certification to which the item is being added
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_CERTIFICATION)
        private final Certification certification;
        /**
         * Reference to the AbstractCertifiableEntity from which the certifiable was retrieved (Bundle, Identity, or AccountGroup object)
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_CERTIFIABLE_ENTITY)
        private final AbstractCertifiableEntity certifiableEntity;
        /**
         * Reference to the certificationEntity to be customized
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_ENTITY)
        private final CertificationEntity entity;
        /**
         * CertificationContext being used to build the certification (rarely used in a rule)
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_CERTIFIABLE_ENTITY)
        private final CertificationContext certContext;
        /**
         * Map in which any data can be stored; shared across multiple rules in the certification generation process
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
