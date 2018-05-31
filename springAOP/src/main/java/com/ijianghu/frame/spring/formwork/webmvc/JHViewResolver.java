package com.ijianghu.frame.spring.formwork.webmvc;

import com.ijianghu.frame.spring.formwork.webmvc.servlet.JHModelAndView;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author kai on
 * @date 2018/5/25 0:02
 */
//设计这个类的主要目的是：
//1、将一个静态文件转变为一个动态文件
    //2、根据用户传送参数不同，产生不同的结果
    //最终输出字符串，交给Response输出
public class JHViewResolver {

    private String viewName;

    private File templateFile;

    public JHViewResolver(String viewName, File templateFile){

        this.viewName = viewName;
        this.templateFile = templateFile;
    }

    public String viewResolver(JHModelAndView mv) throws Exception{
        StringBuffer sb = new StringBuffer();

        RandomAccessFile ra = new RandomAccessFile(this.templateFile,"r");
        String line = null;
        while(null != (line = ra.readLine())){
            Matcher m = matcher(line);
            while (m.find()){
                for(int i =1;i<=m.groupCount();i++){
                    //要把￥{}￥中间的字符串给取出来
                    String paramName = m.group(i);
                    Object paramValue = mv.getModel().get(paramName);
                    if(null == paramValue){continue;}
                    line = line.replaceAll("￥\\{"+paramValue+"\\}",paramValue.toString());
                }
            }
            sb.append(line);
        }
        return sb.toString();
    }

    private Matcher matcher(String str){
        Pattern pattern = Pattern.compile("￥\\{(.+?)\\}￥",Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(str);
        return matcher;
    }


    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public File getTemplateFile() {
        return templateFile;
    }

    public void setTemplateFile(File templateFile) {
        this.templateFile = templateFile;
    }
}
