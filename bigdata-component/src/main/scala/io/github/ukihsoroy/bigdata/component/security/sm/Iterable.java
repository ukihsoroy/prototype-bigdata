package io.github.ukihsoroy.bigdata.component.security.sm;

import java.util.Iterator;

/**
 * @author K.O
 * @param <T>
 */
public interface Iterable<T> extends java.lang.Iterable<T> {

    /**
     * 迭代器
     * @return
     */
    @Override
    Iterator<T> iterator();
}
