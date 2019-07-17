package responsibility.test;

import responsibility.design.FilterInterfaceChain;
import responsibility.user.FilterA;
import responsibility.user.FilterB;

public class FilterTest {

    public static void main(String[] args) {
        FilterInterfaceChain chain = new FilterInterfaceChain();
        chain.register(new FilterA());
        chain.register(new FilterB());
        chain.doFilter();
    }
}
