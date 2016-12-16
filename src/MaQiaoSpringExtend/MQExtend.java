/**
 * 
 */
package MaQiaoSpringExtend;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.LOCAL_VARIABLE;
/*
 * 声明类的泛型类型 jdk8新添
 * import static java.lang.annotation.ElementType.TYPE_PARAMETER;
 * */
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解方法，以确定哪些类的哪些方法可以向外提供服务以及参数的限制<br/>
 * <font color='red'>支持类的注解:@Component;@Scope("singleton");@Service</font><br/>
 * 必须先注解类，再注解方法，否则无法找到此类的方法[先检索类是否含有注解，舍弃接口]<br/>
 * 因为使用asm，所以暂时不支持继承注解<br/>
 * 如果两个方法值相同，具体哪个先运行[上帝安排]<br/>
 * <font color='red'>注意：严禁在此注解下的方法里使用MQExtendSystem，为了防止嵌套死循环[初步判断StackTraceElement]</font><br/>
 * 复杂度加高，把方法名和groupid的检索暂缓开发<br/><br/>
 * METHOD:identifier:用于方法标识符<br/>
 * METHOD:groupid:项目技术性分组<br/>
 * METHOD:explain:用于简单说明<br/>
 * PARAMETER:Null:用于参数是否允许null(如果参数中含有，则参数名严格确定)<br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE, METHOD, PARAMETER, LOCAL_VARIABLE })
@Documented
@Inherited
public @interface MQExtend {
	/**
	 * 标识符 区分大小写
	 * @return String
	 */
	String identifier() default "";

	/**
	 * 运行项目组编号默认为0，方法建立最后设置间隔为100<br/>
	 * @return int
	 */
	int groupid() default 0;

	/**
	 * 简单说明
	 * @return ""
	 */
	String explain() default "";

	/**
	 * 是否允许参数为NULL<br/>
	 * 默认为true
	 * @return boolean
	 */
	boolean Null() default true;

}
