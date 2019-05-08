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
        extends AbstractJavaRuleExecutor<Object, CertificationItemCustomizationRule.CertificationItemCustomizationRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION_NAME = "certification";
    /**
     * Name of certifiable argument name
     */
    public static final String ARG_CERTIFIABLE_NAME = "certifiable";
    /**
     * Name of certifiableEntity argument name
     */
    public static final String ARG_CERTIFIABLE_ENTITY_NAME = "certifiableEntity";
    /**
     * Name of item argument name
     */
    public static final String ARG_ITEM_NAME = "item";
    /**
     * Name of certContext argument name
     */
    public static final String ARG_CERT_CONTEXT_NAME = "certContext";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE_NAME = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationItemCustomizationRule.ARG_CERTIFICATION_NAME,
            CertificationItemCustomizationRule.ARG_CERTIFIABLE_NAME,
            CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY_NAME,
            CertificationItemCustomizationRule.ARG_ITEM_NAME,
            CertificationItemCustomizationRule.ARG_CERT_CONTEXT_NAME,
            CertificationItemCustomizationRule.ARG_STATE_NAME
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
                        CertificationItemCustomizationRule.ARG_CERTIFICATION_NAME))
                .certifiable((Certifiable) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCustomizationRule.ARG_CERTIFIABLE_NAME))
                .certifiableEntity(
                        (AbstractCertifiableEntity) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                                CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY_NAME))
                .item((CertificationItem) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationItemCustomizationRule.ARG_ITEM_NAME))
                .certContext((CertificationContext) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationItemCustomizationRule.ARG_CERT_CONTEXT_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationItemCustomizationRule.ARG_STATE_NAME))
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
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFICATION_NAME)
        private final Certification certification;
        /**
         * Reference to the Certifiable item being created into a CertificationItem
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFIABLE_NAME)
        private final Certifiable certifiable;
        /**
         * Reference to the AbstractCertifiableEntity from which the certifiable was retrieved (Bundle, Identity, or AccountGroup object)
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY_NAME)
        private final AbstractCertifiableEntity certifiableEntity;
        /**
         * Reference to the certificationItem to be customized
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_ITEM_NAME)
        private final CertificationItem item;
        /**
         * CertificationContext being used to build the certification (rarely used in a rule)
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_CERTIFIABLE_ENTITY_NAME)
        private final CertificationContext certContext;
        /**
         * A Map that can be used to store and share data between executions of this rule during
         * a single certification generation process; rules executed in the same certification generation
         * share this state map, allowing data to be passed between them
         */
        @Argument(name = CertificationItemCustomizationRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
    }
}
