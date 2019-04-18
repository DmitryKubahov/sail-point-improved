package rules;

import com.sailpoint.annotation.Rule;

/**
 * Simple java rule for test generation xml with all attributes
 */
@Rule(value = "Rule name - simple rule name", type = sailpoint.object.Rule.Type.AccountGroupRefresh)
public class AllAttributesRuleForTest extends AbstractAllAttributesRuleForTest {

    /**
     * Singleton instance of performance rule
     */
    private static final AllAttributesRuleForTest INSTANCE = new AllAttributesRuleForTest();

    /**
     * Get singleton instance
     *
     * @return singleton instance of current rule
     */
    public static AllAttributesRuleForTest getInstance() {
        return AllAttributesRuleForTest.INSTANCE;
    }


}
