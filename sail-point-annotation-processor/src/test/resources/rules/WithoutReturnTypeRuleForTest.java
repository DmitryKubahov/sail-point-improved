package rules;

import com.sailpoint.annotation.Rule;
import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Java rule for test generating without return type
 */
@Rule
public class WithoutReturnTypeRuleForTest implements JavaRuleExecutor {

    /**
     * List java doc
     */
    @Argument(name = "list", prompt = "prompt for list")
    private List<Application> testArgs;
    /**
     * Identity java doc
     */
    @Argument(name = "identity", prompt = "prompt for return reqester")
    private Identity requester;
    /**
     * Map java doc
     */
    @Argument(name = "map", prompt = "prompt for map")
    private Map mapps;

    /**
     * Contains all possible locales
     */
    @Override
    @Argument(name = "returnArgument", type = ArgumentType.RETURNS, prompt = "prompt for return argument")
    public List<Locale> execute(JavaRuleContext javaRuleContext) {
        return Collections.singletonList(Locale.getDefault());
    }
}
