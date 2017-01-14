/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 与@MQExtendClass、@MQExtendMethod、@MQExtendParam同级，用于修饰使用<br/>
 * 针对注解，加入属性，可暂停以上注解的使用[针对扩展系统]<br/>
 * 删除此注解，则以上注解全程有效<br/>
 * author="Sunjian"注解标识开发人员<br/>
 * 判断状态与前后两个日期的关系。精确到日<br/>
 * 1:"2014-01-01","" :当前日期在2014-01-01之后则返回true<br/>
 * 2:"2014-01-01","2014-02-01" :当前日期在2014-01-01与2014-02-01之间则返回true<br/>
 * 3:"","2014-02-01" :当前日期在2014-02-01之前则返回true<br/>
 * 4:"","" :日期格式不正确或2个日期同时为空则返回false<br/>
 * <font color='red'>如果在时间线段或射线之间，则等同invalid=true</font><br/>
 * invalid=true。则以上注解失效，默认为false，则进行日期上的判断<br/>
 * <font color='red'>注意：此注解只有在服务启动时才能进行判断</font><br/>
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ TYPE, METHOD, PARAMETER })
@Documented
@Inherited
public @interface MQExtenState {
	/**
	 * 开发人员
	 * @return String
	 */
	String author() default "";

	/**
	 * 环境变量[犹豫是否保留，功能已开发]<br/>
	 * 当使用环境变量时<br/>
	 * 则搜索maven中environment的值比较，如果出现，则同级注解有效<br/>
	 * 没有则其它环境同级注解失效<br/>
	 * 注意：在web.xml中加入
	 * 
	 * <pre>
	 * 	&lt;context-param>    
	 *     		&lt;param-name>spring.profiles.active&lt;/param-name>    
	 *     		&lt;param-value>dev&lt;/param-value><!-- maven运行时：${package.environment} -->   
	 * 	&lt;/context-param>
	 * </pre>
	 * @return String[]
	 */
	@Deprecated
	String[] environment() default {};

	/**
	 * 失效性开始时间<br/>
	 * 格式:2016-01-05
	 * @return String
	 */
	String start() default "";

	/**
	 * 失效性结束时间<br/>
	 * 格式:2016-12-01
	 * @return String
	 */
	String end() default "";

	/**
	 * invalid：失效<br/>
	 * True:此注解失效[默认为false]<br/>
	 * 不使用此参数时，则进行其它判断<br/>
	 * @return boolean
	 */
	boolean invalid() default false;

	/**
	 * 简单说明
	 * @return ""
	 */
	String explain() default "";
}
