package cc.wildad.pixiv.query.impl;

import cc.wildad.pixiv.exception.PV_Ex;
import cc.wildad.pixiv.exception.PV_Exception;
import cc.wildad.pixiv.query.UserPicQuery;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserPicQueryImpl implements UserPicQuery {

    @Override
    public List<String> query(String id) throws PV_Exception {
        try {
            URL url = new URL("https://api.imjad.cn/pixiv/v1/?type=member_illust&id=" + id);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);

            connection.connect();

            if (connection.getResponseCode() != 200) {
                throw PV_Ex.newConnEx(new Throwable(), "链接失败，HTTP Code：", connection.getResponseCode());
            }
            StringBuilder body = new StringBuilder();
            try (InputStream in = connection.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(in, "utf8");
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                body.append(reader.readLine());
            }
            JSONObject jsonObject = new JSONObject(body.toString());
            if (!"success".equals(jsonObject.getString("status"))) {
                throw PV_Ex.newQueryEx(new Throwable(), "查询失败，请检查查询参数");
            }
            List<String> ids = new ArrayList<>();
            JSONArray response = jsonObject.getJSONArray("response");
            for (int i = 0; i < response.length(); i++) {
                JSONObject img = response.getJSONObject(i);
                JSONObject urls = img.getJSONObject("image_urls");
                ids.add(urls.getString("large"));
            }
            return ids;
        } catch (MalformedURLException e) {
            throw PV_Ex.newEx(e, "URL地址不合法");
        } catch (IOException e) {
            throw PV_Ex.newConnEx(e, "无法创建流");
        }
    }
}
