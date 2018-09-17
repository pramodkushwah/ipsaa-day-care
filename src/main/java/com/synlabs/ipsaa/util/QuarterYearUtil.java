package com.synlabs.ipsaa.util;

public class QuarterYearUtil {
    int year;
    int quarter;

    public QuarterYearUtil(int quarter,int year){
        this.quarter=quarter;
        this.year=year;
    }
    public int getLastQuarter(){
        switch (quarter){
            case 1:     // forth quarter
                return 4;
            case 2: // first quarter
                year=year-1;
                return 1;
            case 3:  // second quarter
                return 2;
            case 4:  // thired quarter
                return 3;
                default: // case that never come
                    return 0;
        }
    }
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }
}
