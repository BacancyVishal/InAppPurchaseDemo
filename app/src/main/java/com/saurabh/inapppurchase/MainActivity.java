package com.saurabh.inapppurchase;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    // List of product products ID strings which
    // You defined product IDs when you configured your in-app products using the Google Play Console.
    List<String> skuList = new ArrayList<>();

    private BillingClient mBillingClient;
    private SkuDetailsParams.Builder params;

    private boolean isBillingClientConnection; // To check connection is established with Billing client

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an instance of BillingClient
        mBillingClient = BillingClient.newBuilder(this).setListener(this).build();

        // Create connection with BillingClient
        mBillingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
                if (billingResponseCode == BillingClient.BillingResponse.OK) {
                    // The billing client is ready. You can query purchases here.
                    Toast.makeText(MainActivity.this, "You are connected to Billing client. You can purchase now.", Toast.LENGTH_SHORT).show();
                    isBillingClientConnection = true;
                }
            }
            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
                Toast.makeText(MainActivity.this, "Could not connect with Billing client. Please try again later.", Toast.LENGTH_SHORT).show();
                isBillingClientConnection = false;
            }
        });

        // Initializations...

        skuList.add("sku1"); // This is for demo only. Change your own
        skuList.add("sku2"); // This is for demo only. Change your own

        /**
         * an instance of SkuDetailsParams that specifies a list of products ID strings and a SkuType.
         * The SkuType can be either SkuType.INAPP for one-time products, or SkuType.SUBS, for subscriptions.
         */

        params = SkuDetailsParams.newBuilder();
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);

        LinearLayout llSubscribeNews = findViewById(R.id.ll_subscribe_news);
        LinearLayout llSubscribe2 = findViewById(R.id.ll_subscribe_2);

        llSubscribeNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int response = purchase("sku1", BillingClient.SkuType.INAPP); // This is for demo only. Change your own
                if (response == BillingClient.BillingResponse.OK) {

                    // To retrieve information about purchases that a user makes from your app,
                    // call the queryPurchases() method with the purchase type
                    Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);

                    // Verify a purchase &
                    // then Change UI to display that item purchased
                    // For more details see #ReadMe...
                } else {
                    // UI will not change
                }
            }
        });

        llSubscribe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int response = purchase("sku2", BillingClient.SkuType.INAPP); // This is for demo only. Change your own
                if (response == BillingClient.BillingResponse.OK) {

                    // To retrieve information about purchases that a user makes from your app,
                    // call the queryPurchases() method with the purchase type
                    Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(BillingClient.SkuType.INAPP);

                    // Verify a purchase &
                    // then Change UI to display that item purchased
                    // For more details see #ReadMe...
                } else {
                    // UI will not change
                }
            }
        });

        /**
         * The queryPurchases() method uses a cache of the Google Play Store app without initiating a network request.
         *
         * queryPurchaseHistoryAsync() returns the most recent purchase made by the user for each product ID,
         * even if that purchase is expired, cancelled, or consumed.
         */

        mBillingClient.queryPurchaseHistoryAsync(BillingClient.SkuType.INAPP,
                new PurchaseHistoryResponseListener() {
                    @Override
                    public void onPurchaseHistoryResponse(@BillingClient.BillingResponse int responseCode,
                                                          List<Purchase> purchasesList) {
                        if (responseCode == BillingClient.BillingResponse.OK
                                && purchasesList != null) {
                            for (Purchase purchase : purchasesList) {
                                // Process the result.
                            }
                        }
                    }
                });

    }

    /**
     * To start a purchase request from your app
     *
     * @param skuId : Product id to be purchased
     * @param SkuType : Sku type of product
     * @return
     */

    public int purchase (String skuId, String SkuType) {
        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                .setSku(skuId)
                .setType(SkuType)
                .build();
        return mBillingClient.launchBillingFlow(MainActivity.this, flowParams);
    }


    /**
     * This is used to receive updates on purchases initiated by your app,
     * as well as those initiated by the Google Play Store.
     *
     * @param responseCode : Response code of purchase (More details... ReadMe)
     * @param purchases : List of purchased items
     */

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {

        if (responseCode == BillingClient.BillingResponse.OK)  {

            for (Purchase purchase : purchases) {
                for (String sku : skuList) {
                    if (purchase.getSku().equalsIgnoreCase(sku)) {
                        // Display your purchased item lists...+
                    }
                }
            }

        }
    }

    /**
     * The unique Product IDs you created when configuring your in-app products
     * are used to asynchronously query Google Play for in-app product details.
     *
     * To query Google Play for in-app product details, call the querySkuDetailsAsync() method.
     */

    public void queryInAppProductDetails() {

        mBillingClient.querySkuDetailsAsync(params.build(),
                new SkuDetailsResponseListener() {
                    @Override
                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {

                        // Process the result.
                        if (responseCode == BillingClient.BillingResponse.OK
                                && skuDetailsList != null) {
                            for (SkuDetails skuDetails : skuDetailsList) {
                                String sku = skuDetails.getSku();
                                String price = skuDetails.getPrice();

                                if ("premium_upgrade".equals(sku)) {

                                } else if ("gas".equals(sku)) {

                                }
                            }
                        }  else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
                            // Handle an error caused by a user cancelling the purchase flow.
                        } else {
                            // Handle any other error codes.
                        }

                    }
                });
    }
}
