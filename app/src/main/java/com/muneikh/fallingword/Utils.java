package com.muneikh.fallingword;

import android.content.Context;
import android.content.res.Resources;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static String readFileFromRes(Context context, int id) throws IOException {
        Resources res = context.getResources();
        InputStream is = res.openRawResource(id);

        byte[] b = new byte[is.available()];
        is.read(b);

        return new String(b);
    }


}
