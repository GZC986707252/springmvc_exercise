## 数据验证
数据验证主要用于过滤用户输入的错误数据，保证数据的合法性。
数据验证分为**客户端验证** 和 **服务器端验证**。客户端验证主要通过JavaScript代码实现数据合法性校验；服务器端是整个应用程序阻止非法数据的最后防线。
在SpringMVC中，进行数据验证可以利用Spring自带的验证器框架验证数据，也可以使用JSR303实现数据验证。
<!--more-->
### Spring 验证器
#### Validator接口
创建自定义Spring验证器需要实现`org.springframework.validation.Validator`接口，该接口主要有两个方法：
- **`boolean supports(Class<?> aClass)`**：当返回`true`时，验证器可以处理指定的`Class`。
- **`void validate(Object object, Errors errors)`**：该方法功能是验证目标对象`object`，并将验证的错误消息存放在`Errors`对象中。

往`Errors`对象存入错误消息主要有`reject`和`rejectValue`方法：

```java
void reject(String errorCode);
void reject(String errorCode,String defaultMessage);
void rejectValue(String field,String errorCode);
void rejectValue(String field,String errorCode,String defaultMessage)
```
一般只要给reject或者rejectValue方法一个错误代码，SpringMVC就会在消息属性文件中查找错误代码，获取错误消息。例如：
```java
if (productDate != null && productDate.after(new Date())) {
	//date指定验证错误对象的属性域，date.invalid为错误代码
	errors.rejectValue("date", "date.invalid");
}
```
在上述中，“`date.invalid`”就是对应消息属性文件的`key`。例如有个消息属性文件如下：
```java
price.invalid=价格在0和100之间
date.invalid=需要的是一个过去的时间
```
当然也可以直接写入错误消息，即：
```java
errors.rejectValue("date", "需要的是一个过去的时间");
```
#### ValidationUtils类
`org.springframework.validation.ValidationUtils`是一个工具类，提供了相关的方法帮助判断数据是否为空。
例如：
```java
//判断目标对象name属性是否为null或者empty
ValidationUtils.rejectIfEmpty(errors, "name", "商品名称不能为空");
//判断目标对象name属性被trim后是否为null或者empty
ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "商品名称不能为空");
```
#### 验证示例
需要验证的目标对象

```java
public class Product {
	private String name; //不能为空
	private String description; //不能为空
	private double price; //0到100之间
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;  //不能在当前时间之后
	//省略setter和getter方法
}
```

使用Spring验证器进行数据验证，不需要添加额外配置，只需要添加`@Component`注解将自定义验证器类声明为验证组件，并在Controller控制器中使用`@Resource`或者`@Autowired`注解注入自定义验证器。
自定义验证器类
```java
@Component
public class ProductValidator implements Validator {
	@Override
	public boolean supports(Class<?> aClass) {
		// 要验证的model,返回值为false则不验证
		// 判断参数aClass是否是Product本身或者子类
		return Product.class.isAssignableFrom(aClass);
	}
	@Override
	public void validate(Object object, Errors errors) {
		Product product = (Product) object; // object要验证的对象
		// 往Errors对象存入错误消息
		ValidationUtils.rejectIfEmpty(errors, "name", "商品名称不能为空");
		ValidationUtils.rejectIfEmpty(errors, "description", "商品描述不能为空");
		if (product.getPrice() > 100 || product.getPrice() < 0) {
			errors.rejectValue("price", "价格在0和100之间");
		}
		Date productDate = product.getDate();
		// 在系统时间之后
		if (productDate != null && productDate.after(new Date())) {
			errors.rejectValue("date", "创建日期不能是将来时间");
		}
	}
}
```
在控制器中执行验证

```java
@Controller
@RequestMapping("/product")
public class ProductController {
	//省略相关代码
	@Resource
	private Validator validator;
	
	//省略相关代码
	@RequestMapping("/add")
	// BindingResult对象用于接收校验返回的错误消息，必须紧跟在验证对象的后面(两者相邻)
	public String addProduct(Product product, BindingResult result, Model model) {
	//执行数据验证，传入目标对象和错误消息接收对象
		this.validator.validate(product, result);
		if (result.hasErrors()) { // 如果校验错误
			for (FieldError error : result.getFieldErrors()) {
			//后台打印错误消息
				System.out.println(error.getField() + ":" + error.getDefaultMessage());
			}
			//把错误消息写进model,用于前端显示
			model.addAttribute("errors", result.getFieldErrors());
			return "addProduct";
		}
		//省略相关代码
		return "productList";
	}
}
```
### JSR 303验证
JSR 303验证主要有两个方式实现，一个是Hibernate Validator，一个是Apache BVal 。这里主要使用**Hibernate Validator**进行数据验证。

