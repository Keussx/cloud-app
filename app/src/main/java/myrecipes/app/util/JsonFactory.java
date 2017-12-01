package myrecipes.app.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

public class JsonFactory {
	
	public String toJSon(Object o) {

		//jackson object mapper
		ObjectMapper mapper = new ObjectMapper();

		//enable json pretty printing
		//mapper.enable(SerializationFeature.INDENT_OUTPUT);

		try {
			return mapper.writeValueAsString(o);

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<String> toList(String data){
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(data, new TypeReference<List<String>>(){});
		} catch (IOException e) {
			return null;
		}
	}

}
