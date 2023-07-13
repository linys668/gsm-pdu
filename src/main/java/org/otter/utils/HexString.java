package org.otter.utils;

import java.util.Arrays;

public class HexString {
    private final static char[] digits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
    private final static byte[] undigits = new byte[128];

    static {
        char[] lower = "0123456789abcdef".toCharArray();
        char[] upper = "0123456789ABCDEF".toCharArray();
        Arrays.fill(undigits, (byte) -1);
        for (byte i = 0; i < 16; i++) {
            undigits[lower[i]] = i;
        }
        for (byte i = 0; i < 16; i++) {
            undigits[upper[i]] = i;
        }
    }

    public static String bytesToHex(byte[] bytes) {
        int dataLen = bytes.length;
        char[] chars = new char[dataLen << 1];
        for (int i = 0; i < dataLen; i++) {
            chars[i << 1] = digits[(bytes[i] >> 4) & 0xF];
            chars[(i << 1) + 1] = digits[bytes[i] & 0xF];
        }
        return new String(chars);
    }

    public static byte[] hexToBytes(String hex) {
        byte[] bytes = new byte[hex.length() >> 1];
        int dataLen = bytes.length;
        for (int i = 0; i < dataLen; i++) {
            bytes[i] = (byte) (undigits[hex.charAt(i << 1)] << 4 | undigits[hex.charAt((i << 1) + 1)]);
        }
        return bytes;
    }
}
