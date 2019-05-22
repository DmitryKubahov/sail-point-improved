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
import sailpoint.object.Certification;
import sailpoint.object.CertificationEntity;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Rule;
import sailpoint.tools.Util;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Runs during certification generation. It runs once for every CertificationEntity
 * to determine whether the entity should be pre-delegated to a different certifier. This rule can also be used to
 * reassign entities to a different certifier. The difference between reassignment and delegation is that reassigned
 * certifications do not return to the original certifier for review and approval when the assignee has completed
 * signoff and delegated items do.
 * <p>
 * NOTE: This rule runs for each entity identified by the certification specification, so complex processing included
 * in this rule can significantly impact the performance of certification generation. For continuous certifications,
 * this rule runs each time the certification returns to the CertificationRequired state and the Refresh Continuous
 * Certifications task runs.
 * <p>
 * Output:
 * A map of values including the following entries:
 * • recipient: Identity object to whom the certificationEntity should be delegated (recipient or recipientName must be specified)
 * • recipientName: (string) name of the recipient identity (alternative to recipient)
 * • description: (string) description for delegation (if not provided, is generated as “Certify[CertificationEntityName]”)
 * • comments: (string) comments for the recipient of the delegation/reassignment
 * • certificationName: (String) name of the new certification to which this entity is being reassigned (only needed for reassignment, not delegation)
 * • reassign: (Boolean) flag indicating whether this is a reassignment or delegation (true=>reassignment)
 */
@Slf4j
public abstract class CertificationPreDelegationRule
        extends AbstractJavaRuleExecutor<Map<String, Object>, CertificationPreDelegationRule.CertificationPreDelegationRuleArguments> {

    /**
     * Name of certification argument name
     */
    public static final String ARG_CERTIFICATION = "certification";
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
            CertificationPreDelegationRule.ARG_CERTIFICATION,
            CertificationPreDelegationRule.ARG_ENTITY,
            CertificationPreDelegationRule.ARG_CERT_CONTEXT,
            CertificationPreDelegationRule.ARG_STATE
    );

    /**
     * Default constructor
     */
    public CertificationPreDelegationRule() {
        super(Rule.Type.CertificationPreDelegation.name(), CertificationPreDelegationRule.NONE_NULL_ARGUMENTS_NAME);
    }

    /**
     * Build arguments container for current rule
     *
     * @param javaRuleContext - current rule context
     * @return argument container instance
     */
    @Override
    protected CertificationPreDelegationRule.CertificationPreDelegationRuleArguments buildContainerArguments(
            @NonNull JavaRuleContext javaRuleContext) {
        return CertificationPreDelegationRuleArguments.builder()
                .certification((Certification) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationPreDelegationRule.ARG_CERTIFICATION))
                .entity((CertificationEntity) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationPreDelegationRule.ARG_ENTITY))
                .certContext((CertificationContext) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationPreDelegationRule.ARG_CERT_CONTEXT))
                .state((Map<String, Object>) JavaRuleExecutorUtil
                        .getArgumentValueByName(javaRuleContext, CertificationPreDelegationRule.ARG_STATE))
                .build();
    }

    /**
     * Output parameters name
     */
    public enum OutputArguments {
        /**
         * Identity object to whom the certificationEntity should be delegated (recipient or recipientName must be specified)
         */
        RECIPIENT("recipient"),
        /**
         * Identity object to whom the certificationEntity should be delegated (recipient or recipientName must be specified)
         */
        RECIPIENT_NAME("recipientName"),
        /**
         * Description for delegation (if not provided, is generated as “Certify[CertificationEntityName]”)
         */
        DESCRIPTION("description"),
        /**
         * Comments for the recipient of the delegation/reassignment
         */
        COMMENTS("comments"),
        /**
         * Name of the new certification to which this entity is being reassigned (only needed for reassignment, not delegation)
         */
        CERTIFICATION_NAME("certificationName"),
        /**
         * Flag indicating whether this is a reassignment or delegation (true=>reassignment)
         */
        REASSIGN("reassign");

        private final String keyValue;

        /**
         * Constructor with parameters
         *
         * @param keyValue - key value for output map
         */
        OutputArguments(String keyValue) {
            this.keyValue = keyValue;
        }

        /**
         * Get current key value for output map
         *
         * @return key value
         */
        public String getKeyValue() {
            return keyValue;
        }
    }

    /**
     * Result rule builder
     */
    @Data
    @Builder
    public static final class RuleResult {
        /**
         * Identity object to whom the certificationEntity should be delegated (recipient or recipientName must be specified)
         */
        private final Identity recipient;
        /**
         * Identity object to whom the certificationEntity should be delegated (recipient or recipientName must be specified)
         */
        private final String recipientName;
        /**
         * Description for delegation (if not provided, is generated as “Certify[CertificationEntityName]”)
         */
        private final String description;
        /**
         * Comments for the recipient of the delegation/reassignment
         */
        private final String comments;
        /**
         * Name of the new certification to which this entity is being reassigned (only needed for reassignment, not delegation)
         */
        private final String certificationName;
        /**
         * Flag indicating whether this is a reassignment or delegation (true=>reassignment)
         */
        private final Boolean reassign;

        /**
         * Convert result to map
         *
         * @return map of values
         */
        public Map<String, Object> toMap() {
            Map<String, Object> result = new HashMap<>();
            addToResult(result, OutputArguments.RECIPIENT.keyValue, recipient);
            addToResult(result, OutputArguments.RECIPIENT_NAME.keyValue, recipientName);
            addToResult(result, OutputArguments.DESCRIPTION.keyValue, description);
            addToResult(result, OutputArguments.COMMENTS.keyValue, comments);
            addToResult(result, OutputArguments.CERTIFICATION_NAME.keyValue, certificationName);
            addToResult(result, OutputArguments.REASSIGN.keyValue, reassign);
            return Util.isEmpty(result) ? Collections.emptyMap() : result;
        }

        /**
         * If value is not null - add to result map with key
         *
         * @param result - result map
         * @param key    - key value
         * @param value  - value to add
         */
        private void addToResult(Map<String, Object> result, String key, Object value) {
            Optional.ofNullable(value).ifPresent(object -> result.put(key, object));
        }
    }

    /**
     * Arguments container for current rule. Contains:
     * - certification
     * - entity
     * - certContext
     * - state
     */
    @Data
    @Builder
    @ArgumentsContainer
    public static class CertificationPreDelegationRuleArguments {
        /**
         * Reference to the Certification object being created
         */
        @Argument(name = CertificationPreDelegationRule.ARG_CERTIFICATION)
        private final Certification certification;
        /**
         * Reference to the CertificationEntity object being considered for predelegation
         */
        @Argument(name = CertificationPreDelegationRule.ARG_ENTITY)
        private final CertificationEntity entity;
        /**
         * Reference to the CertificationContext interface being  used to create this certification (rarely used within
         * rules; the values accessible through this are also available on the certification or certificationDefinition)
         */
        @Argument(name = CertificationPreDelegationRule.ARG_CERT_CONTEXT)
        private final CertificationContext certContext;
        /**
         * Map in which any data can be stored; shared across multiple rules in the certification generation process
         */
        @Argument(name = CertificationPreDelegationRule.ARG_STATE)
        private final Map<String, Object> state;
    }
}
