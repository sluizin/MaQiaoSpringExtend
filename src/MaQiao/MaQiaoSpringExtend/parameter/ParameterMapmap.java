/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.parameter;

import java.util.HashMap;
import java.util.Map;

import MaQiao.MaQiaoSpringExtend.MQparameter;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameterObject;

/**
 * 建立参数，默认含有Map&lt;Object, Object> model元素
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class ParameterMapmap extends AbstractMQparameter {
	@SuppressWarnings("unused")
	private ParameterMapmap() {
	}

	/**
	 * 建立参数，默认含有Map&lt;Object, Object> model元素
	 * @param urlfile String
	 * @param array ClassParameterObject[]
	 */
	public ParameterMapmap(String urlfile, ClassParameterObject... array) {
		setParameter(MQparameter.getMQparameterFromHttp(urlfile));
		if (this.parameter != null) {
			Map<Object, Object> map = new HashMap<Object, Object>();
			this.parameter.addVariableObj(new ClassParameterObject("map",map));
			for (int i = 0, len = array.length; i < len; i++)
				this.parameter.addVariableObj(array[i]);
		}
	}

	/**
	 * 得到 Map&lt;Object, Object> map
	 * @return Map&lt;Object, Object>
	 */
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getMap() {
		Object obj = getMapObject("map");
		if (obj == null) return null;
		return (Map<Object, Object>) obj;
	}

	/* (non-Javadoc)
	 * @see MaQiao.MaQiaoSpringExtend.Parameter.AbstractMQparameter#getDustbinMap()
	 */
	@Override
	public Map<Object, Object> getDustbinObjectMap() {
		return getMap();
	}

}
