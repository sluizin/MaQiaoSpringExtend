/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotation;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 确定哪些类需要提供服务<br/>
 * <font color='red'>支持类的注解:@Component;@Scope("singleton");@Service</font><br/>
 * 必须先注解类[先检索类是否含有注解，舍弃接口]<br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({TYPE})
@Documented
@Inherited
public @interface MQExtendClass {
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
	/**
	 * 版本
	 * @return String
	 */
	String version() default "";

}
