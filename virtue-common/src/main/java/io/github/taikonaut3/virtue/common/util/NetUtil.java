package io.github.taikonaut3.virtue.common.util;

import io.github.taikonaut3.virtue.common.constant.SystemKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public final class NetUtil {

    private static final Logger logger = LoggerFactory.getLogger(NetUtil.class);
    private static volatile InetAddress LOCAL_ADDRESS = null;
    private static final String IP_REGEX = "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    private static final String PORT_REGEX = "^\\d{1,5}$";

    public static boolean isValidIpPort(String ipPort) {
        String[] parts = ipPort.split(":");
        if (parts.length != 2) {
            return false;
        }
        String ip = parts[0];
        String port = parts[1];
        return isValidIp(ip) && isValidPort(port);
    }

    public static boolean isValidIp(String ip) {
        return Pattern.matches(IP_REGEX, ip);
    }

    public static boolean isValidPort(String port) {
        return Pattern.matches(PORT_REGEX, port);
    }

    public static int ipToInt(String ipAddress) {
        String[] ipArr = ipAddress.split("\\.");
        byte[] ipByteArr = new byte[4];
        for (int i = 0; i < 4; i++) {
            ipByteArr[i] = (byte) (Integer.parseInt(ipArr[i]) & 0xff);
        }
        int ipInt = 0;
        for (byte b : ipByteArr) {
            ipInt <<= 8;
            ipInt |= b & 0xff;
        }
        return ipInt;
    }

    public static String getAddress(InetSocketAddress address) {
        return address.getHostString() + ":" + address.getPort();
    }

    public static InetSocketAddress toInetSocketAddress(String address) {
        String[] parts = address.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid address format: " + address);
        }
        String ip = parts[0];
        int port = Integer.parseInt(parts[1]);
        return new InetSocketAddress(ip, port);
    }

    public static String getAddress(String host, int port) {
        return host + ":" + port;
    }

    public static String getLocalHost() {
        String localHost = System.getProperty(SystemKey.LOCAL_IP);
        if (!StringUtil.isBlank(localHost)) {
            return localHost;
        }
        InetAddress localAddress = getFirstLocalAddress();
        if (localAddress != null) {
            return localAddress.getHostAddress();
        }
        return null;
    }

    public static InetAddress getFirstLocalAddress() {
        if (LOCAL_ADDRESS != null) {
            return LOCAL_ADDRESS;
        }
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                // 排除回环接口和未启用的接口
                if (networkInterface.isUp() && !networkInterface.isLoopback() && !networkInterface.isVirtual()) {
                    Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                    while (inetAddresses.hasMoreElements()) {
                        InetAddress inetAddress = inetAddresses.nextElement();
                        // 排除回环地址和IPv6地址
                        if (!inetAddress.isLoopbackAddress() && !inetAddress.getHostAddress().contains(":")) {
                            LOCAL_ADDRESS = inetAddress;
                            return LOCAL_ADDRESS;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            logger.error("", e);
        }
        return null;
    }
    private NetUtil() {
    }

}
