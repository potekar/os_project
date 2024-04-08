package assembler;

import java.util.Stack;

public class Operations {
    private static Stack <String> stack=new Stack<>();

    public static void push(String val){
        stack.push(val);
    }

    public static String pop()
    {
        return stack.pop();
    }

    public static void add()
    {
        String a=stack.pop();
        String b=stack.pop();
        stack.push(String.valueOf((Integer.parseInt(a)+Integer.parseInt(b))));
    }

    public static void sub()
    {
        String a=stack.pop();
        String b=stack.pop();
        stack.push(String.valueOf((Integer.parseInt(a)-Integer.parseInt(b))));
    }

    public static void mul()
    {
        String a=stack.pop();
        String b=stack.pop();
        stack.push(String.valueOf((Integer.parseInt(a)+Integer.parseInt(b))));
    }

    public static void div()
    {
        String a=stack.pop();
        String b=stack.pop();
        stack.push(String.valueOf((Integer.parseInt(a)/Integer.parseInt(b))));
    }

    public static void inc()
    {
        String a=stack.pop();
        stack.push(String.valueOf((Integer.parseInt(a)+1)));
    }

    public static void dec()
    {
        String a=stack.pop();
        stack.push(String.valueOf((Integer.parseInt(a)-1)));
    }

}
