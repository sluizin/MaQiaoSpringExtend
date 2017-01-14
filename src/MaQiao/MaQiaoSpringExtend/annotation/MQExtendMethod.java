/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 因为使用asm，所以暂时不支持继承注解<br/>
 * 如果两个方法值相同，具体哪个先运行[上帝安排]<br/>
 * <font color='red'>注意：严禁在此注解下的方法里使用MQExtendSystem，为了防止嵌套死循环[初步判断StackTraceElement]</font><br/>
 * 复杂度加高，把方法名和groupid的检索暂缓开发<br/><br/>
 * METHOD:value:用于方法标识符<br/>
 * METHOD:groupid:项目技术性分组<br/>
 * METHOD:explain:用于简单说明<br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({METHOD})
@Documented
@Inherited
public @interface MQExtendMethod {
	/**
	 * 标识符 区分大小写
	 * @return String
	 */
	String value() default "";

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

}
