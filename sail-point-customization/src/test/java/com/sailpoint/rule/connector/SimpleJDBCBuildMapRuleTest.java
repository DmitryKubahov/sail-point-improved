package com.sailpoint.rule.connector;

import com.sailpoint.improved.rule.connector.JDBCBuildMapRule;
import mockit.Mock;
import mockit.MockUp;
import org.junit.Before;
import org.junit.Test;
import sailpoint.api.SailPointContext;
import sailpoint.connector.JDBCConnector;
import sailpoint.object.Application;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.Schema;
import sailpoint.tools.GeneralException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Dummy test for {@link SimpleJDBCBuildMapRule}
 */
public class SimpleJDBCBuildMapRuleTest {

    /**
     * Instance of {@link SimpleJDBCBuildMapRule} to test
     */
    private SimpleJDBCBuildMapRule jdbcBuildMapRule;

    /**
     * Instance of sailpoint context
     */
    private SailPointContext sailPointContext;

    /**
     * Default map of attributes
     */
    private Map<String, Object> defaultMap;

    /**
     * Initialize:
     * - jdbc build map rule instance for testing
     * - mock of sail point context
     * - default map and mock {@link JDBCConnector#buildMapFromResultSet}
     */
    @Before
    public void init() {
        this.jdbcBuildMapRule = new SimpleJDBCBuildMapRule();
        this.sailPointContext = mock(SailPointContext.class);

        this.defaultMap = new HashMap<>();
        new MockUp<JDBCConnector>() {
            @Mock
            public Map<String, Object> buildMapFromResultSet(ResultSet result) {
                return defaultMap;
            }
        };
    }

    /**
     * Test of converting {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} from string to boolean
     * Input
     * - result default map with {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} false value
     * Output
     * - new map with {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} boolean value
     * Expectation:
     * - false value
     */
    @Test
    public void parseFalseValue() throws GeneralException {
        addActiveStatusToDefaultMap(false);

        Map<String, Object> result = this.jdbcBuildMapRule
                .execute(new JavaRuleContext(sailPointContext, buildJDBCRuleArguments()));
        assertNotNull("Result of rule execution must be not null", result);
        assertFalse("Active status must be false",
                (Boolean) defaultMap.get(SimpleJDBCBuildMapRule.ATTR_ACTIVE_STATUS_NAME));
    }

    /**
     * Test of converting {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} from string to boolean
     * Input
     * - result default map with {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} true value
     * Output
     * - new map with {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} boolean value
     * Expectation:
     * - true value
     */
    @Test
    public void parseTrueValue() throws GeneralException {
        addActiveStatusToDefaultMap(true);
        Map<String, Object> result = this.jdbcBuildMapRule
                .execute(new JavaRuleContext(sailPointContext, buildJDBCRuleArguments()));
        assertNotNull("Result of rule execution must be not null", result);
        assertTrue("Active status must be true",
                (Boolean) defaultMap.get(SimpleJDBCBuildMapRule.ATTR_ACTIVE_STATUS_NAME));
    }

    /**
     * Test of converting {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} from string to boolean
     * Input
     * - result default map with {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} NULL value
     * Output
     * - new map with {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} boolean value
     * Expectation:
     * - false value
     */
    @Test
    public void parseNullValue() throws GeneralException {
        this.defaultMap.clear();
        Map<String, Object> result = this.jdbcBuildMapRule
                .execute(new JavaRuleContext(sailPointContext, buildJDBCRuleArguments()));
        assertNotNull("Result of rule execution must be not null", result);
        assertFalse("Active status must be false with NULL value in default map",
                (Boolean) defaultMap.get(SimpleJDBCBuildMapRule.ATTR_ACTIVE_STATUS_NAME));
    }


    /**
     * Generates arguments for jdbc rule
     *
     * @return arguments map
     */
    private Map<String, Object> buildJDBCRuleArguments() {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(JDBCBuildMapRule.ARG_APPLICATION, mock(Application.class));
        arguments.put(JDBCBuildMapRule.ARG_SCHEMA, mock(Schema.class));
        arguments.put(JDBCBuildMapRule.ARG_STATE, Collections.emptyMap());
        arguments.put(JDBCBuildMapRule.ARG_CONNECTION, mock(Connection.class));
        arguments.put(JDBCBuildMapRule.ARG_RESULT_SET, mock(ResultSet.class));
        return arguments;
    }

    /**
     * Add to default map passed {@link SimpleJDBCBuildMapRule#ATTR_ACTIVE_STATUS_NAME} string value
     *
     * @param status - boolean value of status
     */
    private void addActiveStatusToDefaultMap(boolean status) {
        defaultMap.put(SimpleJDBCBuildMapRule.ATTR_ACTIVE_STATUS_NAME,
                status
                        ? SimpleJDBCBuildMapRule.TRUE_ACTIVE_STATUS_VALUE
                        : UUID.randomUUID().toString()
        );
    }

}
