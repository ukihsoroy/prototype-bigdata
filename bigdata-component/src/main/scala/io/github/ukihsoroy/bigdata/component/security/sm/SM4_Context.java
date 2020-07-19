package io.github.ukihsoroy.bigdata.component.security.sm;

public class SM4_Context {
    public int mode = 1;
    public long[] sk = new long[32];
    public boolean isPadding = true;

    public SM4_Context() {
    }
}
