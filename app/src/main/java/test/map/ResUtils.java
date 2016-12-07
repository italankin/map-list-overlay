package test.map;

import android.support.annotation.DrawableRes;

public class ResUtils {

    @DrawableRes
    public static int getIcon(int type) {
        switch (type) {
            case 0:
                return R.drawable.ic_local_dining;
            case 1:
                return R.drawable.ic_local_gas_station;
            case 2:
                return R.drawable.ic_local_hospital;
        }
        return 0;
    }

}
