/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.parameter;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import MaQiao.MaQiaoSpringExtend.MQparameter;
import MaQiao.MaQiaoSpringExtend.MQparameterElement.ClassParameterObject;

/**
 * 建立参数，默认含有ModelAndView mav元素
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class ParameterModelAndViewmav extends AbstractMQparameter {
	@SuppressWarnings("unused")
	private ParameterModelAndViewmav() {
	}

	/**
	 * 建立参数，默认含有ModelAndView mav元素
	 * @param urlfile String
	 * @param array ClassParameterObject[]
	 */
	public ParameterModelAndViewmav(String urlfile, ClassParameterObject... array) {
		this.parameter = MQparameter.getMQparameterFromHttp(urlfile);
		if (this.parameter != null) {
			ModelAndView mav = new ModelAndView();
			this.parameter.addVariableObj(new ClassParameterObject("mav",mav));
			for (int i = 0, len = array.length; i < len; i++)
				this.parameter.addVariableObj(array[i]);
		}
	}

	/**
	 * 得到 ModelAndView mav
	 * @return ModelAndView
	 */
	public ModelAndView getModelAndView() {
		Object obj = getMapObject("mav");
		if (obj == null) return null;
		if (obj instanceof ModelAndView) return (ModelAndView) obj;
		else return null;
	}

	/* (non-Javadoc)
	 * @see MaQiao.MaQiaoSpringExtend.Parameter.AbstractMQparameter#getDustbinMap()
	 */
	@Override
	public Map<Object, Object> getDustbinObjectMap() {
		ModelAndView mav= getModelAndView();
		if(mav==null)return null;		
		return MapChangeString2Object(mav.getModel());
	}
}
