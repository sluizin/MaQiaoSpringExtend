 *通过json控制运行各个类的各个方法的顺序，并得到结果<br/>
 *spring.xml配置文件中加入：<code><pre>
 &lt;!-- 引入相应的上下文用于检索容器内的bean -->
 &lt;context:component-scan base-package="com.wangku.was.special.MaQiaoSpringExtend" /></pre></code>
 *<br/>
 * 注解方法，以确定哪些类的哪些方法可以向外提供服务以及参数的限制<br/>
 * <font color='red'>支持类的注解:@Component;@Scope("singleton");@Service</font><br/>
 * 必须先注解类，再注解方法，否则无法找到此类的方法[先检索类是否含有注解，舍弃接口]<br/>
 * 因为使用asm，所以暂时不支持继承注解<br/>
 * <font color='red'>注意：严禁在此注解下的方法里使用MQExtendSystem，为了防止嵌套死循环[初步判断StackTraceElement]</font><br/>
 * 复杂度加高，把方法名和groupid的检索暂缓开发<br/>
 <br/>
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
        "allowNull": true
    }
}
 </pre></code> 