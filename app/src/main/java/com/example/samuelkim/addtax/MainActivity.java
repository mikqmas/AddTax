package com.example.samuelkim.addtax;

import android.accounts.Account;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v3.inventory.InventoryConnector;
import com.clover.sdk.v3.inventory.TaxRate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private InventoryConnector mInventoryConnector = null;
    private Account mAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this);

            if (mAccount == null) {
                return;
            }
        }
        connect();
    }

    private void connect() {
        if (mAccount != null) {
            mInventoryConnector = new InventoryConnector(this, mAccount, null);
            mInventoryConnector.connect();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disconnect();
    }

    private void disconnect() {
        if (mAccount != null) {
            mInventoryConnector.disconnect();
            mInventoryConnector = null;
        }
    }

    public void createTaxRate(View view) {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    TaxRate taxRate = new TaxRate().setName("test").setRate(100000l);
                    mInventoryConnector.createTaxRate(taxRate);
                    msg += taxRate.toString();
                } catch (Exception e) {
                    Log.e("exception log", e.getMessage(), e.getCause());
                    msg += e.toString();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                TextView log = (TextView) findViewById(R.id.statusText);
                log.append(s + "\n");
            }
        }.execute();
    }
}
