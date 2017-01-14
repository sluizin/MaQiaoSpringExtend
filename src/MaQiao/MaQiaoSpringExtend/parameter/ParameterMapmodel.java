/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.parameter;

import java.util.HashMap;
import java.util.Map;

import MaQiao.MaQiaoSpringExtend.MQparameter;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameterObject;

/**
 * 建立参数，默认含有Map<String, Object> model元素
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class ParameterMapmodel extends AbstractMQparameter {

	@SuppressWarnings("unused")
	private ParameterMapmodel() {
	}

	/**
	 * 建立参数，默认含有Map&lt;String, Object> model元素
	 * @param urlfile String
	 * @param array ClassParameterObject[]
	 */
	public ParameterMapmodel(String urlfile, ClassParameterObject... array) {
		this.parameter = MQparameter.getMQparameterFromHttp(urlfile);
		if (this.parameter != null) {
			Map<String, Object> model = new HashMap<String, Object>();
			this.parameter.addVariableObj(new ClassParameterObject("model",model));
			for (int i = 0, len = array.length; i < len; i++)
				this.parameter.addVariableObj(array[i]);
		}
	}

	/**
	 * 得到 Map&lt;String, Object> model
	 * @return Map&lt;String, Object>
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getModel() {
		Object obj = getMapObject("model");
		if (obj == null) return null;
		return (Map<String, Object>) obj;
	}

	/*
	 * (non-Javadoc)
	 * @see MaQiao.MaQiaoSpringExtend.Parameter.AbstractMQparameter#getDustbinMap()
	 */
	@Override
	public Map<Object, Object> getDustbinObjectMap() {
		Map<String, Object> map = getModel();
		Map<Object, Object> map2 = new HashMap<Object, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet())
			map2.put(entry.getKey(), entry.getValue());
		return map2;
	}

}
