package com.ijianghu.frame.spring.formwork.aop;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kai on
 * @date 2018/5/31 7:01
 */
//对application中expression的java代码封装
public class JHAopConfig {

    private Map<Method,JHAspect> points = new HashMap<Method,JHAspect>();

    public void put(Method target,Object aspect,Method[] points){
        this.points.put(target,new JHAspect(aspect,points));
    }

    public JHAspect get(Method method){
        return this.points.get(method);
    }

    public boolean contains(Method method){
        return this.points.containsKey(method);
    }

    public class JHAspect{
        private Object aspect;
        private Method[] points;

        public JHAspect(Object aspect,Method[] points){
            this.aspect = aspect;
            this.points = points;
        }

        public Object getAspect() {
            return aspect;
        }

        public void setAspect(Object aspect) {
            this.aspect = aspect;
        }

        public Method[] getPoints() {
            return points;
        }

        public void setPoints(Method[] points) {
            this.points = points;
        }
    }
}
