package sc.ustc.test;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import sc.ustc.dao.Configuration;

public class ConfigTest {

	public static void main(String[] args) {

		Map<String,String> m = new Configuration().getDBConfigMap();
		Set<Entry<String,String>> ss = m.entrySet();
		for (Entry<String,String> s : ss) {
			System.out.println(s.getKey()+"∂‘”¶÷µ£∫"+s.getValue());
		}
	
	}

}
