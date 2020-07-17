package CalculatorrExam;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Calculater {


    private ArrayList<LinkedList<TokenItem>> historyList = new ArrayList<>();
    private ArrayList<TokenItem> tokeList = new ArrayList<>();
    private Map<String, ArrayList<TokenItem>>assignValue = new HashMap<>();
    private boolean isError = false;

    public Calculater() {
    }

    public enum Token {

        TK_POW ("\\^"),
        TK_OPEN ("\\("),
        TK_CLOSE ("\\)"),
        TK_MUL ("\\*"),
        TK_DIV ("/"),
        TK_PLUS ("\\+"),
        TK_MINUS ("-"),
        TK_ASSIGN ("="),
        INTEGER ("\\d+"),
        IDENTIFIER ("\\w+");

        private final Pattern pattern;

        Token(String regex) {
            pattern = Pattern.compile("^" + regex);
        }

        int endOfMatch(String s) {
            Matcher m = pattern.matcher(s);

            if (m.find()) {
                return m.end();
            }
            return -1;
        }
    }


    public void setExpression(String input) {

        if(assignValue.size() > 0)
        {
            double result = calculateAssignExpression(input);
            System.out.println(result);
            return;
        }
        //reset data
        tokeList.clear();
        assignValue.clear();
        isError = false;

        //init new input
        this.tokeList = this.lexicalAnalyser(input);
        LinkedList<TokenItem> postfix =  toPostfix(tokeList);
        double result = calculate(postfix);
        if(isError)
        {
            System.out.println("Invalid expression");
        }else {
            System.out.println(result);
        }
    }

    //use for calculate assign value
    public double calculateAssignExpression(String input)
    {
        tokeList.clear();
        isError = false;

        ArrayList<TokenItem> token = this.lexicalAnalyser(input);
        for (TokenItem item : token) {
            if (item.token == Token.IDENTIFIER) {
                ArrayList<TokenItem> assing = assignValue.get(item.value);
                tokeList.addAll(assing);
            }else{
                tokeList.add(item);
            }
        }
        LinkedList<TokenItem> tokenItems = toPostfix(tokeList);
        //clean assignValue value;
        return calculate(tokenItems);

    }


    private ArrayList<TokenItem> lexicalAnalyser(String input) {
        //clean whitespace for string
        input = input.trim();
        input = input.replaceAll("\\s+", "");
        // create string builder for easy to manage
        StringBuilder message = new StringBuilder(input);
        ArrayList<TokenItem> toke = new ArrayList<TokenItem>();
        //loop for filter token
        while (message.length() > 0) {
            for (Token t : Token.values()) {
                int end = t.endOfMatch(message.toString());

                if (end != -1) {
                    Token currentToke = t;
                    String lexical = message.substring(0, end);
                    message.delete(0, end);
                    //System.out.println("toke map : " + currentToke + " lexrma : " + lexrma);
                    toke.add(new TokenItem(currentToke,lexical));
                }
            }
        }

        return toke;

    }

    public void setAssignValue(String input)
    {
        //clean whitespace for string
        input = input.trim();
        input = input.replaceAll("\\s+", "");

        String[] value = input.split("=");
        assignValue.put(value[0],this.lexicalAnalyser(value[1]));

    }

    private LinkedList<TokenItem> toPostfix(ArrayList<TokenItem> list)
    {
        LinkedList<TokenItem> postfix = new LinkedList<>();
        ArrayDeque<TokenItem> stack = new ArrayDeque<>();

        for (TokenItem token:list) {

            if(token.isOp())
            {
                postfix.add(token);
                continue;
            }

            if(token.token == Token.TK_OPEN)
            {
                stack.push(token);
                continue;
            }

            if(token.token == Token.TK_CLOSE)
            {
                while(!(stack.peek().token == Token.TK_OPEN))
                {
                    postfix.add(stack.pop());
                }
                stack.pop();
                continue;
            }
            if(stack.isEmpty() || stack.peek().token == Token.TK_OPEN)
            {
                stack.push(token);
                continue;
            }

            if(token.getOpPriority() > stack.peek().getOpPriority())
            {
                stack.push(token);
                continue;
            }

            while (!stack.isEmpty() && !(stack.peek().token == Token.TK_OPEN)
                && token.getOpPriority() <= stack.peek().getOpPriority())
            {
                postfix.add(stack.pop());
            }
            stack.push(token);
        }

        while  (!stack.isEmpty()){
            postfix.add(stack.pop());
        }

        return postfix;
    }

    private Double calculate(LinkedList<TokenItem> postfix)
    {
        ArrayDeque<Double> stack = new ArrayDeque<>();

        for (int i=postfix.size() - 1;i>=0;i--){
            if(postfix.get(i).isOp())
            {
                if(stack.peek() == null)
                {
                    isError = true;
                    return 0d;
                }
                Double a = stack.pop();
                if(stack.peek() == null)
                {
                    isError = true;
                    return 0d;
                }
                Double b = stack.pop();
                stack.push(postfix.get(i).apply(a,b));
            }
            else
            {

                if(postfix.get(i).token == Token.INTEGER) {
                    stack.push(postfix.get(i).getValueInDouble());
               }else {
                    isError = true;
                    return 0d;
                }
            }

        }
        return  stack.pop();
    }

}













