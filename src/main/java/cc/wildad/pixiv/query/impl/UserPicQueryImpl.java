package cc.wildad.pixiv.query.impl;

import cc.wildad.pixiv.exception.PV_Ex;
import cc.wildad.pixiv.exception.PV_Exception;
import cc.wildad.pixiv.query.UserPicQuery;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class UserPicQueryImpl implements UserPicQuery {

    @Override
    public List<String> query(String id) throws PV_Exception {
        try {
            URL url = new URL("https://api.imjad.cn/pixiv/v1/");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(6000);

            connection.connect();

            if (connection.getResponseCode() == 200) {
                List<String> ids = new ArrayList<>();
                String responseMessage = connection.getResponseMessage();
                return ids;
            } else {
                throw PV_Ex.newConnEx(new Throwable(), "链接失败，HTTP Code：", connection.getResponseCode());
            }

        } catch (MalformedURLException e) {
            throw PV_Ex.newEx(e, "URL地址不合法");
        } catch (IOException e) {
            throw PV_Ex.newConnEx(e, "无法创建流");
        }
    }
}
