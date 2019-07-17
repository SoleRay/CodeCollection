package responsibility.user;

import responsibility.design.FilterInterface;
import responsibility.design.FilterInterfaceChain;

public class FilterB implements FilterInterface {

    @Override
    public void doFilter(String request, String response, FilterInterfaceChain chain) {
        //TODO sth....
        System.out.println("FilterB...");
        chain.doFilter();
    }
}
