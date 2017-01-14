/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotation;

import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 如果使用此注解，则参数名称将被严格控制确定<br/>
 * 匹配规则：<br/>
 * 有参数注解：名称 > 顺序 > 类型 > null<br/>
 * 无参数注解：顺序 > 类型 > null<br/>
 * Null:true 匹配时 默认为null<br/>
 * Null:false 匹配时 默认为newInstance()<br/>
 * Null:用于参数是否允许null[默认为false]<br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ PARAMETER })
@Documented
@Inherited
public @interface MQExtendParam {

	/**
	 * 是否允许参数为NULL<br/>
	 * 默认为false
	 * @return boolean
	 */
	boolean Null() default false;
}
