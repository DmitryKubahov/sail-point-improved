package rules;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import sailpoint.object.Application;
import sailpoint.object.Identity;

import java.util.List;
import java.util.Map;

/**
 * Java rule for test handling empty prompt
 */
@Rule
public class EmptyPromptRuleForTest {

    /**
     * List java doc
     */
    @Argument(name = "list")
    private List<Application> testArgs;
    /**
     * Identity java doc
     */
    @Argument(name = "identity", prompt = "")
    private Identity requester;
    /**
     * Map java doc
     */
    @Argument(name = "map", prompt = "               ")
    private Map mapps;

}
