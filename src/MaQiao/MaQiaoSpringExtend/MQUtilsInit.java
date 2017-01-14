/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;

import MaQiao.MaQiaoSpringExtend.Element.ElementMethod;
import MaQiao.MaQiaoSpringExtend.Element.ElementParameter;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationClassUtils;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationConsts;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationMethodUtils;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationParameterUtils;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationStateUtils;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationMethodUtils.MQExtendMethodClass;
import MaQiao.MaQiaoSpringExtend.annotationCheck.AnnotationParameterUtils.MQExtendParameterClass;
//import MaQiao.MaQiaoSpringExtend.AnnotationCheck.MQAnnotation;
//import MaQiao.MaQiaoSpringExtend.AnnotationCheck.AnnotationUtils;
import MaQiao.MaQiaoSpringExtend.asm.ReadClassAsm;
import MaQiao.MaQiaoSpringExtend.warning.Warning;

/**
 * 初始化时需要的方法
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQUtilsInit {

	/**
	 * 通过上下文按 <code>@Scope("singleton")</code> 进行检索。含有扩展注解的，则提出保存
	 * @param ac ApplicationContext
	 */
	@Deprecated
	static void setApplicationContextClassScopeSingleton(ApplicationContext ac) {
		if (ac == null) return;
		Map<String, Object> map = ac.getBeansWithAnnotation(Scope.class);
		for (String key : map.keySet()) {
			Object obj = map.get(key);
			Warning.show(0, "Scope.class[key:" + key + "]", obj.toString());
			if(!AnnotationClassUtils.isCorrectClass(obj))continue;
			//if (!AnnotationUtils.isMQExtendClass(obj)) continue;
			Warning.show(0, "Scope.class[key:" + key + "]", obj.toString());
			MQApplicationcontext.register(obj);
		}
	}

	/**
	 * 通过上下文按允许的注解组进行检索。含有扩展注解的，则提出保存
	 * @param ac ApplicationContext
	 */
	public static void setApplicationContextClassAllow(ApplicationContext ac) {
		if (ac == null) return;
		for (int i = 0, len = AnnotationConsts.classAnno_Allow.size(); i < len; i++) {
			Map<String, Object> map = ac.getBeansWithAnnotation(AnnotationConsts.classAnno_Allow.get(i));
			for (String key : map.keySet()) {
				Object obj = map.get(key);
				Warning.show(0, AnnotationConsts.classAnno_Allow.get(i).getName() + "[key:" + key + "]", obj.toString());
				MQApplicationcontext.register(obj);
			}
		}
	}


	/**
	 * 把类中的某个方法转成ElementMethod
	 * @param classzz Class< ? >
	 * @param method Method
	 * @return ElementMethod
	 */
	static final ElementMethod getEM(final Class<?> classzz, final Method method) {
		final ElementMethod e = new ElementMethod();
		e.classzz = classzz;
		e.method = method;
		Annotation p = method.getAnnotation(AnnotationConsts.ACC_AnnotationMethod);
		MQExtendMethodClass m = AnnotationMethodUtils.getMethod(p);
		e.value = m.getValue();
		e.explain = m.getExplain();
		e.groupid = m.getGroupid();
		Annotation[][] paraAnno = method.getParameterAnnotations();
		String[] paraName = ReadClassAsm.getParaName(classzz, method);
		Class<?>[] paraClassArray = method.getParameterTypes();
		if (paraAnno.length == paraName.length) for (int i = 0, len = paraName.length; i < len; i++) {
			ElementParameter f = getEP(paraName[i], paraAnno[i]);
			if (f == null) continue;
			f.p_type = method.getParameters()[i].getParameterizedType();
			f.p_class = paraClassArray[i];
			e.parameterList.add(f);
		}
		return e;
	}

	/**
	 * 把某个参数与相应的扩展注解结合得 ElementParameter<br/>
	 * 注意：这里参数对象里没有此参数的类，需要返回到上升通过Method得到
	 * @param panaName String
	 * @param annoArray Annotation[]
	 * @return ElementParameter
	 */
	static final ElementParameter getEP(final String panaName, final Annotation[] annoArray) {
		if (panaName == null || panaName.equals("")) return null;
		ElementParameter f = new ElementParameter();
		for (int i = 0, len = annoArray.length; i < len; i++) {
			if (AnnotationStateUtils.isStateInvalid(annoArray[i])) return null;
			MQExtendParameterClass e=AnnotationParameterUtils.getParameter(annoArray[i]);
			if(e!=null){
				f.p_useAnnotation = true;
				f.p_allowNull=e.isNull();
			}
		}
		f.p_name = panaName;
		return f;
	}

	/**
	 * 判断注解是否是参数注解，是则得到注解值
	 * @param p Annotation
	 * @param f ElementParameter
	 */
	@Deprecated
	static final void isAnnotationParameter(Annotation p, ElementParameter f) {
		if (AnnotationConsts.ACC_AnnotationParameter.isInstance(p)) {
			f.p_useAnnotation = true;
			f.p_allowNull=true;//f.p_allowNull = MQAnnotation.getBoolean(p, "Null", true);
		}
	}

	/**
	 * 分解类
	 * @param classzz
	 */
	@SuppressWarnings("unused")
	static final void splitClass(final Class<?> classzz) {
		if (classzz == null) return;
		String classfile = classzz.getName();
		classfile = classfile.replaceAll("\\.", "/");
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		URL url = loader.getResource(classfile + ".class");
		try {
			InputStream is = url.openStream();
			ClassReader cr = new ClassReader(is);
			ReadClassAsm eac = new ReadClassAsm();
			//cr.accept(eac, 0);
			cr = null;
			is.close();
			is = null;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	@SuppressWarnings("unused")
	static final String[] getParaName(final InputStream is, Method m) {
		final String[] paramNames = new String[m.getParameterTypes().length];
		try {
			ClassReader cr = new ClassReader(is);
			ReadClassAsm eac = new ReadClassAsm();
			/*
			 * cr.accept(new ClassVisitor{
			 * }, 0);
			 */
			cr = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return paramNames;
	}

	/**
	 * 对ElementMethod List进行过滤，判断参数是否合格，返回List
	 * @param list List< ElementMethod >
	 * @param parameter MQparameter
	 * @return List< ElementMethod >
	 */
	static final List<ElementMethod> filter(List<ElementMethod> list, final MQparameter parameter) {
		if (list == null || parameter == null || list.size() == 0) return list;
		int len = list.size();
		List<ElementMethod> newList = new ArrayList<ElementMethod>(0);
		System.out.println("filter list:"+list.size());
		for (int i = 0; i < len; i++)
			if (list.get(i).checkParameter(parameter)) newList.add(list.get(i));
		System.out.println("filter NewList:"+newList.size());
		return newList;
	}

	/**
	 * 判断Method是否需要提出，从方法的修饰符和方法的其它方面进行判断
	 * @param method Method
	 * @return boolean
	 */
	static boolean allowInputMethod(final Method method) {
		return allowInputMethodModifier(method.getModifiers()) && allowInputMethodParameter(method);
	}

	/**
	 * 判断Method是否需要提出，只方法的修饰符的判断
	 * @param modifier int
	 * @return boolean
	 */
	private static boolean allowInputMethodModifier(final int modifier) {
		if (!MQConsts.ACC_allowStatic && Modifier.isStatic(modifier)) return false;
		if (!MQConsts.ACC_allowPrivate && Modifier.isPrivate(modifier)) return false;
		if (!MQConsts.ACC_allowProtected && Modifier.isProtected(modifier)) return false;
		if (!MQConsts.ACC_allowPublic && Modifier.isPublic(modifier)) return false;
		return true;
	}

	/**
	 * 判断Method是否需要提出，按方法
	 * @param method Method
	 * @return boolean
	 */
	private static boolean allowInputMethodParameter(final Method method) {
		return !(!MQConsts.ACC_allowVarArgs && method.isVarArgs());
	}
}
