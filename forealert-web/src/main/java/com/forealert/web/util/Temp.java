package com.forealert.web.util;

import java.util.Scanner;

/**
 * Created by IntelliJ IDEA.
 * User: ashqures
 * Date: 7/25/17
 * Time: 5:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class Temp {


    static long getWays(long n, long[] c){
        // Complete this function
        Long count = 0L;
        for(int i=0;i< c.length;i++){
            if(n%c[i]==0){
                count = count+1;
            }
            Long sum = c[i];
            for(int j= i+1;j < c.length;j++){
                if(n%(c[i]+c[j])==0){
                    count = count+1;
                }else if(n%(sum+c[j])==0 || (n%c[i]==0 && n%c[j]==0)){
                    sum = sum + c[j];
                    count = count+1;
                }else if(n%c[j]!=0 && (n%c[j])%c[i]==0){
                    count = count+1;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        long[] c = new long[m];
        for(int c_i=0; c_i < m; c_i++){
            c[c_i] = in.nextLong();
        }
        // Print the number of ways of making change for 'n' units using coins having the values given by 'c'
        long ways = getWays(n, c);
        System.out.println(ways);
    }
    /*
    *    179 27
            24 6 48 27 36 22 35 15 41 1 26 25 4 8 14 20 9 38 34 40 45 17 33 19 5 43 2
    *
    *     1283414971
    *
    *
    *
    *
    *
    *
    *
    * */

}
