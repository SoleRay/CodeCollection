package strategy.aop.adapter;

import strategy.aop.advice.ColorAdvice;
import strategy.aop.interceptor.ColorInterceptor;

public interface ColorAdapter {

    boolean supportsAdvice(ColorAdvice advice);

    ColorInterceptor getColorInterceptor(ColorAdvice advice);
}
