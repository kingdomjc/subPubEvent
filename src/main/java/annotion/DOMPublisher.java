package annotion;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * 事件发布者
 * @author domjc
 * */
public class DOMPublisher {
   private static Map<Class, List<Method>> observers;
    static{
        observers= ObserverInstall.getObservers();
    }

    public static<T> void publish(Object o){
       List<Method> methods=observers.get(o.getClass());
        for (Method method : methods) {
            try {
                method.invoke(null,o);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
