package spring.cglib;

import org.springframework.cglib.proxy.*;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Method;

public class CglibProxyCreator {

    /** NoOp.INSTANCE 很重要，所有不需要增强的方法，就会匹配到它，因为过滤器accept必须匹配到它*/
    private static final Callback[] CALLBACKS = new Callback[] {
            new CglibProxyCreator.BeanMethodInterceptor(),
            NoOp.INSTANCE
    };


    private static final ConditionalCallbackFilter CALLBACK_FILTER = new ConditionalCallbackFilter(CALLBACKS);


    public static Enhancer newEnhancer(Class<?> superclass)  {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(superclass);
        enhancer.setCallbacks(CALLBACKS);
        enhancer.setCallbackFilter(CALLBACK_FILTER);
        enhancer.setCallbackTypes(CALLBACK_FILTER.getCallbackTypes());
        return enhancer;
    }

    public static Object enhanceClass(Enhancer enhancer){
        return enhancer.createClass();
    }

    public static Object createProxy(Enhancer enhancer){

        return enhancer.create();
    }

    private interface ConditionalCallback extends Callback {

        boolean isMatch(Method candidateMethod);
    }

    private static class ConditionalCallbackFilter implements CallbackFilter {

        private final Callback[] callbacks;

        private final Class<?>[] callbackTypes;

        public ConditionalCallbackFilter(Callback[] callbacks) {
            this.callbacks = callbacks;
            this.callbackTypes = new Class<?>[callbacks.length];
            for (int i = 0; i < callbacks.length; i++) {
                this.callbackTypes[i] = callbacks[i].getClass();
            }
        }

        @Override
        public int accept(Method method) {
            for (int i = 0; i < this.callbacks.length; i++) {
                Callback callback = this.callbacks[i];
                if (!(callback instanceof ConditionalCallback) || ((ConditionalCallback) callback).isMatch(method)) {
                    return i;
                }
            }
            throw new IllegalStateException("No callback available for method " + method.getName());
        }

        public Class<?>[] getCallbackTypes() {
            return this.callbackTypes;
        }
    }

    public static class BeanMethodInterceptor implements MethodInterceptor,ConditionalCallback {

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            System.out.println("do sth before "+method.getName());
            methodProxy.invokeSuper(o,objects);
            System.out.println("do sth after "+method.getName());
            return null;
        }

        @Override
        public boolean isMatch(Method candidateMethod) {
            return candidateMethod.isAnnotationPresent(Bean.class);
        }
    }
}
