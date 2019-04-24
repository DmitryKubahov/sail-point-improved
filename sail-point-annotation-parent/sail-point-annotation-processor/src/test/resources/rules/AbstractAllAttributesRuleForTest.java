package rules;

import com.sailpoint.annotation.common.Argument;
import com.sailpoint.annotation.common.ArgumentType;
import com.sailpoint.annotation.common.ArgumentsContainer;
import sailpoint.object.Application;
import sailpoint.object.Identity;
import sailpoint.object.JavaRuleContext;
import sailpoint.object.JavaRuleExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Abstract all attributes rule for test generation xml with all attributes
 */
public abstract class AbstractAllAttributesRuleForTest implements JavaRuleExecutor {

    /**
     * Contains default locale
     */
    @Override
    @Argument(isReturnsType = true, name = "returnArgument", type = ArgumentType.RETURNS, prompt = "prompt for return argument")
    public List<Locale> execute(JavaRuleContext javaRuleContext) {
        return Collections.singletonList(Locale.getDefault());
    }

    /**
     * Argumnt container
     */
    @ArgumentsContainer
    public static class TestRuleArguments {
        /**
         * List java doc
         */
        @Argument(name = "list", prompt = "prompt for list")
        private List<Application> testArgs;
        /**
         * Identity java doc
         */
        @Argument(prompt = "prompt for return reqester")
        private Identity requester;
        /**
         * Map java doc
         */
        @Argument(prompt = "prompt for map")
        private Map<String, Object> map;
    }
}
