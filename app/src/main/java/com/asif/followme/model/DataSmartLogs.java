package com.asif.followme.model;

import org.json.JSONArray;

/**
 * Created by user on 12/18/2017.
 */

public class DataSmartLogs {


    private JSONArray arr = new JSONArray();
    private String s0="";
    private String s1="";
    private String s2="";
    private String s3="";
    private String s4="";
    private String s5="";
    private String s6="";
    private String s7="";
    private String s8="";
    private String s9="";
    private String s10="";
    private int n = 0;


    public String getS(int i){
        String t;
        switch(i){
            case 0: t = s0;break;
            case 1: t = s1;break;
            case 2: t = s2;break;
            case 3: t = s3;break;
            case 4: t = s4;break;
            case 5: t = s5;break;
            case 6: t = s6;break;
            case 7: t = s7;break;
            case 8: t = s8;break;
            case 9: t = s9;break;
            case 10: t = s10;break;
            default: t = "-";
        }
        return t;
    }

    public void setS(int i, String s){
        switch(i){
            case 0: this.s0 = s;break;
            case 1: this.s1 = s;break;
            case 2: this.s2 = s;break;
            case 3: this.s3 = s;break;
            case 4: this.s4 = s;break;
            case 5: this.s5 = s;break;
            case 6: this.s6 = s;break;
            case 7: this.s7 = s;break;
            case 8: this.s8 = s;break;
            case 9: this.s9 = s;break;
            case 10: this.s10 = s;break;

        }
    }
    public int getSizes() {return n;}
    public void setSizes(int n) {this.n=n;}

    public String getS0(){return  s0;}
    public void setS0(String s){this.s0=s;}


    public String getS1(){return  s1;}
    public void setS1(String s){this.s1=s;}

    public String getS2(){return  s2;}
    public void setS2(String s){this.s2=s;}

    public String getS3(){return  s3;}
    public void setS3(String s){this.s3=s;}

    public String getS4(){return  s4;}
    public void setS4(String s){this.s4=s;}

    public String getS5(){return  s5;}
    public void setS5(String s){this.s5=s;}

    public String getS6(){return  s6;}
    public void setS6(String s){this.s6=s;}

    public String getS7(){return  s7;}
    public void setS7(String s){this.s7=s;}

    public String getS8(){return  s8;}
    public void setS8(String s){this.s8=s;}

    public String getS9(){return  s9;}
    public void setS9(String s){this.s9=s;}

    public String getS10(){return  s10;}
    public void setS10(String s){this.s10=s;}

}
