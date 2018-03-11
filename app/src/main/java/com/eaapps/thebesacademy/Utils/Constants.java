package com.eaapps.thebesacademy.Utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eaapps.thebesacademy.Admin.AdminHome;
import com.eaapps.thebesacademy.R;
import com.eaapps.thebesacademy.Student.StudentHome;
import com.eaapps.thebesacademy.Teacher.HomeTeacher;

import java.util.ArrayList;


public class Constants {
    public static String LOGIN = "login";
    public static String ADMIN = "admin";
    public static String STUDENT = "student";
    public static String TEACHER = "teacher";


    public static ArrayList<String> getEmptyField(Activity activity, EditText... id) {
        ArrayList<String> listEmpty = new ArrayList<>();
        for (int i = 0; i < id.length; i++) {
            if (TextUtils.isEmpty(id[i].getText().toString())) {
                listEmpty.add(id[i].getHint().toString());
            }
        }
        return listEmpty;
    }

    public static ArrayList<String> getStringsEmpty(String[] args, String[] argsTitle) {
        ArrayList<String> listEmpty = new ArrayList<>();
        for (int i = 0; i < argsTitle.length; i++) {
            if (TextUtils.isEmpty(args[i])) {
                listEmpty.add(argsTitle[i]);
            }
        }
        return listEmpty;
    }

    public static Class homeClasses(String user) {

        if (user.equalsIgnoreCase("Admin")) {
            return AdminHome.class;

        } else if (user.equalsIgnoreCase("Student")) {
            return StudentHome.class;
        } else if (user.equalsIgnoreCase("Teacher")) {
            return HomeTeacher.class;

        }
        return null;
    }

    public static void customToast(Context context, String msg) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_toast, null);
        TextView text = v.findViewById(R.id.textToast);
        text.setText(msg);
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM, 0, 0);
        toast.setView(v);
        toast.show();
    }


    public static void customDialogAlter(final Context context, int layoutResource, String msg, String titleButton, final ClickButton clickButton) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResource);
        dialog.setCancelable(false);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        dialogButton.setText(titleButton);
        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_dialog2);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickButton.clickAction();
                dialog.dismiss();
            }
        });
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void customDialogAlter(final Context context, int layoutResource, String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(layoutResource);
        dialog.setCancelable(false);
        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText(msg);
        Button dialogCancel = (Button) dialog.findViewById(R.id.btn_dialog2);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public interface ClickButton {
        void clickAction();
    }

}
