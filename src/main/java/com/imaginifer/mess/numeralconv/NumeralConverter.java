/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginifer.mess.numeralconv;

import java.util.*;


/**
 *
 * @author imaginifer
 */
public class NumeralConverter {

    private static final String[][] NUMERI = {
        {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX"},
        {"", "X", "XX", "XXX", "XL", "L", "LX", "LXX", "LXXX", "XC"},
        {"", "C", "CC", "CCC", "CD", "D", "DC", "DCC", "DCCC", "CM"},
        {"", "M", "MM", "MMM", "(IIII)", "I))", "(I))", "(II))", "(III))", "(IIII))"},
        {"", "((I))", "((II))", "((III))", "((IIII))", "I)))", "((I}", "((II}",
            "((III}", "((IIII}"}, //10e
        {"", "(((I)))", "(((II)))", "(((III)))", "(((IIII)))", "I)}", "(((I}))",
            "(((II)}", "(((III)}", "(((IIII)}"},//100e
        {"", "{(I)}", "{(II)}", "{(III)}", "{(IIII)}", "I}", "{(I})}", "{(II})}",
            "{(III})}", "{(IIII})}"},//m
        {"", "{I}", "{II}", "{III}", "{IIII}", "I})", "{I}})", "{II}})", "{III}})",
            "{IIII}})"},//10m
        {"", "({I})", "({II})", "({III})", "({IIII})", "I}))", "({I}})))", "({II}})))",
            "({III}})))", "({IIII}})))"},//100m
        {"", "(({I}))", "(({II}))", "(({III}))", "(({IIII}))", "I})))", "(({I}}}",
            "(({II}}}", "(({III}}}", "(({IIII}}}"},//mrd
        {"", "((({I})))", "((({II})))", "((({III})))", "((({IIII})))", "I})}",
            "((({I}}}))", "((({II}}}))", "((({III}}}))", "((({IIII}}}))"},//10mrd
        {"", "{({I})}", "{({II})}", "{({III})}", "{({IIII})}", "I}}",
            "{({I}}})}", "{({II}}})}", "{({III}}})}", "{({IIII}}})}"},//100mrd
    };

    private static int idx = 0;
    
    private static final Set<Character> DEC = new HashSet<>();
    
    private static final Set<Character> CENT = new HashSet<>();
    
    static{
        char[] dec1 = {'l', 'c', 'd', 'm'};
        char[] cent1 = {'d', 'm'};
        
        for (char c : dec1) {
            DEC.add(c);
        }
        for (char c : cent1) {
            CENT.add(c);
        }
        
    }

    public static String romanize(long x) throws NumeralConvException {
        if(x < 0 ){
            throw new NumeralConvException("A szám értelmezhetetlen!");
        }
        ArrayList<String> z = assemble(x,false);
        StringBuilder sb = new StringBuilder();
        for (int i = z.size() - 1; i >= 0; i--) {
            sb.append(z.get(i));
        }
        return sb.toString();
    }

    private static ArrayList<String> assemble(long x, boolean apostrophic) {
        ArrayList<String> num = new ArrayList<>();
        if (x == 0) {
            num.add("N");
            return num;
        }
        TreeMap<Integer, String> subtr = assemblySpecialSubtractions(x);
        num.add(subtr.get(subtr.firstKey()));
        x -= subtr.firstKey();
        
        int k = 0;
        while (x > 0) {
            if(!apostrophic && k == 3 && x>=4){
                num.add("˙");
                k = 0;
                subtr = assemblySpecialSubtractions(x);
                num.add(subtr.get(subtr.firstKey()));
                x -= subtr.firstKey();
            }
            int n = (int) (x % 10);
            num.add(NUMERI[k][n]);
            x -= n;
            if (k == 0 && x % 1000 == 990) {
                num.add("XM");
                x -= 990;
            } else if (k == 0 && x % 1000 == 490) {
                num.add("XD");
                x -= 490;
            }
            x /= 10;
            k++;
        }
        return num;
    }
    
    private static TreeMap<Integer, String> assemblySpecialSubtractions(long x){
        TreeMap<Integer, String> res = new TreeMap<>();
        if (x % 1000 == 999) {
            res.put(999, "IM");
            return res;
        } else if (x % 1000 == 499) {
            res.put(499, "ID");
            return res;
        } else if (x % 100 == 99) {
            res.put(99, "IC");
            return res;
        } else if (x % 100 == 49) {
            res.put(49, "IL");
            return res;
        }
        res.put(0, "");
        return res;
    }

    public static int deromanize(String numero) {
        idx = numero.length() - 1;
        return disassemble(numero.toLowerCase());
    }

    private static int disassemble(String numero) {
        int q = 0;
        while (idx >= 0) {
            char c = numero.charAt(idx);
            if (idx == 0 && c == 'n') {
                return q;
            } else if (c == 'i') {
                q++;
            } else if (c == 'v') {
                q += 5 - subtractor(numero);
            } else if (c == 'x') {
                q += 10 - subtractor(numero);
            } else if (c == 'l') {
                q += 50 - subtractor(numero);
            } else if (c == 'c') {
                q += 100 - subtractor(numero);
            } else if (c == 'd') {
                q += 500 - subtractor(numero);
            } else if (c == 'm') {
                q += 1000 - subtractor(numero);
            }
            idx--;
        }
        return q;
    }

    private static int subtractor(String numero) {
        if (idx != 0 && numero.charAt(idx - 1) == 'i') {
            idx--;
            return 1;
        } else if (idx != 0 && DEC.contains(numero.charAt(idx)) 
                && numero.charAt(idx - 1) == 'x') {
            idx--;
            return 10;
        } else if (idx != 0 && CENT.contains(numero.charAt(idx))
                && numero.charAt(idx - 1) == 'c') {
            idx--;
            return 100;
        }
        return 0;
    }
}
