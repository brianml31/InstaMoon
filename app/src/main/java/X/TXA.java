package X;

import android.content.Context;

import androidx.fragment.app.FragmentActivity;

import com.brianml31.instamoon.utils.ToastUtils;
import com.instagram.common.session.UserSession;


public class TXA {
    public static final TXA A00 = new TXA();

    public final void A02(Context context, FragmentActivity fragmentActivity, UserSession userSession){
        ToastUtils.Companion.showShortToast(context, "Open developer options");
    }

//    public final void loadAndLaunchDeveloperOptions(Context context, _2up _2up, FragmentActivity fragmentActivity, UserSession userSession) {
//
//    }

}
