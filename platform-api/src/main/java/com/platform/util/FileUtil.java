package com.platform.util;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.platform.config.ConstantConfig.dog01JsonPath;

public class FileUtil {

    /**
     * File url
     *
     * @param urlAddress url address
     * @param localFileName local file name
     * @param destinationDir destination dir
     */
    public static void fileUrl(String urlAddress, String localFileName, String destinationDir) {

        System.out.println(urlAddress);
        System.out.println(localFileName);
        System.out.println(destinationDir);
        OutputStream outputStream = null;
        URLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlAddress);
            outputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(destinationDir + "/" + localFileName + ".json")));
            urlConnection = url.openConnection();
            inputStream = urlConnection.getInputStream();

            byte[] buf = new byte[1024];
            int byteRead, byteWritten = 0;
            while ((byteRead = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, byteRead);
                outputStream.flush();
                byteWritten += byteRead;
            }
        } catch (Exception e) {
            System.out.println("下载文件异常：" + e.getMessage());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (Exception e) {
                System.out.println("下载文件异常：" + e.getMessage());
            }
        }
    }

    /**
     * File download
     *
     * @param urlAddress url address
     * @param destinationDir destination dir
     */
    public static void fileDownload(String urlAddress, String destinationDir) {

        int slashIndex = destinationDir.lastIndexOf('/');
        int periodIndex = destinationDir.lastIndexOf('.');

        System.out.println(slashIndex);
        System.out.println(periodIndex);

        String fileName = destinationDir.substring(slashIndex + 1, periodIndex);

        if (periodIndex >= 1 && slashIndex >= 0 && slashIndex < urlAddress.length() - 1) {
            fileUrl(urlAddress, fileName, destinationDir);
        } else {
            System.err.println("path or file name.");
        }
        System.err.println(fileName);
    }

    /**
     * @功能 下载临时素材接口
     * @param fileName 文件将要保存的名字
     * @param method 请求方法，包括POST和GET
     * @param url 请求的路径
     * @return
     */

    public static Boolean saveUrlAs(String url, String fileName, String method){

        //创建不同的文件夹目录
        File file = new File(dog01JsonPath);
        //判断文件夹是否存在
        if (!file.exists()) {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setRequestProperty("User-Agent", "Mozilla/4.76");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!dog01JsonPath.endsWith("/")) dog01JsonPath += "/";
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(dog01JsonPath + fileName + ".json");
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("抛出异常！！");
            return false;
        }
        return true;
    }

}
