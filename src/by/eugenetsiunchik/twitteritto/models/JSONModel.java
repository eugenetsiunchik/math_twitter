package by.eugenetsiunchik.twitteritto.models;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JSONModel {

	protected JSONObject jo;
	
	public JSONModel(JSONObject jsonObject) {
		jo = jsonObject;
	}
	
	public JSONModel(String jsonString) {
		try {
			jo = new JSONObject(jsonString);
		} catch (JSONException e) {
			throw new IllegalArgumentException("json is not valid");
		}
	}
	
	public JSONModel() {
		jo = new JSONObject();
	}
	
	public JSONObject getSubObject (String key){
		try {
			return jo.getJSONObject(key);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String getString(String key) throws JSONException {
		if (jo.isNull(key)) {
			return null;
		}
		return jo.optString(key);
	}
	public Long getLong(String key) throws JSONException {
		if(jo.isNull(key)){
			return null;
		}
		return jo.optLong(key);
		
		
	}
}
