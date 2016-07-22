# HttpDemo
volley+okhttp 框架 访问网络

##简介

> 支持post，get请求，上传单个文件，上传多个文件，表单提交
       
##如何一行代码就可以访问网络
 
###1，访问网络带参数post请求
  

```
   String url = "http://dev.bodyplus.cc:8088/api/users?do=smsCode";
        //参数
        Map<String, String> params = new HashMap<>();
        params.put("smsType", "1");
        params.put("smsPhone", "18219200511");
        params.put("type", "2");
        //最主要的就是这一行
        NetworkUtils.getInstance().sendPostRequest(url, params, new RequestCallback() {
            @Override
            public void onSuccess(Object object) {

                Log.d("1008611", object.toString());
                data.setText(object.toString());
            }

            @Override
            public void onFail(String string) {
                Log.d("1008611", string);
                data.setText(string);
            }
        });
     
```
     
     
这需要调用上面这个方法就可以访问网络了，是不是很简单
###2，上传一个带参数的文件


  

```
	    //上传地址
        String url = "";

        //参数
        Map<String, String> params = new HashMap<>();

        params.put("key", "value");

        String path = "";
        //需要上传的文件
        File file = new File(path);

        NetworkUtils.getInstance().uploadFile(url, params, "file", file, new RequestCallback() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onFail(String string) {
            }
        });
```
##如何使用

很简单，按照我说的做，你就可以使用啦，
>netutils的包下就是这个访问网络的所有类，所以直接复制到你的项目里


![这里写图片描述](http://img.blog.csdn.net/20160721210506697)

>还有一个就是libs下有两个jar包，也复制到你的项目里

![这里写图片描述](http://img.blog.csdn.net/20160722093651292)
>另外一个就是gradle文件
    
	dependencies {
	    compile 'eu.the4thfloor.volley:com.android.volley:2015.05.28'
	    compile 'com.squareup.okhttp3:okhttp:3.4.0-RC1'
	    compile 'com.google.code.gson:gson:2.7'
	    compile files('libs/org.apache.http.legacy.jar')
	    compile files('libs/httpmime-4.2.5.jar')
	}


>做完了这些，就算移植成功了，恭喜你，可以用volley+okhttp访问网络了。

