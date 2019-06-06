package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractNoneOutputJavaRuleExecutor;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.CertificationContext;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Certifiable;
import sailpoint.object.Certification;
import sailpoint.object.CertificationItem;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Run when a certification is generated. It allows the CertificationItem to
 * be customized; for example, default values can be calculated for the custom fields. This rule is generally used
 * only when custom fields have been added to CertificationItem for the installation.
 * <p>
 * NOTE: The CertificationItemCustomization rule runs for each certifiable item attached at a certificationEntity
 * before that entity’s {@link CertificationEntityCustomizationRule} runs.
 * <p>
 * Output:
 * None. The CertificationItem object passed as parameter to the rule should be edited directly by the rule.
 */
@Slf4j
public abstract class CertificationItemCustomizationRule
        extends AbstractNoneOutputJavaRuleExecutor<CertificationItemCustomizationRule.CertificationItemCustomizationRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";
    /**
     * Name of certifiable argument name
     */
    public static final String ARG_CERTIFIABLE = "certifiable";
    /**
     * Name of certifiableEntity argument name
     */
    public static final String ARG_CERTIFIABLE_ENTITY = "certifiableEntity";
    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM = "item";
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
            CertificationItemCustomizationRule.ARG_CERTIFICATION,
            CertificationItemCustomizationRule.ARG_CERTIFIABLE,
            CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY,
            CertificationItemCustomizationRule.ARG_ITEM,
            CertificationItemCustomizationRule.ARG_CERT_CONTEXT,
            CertificationItemCustomizationRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public CertificationItemCustomizationRule() {
        super(Rule.Type.CertificationItemCustomization.name(),
                CertificationItemCustomizationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationItemCustomizationRule.CertificationItemCustomizationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationItemCustomizationRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCustomizationRule.ARG_CERTIFICATION))
                .certifiable((Certifiable) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCustomizationRule.ARG_CERTIFIABLE))
                .certifiableEntity(
                        (AbstractCertifiableEntity) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                                CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY))
                .item((CertificationItem) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationItemCustomizationRule.ARG_ITEM))
                .certContext((CertificationContext) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCustomizationRule.ARG_CERT_CONTEXT))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationItemCustomizationRule.ARG_STATE))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - certifiable
     * - certifiableEntity
     * - item
     * - certContext
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationItemCustomizationRuleArguments {
        /**
         * Reference to the Certification to which the item is being added
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFICATION)
        private final Certification certification;
        /**
         * Reference to the Certifiable item being created into a CertificationItem
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFIABLE)
        private final Certifiable certifiable;
        /**
         * Reference to the AbstractCertifiableEntity from which the certifiable was retrieved (Bundle, Identity, or AccountGroup object)
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY)
        private final AbstractCertifiableEntity certifiableEntity;
        /**
         * Reference to the certificationItem to be customized
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_ITEM)
        private final CertificationItem item;
        /**
         * CertificationContext being used to build the certification (rarely used in a rule)
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY)
        private final CertificationContext certContext;
        /**
         * A Map that can be used to store and share data between executions of this rule during
         * a single certification generation process; rules executed in the same certification generation
         * share this state map, allowing data to be passed between them
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
