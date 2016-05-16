package com.begentgroup.miniapplication.manager;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.begentgroup.miniapplication.MyApplication;
import com.begentgroup.miniapplication.data.FacebookFeed;
import com.begentgroup.miniapplication.data.FacebookFeedsResult;
import com.begentgroup.miniapplication.data.FacebookIdResult;
import com.begentgroup.miniapplication.data.FacebookUploadResult;
import com.begentgroup.miniapplication.data.MyInfo;
import com.begentgroup.miniapplication.data.MyPictureResult;
import com.begentgroup.miniapplication.data.TStoreCategory;
import com.begentgroup.miniapplication.data.TStoreCategoryProduct;
import com.begentgroup.miniapplication.data.TStoreCategoryProductResult;
import com.begentgroup.miniapplication.data.TStoreCategoryResult;
import com.begentgroup.miniapplication.data.TStoreProduct;
import com.begentgroup.miniapplication.data.TStoreProductDetailResult;
import com.begentgroup.miniapplication.login.MyResult;
import com.begentgroup.miniapplication.login.MyResultError;
import com.begentgroup.miniapplication.login.MyResultUser;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.URLEncoder;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by dongja94 on 2016-05-09.
 */
public class NetworkManager {
    private static NetworkManager instance;
    public static NetworkManager getInstance() {
        if (instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }

    private static final int DEFAULT_CACHE_SIZE = 50 * 1024 * 1024;
    private static final String DEFAULT_CACHE_DIR = "miniapp";
    OkHttpClient mClient;
    private NetworkManager() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        Context context = MyApplication.getContext();
        CookieManager cookieManager = new CookieManager(new PersistentCookieStore(context), CookiePolicy.ACCEPT_ALL);
        builder.cookieJar(new JavaNetCookieJar(cookieManager));

        File dir = new File(context.getExternalCacheDir(), DEFAULT_CACHE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        builder.cache(new Cache(dir, DEFAULT_CACHE_SIZE));

        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(30, TimeUnit.SECONDS);
        builder.writeTimeout(30, TimeUnit.SECONDS);

        mClient = builder.build();
    }

    public interface OnResultListener<T> {
        public void onSuccess(Request request, T result);
        public void onFail(Request request, IOException exception);
    }

    private static final int MESSAGE_SUCCESS = 1;
    private static final int MESSAGE_FAIL = 2;

    class NetworkHandler extends Handler {
        public NetworkHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            NetworkResult result = (NetworkResult)msg.obj;
            switch (msg.what) {
                case MESSAGE_SUCCESS :
                    result.listener.onSuccess(result.request, result.result);
                    break;
                case MESSAGE_FAIL :
                    result.listener.onFail(result.request, result.excpetion);
                    break;
            }
        }
    }

    NetworkHandler mHandler = new NetworkHandler(Looper.getMainLooper());

    static class NetworkResult<T> {
        Request request;
        OnResultListener<T> listener;
        IOException excpetion;
        T result;
    }

    Gson gson = new Gson();

