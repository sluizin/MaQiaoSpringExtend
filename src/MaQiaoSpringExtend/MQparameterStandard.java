/**
 * 
 */
package MaQiaoSpringExtend;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sunjian
 * @version 1.0
 * @since jdk1.7
 */
public class MQparameterStandard {
	Map<String, Object> model= new HashMap<String,Object>();
	public MQparameterStandard(){
		
	}
	public MQparameterStandard(String http){
		
	}
	public final Map<String, Object> getModel() {
		return model;
	}
	public final void setModel(Map<String, Object> model) {
		this.model = model;
	}
	
}
