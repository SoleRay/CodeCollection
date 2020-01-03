package reflect;

import java.util.List;

public class MultipleBean<T> {


    /** ParameterizedType -> GenericArrayType */
    private List<T[]> tArray;

    /** TypeVariable */
    private T t;

    /** ParameterizedType -> WildcardType */
    private List<? extends String> wlist;

    /** ParameterizedType -> Class */
    private List<String> strList;

    /** Class */
    private int x;

    /** Class */
    private String str;
}
