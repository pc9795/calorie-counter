package service.calorie.util;

import service.calorie.exceptions.InvalidSearchQueryException;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 15:47
 * Purpose: TODO:
 **/
public class SpecificationUtils {
    private static List<String> operators = Arrays.asList("ge", "gt", "le", "lt", "eq", "and", "or", "(", ")");


    public static List<String> infixToPostFix(String query) throws InvalidSearchQueryException {
        SearchQueryTokenizer tokenizer = new SearchQueryTokenizer(query.toLowerCase());
        List<String> result = new LinkedList<>();
        ArrayDeque<String> stack = new ArrayDeque<>();
        String token = tokenizer.nextToken();

        while (token != null) {
            if (!isOperator(token)) {
                result.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    result.add(stack.pop());
                }
                if (!stack.isEmpty() && !stack.peek().equals("(")) {
                    throw new InvalidSearchQueryException();
                } else {
                    stack.pop();
                }
            } else {
                while (!stack.isEmpty() && precedence(token) <= precedence(stack.peek())) {
                    result.add(stack.pop());
                }
                stack.push(token);
            }
            token = tokenizer.nextToken();
        }
        while (!stack.isEmpty()) {
            if (stack.peek().equals("(")) {
                throw new InvalidSearchQueryException("Unbalanced parenthesis found");
            }
            result.add(stack.pop());
        }
        return result;
    }

    public static ApiSpecification buildSpecFromPostfixList(List<String> postfix) throws InvalidSearchQueryException {
        ApiSpecification spec = null;
        ArrayDeque<ApiSpecification> stack = new ArrayDeque<>();

        for (String token : postfix) {
        }
        return spec;

    }

    private static boolean isOperator(String op) {
        return operators.contains(op);
    }

    private static boolean isAndOperator(String op) {
        return op.equals("and");
    }

    private static boolean isOrOperator(String op) {
        return op.equals("or");
    }

    private static int precedence(String op) throws InvalidSearchQueryException {
        switch (op) {
            case "ge":
            case "gt":
            case "le":
            case "lt":
                return 4;
            case "eq":
                return 3;
            case "and":
                return 2;
            case "or":
                return 1;
            case "(":
                return 0;
        }
        throw new InvalidSearchQueryException(String.format("Can't get precedence of: %s", op));
    }

    private static class SearchQueryTokenizer {
        private final String query;
        private int counter = 0;

        SearchQueryTokenizer(String query) {
            this.query = query;
        }

        String nextToken() {
            //Remove spaces;
            for (; counter < query.length(); counter++) {
                if (query.charAt(counter) != ' ') {
                    break;
                }
            }
            if (counter == query.length()) {
                return null;
            }
            char c = query.charAt(counter);
            if (c == '(' || c == ')') {
                counter++;
                return "" + c;
            }
            int first = counter;
            //Retrieve next token. We assume everything is separated by space.
            for (; counter < query.length(); counter++) {
                c = query.charAt(counter);
                if (c == ' ' || c == ')') {
                    return query.substring(first, counter);
                }
            }
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            System.out.println(SpecificationUtils.infixToPostFix("(date eq '2016-05-01') AND ((number_of_calories gt 20) OR (number_of_calories lt 10))"));
        } catch (InvalidSearchQueryException e) {
            e.printStackTrace();
        }
    }
}