#### 下载
- [整合了所需的jar](https://pan.baidu.com/s/1I2fbOq-u3SQ5ZXJ3RnxwoA) 【提取码：gg7y】

#### 配置验证器
```html
<!-- 配置数据校验 -->
<bean id="localValidator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean">
	<!-- 
		如果将错误消息放在属性文件,则需要配置属性文件，
		并且配置以下属性，将属性文件与HibernateValidator关联
	-->
	<!-- Hibernate校验器 -->
	<!-- <property name="providerClass" value="org.hibernate.validator.HibernateValidator"/> -->
	<!-- 指定校验使用的资源文件，在文件中配置校验错误消息，如果不指定，默认使用classpath下的ValidationMessages.properties -->
	<!-- <property name="validationMessageSource" ref=""/> -->
</bean>
<!-- 配置属性文件关联HibernateValidator时需要设置validator的值-->
<mvc:annotation-driven validator="localValidator"/>
```
如果没有属性文件，则直接配置`LocalValidatorFactoryBean`即可。
```html
<bean id="localValidator" class="org.springframework.validation.beanvalidation.LocalValidatorFactoryBean"/>
```
#### 标注类型
JSR 303不需要写验证器，但需要使用它的标注类型在模型属性上进行约束。
##### 空检查
- `@Null` ：验证对象是否为null，不为null即验证错误。
- `@NotNull` ：验证对象是否为null，无法检测长度为0的字符串。
- `@NotBlank`：检查约束字符串是不是null，并且判断被trim后长度是否大于0。
- `@NotEmpty` ：检查约束元素是否为null，或者为empty。

```java
@NotBlank(message = "商品名不能为空")
private String name;
```
##### boolean检查
- `@AssertTrue`：验证boolean属性是否为true。
- `@AssertFalse`：验证boolean属性是否为true。
##### 长度检查
- `@Size(min= ,max= )`：验证对象（Array、Collection、Map、String）长度是否在给定范围。
- `@Length(min= ,max= )`：验证字符串长度是否在给定范围。
##### 日期检查
- `@Past`：验证Date和Calendar对象是否在当前时间之前。
- `@Future`：验证Date和Calendar对象是否在当前时间之后。
```java
@Past(message = "创建日期需要是一个过去时间")
private Date date;
```
##### 数值检查
- `@Min`：验证Number和String对象是否大于指定值。
- `@Max`：验证Number和String对象是否小于指定值。
- `@DecimalMax`： 被标注的值必须不大于约束中指定的最大值，这个约束的參数是一个通过BigDecimal定义的最大值的字符串表示，小数存在精度。
- `@DecimalMin`：被标注的值必须不小于约束中指定的最小值，这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示，小数存在精度。
- `@Digits`： 验证Number和String的构成是否合法。
- `@Digits(integer= , fraction=)`: 验证字符串是否符合指定格式的数字，integer 指定整数精度，fraction 指定小数精度。
- `@Range(min= , max= )`：检查数字是否介于min和max之间。
- `@CreditCardNumber`：信用卡验证。
- `@Email`：验证是否为邮件地址，如果为null，不进行验证， 直接通过。
```java
@Range(min = 0, max = 100, message = "价格只能在0和100之间")
private double price;
```

##### 其他检查
- `@Pattern`：验证String对象是否符合正则表达式。
- `@Valid`： 对关联对象进行校验，如果关联对象是个集合或者数组，那么对其中的元素进行校验，如果是一个map， 则对其中的值部分进行校验。

在Controller控制器中的处理方法中，通过`@Valid`注解指定需要验证的对象。
```java
@RequestMapping("/add")
// BindingResult对象用于接收校验返回的错误消息，必须紧跟在验证对象的后面(两者相邻)
public String addProduct(@Valid Product product, BindingResult result, Model model) 
```
#### 验证示例
需要验证的目标对象类

```java
public class Product {
	@NotBlank(message = "商品名不能为空")
	private String name;

	@NotBlank(message = "商品描述不能为空")
	private String description;

	@Range(min = 0, max = 100, message = "价格只能在0和100之间")
	private double price;

	@Past(message = "创建日期需要是一个过去时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date date;
	//省略setter和getter方法
}
```
控制器关键代码

```java
@Controller
@RequestMapping("/product")
public class ProductController {
	// 获得日志记录对象
	private static final Log logger = LogFactory.getLog(ProductController.class);
	@Autowired
	private ProductService productService;
	
	@RequestMapping("/add")
	// BindingResult对象用于接收校验返回的错误消息，必须紧跟在验证对象的后面(两者相邻)
	public String addProduct(@Valid Product product, BindingResult result, Model model) {
		if (result.hasErrors()) { // 如果校验错误
			for (FieldError error : result.getFieldErrors()) {
				System.out.println(error.getField() + ":" + error.getDefaultMessage());
			}
			model.addAttribute("errors", result.getFieldErrors());
			return "addProduct";
		}
		if (!productService.add(product)) {
			return "addProduct";
		}
		logger.info("添加成功");
		model.addAttribute("products", productService.getProducts());
		return "productList";
	}
}
```
### 运行效果
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200323014337648.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)
![在这里插入图片描述](https://img-blog.csdnimg.cn/20200323014447164.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3FxXzQxMTkzMTMz,size_16,color_FFFFFF,t_70)
