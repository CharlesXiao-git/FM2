package com.freightmate.harbour.helper;

import java.util.*;

public class ListHelper {
    /**
     * Combine two lists of objects, and return a list of combined without duplicates
     */
    public static <T> List<T> extend(List<T> list1, List<T> list2){
        return extend(list1, list2, true);
    }

    public static <T> List<T> extend(List<T> list1, List<T> list2, boolean nullOk){
        if (nullOk && Objects.isNull(list2)){
            return list1;
        }
        if (nullOk && Objects.isNull(list1)){
            return list2;
        }

        Set<T> set = new LinkedHashSet<>(list1);
        set.addAll(list2);
        return new ArrayList<T>(set);
    }
}