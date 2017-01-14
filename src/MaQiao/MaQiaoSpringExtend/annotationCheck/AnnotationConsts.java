/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotationCheck;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import MaQiao.MaQiaoSpringExtend.annotation.MQExtenState;
import MaQiao.MaQiaoSpringExtend.annotation.MQExtendClass;
import MaQiao.MaQiaoSpringExtend.annotation.MQExtendMethod;
import MaQiao.MaQiaoSpringExtend.annotation.MQExtendParam;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class AnnotationConsts {
	/** 设置 注解属性 注解类 */
	public static final Class<? extends Annotation> ACC_AnnotationState = MQExtenState.class;
	/** 设置 类 需要定义的注解类 */
	public static final Class<? extends Annotation> ACC_AnnotationClass = MQExtendClass.class;
	/** 设置方法 需要定义的注解类 */
	public static final Class<? extends Annotation> ACC_AnnotationMethod = MQExtendMethod.class;
	/** 设置参数 需要定义的注解类 */
	public static final Class<? extends Annotation> ACC_AnnotationParameter = MQExtendParam.class;
	
	/**
	 * 过滤Class中含有的特别注解，例如：Controller等<br/>
	 * 结果是含有这些注解的类不进入容器，与springmvc等分离
	 */
	public static final List<Class<? extends Annotation>> classAnno_filter = new ArrayList<Class<? extends Annotation>>(1);
	static {
		classAnno_filter.add(org.springframework.stereotype.Controller.class);
	}
	/**
	 * 判断Class中是否含有此注解，例如：@Component等<br/>
	 * 如果没有个列表中的注解，则不进入容器
	 */
	public static final List<Class<? extends Annotation>> classAnno_Allow = new ArrayList<Class<? extends Annotation>>(2);
	static {
		classAnno_Allow.add(org.springframework.stereotype.Component.class);
		classAnno_Allow.add(org.springframework.stereotype.Service.class);
	}

	/**
	 * 过滤方法中含有的特别注解<br/>
	 * 如果没有个列表中的注解，则不进入容器
	 */
	public static final List<Class<? extends Annotation>> methodAnno_filter = new ArrayList<Class<? extends Annotation>>(0);
	static {
	}
	/**
	 * 判断Method中是否含有此注解，例如：@Deprecated等<br/>
	 * 如果没有个列表中的注解，则不进入容器
	 */
	public static final List<Class<? extends Annotation>> methodAnno_Allow = new ArrayList<Class<? extends Annotation>>(0);
	static {
	}
	/**
	 * 过滤参数中含有的特别注解<br/>
	 * 如果没有个列表中的注解，则不进入容器
	 */
	public static final List<Class<? extends Annotation>> parameterAnno_filter = new ArrayList<Class<? extends Annotation>>(0);
	static {
	}
	/**
	 * 判断parameter中是否含有此注解，例如：@PathVariable等<br/>
	 * 如果没有个列表中的注解，则不进入容器
	 */
	public static final List<Class<? extends Annotation>> parameterAnno_Allow = new ArrayList<Class<? extends Annotation>>(0);
	static {
	}
}
