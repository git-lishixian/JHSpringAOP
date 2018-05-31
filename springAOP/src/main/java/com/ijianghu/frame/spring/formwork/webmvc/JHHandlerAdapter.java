package com.ijianghu.frame.spring.formwork.webmvc;

import com.ijianghu.frame.spring.formwork.webmvc.servlet.JHModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;

/**
 * @author kai on
 * @date 2018/5/24 23:58
 */
//解耦 专人干专事
public class JHHandlerAdapter {

    private Map<String,Integer> paramMapping;

    public JHHandlerAdapter(Map<String,Integer> paramMapping){
        this.paramMapping = paramMapping;
    }

    /**
     *
     * @param req
     * @param resp
     * @param handler  为什么把handler 传进来 ，因为handler中包含了controlelr,method.url信息
     * @return
     */
    public JHModelAndView handle(HttpServletRequest req, HttpServletResponse resp, JHHandlerMapping handler) throws  Exception{
        //根据用户请求的参数信息，跟method中的参数信息进行动态匹配
        //resp 传进来的目的只有一个：只是为了将其赋值给方法参数，仅此而已


        //只有当用户传过来的ModelAndView为空的时候，才会new一个默认的

        //1、要准备好这个方法的形参列表
        //方法重载：形参的决定因素：参数个数、参数类型、参数顺序、方法的名字
        Class<?>[] paramTypes = handler.getMethod().getParameterTypes();
        //2、拿到自定义命名参数所在的位置
        //用户通过url传过来的参数列表
        Map<String,String[]> reqParameterMap = req.getParameterMap();
        //3、构造实参列表
        Object[] paramValues = new Object[paramTypes.length];
        for (Map.Entry<String,String[]> param: reqParameterMap.entrySet()) {
            String value = Arrays.toString(param.getValue()).replaceAll("\\[|\\]", "").replaceAll("\\s","");

            if(!this.paramMapping.containsKey(param.getKey())){continue;}
            int index = this.paramMapping.get(param.getKey());

            //因为页面上传过来的值都是String类型的，而在方法中定义的类型是千变万化的
            //要针对我们传过来的参数进行类型转换
            paramValues[index] = castStringValue(value,paramTypes[index]);
        }

        Integer reqIndex = this.paramMapping.get(HttpServletRequest.class.getName());
        paramValues[reqIndex] = req;

        Integer respIndex = this.paramMapping.get(HttpServletResponse.class.getName());
        paramValues[respIndex] = resp;
        //4、从handler中取出controller、method,然后利用反射机制进行调用

        Object result = handler.getMethod().invoke(handler.getController(), paramValues);

        if(result == null){return null;}

        boolean isModelAndView = handler.getMethod().getReturnType() == JHModelAndView.class;

        if(isModelAndView){
            return (JHModelAndView)result;
        }else{
            return null;
        }
    }

    private Object castStringValue(String value,Class<?> clazz){
        Object result = null;
        if(clazz == String.class){
            result = String.valueOf(value);
        }else if(clazz == Integer.class){
            result = Integer.valueOf(value);
        }else if(clazz == int.class){
            result  = Integer.valueOf(value).intValue();
        }else{
            result = value;
        }

        return result;
    }
}
