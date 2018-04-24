package cc.wildad.pixiv.transmitter;

import com.sun.corba.se.spi.orbutil.threadpool.ThreadPool;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 图片下载器
 */
public class PicDownloader extends Thread {

    private static final FileSystem FILE_SYSTEM = FileSystems.getDefault();

    private String id;
    private URL picURL;
    private File downloadDir;

    private static ExecutorService executor = Executors.newFixedThreadPool(8);

    public static PicDownloader newDownloader(String picUrl) throws MalformedURLException {
        PicDownloader picDownloader = new PicDownloader(picUrl);
        Future<?> submit = executor.submit(picDownloader);
        return picDownloader;
    }

    public static void shutdown() {
        executor.shutdown();
    }

    /**
     * 图片下载器构造方法
     *
     * @param picUrl 图片的URL
     * @throws MalformedURLException URL不合法
     */
    private PicDownloader(String picUrl) throws MalformedURLException {
        picURL = new URL(picUrl);
    }

    /**
     * 设置下载的目录
     *
     * @param dir 下载目录
     * @return 链式调用
     */
    public PicDownloader setDownloadDir(String dir) {
        this.downloadDir = new File(dir);
        return this;
    }

    /**
     * 设置下载的目录
     *
     * @param dir 下载目录
     * @return 链式调用
     */
    public PicDownloader setDownloadDir(File dir) {
        this.downloadDir = dir;
        return this;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) picURL.openConnection();
            connection.addRequestProperty("referer", "http://www.pixiv.net/");
            connection.setConnectTimeout(6000);
            connection.setRequestMethod("GET");
            connection.connect();

            if (downloadDir == null) return;
            if (!downloadDir.exists()) {
                downloadDir.mkdirs();
            }

            Path path = FILE_SYSTEM.getPath(picURL.getPath());
            File downloadFile = new File(downloadDir, path.getFileName().toString());
            if (!downloadFile.exists()) {
                downloadFile.createNewFile();
            }

            try (InputStream inputStream = connection.getInputStream();
                 OutputStream outputStream = new FileOutputStream(downloadFile);
                 BufferedInputStream in = new BufferedInputStream(inputStream);
                 BufferedOutputStream out = new BufferedOutputStream(outputStream)) {
                byte[] arr = new byte[10240];
                int l;
                while ((l = in.read(arr)) > 0) {
                    out.write(arr, 0, l);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            PicDownloader.newDownloader("https://i.pximg.net/img-original/img/2016/02/26/00/17/16/55486351_p0.jpg")
                         .setDownloadDir("C:\\Users\\05370\\Desktop")
                         .start();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
