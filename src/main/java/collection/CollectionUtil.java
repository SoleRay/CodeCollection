package collection;

import org.apache.poi.ss.formula.functions.T;

import java.util.Collection;
import java.util.Collections;

public class CollectionUtil {

    private Collection<T> getSigleton(T t){
        return Collections.singleton(t);
    }
}
