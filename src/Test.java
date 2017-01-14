/**
 * 
 */


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import MaQiao.MaQiaoSpringExtend.MQRUN;
import MaQiao.MaQiaoSpringExtend.parameter.ParameterMapmodel;

/**
 *   extends SiteBaseController
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Controller
public class Test{
	@RequestMapping({ "/Test3"})
	public ModelAndView test3(ModelAndView mav){
		String jsonFile="http://192.168.1.98:90/static/zhuanti/MQparameterJson/test.txt";
		ParameterMapmodel f=new ParameterMapmodel(jsonFile);
		f.getModel().put("first", "go...");
		int a=55;
		f.getModel().put("size", a);
		MQRUN.staticRun(f);
		mav.getModel().put("model", f.getModel());
		mav.setViewName("/ACCzhuanti/Test/index");
		return mav;
	}
}
