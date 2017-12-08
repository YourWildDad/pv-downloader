package cc.wildad.pixiv.test;

import cc.wildad.pixiv.exception.PV_Exception;
import cc.wildad.pixiv.query.UserPicQuery;
import cc.wildad.pixiv.query.impl.UserPicQueryImpl;
import cc.wildad.pixiv.transmitter.PicDownloader;

import java.io.File;
import java.net.MalformedURLException;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        UserPicQuery userPicQuery = new UserPicQueryImpl();
        try {
            List<String> urls = userPicQuery.query("12793879");

            final File downloadDir = new File("F:\\图库");
            urls.forEach(url -> {
                try {
                    PicDownloader.newDownloader(url).setDownloadDir(downloadDir).start();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
        } catch (PV_Exception e) {
            e.printStackTrace();
        }

    }
}
