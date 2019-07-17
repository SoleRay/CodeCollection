package responsibility.design;

import java.util.ArrayList;
import java.util.List;

public class FilterInterfaceChain {

    private List<FilterInterface> filterInterfaces;

    private int index;

    private String request;

    private String response;

    public FilterInterfaceChain() {
        filterInterfaces = new ArrayList<>();
    }

    public void doFilter(){
        if(index<filterInterfaces.size()){
            filterInterfaces.get(index++).doFilter(request,response,this);
        }
    }

    public void register(FilterInterface filterInterface){
        filterInterfaces.add(filterInterface);
    }
}