    private static final String TSTORE_SERVER = "http://apis.skplanetx.com";
    private static final String TSTORE_CATEGORY_URL = TSTORE_SERVER + "/tstore/categories?version=1";
    public Request getTStoreCategory(Object tag, OnResultListener<List<TStoreCategory>> listener) {
        Request request = new Request.Builder()
                .url(TSTORE_CATEGORY_URL)
                .header("Accept","application/json")
                .header("appKey","458a10f5-c07e-34b5-b2bd-4a891e024c2a")
                .build();
        final NetworkResult<List<TStoreCategory>> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    TStoreCategoryResult data = gson.fromJson(response.body().charStream(), TStoreCategoryResult.class);
                    result.result = data.tstore.categories.categoryList;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String TSTORE_CATEGORY_PRODUCT_URL = TSTORE_SERVER + "/tstore/categories/%s?version=1&page=%s&count=%s&order=%s";

    public static final String CATEGORY_PRODUCT_ORDER_BEST_C = "C";
    public static final String CATEGORY_PRODUCT_ORDER_BEST_F = "F";
    public static final String CATEGORY_PRODUCT_ORDER_NEW = "N";
    public static final String CATEGORY_PRODUCT_ORDER_R = "R";

    public Request getTStoreCategoryProductList(Object tag, String code, int page, int count, String order,
                                                OnResultListener<TStoreCategoryProduct> listener) {
        String url = String.format(TSTORE_CATEGORY_PRODUCT_URL, code, page, count, order);
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("appKey","458a10f5-c07e-34b5-b2bd-4a891e024c2a")
                .build();

        final NetworkResult<TStoreCategoryProduct> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    TStoreCategoryProductResult data = gson.fromJson(response.body().charStream(), TStoreCategoryProductResult.class);
                    result.result = data.tstore;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String TSTORE_SEARCH_PRODUCT_URL = TSTORE_SERVER + "/tstore/products?version=1&searchKeyword=%s&page=%s&count=%s&order=%s";

    public static final String SEARCH_PRODUCT_ORDER_R = "R";
    public static final String SEARCH_PRODUCT_ORDER_L = "L";
    public static final String SEARCH_PRODUCT_ORDER_D = "D";

    public Request getTStoreSearchProductList(Object tag, String keyword, int page, int count, String order,
                                                OnResultListener<TStoreCategoryProduct> listener) throws UnsupportedEncodingException {
        String url = String.format(TSTORE_SEARCH_PRODUCT_URL, URLEncoder.encode(keyword,"utf-8"), page, count, order);
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("appKey","458a10f5-c07e-34b5-b2bd-4a891e024c2a")
                .build();

        final NetworkResult<TStoreCategoryProduct> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    TStoreCategoryProductResult data = gson.fromJson(response.body().charStream(), TStoreCategoryProductResult.class);
                    result.result = data.tstore;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String TSTORE_DETAIL_PRODUCT = TSTORE_SERVER + "/tstore/products/%s?version=1";

    public Request getTStoreDetailProduct(Object tag, String productId,
                                              OnResultListener<TStoreProduct> listener) {
        String url = String.format(TSTORE_DETAIL_PRODUCT, productId);
        Request request = new Request.Builder()
                .url(url)
                .header("Accept","application/json")
                .header("appKey","458a10f5-c07e-34b5-b2bd-4a891e024c2a")
                .build();

        final NetworkResult<TStoreProduct> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    TStoreProductDetailResult data = gson.fromJson(response.body().charStream(), TStoreProductDetailResult.class);
                    data.tstore.product.makePreviewUrlList();
                    result.result = data.tstore.product;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String FACEBOOK_SERVER = "https://graph.facebook.com";
    private static final String FACEBOOK_FEEDS = FACEBOOK_SERVER + "/v2.6/me/feed?access_token=%s";

    public Request getFacebookFeeds(Object tag, String token,
                                          OnResultListener<List<FacebookFeed>> listener) {
        String url = String.format(FACEBOOK_FEEDS, token);
        Request request = new Request.Builder()
                .url(url)
                .build();

        final NetworkResult<List<FacebookFeed>> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    FacebookFeedsResult data = gson.fromJson(response.body().charStream(), FacebookFeedsResult.class);
                    data.convertStringToDate();
                    result.result = data.feeds;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String FACEBOOK_MY_INFO = FACEBOOK_SERVER + "/v2.6/me?fields=id,name,email&access_token=%s";

    public Request getFacebookMyInfo(Object tag, String token,
                                    OnResultListener<MyInfo> listener) {
        String url = String.format(FACEBOOK_MY_INFO, token);
        Request request = new Request.Builder()
                .url(url)
                .build();

        final NetworkResult<MyInfo> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    MyInfo data = gson.fromJson(response.body().charStream(), MyInfo.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String FACEBOOK_MY_PICTURE = FACEBOOK_SERVER + "/v2.6/me/picture?type=large&access_token=%s";

    public Request getFacebookMyPicture(Object tag, String token,
                                     OnResultListener<String> listener) {
        String url = String.format(FACEBOOK_MY_PICTURE, token);
        Request request = new Request.Builder()
                .url(url)
                .build();

        final NetworkResult<String> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    MyPictureResult data = gson.fromJson(text, MyPictureResult.class);
                    result.result = data.data.url;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String FACEBOOK_POST = FACEBOOK_SERVER + "/v2.6/me/feed?access_token=%s";

    public Request getFacebookPost(Object tag, String token,
                                   String message,
                                   String caption,
                                   String link,
                                   String picture,
                                   String name,
                                   String description,
                                        OnResultListener<String> listener) {
        String url = String.format(FACEBOOK_POST, token);

        RequestBody body = new FormBody.Builder()
                .add("message", message)
                .add("link", link)
                .add("caption", caption)
                .add("picture", picture)
                .add("name", name)
                .add("description", description)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        final NetworkResult<String> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    FacebookIdResult data = gson.fromJson(text, FacebookIdResult.class);
                    result.result = data.id;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String FACEBOOK_UPLOAD_PHOTO = FACEBOOK_SERVER + "/v2.6/me/photos?access_token=%s";

    public Request getFacebookUpload(Object tag, String token,
                                   String caption,
                                   File file,
                                   OnResultListener<FacebookUploadResult> listener) {
        String url = String.format(FACEBOOK_UPLOAD_PHOTO, token);

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("caption", caption)
                .addFormDataPart("picture", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpeg"), file))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        final NetworkResult<FacebookUploadResult> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    FacebookUploadResult data = gson.fromJson(text, FacebookUploadResult.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String MY_SERVER = "https://dongjaeducation.appspot.com";
    private static final String URL_SIGN_UP = MY_SERVER + "/signup";

    public Request signup(Object tag, String username,
                                     String email,
                                     String password,
                                     String registrationId,
                                     OnResultListener<MyResultUser> listener) {
        RequestBody body = new FormBody.Builder()
                .add("username", username)
                .add("password", password)
                .add("email",email)
                .add("registrationId", registrationId)
                .build();

        Request request = new Request.Builder()
                .url(URL_SIGN_UP)
                .post(body)
                .build();

        final NetworkResult<MyResultUser> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    MyResultUser data = gson.fromJson(text, MyResultUser.class);
                    result.result = data;
                    mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }

    private static final String URL_SIGN_IN = MY_SERVER + "/signin";

    public Request signin(Object tag,
                          String email,
                          String password,
                          String registrationId,
                          OnResultListener<MyResultUser> listener) {
        RequestBody body = new FormBody.Builder()
                .add("password", password)
                .add("email",email)
                .add("registrationId", registrationId)
                .build();

        Request request = new Request.Builder()
                .url(URL_SIGN_IN)
                .post(body)
                .build();

        final NetworkResult<MyResultUser> result = new NetworkResult<>();
        result.request = request;
        result.listener = listener;
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                result.excpetion = e;
                mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_FAIL, result));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String text = response.body().string();
                    MyResult status = gson.fromJson(text, MyResult.class);
                    if (status.code == 1) {
                        MyResultUser data = gson.fromJson(text, MyResultUser.class);
                        result.result = data;
                        mHandler.sendMessage(mHandler.obtainMessage(MESSAGE_SUCCESS, result));
                    } else {
                        MyResultError data = gson.fromJson(text, MyResultError.class);
                        throw new IOException(data.result);
                    }
                } else {
                    throw new IOException(response.message());
                }
            }
        });
        return request;
    }


}
