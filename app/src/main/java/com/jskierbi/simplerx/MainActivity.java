package com.jskierbi.simplerx;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private MainActivityVm mVm;

    private Subscription dataLoadSubscription   = Subscriptions.empty();
    private Subscription userActionSubscription = Subscriptions.empty();
    private Subscription dialogSubscription     = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVm = MainActivityVm.create();

        findViewById(R.id.btn_call_async).setOnClickListener((view) -> subscribeUserAction(Calendar.getInstance()));
        subscribeLoadData();
    }

    @Override
    protected void onDestroy() {
        dataLoadSubscription.unsubscribe();
        userActionSubscription.unsubscribe();
        dialogSubscription.unsubscribe(); // Hide dialog - prevent window leak
        super.onDestroy();
    }

    private void subscribeLoadData() {
        dataLoadSubscription.unsubscribe();
        dataLoadSubscription = mVm.getData().subscribe((dto) -> {
            ((TextView) findViewById(R.id.text_title)).setText(dto.title);
            findViewById(R.id.btn_call_async).setVisibility(View.VISIBLE);
        }, (error) -> {
            // Perform scoped error handling here
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        });
    }

    private void subscribeUserAction(Calendar now) {
        ((TextView) findViewById(R.id.text_manual_async_result)).setText("");
        userActionSubscription.unsubscribe(); // we're not interested in listening to user action anymore
        userActionSubscription = mVm.performUserAction(now).subscribe((dto) -> {
            ((TextView) findViewById(R.id.text_manual_async_result)).setText(dto.text);
        }, (error) -> {
            // Use closure-captured parameter to perform retry
            Log.e("MainActivity", "Terrible thing happened!", error);
            final Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("Operation has failed")
                .setPositiveButton("retry", (view, which) -> subscribeUserAction(now))
                .show();
            dialogSubscription.unsubscribe();
            dialogSubscription = Subscriptions.create(() -> dialog.dismiss());
        });
    }
}
