
### 拦截器概述
SpringMVC的拦截器和Java Servlet的过滤器（Filter）类似，主要用于拦截用户请求并进行相关处理，通常应用在权限验证、记录请求信息的日志、判断用户登录等功能上。

### 拦截器的定义
在SpringMVC中定义一个拦截器需要对拦截器进行定义和配置。定义一个拦截器主要有两种方式：

- 通过实现`HandlerInterceptor`接口或者继承`HandlerInterceptor`接口的实现类来定义。
- 通过实现`WebRequestInterceptor`接口或者继承`WebRequestInterceptor`接口的实现类来定义。

```java
public class TestInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle方法在控制器处理请求方法前执行");
        return true;
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle方法在控制器处理方法调用之后，返回视图之前执行");
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterComletion方法在控制器处理方法执行完毕后执行，即视图渲染结束后执行");
    }
}
```
上述拦截器定义中实现了`HandlerInterceptor`接口，并实现了接口中的3个方法。

- `preHandle` 方法：该方法在控制器处理请求方法之前执行，其返回值表示是否中断后续操作。`true`代表请求放行，继续向下执行；`false`表示中断后续操作。
- `postHandle` 方法：该方法在控制器处理请求方法调用之后，返回视图之前执行。
- `afterCompletion` 方法：该方法在控制器处理请求方法执行完毕后执行，即视图渲染结束后执行。

### 拦截器的配置
拦截器定义完成后，还需要在SpringMVC的配置文件中配置才能使用。
```html
<!--配置拦截器-->
<mvc:interceptors>
    <!--配置一个全局拦截器，拦截所有请求-->
    <bean class="interceptor.TestInterceptor"/>
    <!--配置指定路径的拦截器-->
    <mvc:interceptor>
        <!--配置拦截器作用的路径 "/**"表示拦截一切请求-->
        <mvc:mapping path="/**"/>
        <!--配置不需要拦截作用的路径-->
        <mvc:exclude-mapping path="xxx"/>
        <!--指定拦截器-->
        <bean class="interceptor.Interceptor1"/>
    </mvc:interceptor>
    <mvc:interceptor>
     	<!--配置拦截器作用的路径 拦截以"/interceptor/test"结尾的路径-->
      	<mvc:mapping path="/interceptor/test"/>
     	<!--指定拦截器-->
     	<bean class="interceptor.Interceptor2"/>
  	</mvc:interceptor>
</mvc:interceptors>
```
上述配置中，`<mvc:mapping ... />` 和`<mvc:exclude-mapping ... />`可以根据需要配置多个；“`*`”作为通配符，只能匹配一层路径，例如`/user/*`，可以匹配`/user/xxx`、`user/xxx`，但是不能匹配`/user/xxx/xxx`、`/user/xxx/xxx/xxx`，如果需要匹配多层任意路径，则需要这样写“`/user/**`”。

需要注意的是，`<mvc:interceptor>`的子元素必须按照`<mvc:mapping ... />` 、`<mvc:exclude-mapping ... />` 、`<bean class="xxx"/>`的顺序配置。

### 拦截器的执行流程
#### 单个拦截器执行流程
如果之定义一个拦截器，那么拦截到请求时，首先执行`preHandle`方法，如果该方法返回`true`，则继续执行控制器的处理请求方法，返回`false`就中断后续执行。如果`preHandle`方法返回`true`，并且控制器的处理请求方法执行后，返回视图前 执行`postHandle`方法，返回视图后才执行`afterCompletion`方法。

**例如：在利用前面定义一个`TestInterceptor`拦截器，对其进行配置：**

```html
    <!--配置拦截器-->
    <mvc:interceptors>
        <!--配置一个全局拦截器，拦截所有请求-->
        <bean class="interceptor.TestInterceptor"/>
    </mvc:interceptors>
```
**然后再创建一个处理请求的InterceptorController 控制器：**
```java
@Controller
@RequestMapping("/interceptor")
public class InterceptorController {
    @RequestMapping("/test")
    public String testInterceptor() {
        System.out.println("正在测试拦截器,执行控制器中的处理请求方法中");
        return "/interceptor/test.jsp";
    }
}
```
**创建JSP视图文件：**
```java
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
渲染视图...
<% System.out.println("视图渲染结束...");%>
</body>
</html>
```
访问`/interceptor/test`地址，运行后结果：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200315153332600.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)
#### 多个拦截器执行流程
当需要配置多个拦截器同时工作时，它们的`preHandle`方法将按照配置文件的顺序执行，`postHandle`方法和`afterCompletion`方法则按照配置顺序的反序执行。

**现在，在前面的例子基础上，定义两个拦截器，分别为`Interceptor1`和`Interceptor2`。**

**配置拦截器：**
```html
<mvc:interceptors>
    <!--配置指定路径的拦截器-->
    <mvc:interceptor>
        <!--配置拦截器作用的路径 "/**"表示拦截一切请求-->
        <mvc:mapping path="/**"/>
        <!--指定拦截器-->
        <bean class="interceptor.Interceptor1"/>
    </mvc:interceptor>
    <mvc:interceptor>
     	<!--配置拦截器作用的路径 拦截以"/interceptor/test"结尾的路径-->
      	<mvc:mapping path="/interceptor/test"/>
     	<!--指定拦截器-->
     	<bean class="interceptor.Interceptor2"/>
  	</mvc:interceptor>
</mvc:interceptors>
```
最后运行结果 ：
![在这里插入图片描述](https://img-blog.csdnimg.cn/2020031515450520.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)
