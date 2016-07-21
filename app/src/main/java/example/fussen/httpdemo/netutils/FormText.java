package example.fussen.httpdemo.netutils;

/**
 * 表单提交的参数
 */
public class FormText {

    /*参数的名称*/
    private String mName ;
    /*参数的值*/
    private String mValue ;

    public FormText(String name, String value){
        mName = name;
        mValue = value;
    }

    public String getName(){
        return mName;
    }

    public String getValue(){
        return mValue;
    }
}
