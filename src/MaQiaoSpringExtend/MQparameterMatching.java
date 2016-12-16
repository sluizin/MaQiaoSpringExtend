/**
 * 
 */
package MaQiaoSpringExtend;

import java.lang.reflect.Method;

import MaQiaoSpringExtend.MQparameterElement.ClassParameter;

/**
 * 实体对象数组与方法参数的匹配<br/>
 * 匹配规则：名称 > 顺序 > 类型 > null<br/>
 * 重点：数组之间的匹配
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public final class MQparameterMatching {

	/**
	 * 方法参数的匹配<br/>
	 * @param m Method
	 * @param parameter setupParameter
	 * @return Object[]
	 */
	static final Object[] matching(final Method m, final MQparameter parameter) {
		if (m == null) return new Object[0];
		//Object[] arraytarget = new Object[m.getParameterTypes().length];
		return matchingClassTypeOrder(new Object[m.getParameterTypes().length], m.getParameterTypes(), parameter.setupParameter,parameter.getParameterObjectArray());
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
		MQWarning.show("news[]", true);
		int length=inputObjArray.length;
		Object[] newInputObjectArray=new Object[length];
		System.arraycopy(inputObjArray, 0, newInputObjectArray, 0, length);
		for(int i=0;i<inputObjArray.length;i++)
			MQWarning.show("inputObjArray["+i+"]", inputObjArray[i].toString());
		Object[] news= matchingClassTypeOrder(arraytarget, m.getParameterTypes(), pc, newInputObjectArray);
		for(int i=0;i<news.length;i++)
			MQWarning.show("news["+i+"]", news[i].toString());
			
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
				if (isMatching(sourceArrayg[i],obj.getClass())) {
					//MQWarning.show("参数找到匹配类->"+sourceArrayg[i].getName(), obj.getClass().getName());
					t = true;
					break;
				}
			}
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
	 * 参数类型进行匹配<br/>
	 * 允许类与接口的匹配<br/>
	 * class1为主，并允许为接口<br/>
	 * @param class1 Class< ? >
	 * @param class2 Class< ? >
	 * @return boolean
	 */
	static boolean isMatching(final Class<?> class1, final Class<?> class2) {
		if (class1 == null || class2 == null) return false;
		if (class1 == class2) return true;
		if (class1.getName().equals(class2.getName())) return true;
		if(class1.isInterface() && !class2.isInterface() && MQSpringUtils.isInterface(class2,class1))return true;
		return false;
	}
}
