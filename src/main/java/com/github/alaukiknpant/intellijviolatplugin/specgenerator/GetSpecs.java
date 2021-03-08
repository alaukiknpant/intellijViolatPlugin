package com.github.alaukiknpant.intellijviolatplugin.specgenerator;

import com.github.cliftonlabs.json_simple.JsonObject;

import java.lang.reflect.Method;
import java.util.*;

public class GetSpecs {

    public GetSpecs() {
    }

    public static JsonObject getSpecs(Class cls) throws ClassNotFoundException {
//        Class cls = Class.forName(methodName);
//        Package pack = cls.getPackage();
//        String packageName = pack.getName();
        String className = cls.getName();
//        className = packageName + "." + className;
        Method[] methods = cls.getMethods();
        ArrayList<Method> methods1 = new ArrayList<Method>();
        int idx = 0;
        while (idx < methods.length && !methods[idx].getName().equals("wait")) {
            methods1.add(methods[idx]);
            idx++;
        }
        methods = methods1.toArray(new Method[0]);
        Methoed[] methoeds = new Methoed[methods.length];
        for (int i = 0; i < methods.length; i++) {
            String method_name = methods[i].getName();
            System.out.println(method_name);
            Class[] parameters = methods[i].getParameterTypes();
            ArrayList<String> pars = new ArrayList<String>(parameters.length);
            for (int q = 0; q < parameters.length; q++) {
                Class parameterType = parameters[q];
                String parameter_name = parameterType.getName();
                pars.add(parameter_name);
            }
            boolean isReturnVoid = methods[i].getReturnType().equals(Void.TYPE);
            Methoed m = new Methoed(method_name, pars, isReturnVoid, true, true, "complete");
            methoeds[i] = m;
        }
        ClassJSON c = new ClassJSON(className, 3, methoeds);
        return c.classToJSON();
    }
}

