package rules;

import com.sailpoint.annotation.Rule;
import sailpoint.object.Application;
import sailpoint.object.Identity;

import java.util.List;
import java.util.Map;

/**
 * Java rule for test generating rule without arguments
 */
@Rule
public class WithoutArgumentsRuleForTest {

    /**
     * List java doc
     */
    private List<Application> testArgs;
    /**
     * Identity java doc
     */
    private Identity requester;
    /**
     * Map java doc
     */
    private Map mapps;
}
