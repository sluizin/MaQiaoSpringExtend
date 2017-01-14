 *通过json控制运行各个类的各个方法的顺序，并得到结果<br/>
 *spring.xml配置文件中加入：<code><pre>
 &lt;!-- 引入相应的上下文用于检索容器内的bean -->
 &lt;context:component-scan base-package="MaQiao.MaQiaoSpringExtend" /></pre></code>
 *<br/>
 * 注解方法，以确定哪些类的哪些方法可以向外提供服务以及参数的限制<br/>
 * <font color='red'>支持类的注解:@Component;@Scope("singleton");@Service</font><br/>
 * 必须先注解类，再注解方法，否则无法找到此类的方法[先检索类是否含有注解，舍弃接口]<br/>
 * 因为Java只支持一个父类，所以为了避开继承，如果使用接口则不能使用extends InitializingBean,DisposableBean，直接从接口省掉生成与销毁Bean的自动注册方法[可以保留Bean的注册机制]<br/>
 * 因为使用asm，所以暂时不支持继承注解<br/>
 * <font color='red'>注意：严禁在此注解下的方法里使用MQExtendSystem，为了防止嵌套死循环[初步判断StackTraceElement]</font><br/>
 * 复杂度加高，把方法名和groupid的检索暂缓开发<br/>
 * 说明：<br/>
 * web.xml文件中加入maven环境变量：<br/>
<code><pre>
	<context-param>    
    	<param-name>spring.profiles.active</param-name>    
    	<param-value>dev</param-value><!-- maven运行时：${package.environment} -->    
	</context-param> 
</pre></code>
 * 在类、方法、方法参数上加上各自的注解：
 <code><pre>
 	类[完全标准]
	@MQExtenState(author="Sunjian",start = "2017-01-01", end = "2017-02-01", invalid = false,environment={"dev","test"})
	@MQExtendClass(value = "例类", explain = "关于某个提取数据表的操作的扩展", groupid = 0,version="1.0")
	public class XXXXXXXXX {

	方法与参数[完全标准]
	@MQExtenState(author="Sunjian",start = "2017-01-01", end = "2017-02-01", invalid = false,environment={"dev"})
	@MQExtendMethod(value = "com.XXX.was.bank.money.class.name", groupid = 3, explain = "此方法只把某个变量加10")
	public final void addmoneyXXX(@MQExtenState(author="Sunjian",start = "2017-01-01", end = "2017-02-01", invalid = false) @MQExtendParam(Null = true) Map<String, Object> model,Integer size ){
	
	简要说明：
	1:MQExtenState注解：
	控制@MQExtendClass、@MQExtendMethod、@MQExtendParam这三个注解的失效与否
	如果invalid=true或当前日期在start至end之间或在start之后或end之前[2个日期其中有一个格式正确]，则同级注解失效，其它则同级注解有效[包括日期格式不正确等]
	2:@MQExtendClass注解：
	只负责向系统说明此类为扩展类，参与检索，具体内部注解值暂无意义
	直接使用注解来区分类，暂时不换成接口区分
	3:@MQExtendMethod注解：
	此注解负责确定哪些方法用于系统检索。
	其中value注解值与json中的array的字符串数组相关，array数据组直接与此value进行判断
	4:@MQExtendParam注解：
	此注解用于修饰方法参数，
	如果方法参数中含此注解，则方法参数的参数名用于匹配检索，其中Null注解值用于判断匹配类型时允许为null。
	[没有找到此类型对象时]
	当Null为true时，则匹配方法参数时，此参数的对象允许为null
	当Null为false时，则匹配方法参数时，此参数的对象为此类型的newInstance()
	当json设置允许null与注解允许null，冲突，注解优先级高
	匹配规则：
	有参数注解：名称 > 顺序 > 类型 > null
	无参数注解：顺序 > 类型 > null
	
	详情请参考注解说明

 </pre></code>
 <code><pre>
 附录Json:
 {
    "identifier-": "如果使用这个判断则直接取消前面 - 号",
    "identifier": {
        "matching-": "是否使用indeof如果为真则使用indexOf进行判断，如果为假则直接对字符串进行比较equals",
        "matching": false,
        "order-": "是否进行排序，如果为真则以Array组进行排序依次进入入口端寻找，如果为假则直接使用入口端List直接寻找",
        "order": true,
        "array-": "具体的扫描对象数组",
        "array": [
            "00",
            "ee",
            "11",
            "AAA",
            "cc",
            "xx",
            "aa",
            "YYYY",
            "TTTT"
        ]
    },
    "setupSystem-": "系统设置",
    "setupSystem": {
        "allowMulti-": "系统允许运行多个对象[同类]",
        "allowMulti": false,
        "ignoreCase-": "是否忽略大小写",
        "ignoreCase": false
    },
    "setupParameter-": "参数设置",
    "setupParameter": {
        "isMulti-": "允许一个变量同时给多个参数赋值",
        "isMulti": false,
        "allowNull-": "如果参数只没有找到相应的变量，则允许赋NULL",
        "allowNull": false
    }
}
 </pre></code> 