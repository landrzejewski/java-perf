package pl.training.supernova.examples;

import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class Template {

    private static final String EXPRESSION_START = "\\$\\{";
    private static final String EXPRESSION_END = "}";
    private static final Pattern EXPRESSION = Pattern.compile("\\$\\{\\w+}");
    private static final String INVALID_VALUE = ".*\\W+.*";

    private final String textWithExpressions;

    public Template(String textWithExpressions) {
        this.textWithExpressions = textWithExpressions;
        validate();
    }

    private void validate() {
        if (getExpressions().map(MatchResult::group).collect(toSet()).size() != getExpressions().count()) {
            throw new IllegalArgumentException();
        }
    }

    public String evaluate(Map<String, String> data) {
        validateData(data);
        return substituteData(data);
    }

    private void validateData(Map<String, String> data) {
        if (isDataIncomplete(data) || isDataNotValid(data)) {
            throw new IllegalArgumentException();
        }
    }

    private boolean isDataIncomplete(Map<String, String> data) {
        return data.size() != getExpressions().count();
    }

    private boolean isDataNotValid(Map<String, String> data) {
        return data.values().stream().anyMatch(value -> value.matches(INVALID_VALUE));
    }

    private String substituteData(Map<String, String> data) {
        var result = textWithExpressions;
        for (Map.Entry<String, String> entry : data.entrySet()) {
            var expression = createExpression(entry.getKey());
            result = result.replaceAll(expression, entry.getValue());
        }
        return result;
    }

    private Stream<MatchResult> getExpressions() {
        return EXPRESSION.matcher(textWithExpressions).results();
    }

    private String createExpression(String key) {
        return EXPRESSION_START + key + EXPRESSION_END;
    }

}
