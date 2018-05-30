# InAppPurchaseDemo

Android Google Play Billing Demo

Google Play Billing is a service provided by Google Play that lets you sell digital content from inside an Android app or “in-app.” This document describes the fundamental building blocks of a Google Play Billing solution. 

Google Play Billing can be used to sell two types of in-app products:

One-time products: an in-app product requiring a single, non-recurring charge to the user's form of payment. Additional game levels, premium loot boxes, or media files, are examples of one-time products. The Google Play Console refers to one-time products as "managed products", and the Google Play Billing library specifies one-time products as "INAPP".
Subscriptions: An in-app product requiring a recurring charge to the user's form of payment. Online magazines or music streaming services are examples of subscriptions.

Step 1 : Add this in gradle...

dependencies {
    ...
    implementation 'com.android.billingclient:billing:1.1'
}

Step 2 : Create an instance of BillingClient

// create new Person
private BillingClient mBillingClient;
...
mBillingClient = BillingClient.newBuilder(mActivity).setListener(this).build();
mBillingClient.startConnection(new BillingClientStateListener() {
    @Override
    public void onBillingSetupFinished(@BillingResponse int billingResponseCode) {
        if (billingResponseCode == BillingResponse.OK) {
            // The billing client is ready. You can query purchases here.
        }
    }
    @Override
    public void onBillingServiceDisconnected() {
        // Try to restart the connection on the next request to
        // Google Play by calling the startConnection() method.
    }
});

Step 3 : Implement PurchasesUpdatedListener's onPurchasesUpdated() method to receive updates on purchases initiated by your app, as well as those initiated by the Google Play Store.

Step 4 : To start a purchase request from your app, call the launchBillingFlow() method from the UI thread.
    Pass a reference to a BillingFlowParams object containing the relevant data to complete the purchase, such as the product ID (skuId) of the item and product type (SkuType.INAPP for a one-time product or SkuType.SUBS for a subscription).

When you call the launchBillingFlow() method, the system displays the Google Play purchase screen.

Step 5 : To query Google Play for in-app product details, call the querySkuDetailsAsync() method.

When calling this method, pass an instance of SkuDetailsParams that specifies a list of products ID strings and a SkuType. The SkuType can be either SkuType.INAPP for one-time products, or SkuType.SUBS, for subscriptions.
    
Query most recent purchases 

The queryPurchases() method uses a cache of the Google Play Store app without initiating a network request.

If you need to check the most recent purchase made by the user for each product ID, you can use the queryPurchaseHistoryAsync() method.
