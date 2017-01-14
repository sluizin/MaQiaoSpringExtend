/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import MaQiao.MaQiaoSpringExtend.Element.ElementMethod;
import MaQiao.MaQiaoSpringExtend.Element.ElementParameter;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameter;
import MaQiao.MaQiaoSpringExtend.basicTypes.BasicTypesMatching;

/**
 * 实体对象数组与方法参数的匹配<br/>
 * 匹配规则：<br/>
 * 有参数注解：名称 > 顺序 > 类型 > null<br/>
 * 无参数注解：顺序 > 类型 > null<br/>
 * 重点：数组之间的匹配
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQparameterMatching {
	/**
	 * 方法参数的匹配<br/>
	 * 匹配规则：名称 > 顺序 > 类型 > null<br/>
	 * @param m Method
	 * @param elementMethod ElementMethod
	 * @param parameter setupParameter
	 * @return Object[]
	 */
	static final Object[] matching(final Method m, ElementMethod elementMethod, final MQparameter parameter) {
		if (m == null || parameter == null) return new Object[0];
		return matchingClassTypeOrder(m, elementMethod, parameter);
	}

	/**
	 * 方法参数的匹配<br/>
	 * 匹配规则：名称 > 顺序 > 类型 > null<br/>
	 * @param m Method 方法
	 * @param elementMethod ElementMethod 方法分解
	 * @param parameter MQparameter 输入参数
	 * @return Object[] 输出对象数组，用于运行
	 */
	static Object[] matchingClassTypeOrder(final Method m, ElementMethod elementMethod, final MQparameter parameter) {
		final Class<?>[] sourceArray = m.getParameterTypes();
		final int len = sourceArray.length;

		//List<ClassParameterObject> parameterObjList = parameter.parameterObjList;
		/* 参数名称数组 */
		//String[] paraNameArray = elementMethod.getParameterNameArray();

		final ClassParameter pc = parameter.setupParameter;

		final Object[] ouputObjArray = new Object[len];
		if (len == 0) return ouputObjArray;

		/* 把对象List转成二维数组，用于存储variable和obj，用于非重复时删除 */
		Object[][] inputParaNameValueArray = parameter.getParameterArray();
		final int inputParaLen = inputParaNameValueArray.length;
		for (int i = 0; i < inputParaLen; i++) {
			System.out.println("[" + i + "][0]:" + inputParaNameValueArray[i][0].toString() + "\t[" + i + "][1]:" + inputParaNameValueArray[i][1].toString());
		}
		Parameter[] paras = m.getParameters();

		for (int i = 0, startPoint = 0; i < len; i++) {
			Object obj = null;
			/* 得到当前参数的属性 */
			ElementParameter ep = elementMethod.parameterList.get(i);
			int findi = arraySearchObject(i, ep, paras[i], sourceArray[i], inputParaNameValueArray, startPoint);
			if (findi > -1) {
				startPoint = findi;
				findi = findi % inputParaLen;
				obj = inputParaNameValueArray[findi][1];
				//System.out.println("found["+findi+"]:"+obj.toString());
				//System.out.println("found["+findi+"]:"+obj.getClass().getName());
				if (!pc.isMulti) inputParaNameValueArray[findi][0] = inputParaNameValueArray[findi][1] = null;
			} else {
				obj = BasicTypesMatching.getDefaultObject(sourceArray[i], pc.allowNull);
			}
			System.out.println("sourceArray[" + i + "]:" + sourceArray[i].getName() + " <- " + (obj == null ? "null" : obj.getClass()));
			ouputObjArray[i] = obj;
		}
		return ouputObjArray;
	}

	/**
	 * 从对象数组中提供某类的对象，从startPoint向后<br/>
	 * 匹配规则：<br/>
	 * 有参数注解：名称 > 顺序 > 类型 > null<br/>
	 * 无参数注解：顺序 > 类型 > null<br/>
	 * 如果没有找到，则返回-1;
	 * @param point int
	 * @param ep ElementParameter
	 * @param classzz Class&lt;?>
	 * @param inputParaNameValueArray Object[][]
	 * @param startPoint int
	 * @return int
	 */
	private static final int arraySearchObject(final int point, final ElementParameter ep, final Parameter para, final Class<?> classzz, final Object[][] inputParaNameValueArray, final int startPoint) {
		final int len = inputParaNameValueArray.length;
		int starti = startPoint;
		/* 对参数名称进行匹配 */
		String variable;
		Object variableObj;
		Object obj;
		//System.out.println("装箱前类型getParameterizedType："+para.getParameterizedType().toString());
		/* 先判断是否是装箱前的类型，如果是的话换成装箱后的类型，用于类型的比较 */
		Class<?> newClasszz = BasicTypesMatching.getBasicTypesClass(para.getParameterizedType().toString());
		if (newClasszz == null) newClasszz = classzz;
		if (ep.p_useAnnotation) {
			/*
			 * 有参数注解：名称 > 顺序 > 类型 > null<br/>
			 */
			for (int i = 0; i < len; i++) {
				int pp = starti % len;
				obj = inputParaNameValueArray[pp][0];
				if (obj != null) {
					variable = (String) obj;
					if (ep.p_name.equals(variable)) {
						variableObj = inputParaNameValueArray[pp][1];
						if (variableObj != null && BasicTypesMatching.isMatching(newClasszz, variableObj.getClass())) return starti;
					}
				}
				starti++;
				/*
				 * 有参数注解：顺序 > 类型 > null<br/>
				 */
			}
		}
		/*
		 * 对参数类型进行匹配
		 * for (int i = 0; i < len; i++) {
		 * int pp = starti % len;
		 * obj = inputParaNameValueArray[pp][0];
		 * if (obj != null) {
		 * variableObj = inputParaNameValueArray[pp][1];
		 * if (variableObj != null && BasicTypes.isMatching(newClasszz, variableObj.getClass())) return starti;
		 * }
		 * starti++;
		 * }
		 */
		return -1;
	}
}
