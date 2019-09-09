package com.fenda.ai.json;


import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lavon.liyuanfang on 2019/5/28.
 */

public class Jsonparse {
    private final static String TAG = "Jsonparse";

    public static String parseInputText(String input)
    {
        try {
            JSONObject jo = new JSONObject(input);
            String text = jo.optString("text", "");
            String var = jo.optString("var", "");
            return text+var;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
