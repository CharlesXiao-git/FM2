package com.freightmate.harbour.helper;

import com.freightmate.harbour.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ListHelper {
    private static final Logger LOG = LoggerFactory.getLogger(ListHelper.class);
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

    /**
     * Convert an interator to a list for easier handling
     * @param iterator
     * @param <T>
     * @return
     */
    public static <T> List<T> getListFromIterator(Iterator<T> iterator) {
        Iterable<T> iterable = () -> iterator;

        return StreamSupport
                .stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * Split a list into a stream of lists to allow batching.
     * @param source The list to split
     * @param length the size of the batches
     * @param <T> the type of the list
     * @return Steram of lists with size length
     */
    public static <T> Stream<List<T>> batch(List<T> source, int length) {
        if (length <= 0)
            throw new IllegalArgumentException("length = " + length);
        int size = source.size();
        if (size <= 0)
            return Stream.empty();
        int fullChunks = (size - 1) / length;
        return IntStream.range(0, fullChunks + 1).mapToObj(
                n -> source.subList(n * length, n == fullChunks ? size : (n + 1) * length));
    }

    /**
     * This helper function is to get the first item of the list. stream().findFirst() returns an Optional field
     * which can be cumbersome when implementing this inside the service because of the required validation to check
     * if it is empty or not. This function is to safe the time or extra line of codes from validating and converting the
     * Optional.
     * @param items     This is the list object that the function will try to get the first item from
     * @param <T>       This is the type of the list
     * @return This function will return the first item back to the caller
     */
    public static <T> T getFirst(List<T> items) {
        Optional<T> first = items.stream().findFirst();
        if (first.isEmpty()) {
            LOG.error("Unable to get first item from the list");
            throw new BadRequestException("Unable to get first item from the list");
        }
        return first.orElseThrow();
    }
}