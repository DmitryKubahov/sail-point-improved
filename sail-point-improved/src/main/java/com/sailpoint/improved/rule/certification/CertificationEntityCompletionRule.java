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
import sailpoint.tools.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Run when a CertificationEntity is refreshed and has been determined to
 * be otherwise complete (i.e. all certification items on the entity are complete). The certification refresh process
 * occurs when changes to an access review are saved by the user. This rule determines whether the entity is still
 * missing any information. For example, the entity may require a 'classification' value to be present in a custom
 * field to be complete. If errors are found, the error messages (either plain-text messages or keys that map to
 * messages in the message catalog) are added to a List and returned to the caller, which tells IdentityIQ to mark
 * the Entity as still incomplete.
 * <p>
 * This rule was created to permit custom logic around CertificationItem extended attributes. In practice these
 * extended attributes and this rule type are seldom used.
 * <p>
 * Output:
 * None. List of message objects or strings if errors were found (any contents in list mean that the Entity is not complete);
 * null if entity is complete
 * <T> - type of return elements:
 * - {@link Message}
 * - {@link String}
 */
@Slf4j
public abstract class CertificationEntityCompletionRule<T extends Object>
        extends AbstractJavaRuleExecutor<List<T>, CertificationEntityCompletionRule.CertificationEntityCompletionRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION_NAME = "certification";
    /**
     * Name of certificationEntity argument name
     */
    public static final String ARG_CERTIFICATION_ENTITY_NAME = "certificationEntity";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE_NAME = "state";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationEntityCompletionRule.ARG_CERTIFICATION_NAME,
            CertificationEntityCompletionRule.ARG_CERTIFICATION_ENTITY_NAME,
            CertificationEntityCompletionRule.ARG_STATE_NAME
    );

    /**
     * Default constructor
     */
    public CertificationEntityCompletionRule() {
        super(Rule.Type.CertificationEntityCompletion.name(),
                CertificationEntityCompletionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationEntityCompletionRule.CertificationEntityCompletionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationEntityCompletionRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationEntityCompletionRule.ARG_CERTIFICATION_NAME))
                .certificationEntity((CertificationEntity) JavaRuleExecutorUtil.getArgumentValueByName(javaRuleContext,
                        CertificationEntityCompletionRule.ARG_CERTIFICATION_ENTITY_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationEntityCompletionRule.ARG_STATE_NAME))
                .build();
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - certificationEntity
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationEntityCompletionRuleArguments {
        /**
         * A reference to the Certification object being refreshed
         */
        @Argument(name = CertificationEntityCompletionRule.ARG_CERTIFICATION_NAME)
        private final Certification certification;
        /**
         * A reference to the CertificationEntity object being refreshed
         */
        @Argument(name = CertificationEntityCompletionRule.ARG_CERTIFICATION_ENTITY_NAME)
        private final CertificationEntity certificationEntity;
        /**
         * Map in which any data can be stored;
         * shared across multiple rules run in the same completion process
         * (e.g. certificationItemCompletion and CertificationEntityCompletion rules can share a state map)
         */
        @Argument(name = CertificationEntityCustomizationRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
    }
}
