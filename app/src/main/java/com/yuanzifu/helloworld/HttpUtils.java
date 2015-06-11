package com.yuanzifu.helloworld;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils
{

    // ����������˵�url
    //private static String PATH = "https://w.seu.edu.cn/portal/login.php";
    private static String PATH = "http://www.seu.edu.cn";
    private static URL url;

    public HttpUtils()
    {
        // TODO Auto-generated constructor stub
    }

    static
    {
        try
        {
            url = new URL(PATH);
        } catch (MalformedURLException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @param params
     *            ��д��url�Ĳ���
     * @param encode
     *            �ֽڱ���
     * @return
     */
    public static String sendPostMessage(Map<String, String> params, String encode)
    {
        System.setProperty("jsse.enableSNIExtension", "false");
        // ��ΪStringBuffer��ʼ�����ַ���
        StringBuffer buffer = new StringBuffer();
        try
        {
            if (params != null && !params.isEmpty())
            {
                for (Map.Entry<String, String> entry : params.entrySet())
                {
                    // ���ת�����
                    buffer.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), encode)).append("&");
                }
                buffer.deleteCharAt(buffer.length() - 1);
            }
            // System.out.println(buffer.toString());
            // ɾ��������һ��&

            //System.out.println("-->>" + buffer.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(30000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);// ��ʾ�ӷ�������ȡ����
            urlConnection.setDoOutput(true);// ��ʾ�������д����
            // ����ϴ���Ϣ���ֽڴ�С�Լ�����
            byte[] mydata = buffer.toString().getBytes();
            // ��ʾ������������������ı�����
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.setRequestProperty("Content-Length", String.valueOf(mydata.length));
            // ��������,��������������
            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(mydata, 0, mydata.length);
            outputStream.close();
            // ��÷�������Ӧ�Ľ����״̬��
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200)
            {
                return changeInputStream(urlConnection.getInputStream(), encode);
            }
        } catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    /**
     * ��һ��������ת����ָ��������ַ���
     *
     * @param inputStream
     * @param encode
     * @return
     */
    private static String changeInputStream(InputStream inputStream, String encode)
    {
        // TODO Auto-generated method stub
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if (inputStream != null)
        {
            try
            {
                while ((len = inputStream.read(data)) != -1)
                {
                    outputStream.write(data, 0, len);
                }
                result = new String(outputStream.toByteArray(), encode);
            } catch (IOException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void main(String[] a)
    {
        // TODO Auto-generated method stub
        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "220131440");
        params.put("password", "574423");
        System.out.println("System-"+params.toString());
        String result = HttpUtils.sendPostMessage(params, "utf-8");
        System.out.println("--System->>" + result);
    }

}
