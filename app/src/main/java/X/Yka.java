package X;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.brianml31.instamoon.utils.ToastUtils;
import com.instagram.common.session.UserSession;


public class Yka {
    public static final Yka A00 = new Yka();

    public final void A02(Context context, FragmentActivity fragmentActivity, UserSession userSession){
        ToastUtils.Companion.showShortToast(context, "Open developer options");
    }

//    public final void loadAndLaunchDeveloperOptions(Context context, _2up _2up, FragmentActivity fragmentActivity, UserSession userSession) {
//
//    }

}
