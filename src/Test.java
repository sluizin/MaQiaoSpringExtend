/**
 * 
 */


import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


//import com.wangku.was.categorynet.common.controller.SiteBaseController;
import com.wangku.was.special.MaQiaoSpringExtend.MQRUN;
import com.wangku.was.special.MaQiaoSpringExtend.MQparameter;
import com.wangku.was.special.MaQiaoSpringExtend.MQparameterElement.ClassParameterObject;

/**
 *   extends SiteBaseController
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Controller
public class Test{
	@RequestMapping({ "/Test"})
	public ModelAndView test1(ModelAndView mav){
		String urlfile="http://192.168.1.98:90/static/zhuanti/MQparameterJson/test.txt";
		//MQparameter e = MQparameter.getMQparameterFromHttp(urlfile);
		Map<String, Object> model= new HashMap<String,Object>();
		model.put("testing....", "ok");
		model.put("change", 10);
		//e.addVariableObj(model,"model");
		MQparameter f=MQparameter.getMQparameterFromHttp(urlfile,new ClassParameterObject("model", model));
		System.out.println("model::::"+model.toString());
		MQRUN.run(f);
		mav.getModel().put("model", model);
		mav.setViewName("/ACCzhuanti/Test/index");
		return mav;
	}
}
