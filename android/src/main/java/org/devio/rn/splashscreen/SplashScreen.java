package org.devio.rn.splashscreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * SplashScreen
 * 启动屏
 * from：http://www.devio.org
 * Author:CrazyCodeBoy
 * GitHub:https://github.com/crazycodeboy
 * Email:crazycodeboy@gmail.com
 */
public class SplashScreen {
    private static Dialog mSplashDialog;
    private static WeakReference<Activity> mActivity;

    static class FullscreenDialog extends Dialog {

        public FullscreenDialog(@NonNull Context context) {
            super(context);
            setOwnerActivity((Activity) context);
        }

        public FullscreenDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
            setOwnerActivity((Activity) context);
        }

        protected FullscreenDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
            setOwnerActivity((Activity) context);
        }
    }

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity, final boolean fullScreen) {
        if (activity == null) return;
        mActivity = new WeakReference<Activity>(activity);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    if (fullScreen) {
                        mSplashDialog = new FullscreenDialog(activity, R.style.SplashScreen_Fullscreen);
                    } else {
                        mSplashDialog = new Dialog(activity, R.style.SplashScreen_SplashTheme);
                    }

                    mSplashDialog.setContentView(R.layout.launch_screen);
                    mSplashDialog.setCancelable(false);


                    if (!mSplashDialog.isShowing()) {
                        mSplashDialog.show();
                        Window dialogWindow = mSplashDialog.getWindow();
                        if (dialogWindow != null) {
                            Display display = dialogWindow.getWindowManager().getDefaultDisplay();
                            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                            Point point = new Point();
                            display.getSize(point);
                            lp.width = point.x;
                            lp.height = point.y - getStatusBarHeight(activity);
                            dialogWindow.setAttributes(lp);
                        }
                    }
                }
            }
        });
    }

    public static int getStatusBarHeight(@NonNull Activity activity) {
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

    /**
     * 打开启动屏
     */
    public static void show(final Activity activity) {
        show(activity, false);
    }

    /**
     * 关闭启动屏
     */
    public static void hide(Activity activity) {
        if (activity == null) {
            if (mActivity == null) {
                return;
            }
            activity = mActivity.get();
        }
        if (activity == null) return;

        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSplashDialog != null && mSplashDialog.isShowing()) {
                    mSplashDialog.dismiss();
                    mSplashDialog = null;
                }
            }
        });
    }
}
