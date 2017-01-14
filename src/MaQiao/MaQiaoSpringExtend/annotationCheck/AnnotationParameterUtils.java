/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.annotationCheck;

import java.lang.annotation.Annotation;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class AnnotationParameterUtils {
	/**
	 * 把方法注解转成状态对象，用于检索
	 * @param p Annotation
	 * @return MQExtendStateClass
	 */
	public static final MQExtendParameterClass getParameter(Annotation p) {
		if (p == null) return null;
		if (!AnnotationConsts.ACC_AnnotationParameter.isInstance(p)) return null;
		MQExtendParameterClass e = new MQExtendParameterClass();
		e.Null = MQAnnotation.getBoolean(p, "Null", true);
		return e;
	}

	/**
	 * 类注解类
	 * @author Sunjian
	 * @version 1.0
	 * @since jdk1.7
	 */
	public static final class MQExtendParameterClass {
		boolean Null = false;

		public final boolean isNull() {
			return Null;
		}

		public final void setNull(boolean null1) {
			Null = null1;
		}

	}
}
