package adevlibs.net;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

/**
 * json parser
 *
 * @author wangfan
 */
public class JSONParser {

    private static JSONParser sInstance = new JSONParser();

    private Gson mGson = new Gson();

    private JSONParser() {
    };

    public static JSONParser getInstance() {
        return sInstance;
    }

    public <T> T parser(Class<T> clazz, JsonElement data) {
        
        T bean = null;
        
        try {
            bean = mGson.fromJson(data, clazz);
            System.out.println("jsonbean="+bean);
        } catch (Exception e) {
            System.out.println(e);
        }
        return bean;
    }
    public <T> T parser(Class<T> clazz, String data) {

        T bean = null;

        try {
            bean = mGson.fromJson(data, clazz);
            System.out.println("jsonbean="+bean);
        } catch (Exception e) {
        		System.out.println(e);
        }
        return bean;
    }

    public <T> T parser(JsonElement data,Type clazz) {
        T bean = null;
        try {
            bean = mGson.fromJson(data, clazz);
            System.out.println("jsonbean="+bean);
        } catch (Exception e) {
            System.out.println(e);
        }
        return bean;
    }
    public <T> T parser(String data,Type clazz) {
        T bean = null;
        try {
            bean = mGson.fromJson(data, clazz);
            System.out.println("jsonbean="+bean);
        } catch (Exception e) {
        		System.out.println(e);
        }
        return bean;
    }

    public String toJson(Object object) {
        return mGson.toJson(object);
    }
}
