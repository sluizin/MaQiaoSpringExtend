/**
 * 
 */
package MaQiaoSpringExtend;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.ModelAndView;

import sun.misc.Unsafe;

/**
 * 常量池
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQSpringConsts {
	/** 是否允许调用static方法[暂时不开发静态方法的调用] */
	static final boolean ACC_allowStatic = false;
	/** 是否允许调用public方法 */
	public static boolean ACC_allowPublic = true;
	/** 是否允许调用private方法 */
	public static boolean ACC_allowPrivate = false;
	/** 是否允许调用protected方法 */
	public static boolean ACC_allowProtected = false;
	/** 是否允许调用带有可变数量的参数 */
	static final boolean ACC_allowVarArgs = false;
	/**
	 * 判断Method是否需要提出，从方法的修饰符和方法的其它方面进行判断
	 * @param method Method
	 * @return boolean
	 */
	static boolean allowInputMethod(final Method method) {;
		return allowInputMethodModifier(method.getModifiers()) && allowInputMethodParameter(method) ;
	}
	/**
	 * 判断Method是否需要提出，只方法的修饰符的判断
	 * @param modifier int
	 * @return boolean
	 */
	private static boolean allowInputMethodModifier(final int modifier) {
		if (!ACC_allowStatic && Modifier.isStatic(modifier)) return false;
		if (!ACC_allowPrivate && Modifier.isPrivate(modifier)) return false;
		if (!ACC_allowProtected && Modifier.isProtected(modifier)) return false;
		if (!ACC_allowPublic && Modifier.isPublic(modifier)) return false;
		return true;
	}
	/**
	 * 判断Method是否需要提出，按方法
	 * @param modifier int
	 * @return boolean
	 */
	private static boolean allowInputMethodParameter(final Method method) {
		if (!ACC_allowVarArgs && method.isVarArgs()) return false;
		return true;
	}

	static final AnnoMQExtendElement ACC_annotion = new AnnoMQExtendElement();
	/**
	 * 指定路径数组中检索接口
	 */
	@Deprecated
	public static final int ACC_Search_Appoint = 1;
	/**
	 * 在ioc中检索接口和对象
	 */
	public static final int ACC_Search_IOC = 2;
	/**
	 * 在base中检索接口和对象
	 */
	public static final int ACC_Search_Base = 4;

	/**
	 * 过滤Class中含有的特别注解，例如：Controller等<br/>
	 * 结果是含有这些注解的类不进入容器，与springmvc等分离
	 */
	static final List<Class<? extends Annotation>> classAnnotationFilter = new ArrayList<Class<? extends Annotation>>();
	static {
		classAnnotationFilter.add(org.springframework.stereotype.Controller.class);
	}
	static final List<Class<? extends Annotation>> classAnnotationAllow = new ArrayList<Class<? extends Annotation>>();
	static {
		classAnnotationAllow.add(org.springframework.stereotype.Component.class);
		classAnnotationAllow.add(org.springframework.stereotype.Service.class);
	}	
	/** 是否输出初始化状态 */
	public static boolean ACC_MQInitViewwarning = true;
	/** 初始化状态的前缀 */
	static final String ACC_MQInitHead = "MQExtend->Initialization->";
	/** 是否输出运行状态 */
	public static boolean ACC_MQRunViewwarning = true;
	/** 输出运行状态的前缀 */
	static final String ACC_MQRunHead = "MQExtend->Run->";

	/**
	 * 注解对象中各属性名称，用于读取注解值时使用相对字符串
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	static final class AnnoMQExtendElement {
		/** 标识符名称 */
		String identifier = "identifier";
		/** 说明 */
		String explain = "explain";
		/** 方法分组 */
		String groupid = "groupid";
		/** 参数是否允许null */
		String Null = "Null";
	}
	public static final Unsafe UNSAFE;
	static {
		try {
			final Field field = Unsafe.class.getDeclaredField("theUnsafe");
			field.setAccessible(true);
			UNSAFE = (Unsafe) field.get(null);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
