package com.fenda.calculator;

import android.util.Log;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @author matt.Ljp
 * @time 2019/9/24 18:15
 * @description
 */
public class InfixToSuffix {
    /**
     * 中缀转后缀
     * @param str 从键盘读入的String类型的对象，也就是要求的表达式
     * @return 返回一个ArrayList类型的对象，也就是一个集合，也即是后缀表达式，让cal方法接收，
     */
    public static ArrayList Suffix(StringBuilder str){
        /*
         * Stack() 构造方法
         * void add(int index, Object element) 在此向量的指定位置插入指定的元素
         * boolean empty() 测试堆栈是否为空
         * Object pop( ) 移除堆栈顶部的对象，并作为此函数的值返回该对象
         * Object push(Object element) 把项压入堆栈顶部
         */

        for (int i = 1; i < str.length(); i++){ //识别到'(-'自动补0
            if (str.charAt(i) == '-' && str.charAt(i - 1) == '('){
                str.insert(i, '0');
            }
        }

        StringBuilder temp = new StringBuilder(); // 当做中间字符串，临时存放数字，方便往list中添加

        List<String> list = new ArrayList<>(); // 用于存储分割后的数字和符号

        ArrayList<String> result = new ArrayList<>(); //作为结果集合，后缀表达式保存在其中

        for (int i = 0; i < str.length(); i++){
            // if ( i != str.length() - 1 ){ //对不是最后一位数字的
            if ((str.charAt(i) >= '0' && str.charAt(i) <= '9') || str.charAt(i) == '.'){ // 判断是不是数字
                if (str.charAt(i) == '.' && temp.length() == 0){ //如果此位为'.'，且这个时候中间字符串为空，自动补0
                    temp.append(0); // 添加0，在添加'.'
                    temp.append(str.charAt(i));
                } else {
                    temp.append(str.charAt(i)); // 是数字，就往中间字符串添加
                }
                if (i == str.length() - 1){ //对最后一位进行单独判断，如果是字符串最后一位，直接进行添加到list操作
                    list.add(temp.toString());
                }
            } else  { // 不是数字
                if (temp.length() != 0) {
                    list.add(temp.toString()); // 判断中间字符串长度是不是0，不是0就往list中添加
                }
                list.add(String.valueOf(str.charAt(i))); // 将符号往list中添加
                temp.delete(0, temp.length()); // 清空中间字符串
            }
            // } else { //单独把最后一个字符拿出来进行判断
            //     if ((str.charAt(i) >= '0' && str.charAt(i) <= '9') || str.charAt(i) == '.'){ // 下面代码注释参考上面
            //         temp.append(str.charAt(i));
            //
            //     } else  {
            //         if (temp.length() != 0)
            //             list.add(temp.toString()); // 判断中间字符串长度是不是0，不是0就往list中添加
            //         list.add(String.valueOf(str.charAt(i)));
            //         temp.delete(0, temp.length());
            //     }
            // }
        }

        // 遍历输出
        for (String aList : list) {
            System.out.print(aList + " ");
        }
        System.out.println();

        // 定义栈
        Stack<String> stack = new Stack<>();

        // 定义符号的优先级
        Map<Character, Integer> map = new HashMap<>();
        map.put('(', 0);
        map.put(')', 0);
        map.put('*', 1);
        map.put('/', 1);
        map.put('+', 2);
        map.put('-', 2);

        for ( String s : list ) {
            // System.out.println("s=" + s);
            if ( s.equals("*") || s.equals("/") || s.equals("+") || s.equals("-") || s.equals("(") || s.equals(")") ){ //不为数字
                if (stack.size() == 0){ //如果当前栈里面没有元素，不管是啥，直接进栈
                    stack.push(s);
                } else { //如果栈中有元素
                    if (s.equals(")")) { //如果当前是),那就的往前找(与其对应输出
                        if (!stack.empty()){ //容错，不然会有EmptyStackException，下面的类似的表达式同理
                            while (!stack.peek().equals("(")){ //循环，从栈顶开始循环，一直到栈顶为(，退出循环
                                // System.out.print(stack.pop() + " "); //输出并且把栈顶元素移除
                                result.add(stack.pop()); //保存到结果集合中，并且把栈顶元素移除
                                if (stack.empty()){//容错，不然会有EmptyStackException,见上面
                                    break;
                                }
                            }
                            if (!stack.empty()){
                                if (stack.peek().equals("("))//如果此时栈顶元素为(,就将他移除，原因是后缀表达式不用(，而在之前我们将他压入了栈中,具体见下面第5行
                                    stack.pop();
                            }
                        }
                    } else{
                        if (s.equals("(")){ //如果是'('将其入栈
                            stack.push(s);
                        } else {
                            if (stack.peek().charAt(0) != '('){
                                if ( map.get(s.charAt(0)) < map.get(stack.peek().charAt(0)) ){ //栈顶符号的优先级高于元素优先级，也就是数字小，进栈
                                    stack.push(s);
                                } else { //栈顶符号优先级低于元素优先级，输出并循环
                                    while ((map.get(s.charAt(0)) >= map.get(stack.peek().charAt(0))) && !stack.empty()){
                                        // System.out.print(stack.pop() + " ");
                                        result.add(stack.pop());
                                        if (stack.empty()){
                                            break;
                                        }
                                        if (stack.peek().equals("(")){
                                            break;
                                        }
                                    }
                                    stack.push(s);
                                }
                            } else {
                                stack.push(s);
                            }
                        }
                    }
                }
            } else { //是数字
                result.add(s);
            }
        }

        while (!stack.empty()){ //最后遍历栈，把结果保存到result中，直到栈被清空
            result.add(stack.pop());
        }
        return result;
    }

