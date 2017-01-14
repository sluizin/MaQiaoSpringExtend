/**
 * 
 */
package MaQiao.MaQiaoSpringExtend.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import MaQiao.MaQiaoSpringExtend.MQContainer;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
@Controller
@RequestMapping(value = "/MaQiaoSpringExtend")
public class MQController {

	@ResponseBody
	@RequestMapping(value = "/MQContainer/html", produces = "text/html;charset=utf-8")
	public String getMQContainerHtml(){
		List<String> outList=MQContainer.outputList();
		StringBuilder sb=new StringBuilder();
		for (int i = 0,len=outList.size(); i < len; i++) {
			sb.append(outList.get(i));
		}
		return sb.toString();
	}
	@ResponseBody
	@RequestMapping(value = "/MQContainer/json", produces = "application/json;charset=utf-8")
	public String getMQContainerJson(){
		return MQContainer.outputJson();
	}
}
