/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.parameter;

import java.util.Map;
import MaQiao.MaQiaoSpringExtend.MQparameter;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameterObject;

/**
 * 参数
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class Parameter extends AbstractMQparameter {

	@SuppressWarnings("unused")
	private Parameter() {
	}

	public Parameter(String urlfile, ClassParameterObject... array) {
		this.parameter = MQparameter.getMQparameterFromHttp(urlfile,array);
	}

	/* (non-Javadoc)
	 * @see MaQiao.MaQiaoSpringExtend.Parameter.AbstractMQparameter#getDustbinMap()
	 */
	@Override
	public Map<Object, Object> getDustbinObjectMap() {
		return null;
	}


}
