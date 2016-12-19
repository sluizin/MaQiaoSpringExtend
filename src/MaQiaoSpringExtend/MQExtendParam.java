/**
 * 
 */
package MaQiaoSpringExtend;

import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果使用此注解，则参数名称将被严格控制确定<br/>
 * Null:用于参数是否允许null<br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({PARAMETER})
@Documented
@Inherited
public @interface MQExtendParam {

	/**
	 * 是否允许参数为NULL<br/>
	 * 默认为true
	 * @return boolean
	 */
	boolean Null() default true;
}
