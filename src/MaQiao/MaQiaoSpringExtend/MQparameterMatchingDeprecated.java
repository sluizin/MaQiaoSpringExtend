/**
 * 
 */
package MaQiao.MaQiaoSpringExtend;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameter;
import MaQiao.MaQiaoSpringExtend.basicTypes.BasicTypesMatching;
import MaQiao.MaQiaoSpringExtend.warning.Warning;

/**
 * 实体对象数组与方法参数的匹配<br/>
 * 匹配规则：名称 > 顺序 > 类型 > null<br/>
 * 重点：数组之间的匹配
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Deprecated
public final class MQparameterMatchingDeprecated {

	/**
	 * 方法参数的匹配<br/>
	 * @param m Method
	 * @param parameter setupParameter
	 * @return Object[]
	 */
	static final Object[] matching(final Method m, final MQparameter parameter) {
		if (m == null || parameter == null) return new Object[0];
		//Object[] arraytarget = new Object[m.getParameterTypes().length];
		return matchingClassTypeOrder(m, parameter);

		//return matchingClassTypeOrder(new Object[m.getParameterTypes().length], m.getParameterTypes(), parameter.setupParameter,parameter.getParameterObjectArray());
	}
	/**
	 * 方法参数的匹配<br/>
	 * @param m Method
	 * @param paraNameArray String[]
	 * @param parameter setupParameter
	 * @return Object[]
	 */
	static final Object[] matching(final Method m,String[] paraNameArray, final MQparameter parameter) {
		if (m == null || parameter == null) return new Object[0];
		//Object[] arraytarget = new Object[m.getParameterTypes().length];
		
		return matchingClassTypeOrder(m,paraNameArray, parameter);

		//return matchingClassTypeOrder(new Object[m.getParameterTypes().length], m.getParameterTypes(), parameter.setupParameter,parameter.getParameterObjectArray());
	}
	
	/**
	 * 方法参数的匹配<br/>
	 * 匹配规则：名称 > 顺序 > 类型 > null<br/>
	 * @param m Method
	 * @param inputObjArray Object[]
	 * @return Object[]
	 */
	static final Object[] matching(final Method m, final ClassParameter pc, Object... inputObjArray) {
		if (m == null) return new Object[0];
		Object[] arraytarget = new Object[m.getParameterTypes().length];
		Warning.show("news[]", true);
		int length = inputObjArray.length;
		Object[] newInputObjectArray = new Object[length];
		System.arraycopy(inputObjArray, 0, newInputObjectArray, 0, length);
		for (int i = 0; i < inputObjArray.length; i++)
			Warning.show("inputObjArray[" + i + "]", inputObjArray[i].toString());
		Object[] news = matchingClassTypeOrder(arraytarget, m.getParameterTypes(), pc, newInputObjectArray);
		for (int i = 0; i < news.length; i++)
			Warning.show("news[" + i + "]", news[i].toString());

		return news;
	}

	@Deprecated
	static Object[] matching(final Class<?>[] sourceArrayg, final String[] sourceParaNames, final ClassParameter pc, final String[] setParanames, Object... inputObjArray) {
		if (sourceArrayg.length == 0 || sourceParaNames.length == 0 || inputObjArray.length == 0) return new Object[0];
		final int len = sourceArrayg.length;
		Object[] ouputObjArray = new Object[len];
		/* 按名称匹配(隐含类型匹配) */
		ouputObjArray = matchingName(ouputObjArray, sourceArrayg, setParanames, pc, setParanames, ouputObjArray);
		/* 按类型匹配 */
		ouputObjArray = matchingClassTypeOrder(ouputObjArray, sourceArrayg, pc, inputObjArray);
		return ouputObjArray;
	}

	/**
	 * 按参数名称匹配
	 * @param ouputObjArray Object[]
	 * @param sourceArray< ? >[]
	 * @param sourceParaNames String[]
	 * @param pc ParameterClass
	 * @param setParanames String[]
	 * @param inputObjArray Object[]
	 * @return Object[]
	 */
	@Deprecated
	public static Object[] matchingName(final Object[] ouputObjArray, final Class<?>[] sourceArrayg, String[] sourceParaNames, final ClassParameter pc, final String[] setParanames, Object... inputObjArray) {
		/**
		 * ....省略判断验证
		 */
		final int len = ouputObjArray.length;
		Object obj;
		int point;
		for (int i = 0; i < len; i++) {
			if (ouputObjArray[i] != null) continue;
			point = searchParaName(sourceParaNames, setParanames[i]);
			if (point > -1) {
				obj = inputObjArray[point];
				if (obj == null) continue;
				if (obj.getClass() == sourceArrayg[i]) ouputObjArray[i] = obj;
			}
		}
		return ouputObjArray;
	}

	/**
	 * 返回第一个参数名称相同的位置，否则返回-1
	 * @param paranameArray String[]
	 * @param key String
	 * @return int
	 */
	private static final int searchParaName(final String[] paranameArray, final String key) {
		for (int i = 0, len = paranameArray.length; i < len; i++)
			if (key.equals(paranameArray[i])) return i;
		return -1;
	}

	/**
	 * 按类型顺序匹配
	 * @param ouputObjArray Object[]
	 * @param sourceArrayg Class< ? >[]
	 * @param pc ParameterClass
	 * @param inputObjArray Object[]
	 * @return Object[]
	 */
	@Deprecated
	static Object[] matchingClassTypeOrder(final Object[] ouputObjArray, final Class<?>[] sourceArrayg, ClassParameter pc, Object... inputObjArray) {
		final int len = sourceArrayg.length;
		if (len == 0) return ouputObjArray;
		final int len2 = inputObjArray.length;
		if (len2 == 0) return ouputObjArray;
		int i, ii, iii, point = 0;
		boolean t = false;
		Object obj;
		for (i = iii = 0; i < len; i++, t = false) {
			if (ouputObjArray[i] != null) continue;
			for (ii = 0; ii < len2; ii++) {
				iii = (point++) % len2;
				obj = inputObjArray[iii];
				if (obj == null) continue;
				if (BasicTypesMatching.isMatching(sourceArrayg[i], obj.getClass())) {
					//MQWarning.show("参数找到匹配类->"+sourceArrayg[i].getName(), obj.getClass().getName());
					t = true;
					break;
				}
			}
			System.out.println("ouputObjArray[" + i + "]:" + ouputObjArray[i]);
			System.out.println("inputObjArray[" + iii + "]:" + inputObjArray[iii]);
			if (t) {
				ouputObjArray[i] = inputObjArray[iii];
				if (!pc.isMulti) inputObjArray[iii] = null;
			} else {
				if (pc.allowNull) ouputObjArray[i] = null;
				else {
					try {
						ouputObjArray[i] = Class.forName(sourceArrayg[i].getName()).newInstance();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return ouputObjArray;
	}

	/**
	 * 按类型顺序匹配
	 * @param m Method
	 * @param parameter MQparameter
	 * @return Object[]
	 */
	@Deprecated
	static Object[] matchingClassTypeOrderDeprecated(final Method m, final MQparameter parameter) {
		final Object[] ouputObjArray = new Object[m.getParameterTypes().length];
		final Class<?>[] sourceArray = m.getParameterTypes();
		ClassParameter pc = parameter.setupParameter;
		Object[] inputObjArray = parameter.getParameterObjectArray();

		final int len = ouputObjArray.length;
		if (len == 0) return ouputObjArray;
		final int len2 = inputObjArray.length;
		if (len2 == 0) return ouputObjArray;

		Parameter[] paras = m.getParameters();
		int i, ii, iii, point = 0;
		boolean t = false;
		Object obj;
		for (i = iii = 0; i < len; i++, t = false) {
			if (ouputObjArray[i] != null) continue;
			for (ii = 0; ii < len2; ii++) {
				iii = (point++) % len2;
				obj = inputObjArray[iii];
				if (obj == null) continue;
				if (BasicTypesMatching.isMatching(sourceArray[i], obj.getClass())) {
					t = true;
					break;
				}
			}
			/*
			 * System.out.println("pname:" + pa.getClass());
			 * Type type = paras[i].getType();
			 * Parameter pa = paras[i];
			 * System.out.println("pname getType:" + type);
			 * System.out.println("pname getType:" + type.toString());
			 * System.out.println("pname getParameterizedType:" + pa.getParameterizedType());
			 * System.out.println("pname getParameterizedType:" + pa.getParameterizedType().toString());
			 * System.out.println("newInstance:" + sourceArray[i].getName());
			 */
			if (t) {
				ouputObjArray[i] = inputObjArray[iii];
				if (!pc.isMulti) inputObjArray[iii] = null;
			} else {
				Type type = paras[i].getType();
				System.out.println("pname getType:" + paras[i].toString());
				System.out.println("pname getType:" + paras[i].getName());
				System.out.println("pname getType:" + type.toString());
				ouputObjArray[i] = BasicTypesMatching.getDefaultObject(type, sourceArray[i], pc.allowNull);
			}
		}
		return ouputObjArray;
	}

	static Object[] matchingClassTypeOrder(final Method m, final MQparameter parameter) {
		final Class<?>[] sourceArray = m.getParameterTypes();
		final Object[] inputObjArray = parameter.getParameterObjectArray();
		final ClassParameter pc = parameter.setupParameter;
		int len = sourceArray.length;
		final Object[] ouputObjArray = new Object[len];
		if (len == 0) return ouputObjArray;
		Parameter[] paras = m.getParameters();
		for (int i = 0, startPoint = 0; i < len; i++) {
			Object obj = null;
			System.out.println("sourceArray[i]:" + sourceArray[i].getName());
			System.out.println("sourceArray[i]:" + sourceArray[i].getSimpleName());
			int findi = arraySearchObject(sourceArray[i], inputObjArray, startPoint);
			if (findi > -1) {
				System.out.println(findi + "/" + inputObjArray.length);
				obj = inputObjArray[findi];
				if (!pc.isMulti) inputObjArray[findi] = null;
				startPoint = findi;
			} else {
				obj = BasicTypesMatching.getDefaultObject(paras[i].getType(), sourceArray[i], pc.allowNull);
			}
			ouputObjArray[i] = obj;
		}
		return ouputObjArray;
	}

	static Object[] matchingClassTypeOrder(final Method m,String[] paraNameArray, final MQparameter parameter) {
		final Class<?>[] sourceArray = m.getParameterTypes();
		final Object[] inputObjArray = parameter.getParameterObjectArray();		
		//List<ClassParameterObject> parameterObjList = parameter.parameterObjList;
		final ClassParameter pc = parameter.setupParameter;
		int len = sourceArray.length;
		final Object[] ouputObjArray = new Object[len];
		if (len == 0) return ouputObjArray;
		Parameter[] paras = m.getParameters();
		//Object[][] ddd=parameter.getParameterArray();
		
		
		for (int i = 0, startPoint = 0; i < len; i++) {
			Object obj = null;
			System.out.println("sourceArray[i]:" + sourceArray[i].getName());
			System.out.println("sourceArray[i]:" + sourceArray[i].getSimpleName());
			int findi = arraySearchObject(sourceArray[i], inputObjArray, startPoint);
			if (findi > -1) {
				System.out.println(findi + "/" + inputObjArray.length);
				obj = inputObjArray[findi];
				if (!pc.isMulti) inputObjArray[findi] = null;
				startPoint = findi;
			} else {
				obj = BasicTypesMatching.getDefaultObject(paras[i].getType(), sourceArray[i], pc.allowNull);
			}
			ouputObjArray[i] = obj;
		}
		return ouputObjArray;
	}
	/**
	 * 从对象数组中提供某类的对象，从startPoint向后 名称>顺序>类型>null<br/>
	 * 如果没有找到，则返回-1<br/>
	 * @param classzz Class&lt;?>
	 * @param paraNameArray String[]
	 * @param parameterObjList List&lt;ClassParameterObject>
	 * @param startPoint int
	 * @return int
	 */
	@SuppressWarnings("unused")
	private static final int arraySearchObject(final Class<?> classzz,String[] paraNameArray, final Object[][] parameterParaArray, final int startPoint) {
		Object obj = null;
		int len = parameterParaArray.length;
		int starti = (startPoint < 0) ? 0 : startPoint;
		/* 按名称检索 */
		for (int i = 0,len2=parameterParaArray.length; i < len2; i++) {
			if(parameterParaArray[i][1]!=null){
				Object obj2=parameterParaArray[i][0];
			}
		}
		for (int i = 0; i < len; i++) {
			obj = parameterParaArray[(starti++) % len][1];
			if (obj == null) continue;
			if (BasicTypesMatching.isMatching(classzz, obj.getClass())) return starti-1;
		}
		return -1;
	}

	/**
	 * 从对象数组中提供某类的对象，从startPoint向后<br/>
	 * 如果没有找到，则返回-1;
	 * @param classzz Class&lt;?>
	 * @param inputObjArray Object[]
	 * @param startPoint int
	 * @return int
	 */
	private static final int arraySearchObject(final Class<?> classzz, final Object[] inputObjArray, final int startPoint) {
		Object obj = null;
		int len = inputObjArray.length;
		int starti = startPoint;
		for (int i = 0; i < len; i++) {
			obj = inputObjArray[(starti++) % len];
			if (obj == null) continue;
			if (BasicTypesMatching.isMatching(classzz, obj.getClass())) return starti - 1;
		}
		return -1;
	}

}
