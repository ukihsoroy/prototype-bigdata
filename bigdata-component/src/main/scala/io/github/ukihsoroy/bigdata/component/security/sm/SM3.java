package io.github.ukihsoroy.bigdata.component.security.sm;

public class SM3 {
    public static final byte[] IV = new byte[]{(byte)115, (byte)-128, (byte)22, (byte)111, (byte)73, (byte)20, (byte)-78, (byte)-71, (byte)23, (byte)36, (byte)66, (byte)-41, (byte)-38, (byte)-118, (byte)6, (byte)0, (byte)-87, (byte)111, (byte)48, (byte)-68, (byte)22, (byte)49, (byte)56, (byte)-86, (byte)-29, (byte)-115, (byte)-18, (byte)77, (byte)-80, (byte)-5, (byte)14, (byte)78};
    public static int[] Tj = new int[64];

    private SM3() {
    }

    public static byte[] cf(byte[] v, byte[] b) {
        return convert(cf(convert(v), convert(b)));
    }

    private static int[] convert(byte[] arr) {
        int[] out = new int[arr.length / 4];
        byte[] tmp = new byte[4];

        for(int i = 0; i < arr.length; i += 4) {
            System.arraycopy(arr, i, tmp, 0, 4);
            out[i / 4] = bigEndianByteToInt(tmp);
        }

        return out;
    }

    private static byte[] convert(int[] arr) {
        byte[] out = new byte[arr.length * 4];
        Object tmp = null;

        for(int i = 0; i < arr.length; ++i) {
            byte[] var4 = bigEndianIntToByte(arr[i]);
            System.arraycopy(var4, 0, out, i * 4, 4);
        }

        return out;
    }

    public static int[] cf(int[] argV, int[] argB) {
        int a = argV[0];
        int b = argV[1];
        int c = argV[2];
        int d = argV[3];
        int e = argV[4];
        int f = argV[5];
        int g = argV[6];
        int h = argV[7];
        int[][] arr = expand(argB);
        int[] w = arr[0];
        int[] w1 = arr[1];

        for(int out = 0; out < 64; ++out) {
            int ss1 = bitCycleLeft(a, 12) + e + bitCycleLeft(Tj[out], out);
            ss1 = bitCycleLeft(ss1, 7);
            int ss2 = ss1 ^ bitCycleLeft(a, 12);
            int tt1 = ffj(a, b, c, out) + d + ss2 + w1[out];
            int tt2 = ggj(e, f, g, out) + h + ss1 + w[out];
            d = c;
            c = bitCycleLeft(b, 9);
            b = a;
            a = tt1;
            h = g;
            g = bitCycleLeft(f, 19);
            f = e;
            e = p0(tt2);
        }

        return new int[]{a ^ argV[0], b ^ argV[1], c ^ argV[2], d ^ argV[3], e ^ argV[4], f ^ argV[5], g ^ argV[6], h ^ argV[7]};
    }

    private static int[][] expand(int[] argB) {
        int[] w = new int[68];
        int[] w1 = new int[64];

        int arr;
        for(arr = 0; arr < argB.length; ++arr) {
            w[arr] = argB[arr];
        }

        for(arr = 16; arr < 68; ++arr) {
            w[arr] = p1(w[arr - 16] ^ w[arr - 9] ^ bitCycleLeft(w[arr - 3], 15)) ^ bitCycleLeft(w[arr - 13], 7) ^ w[arr - 6];
        }

        for(arr = 0; arr < 64; ++arr) {
            w1[arr] = w[arr] ^ w[arr + 4];
        }

        return new int[][]{w, w1};
    }

    private static byte[] bigEndianIntToByte(int num) {
        return back(SMUtil.intToBytes(num));
    }

    private static int bigEndianByteToInt(byte[] bytes) {
        return SMUtil.byteToInt(back(bytes));
    }

    private static int ffj(int argX, int argY, int argZ, int j) {
        return j >= 0 && j <= 15?ff1j(argX, argY, argZ):ff2j(argX, argY, argZ);
    }

    private static int ggj(int argX, int argY, int argZ, int j) {
        return j >= 0 && j <= 15?gg1j(argX, argY, argZ):gg2j(argX, argY, argZ);
    }

    private static int ff1j(int argX, int argY, int argZ) {
        return argX ^ argY ^ argZ;
    }

    private static int ff2j(int argX, int argY, int argZ) {
        return argX & argY | argX & argZ | argY & argZ;
    }

    private static int gg1j(int argX, int argY, int argZ) {
        return argX ^ argY ^ argZ;
    }

    private static int gg2j(int argX, int argY, int argZ) {
        return argX & argY | ~argX & argZ;
    }

    private static int p0(int argX) {
        int y = rotateLeft(argX, 9);
        y = bitCycleLeft(argX, 9);
        int z = rotateLeft(argX, 17);
        z = bitCycleLeft(argX, 17);
        int t = argX ^ y ^ z;
        return t;
    }

    private static int p1(int argX) {
        int t = argX ^ bitCycleLeft(argX, 15) ^ bitCycleLeft(argX, 23);
        return t;
    }

    public static byte[] padding(byte[] in, int bLen) {
        int k = 448 - (8 * in.length + 1) % 512;
        if(k < 0) {
            k = 960 - (8 * in.length + 1) % 512;
        }

        ++k;
        byte[] padd = new byte[k / 8];
        padd[0] = -128;
        long n = in.length * 8 + bLen * 512;
        byte[] out = new byte[in.length + k / 8 + 8];
        byte pos = 0;
        System.arraycopy(in, 0, out, 0, in.length);
        int var9 = pos + in.length;
        System.arraycopy(padd, 0, out, var9, padd.length);
        var9 += padd.length;
        byte[] tmp = back(SMUtil.longToBytes(n));
        System.arraycopy(tmp, 0, out, var9, tmp.length);
        return out;
    }

    private static byte[] back(byte[] in) {
        byte[] out = new byte[in.length];

        for(int i = 0; i < out.length; ++i) {
            out[i] = in[out.length - i - 1];
        }

        return out;
    }

    public static int rotateLeft(int x, int n) {
        return x << n | x >> 32 - n;
    }

    private static int bitCycleLeft(int n, int bitLen) {
        bitLen %= 32;
        byte[] tmp = bigEndianIntToByte(n);
        int byteLen = bitLen / 8;
        int len = bitLen % 8;
        if(byteLen > 0) {
            tmp = byteCycleLeft(tmp, byteLen);
        }

        if(len > 0) {
            tmp = bitSmall8CycleLeft(tmp, len);
        }

        return bigEndianByteToInt(tmp);
    }

    private static byte[] bitSmall8CycleLeft(byte[] in, int len) {
        byte[] tmp = new byte[in.length];

        for(int i = 0; i < tmp.length; ++i) {
            byte t1 = (byte)((in[i] & 255) << len);
            byte t2 = (byte)((in[(i + 1) % tmp.length] & 255) >> 8 - len);
            byte t3 = (byte)(t1 | t2);
            tmp[i] = t3;
        }

        return tmp;
    }

    private static byte[] byteCycleLeft(byte[] in, int byteLen) {
        byte[] tmp = new byte[in.length];
        System.arraycopy(in, byteLen, tmp, 0, in.length - byteLen);
        System.arraycopy(in, 0, tmp, in.length - byteLen, byteLen);
        return tmp;
    }

    static {
        int i;
        for(i = 0; i < 16; ++i) {
            Tj[i] = 2043430169;
        }

        for(i = 16; i < 64; ++i) {
            Tj[i] = 2055708042;
        }

    }
}
