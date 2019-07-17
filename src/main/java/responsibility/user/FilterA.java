package responsibility.user;

import responsibility.design.FilterInterface;
import responsibility.design.FilterInterfaceChain;

public class FilterA implements FilterInterface {

    @Override
    public void doFilter(String request, String response, FilterInterfaceChain chain) {
        //TODO sth....
        System.out.println("FilterA...");
        chain.doFilter();
    }
}
