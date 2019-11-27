package strategy.aop.adapter.impl;

import strategy.aop.adapter.ColorAdapter;
import strategy.aop.advice.ColorAdvice;
import strategy.aop.advice.BlueAdvice;
import strategy.aop.interceptor.ColorInterceptor;
import strategy.aop.interceptor.impl.BlueInterceptor;

public class BlueAdapter implements ColorAdapter {

    @Override
    public boolean supportsAdvice(ColorAdvice advice) {
        return (advice instanceof BlueAdvice);
    }

    @Override
    public ColorInterceptor getColorInterceptor(ColorAdvice advice) {
        return new BlueInterceptor((BlueAdvice) advice);
    }
}
