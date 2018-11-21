package com.dubbo.trace.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author 王柱星
 * @version 1.0
 * @title
 * @time 2018年10月29日
 * @since 1.0
 */
public class NetworkUtils {
    public static String ip;
    private static InetAddress addr;

    static {
        try {
            addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress().toString(); //获取本机ip
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static int ipToLong(String strIp) {
        int[] ip = new int[4];
        //先找到IP地址字符串中.的位置  
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型  
        ip[0] = Integer.parseInt(strIp.substring(0, position1));
        ip[1] = Integer.parseInt(strIp.substring(position1 + 1, position2));
        ip[2] = Integer.parseInt(strIp.substring(position2 + 1, position3));
        ip[3] = Integer.parseInt(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    public static int getIp() {
        return ipToLong(ip);
    }
}
