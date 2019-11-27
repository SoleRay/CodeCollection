package strategy.aop.registry;

import strategy.aop.adapter.ColorAdapter;
import strategy.aop.advice.ColorAdvice;
import strategy.aop.interceptor.ColorInterceptor;

public interface AdapterRegistry {

    ColorInterceptor getColorInterceptors(ColorAdvice advice);

    void registerColorAdapter(ColorAdapter adapter);
}
