package sk.kasv.mrazik.fitfusion.utils;

import com.google.gson.Gson;

public class GsonUtil {

    private GsonUtil() {}

    private static class GsonHolder {
        private static final Gson INSTANCE = new Gson();
    }

    // Public method to access the Gson instance
    public static Gson getInstance() {
        return GsonHolder.INSTANCE;
    }
}
