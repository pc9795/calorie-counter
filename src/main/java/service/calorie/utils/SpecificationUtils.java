package service.calorie.utils;

import org.springframework.data.jpa.domain.Specification;
import service.calorie.exceptions.InvalidDataException;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created By: Prashant Chaubey
 * Created On: 27-10-2019 15:47
 * Purpose: TODO:
 **/
public class SpecificationUtils {
    private static List<String> operators = Arrays.asList("gt", "ge", "lt", "le", "eq", "and", "or", "(", ")");

    static SearchOption searchOptionFromStr(String optionStr) throws InvalidDataException {
        switch (optionStr) {
            case "eq":
                return SearchOption.EQUALS;
            case "gt":
                return SearchOption.GREATER_THAN;
            case "ge":
                return SearchOption.GREATER_THAN_OR_EQUAL_TO;
            case "lt":
                return SearchOption.LESS_THAN;
            case "le":
                return SearchOption.LESS_THAN_OR_EQUAL_TO;
            case "and":
                return SearchOption.AND;
            case "or":
                return SearchOption.OR;
        }
        throw new InvalidDataException(String.format("Not a valid search option %s", optionStr));
    }

    private static List<String> infixToPostFix(String query) throws InvalidDataException {
        SearchQueryTokenizer tokenizer = new SearchQueryTokenizer(query.toLowerCase());
        List<String> result = new ArrayList<>();
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
                    throw new InvalidDataException("Unbalanced braces");
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
                throw new InvalidDataException("Unbalanced parenthesis found");
            }
            result.add(stack.pop());
        }
        return result;
    }

    private static <T> Specification<T> buildSpecFromPostfixList(List<String> postfix) throws InvalidDataException {
        ArrayDeque<Specification<T>> stack = new ArrayDeque<>();
        int size = postfix.size();

        for (int i = 0; i < size; i++) {
            if (!isOperator(postfix.get(i))) {
                if (size - i < 3) {
                    throw new InvalidDataException("Invalid search query");
                }
                stack.push(new ApiSpecification<>(
                        new SearchCriteria(postfix.get(i), postfix.get(i + 1), postfix.get(i + 2))));
                i += 2;
            } else {
                SearchOption option = searchOptionFromStr(postfix.get(i));
                if (option != SearchOption.AND && option != SearchOption.OR) {
                    throw new InvalidDataException("Found options other than AND and OR");
                }
                if (stack.size() < 2) {
                    throw new InvalidDataException("Invalid search query");
                }
                Specification<T> first = stack.pop();
                Specification<T> second = stack.pop();
                if (option == SearchOption.OR) {
                    stack.push(first.and(second));
                } else {
                    stack.push(first.or(second));
                }
            }
        }
        return stack.peek();

    }

    private static boolean isOperator(String op) {
        return operators.contains(op);
    }

    private static int precedence(String op) throws InvalidDataException {
        if (op.equals("(")) {
            return 0;
        }
        SearchOption searchOption = searchOptionFromStr(op);
        switch (searchOption) {
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL_TO:
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL_TO:
                return 4;
            case EQUALS:
                return 3;
            case AND:
                return 2;
            case OR:
                return 1;
        }
        throw new InvalidDataException(String.format("Can't get precedence of: %s", op));
    }

    public static <T> Specification<T> getSpecFromQuery(String query) throws InvalidDataException {
        List<String> postFixedQuery = infixToPostFix(query);
        return buildSpecFromPostfixList(postFixedQuery);
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
}
