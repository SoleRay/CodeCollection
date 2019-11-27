package strategy.aop.registry.impl;

import strategy.aop.adapter.ColorAdapter;
import strategy.aop.adapter.impl.BlueAdapter;
import strategy.aop.adapter.impl.GreenAdapter;
import strategy.aop.adapter.impl.RedAdapter;
import strategy.aop.advice.ColorAdvice;
import strategy.aop.interceptor.ColorInterceptor;
import strategy.aop.registry.AdapterRegistry;

import java.util.ArrayList;
import java.util.List;

public class DefaultAdapterRegistry implements AdapterRegistry {

    private final List<ColorAdapter> adapters = new ArrayList<>(3);

    public DefaultAdapterRegistry() {
        registerColorAdapter(new RedAdapter());
        registerColorAdapter(new BlueAdapter());
        registerColorAdapter(new GreenAdapter());
    }

    @Override
    public ColorInterceptor getColorInterceptors(ColorAdvice advice) {

        for(ColorAdapter adapter : adapters){
            if(adapter.supportsAdvice(advice)){
                return adapter.getColorInterceptor(advice);
            }
        }

        return null;
    }

    @Override
    public void registerColorAdapter(ColorAdapter adapter) {
        adapters.add(adapter);
    }
}
