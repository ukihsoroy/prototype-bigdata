package io.github.ukihsoroy.bigdata.component.security.sm;

public class SMUtil {
    public SMUtil() {
    }

    public static byte[] hexStringToBytes(String hexString) {
        if(hexString != null && !"".equals(hexString)) {
            hexString = hexString.toUpperCase();
            int length = hexString.length() / 2;
            char[] hexChars = hexString.toCharArray();
            byte[] d = new byte[length];

            for(int i = 0; i < length; ++i) {
                int pos = i * 2;
                d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
            }

            return d;
        } else {
            return null;
        }
    }

    public static byte charToByte(char c) {
        return (byte)"0123456789ABCDEF".indexOf(c);
    }

    public static byte[] longToBytes(long num) {
        byte[] bytes = new byte[8];

        for(int i = 0; i < 8; ++i) {
            bytes[i] = (byte)((int)(255L & num >> i * 8));
        }

        return bytes;
    }

    public static byte[] intToBytes(int num) {
        byte[] bytes = new byte[]{(byte)(255 & num >> 0), (byte)(255 & num >> 8), (byte)(255 & num >> 16), (byte)(255 & num >> 24)};
        return bytes;
    }

    public static int byteToInt(byte[] bytes) {
        byte num = 0;
        int temp = (255 & bytes[0]) << 0;
        int num1 = num | temp;
        temp = (255 & bytes[1]) << 8;
        num1 |= temp;
        temp = (255 & bytes[2]) << 16;
        num1 |= temp;
        temp = (255 & bytes[3]) << 24;
        num1 |= temp;
        return num1;
    }
}
