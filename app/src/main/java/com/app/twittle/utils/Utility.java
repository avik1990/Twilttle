package com.app.twittle.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.widget.Toast;

import com.app.twittle.AboutUs;
import com.app.twittle.ContactUs;
import com.app.twittle.Feedbackactivity;
import com.app.twittle.OrderCancellation;
import com.app.twittle.Privacy;
import com.app.twittle.R;
import com.app.twittle.Refund;
import com.app.twittle.Replacement;
import com.app.twittle.ShippingContent;
import com.app.twittle.TermsCondition;

public class Utility {

    public static ConnectionDetector cd;
    public static String PACKAGE_NAME;

    public static void openNavDrawer(int id, final Context mContext) {
        cd = new ConnectionDetector(mContext);
        if (id == R.id.nav_aboutus) {
            if (cd.isConnected()) {
                if (!(mContext instanceof AboutUs)) {
                    Intent profileintent = new Intent(mContext, AboutUs.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }

        if (id == R.id.nav_privacypolicy) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Privacy)) {
                    Intent profileintent = new Intent(mContext, Privacy.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
            //showLogoutAlert(mContext, "Are you sure?", "Logout");
        }

        if (id == R.id.nav_terms) {
            if (cd.isConnected()) {
                if (!(mContext instanceof TermsCondition)) {
                    Intent profileintent = new Intent(mContext, TermsCondition.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }

        if (id == R.id.nav_contacts) {
            //  showLogoutAlert(mContext, "Are you sure?", "Logout");
        }
        if (id == R.id.nav_ordercancellation) {
            if (cd.isConnected()) {
                if (!(mContext instanceof OrderCancellation)) {
                    Intent profileintent = new Intent(mContext, OrderCancellation.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_replacement) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Replacement)) {
                    Intent profileintent = new Intent(mContext, Replacement.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_refund) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Refund)) {
                    Intent profileintent = new Intent(mContext, Refund.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_shareapp) {
            PACKAGE_NAME = mContext.getApplicationContext().getPackageName();
            shareAll(mContext, "", "", mContext.getResources().getString(R.string.share_pkg) + PACKAGE_NAME + "&hl=en");
        }

        if (id == R.id.nav_feedback) {
            if (cd.isConnected()) {
                if (!(mContext instanceof Feedbackactivity)) {
                    Intent profileintent = new Intent(mContext, Feedbackactivity.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_rateapp) {
            final String appPackageName = mContext.getPackageName(); // getPackageName() from Context or Activity object
            try {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        }
        if (id == R.id.nav_contacts) {
            if (cd.isConnected()) {
                if (!(mContext instanceof ContactUs)) {
                    Intent profileintent = new Intent(mContext, ContactUs.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
        if (id == R.id.nav_shipping) {
            if (cd.isConnected()) {
                if (!(mContext instanceof ShippingContent)) {
                    Intent profileintent = new Intent(mContext, ShippingContent.class);
                    mContext.startActivity(profileintent);
                }
            } else {
                showToastShort(mContext, "No Internet");
            }
        }
    }


    public static void shareAll(Context mContext, String heading, String sub, String links) {
        //String title = heading;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, links);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, heading);
        mContext.startActivity(Intent.createChooser(sharingIntent, "Share Using"));
    }

    public static void showToastShort(Context mContext, String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

}
