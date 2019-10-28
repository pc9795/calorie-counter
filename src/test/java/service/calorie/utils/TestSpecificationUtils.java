package service.calorie.utils;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 28-10-2019 22:05
 * Purpose: Test
 **/
public class TestSpecificationUtils {

    @Test
    public void testNextTokenEmpty() {
        assert new SpecificationUtils.SearchQueryTokenizer("     ").nextToken() == null;
    }

    @Test
    public void testNextToken() {
        String query = "(date eq '2016-05-01') AND ((number_of_calories gt 20) OR (number_of_calories lt 10))";
        SpecificationUtils.SearchQueryTokenizer tokenizer = new SpecificationUtils.SearchQueryTokenizer(query);
        List<String> tokens = new ArrayList<>();
        String token = tokenizer.nextToken();

        while (token != null) {
            tokens.add(token);
            token = tokenizer.nextToken();
        }
        List<String> expected = Arrays.asList("(", "date", "eq", "'2016-05-01'", ")", "AND", "(", "(",
                "number_of_calories", "gt", "20", ")", "OR", "(", "number_of_calories", "lt", "10", ")", ")");
        assert tokens.equals(expected);
    }

    @Test(expected = InvocationTargetException.class)
    public void testInfixToPostfixUnbalancedBraces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = SpecificationUtils.class.getDeclaredMethod("infixToPostFix", String.class);
        m.setAccessible(true);
        m.invoke(null, "ab)");
    }

    @Test(expected = InvocationTargetException.class)
    public void testInfixToPostfixUnbalancedBraces2() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = SpecificationUtils.class.getDeclaredMethod("infixToPostFix", String.class);
        m.setAccessible(true);
        m.invoke(null, "(ab");
    }

    @Test
    public void testInfixToPostfix() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method m = SpecificationUtils.class.getDeclaredMethod("infixToPostFix", String.class);
        m.setAccessible(true);
        String query = "(date eq '2016-05-01') AND ((number_of_calories gt 20) OR (number_of_calories lt 10))";
        List<String> expected = Arrays.asList("date", "'2016-05-01'", "eq", "number_of_calories", "20", "gt",
                "number_of_calories", "10", "lt", "or", "and");
        assert m.invoke(null, query).equals(expected);
    }

    @Test(expected = InvocationTargetException.class)
    public void testBuildSpecFromPostFixListInvalid() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method m = SpecificationUtils.class.getDeclaredMethod("buildSpecFromPostfixList", List.class,
                SpecificationAttrConverter.class);
        m.setAccessible(true);
        List<String> postfixedResult = Arrays.asList("a", "b");
        m.invoke(null, postfixedResult, null);
    }

    @Test(expected = InvocationTargetException.class)
    public void testBuildSpecFromPostFixListInvalid2() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method m = SpecificationUtils.class.getDeclaredMethod("buildSpecFromPostfixList", List.class,
                SpecificationAttrConverter.class);
        m.setAccessible(true);
        List<String> postfixedResult = Arrays.asList("gt", "le");
        m.invoke(null, postfixedResult, null);
    }

    @Test(expected = InvocationTargetException.class)
    public void testBuildSpecFromPostFixListInvalid3() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method m = SpecificationUtils.class.getDeclaredMethod("buildSpecFromPostfixList", List.class,
                SpecificationAttrConverter.class);
        m.setAccessible(true);
        List<String> postfixedResult = Arrays.asList("a", "b", "gt", "and");
        m.invoke(null, postfixedResult, null);
    }

    @Test
    public void testBuildSpecFromPostFixList() throws NoSuchMethodException,
            InvocationTargetException, IllegalAccessException {
        Method m = SpecificationUtils.class.getDeclaredMethod("buildSpecFromPostfixList", List.class,
                SpecificationAttrConverter.class);
        m.setAccessible(true);
        List<String> postfixedResult = Arrays.asList("date", "'2016-05-01'", "eq", "number_of_calories", "20", "gt",
                "number_of_calories", "10", "lt", "or", "and");
        // As Specification object is a lambda it is more of a test case to debug and deep dive the working of this
        // piece of code.
        m.invoke(null, postfixedResult, null);
    }

}
