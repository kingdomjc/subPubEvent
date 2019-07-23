package annotion;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 把所有订阅者保存内存
 * */
public class ObserverInstall<T> {
    private static Map<Class, List<Method>> observers=new HashMap<>();
    private static  List<String> classPaths = new ArrayList<String>();
    static{
        scannerPackage();
        register();
    }

    private static void scannerPackage(){
        String path= ObserverInstall.class.getResource("/").getPath();
        path= URLDecoder.decode(path);
        doPath(new File(path));
        String[] split = path.split("/");
        String splitSymbols=split[split.length-1];
        classPaths=classPaths.stream().map(p->{
            String[] allPaths = p.split(splitSymbols+"\\\\");
            String[] allPath = allPaths[1].split("\\.");
            return allPath[0].replace('\\', '.');
        }).collect(Collectors.toList());
    }
    private static void register(){
        classPaths.forEach(s->{
            try {
                Class<?> aClass = Class.forName(s);
               processAnnotions(aClass);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    private static void processAnnotions(Class o){
        Method[] methods = o.getDeclaredMethods();
        for (Method method:methods) {
            Observers annotation = method.getAnnotation(Observers.class);
            if(annotation!=null){
                Class[] types=method.getParameterTypes();
                method.setAccessible(true);
                addObserver(types,method);
            }
        }
    }

    private static void addObserver(Class[] types,Method o){
        for (Class type:types) {
            if(observers.get(type)==null){
                List list=new ArrayList<>();
                list.add(o);
                observers.put(type,list);
            }else{
                List list=observers.get(type);
                list.add(o);
                observers.put(type,list);
            }
        }
    }
    private static void doPath(File file) {
        if (file.isDirectory()) {//文件夹
            //文件夹我们就递归
            File[] files = file.listFiles();
            for (File f1 : files) {
                doPath(f1);
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
    }

    static Map<Class, List<Method>> getObservers() {
        return observers;
    }

}
