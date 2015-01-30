/*
Copyright 2014 Google Inc. All rights reserved.

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/

package com.example.mirnabouchra.cloudolympics;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.support.v4.util.LruCache;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader;

import org.apache.http.client.HttpClient;

/**
 * VolleyUtils utils.
 */
public final class VolleyUtils {
    private static class VolleyImageCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(100);

        @Override
        public Bitmap getBitmap(String s) {
            return cache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            cache.put(s, bitmap);
        }
    }

    private VolleyUtils(){}

    private static ImageLoader imageLoader;
    private static RequestQueue requestQueue;

    public static RequestQueue getRequestQueue(Context context) {
        ensureOnMainThread();
        if (requestQueue == null) {
            requestQueue = createRequestQueue(context);
            requestQueue.start();
        }
        return requestQueue;
    }

    public static ImageLoader getImageLoader(Context context) {
        ensureOnMainThread();
        if (imageLoader == null) {
            imageLoader = new ImageLoader(getRequestQueue(context), new VolleyImageCache());
        }
        return imageLoader;
    }

    private static RequestQueue createRequestQueue(Context context) {
        // Ensure we have the application context - do not want to hold on to a shorter-lived
        // context (like an Activity).
        Context appContext = context.getApplicationContext();

        Network network = new BasicNetwork(new HurlStack());
        Cache cache = new DiskBasedCache(appContext.getCacheDir(), 1024 * 1024); // 1MB cap.
        return new RequestQueue(cache, network);
    }

    private static void ensureOnMainThread() {
        if (Looper.getMainLooper().getThread() != Looper.myLooper().getThread()) {
            throw new IllegalStateException("Must be called on the main thread.");
        }
    }
}