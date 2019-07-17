package responsibility.design;

public interface FilterInterface {

    void doFilter(String request,String response,FilterInterfaceChain chain);
}
