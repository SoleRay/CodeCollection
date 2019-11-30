package common.beans;

import org.apache.commons.beanutils.BeanUtils;

import java.util.HashMap;
import java.util.Map;

public class BeanAndMapConvert {

    public static void beanToMap() throws Exception{
        Bean bean = new Bean(1,"box",new Bean());
        Map<String,String> map = BeanUtils.describe(bean);
        System.out.println(map.get("attr3"));
    }

    public static void mapToBean() throws Exception{
        Bean bean = new Bean();
        Map<String,String> map = new HashMap<>();
        map.put("attr1","1");
        map.put("attr2","cup");
        BeanUtils.populate(bean,map);
        System.out.println(bean);
    }

    public static void main(String[] args) throws Exception {
//        beanToMap();
        mapToBean();
    }

}
