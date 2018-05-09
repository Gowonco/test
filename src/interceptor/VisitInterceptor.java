package interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;

import java.lang.reflect.Method;

public class VisitInterceptor implements Interceptor {

    @Override
    public void intercept(Invocation inv) {
        Controller controller=inv.getController();
        controller.getResponse().addHeader("Access-Control-Allow-Origin", "*");
        System.out.println("Before method invoking");
        inv.invoke();
        System.out.println("After method invoking");
    }
}
