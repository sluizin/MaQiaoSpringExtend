/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import MaQiao.MaQiaoSpringExtend.MQparameter;

/**
 * 参数虚类
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public abstract class AbstractMQparameter {
	MQparameter parameter = new MQparameter();

	/**
	 * 获取MQparameter参数
	 * @return MQparameter
	 */
	public MQparameter getParameter() {
		return parameter;
	}

	public void addVariableObj(String variable, Object obj) {
		if (variable != null && variable.length()>0 && obj != null) {
			parameter.addVariableObj(variable, obj);
		}
	}

	/**
	 * 得到variable 标识的变量
	 * @param variable String
	 * @return Object
	 */
	public Object getMapObject(String variable) {
		if (parameter == null) return null;
		return parameter.getVariableObj(variable);
	}

	public final void setParameter(MQparameter parameter) {
		this.parameter = parameter;
	}

	/**
	 * 得到参数中的Map&lt;Object, Object>用于保存过程变量
	 * @return Map&lt;Object, Object>
	 */
	public abstract Map<Object, Object> getDustbinObjectMap();

	/**
	 * Map转换
	 * @param map Map&lt;String, Object>
	 * @return Map&lt;Object, Object>
	 */
	Map<Object, Object> MapChangeString2Object(Map<String, Object> map) {
		Map<Object, Object> map2 = new HashMap<Object, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet())
			map2.put(entry.getKey(), entry.getValue());
		return map2;
	}

	/**
	 * Map转换
	 * @param map Map&lt;Object, Object>
	 * @return Map&lt;String, Object>
	 */
	Map<String, Object> MapChangeObject2String(Map<Object, Object> map) {
		Map<String, Object> map2 = new HashMap<String, Object>();
		for (Entry<Object, Object> entry : map.entrySet())
			map2.put(entry.getKey().toString(), entry.getValue());
		return map2;
	}

}
