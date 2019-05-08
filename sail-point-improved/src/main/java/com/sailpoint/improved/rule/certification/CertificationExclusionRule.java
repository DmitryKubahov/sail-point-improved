package com.sailpoint.improved.rule.certification;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentsContainer;
import com.sailpoint.improved.rule.AbstractJavaRuleExecutor;
import com.sailpoint.improved.rule.connector.FileParsingRule;
import com.sailpoint.improved.rule.util.JavaRuleExecutorUtil;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import sailpoint.api.CertificationContext;
import sailpoint.object.AbstractCertifiableEntity;
import sailpoint.object.Certifiable;
import sailpoint.object.Certification;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Used to exclude specific items from a Certification based on an evaluation of the
 * entity being certified or the certifiable items that are part of the certification. The certificationEntity depends on
 * the type of certification being run and can be a bundle (role), account group, or Identity. CertificationItems are
 * dependent on the entity and the certification type. For example, for Identities, items can be roles or
 * entitlements; for account groups, they can be permissions or members. For roles, they are required/permitted
 * roles, entitlements, or members.
 * <p>
 * The rule is passed two lists: items and itemsToExclude. Items includes all CertificationItems belonging to the
 * CertificationEntity that are identified for inclusion in the certification. The rule must remove items from that list
 * to exclude them from the certification and must put them in the itemsToExclude list to make them appear in the
 * Exclusions list for the certification.
 * <p>
 * NOTE: This rule runs for each CertificationEntity identified by the certification specification, so complex
 * processing included in this rule can significantly impact the performance of certification generation. For
 * continuous certifications, this rule runs each time the certification returns to the CertificationRequired state and
 * the Refresh Continuous Certifications task runs.
 * <p>
 * Output:
 * An optional explanation describing why the entity’s items were excluded; this is shown on the Exclusions list for each item
 * excluded from the certification; if rule excludes items for different entities for different reasons, this can identify the applicable
 * exclusion conditions when the exclusion list is examined
 */
@Slf4j
public abstract class CertificationExclusionRule
        extends AbstractJavaRuleExecutor<String, CertificationExclusionRule.CertificationExclusionRuleArguments> {

    /**
     * Name of entity argument name
     */
    public static final String ARG_ENTITY_NAME = "entity";
    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION_NAME = "certification";
    /**
     * Name of certContext argument name
     */
    public static final String ARG_CERT_CONTEXT_NAME = "certContext";
    /**
     * Name of items argument name
     */
    public static final String ARG_ITEMS_NAME = "items";
    /**
     * Name of itemsToExclude argument name
     */
    public static final String ARG_ITEMS_TO_EXCLUDE_NAME = "itemsToExclude";
    /**
     * Name of state argument name
     */
    public static final String ARG_STATE_NAME = "state";
    /**
     * Name of identity argument name
     */
    public static final String ARG_IDENTITY_NAME = "identity";

    /**
     * None nulls arguments
     */
    public static final List<String> NONE_NULL_ARGUMENTS_NAME = Arrays.asList(
            CertificationExclusionRule.ARG_ENTITY_NAME,
            CertificationExclusionRule.ARG_CERTIFICATION_NAME,
            CertificationExclusionRule.ARG_CERT_CONTEXT_NAME,
            CertificationExclusionRule.ARG_ITEMS_NAME,
            CertificationExclusionRule.ARG_ITEMS_TO_EXCLUDE_NAME,
            CertificationExclusionRule.ARG_STATE_NAME,
            CertificationExclusionRule.ARG_IDENTITY_NAME
    );

    /**
     * Default constructor
     */
    public CertificationExclusionRule() {
        super(Rule.Type.CertificationExclusion.name(), CertificationExclusionRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationExclusionRule.CertificationExclusionRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationExclusionRuleArguments.builder()
                .entity((AbstractCertifiableEntity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationExclusionRule.ARG_ENTITY_NAME))
                .certification((Certification) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationExclusionRule.ARG_CERTIFICATION_NAME))
                .certContext((CertificationContext) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationExclusionRule.ARG_CERT_CONTEXT_NAME))
                .items((List<Certifiable>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationExclusionRule.ARG_ITEMS_NAME))
                .itemsToExclude((List<Certifiable>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationExclusionRule.ARG_ITEMS_TO_EXCLUDE_NAME))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationExclusionRule.ARG_STATE_NAME))
                .identity((AbstractCertifiableEntity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationExclusionRule.ARG_IDENTITY_NAME))
                .build();
    }

    /**
     * Arguments container for {@link FileParsingRule}. Contains:
     * - entity
     * - certification
     * - certContext
     * - items
     * - itemsToExclude
     * - state
     * - identity
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationExclusionRuleArguments {
        /**
         * The entity being considered for certification: a Bundle, Account Group, or Identity object
         */
        @Argument(name = CertificationExclusionRule.ARG_ENTITY_NAME)
        private final AbstractCertifiableEntity entity;
        /**
         * The current certification object being constructed
         */
        @Argument(name = CertificationExclusionRule.ARG_CERTIFICATION_NAME)
        private final Certification certification;
        /**
         * The CertificationContext interface used in generating this certification (rarely used within rules; the values accessible through
         * this are also available on the certification or certificationDefinition)
         */
        @Argument(name = CertificationExclusionRule.ARG_CERT_CONTEXT_NAME)
        private final CertificationContext certContext;
        /**
         * List of Certifiable items for a given identity; this rule must remove items from this list to prevent
         * them from being certified
         */
        @Argument(name = CertificationExclusionRule.ARG_ITEMS_NAME)
        private final List<Certifiable> items;
        /**
         * List of Certifiable items to be excluded from the certification; this rule must add items to this list
         * to have them included in the Exclusions list visible from the certification after it is generated
         */
        @Argument(name = CertificationExclusionRule.ARG_ITEMS_TO_EXCLUDE_NAME)
        private final List<Certifiable> itemsToExclude;
        /**
         * Map in which any data can be stored; shared across multiple rules in the certification generation process
         */
        @Argument(name = CertificationExclusionRule.ARG_STATE_NAME)
        private final Map<String, Object> state;
        /**
         * A second copy of the AbstractCertifiableEntity if it is an Identity object; this is passed in for
         * backward compatibility only; newly written rules should reference the entity argument instead
         */
        @Argument(name = CertificationExclusionRule.ARG_IDENTITY_NAME)
        private final AbstractCertifiableEntity identity;
    }
}