    /**
     * 用于计算后缀表达式的结果
     * @param arrayList 从Suffix传出来的后缀表达式
     * @return 返回String类型对象，也就是最后的结果
     */

    public static String Cal(ArrayList arrayList){
        int length = arrayList.size();
        String[] arr = new String[length]; //转为字符串数组,方便模拟栈

        for (int i = 0; i < arrayList.size(); i++){
            arr[i] = (String)arrayList.get(i);
        }

        List<String> list = new ArrayList<>();
        for (String anArr : arr) { //此处就是上面说的运算过程，因为list.remove的缘故，所以取出最后一个数个最后两个数  都是size-2
            int size = list.size();
            switch (anArr) {
                case "+":
                    BigDecimal a = new BigDecimal(list.remove(size - 2)).add(new BigDecimal(list.remove(size - 2)));
                    // list.add(String.valueOf(a.stripTrailingZeros().toPlainString()));
                    list.add(a.stripTrailingZeros().toString()); //去掉结果最后的0
                    break;
                case "-":
                    BigDecimal b = new BigDecimal(list.remove(size - 2)).subtract(new BigDecimal(list.remove(size - 2)));
                    list.add(b.stripTrailingZeros().toString());
                    break;
                case "*":
                    BigDecimal c = new BigDecimal(list.remove(size - 2)).multiply(new BigDecimal(list.remove(size - 2)));
                    list.add(c.stripTrailingZeros().toString());
                    break;
                case "/":
                    BigDecimal d = new BigDecimal(list.remove(size - 2)).divide(new BigDecimal(list.remove(size - 2)), 10, BigDecimal.ROUND_HALF_UP);
                    list.add(d.stripTrailingZeros().toString());
                    break;
                default:
                    list.add(anArr);
                    break;//如果是数字  直接放进list中
            }
        }

        if (list.size() == 1){
            if (list.get(0).length() < 30){ //当结果长度不长时，就直接输出
                BigDecimal bd = new BigDecimal(list.get(0));
                return bd.toPlainString(); //BigDecimal默认直接输出
            } else { //当结果过长时，就用科学计数法输出
                double d = Double.valueOf(list.get(0));
                return String.valueOf(d); //Double会使用科学计数法输出
            }
        } else {
            return "运算失败";
        }
    }
}