package com.example.mirnabouchra.cloudolympics;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ImageAdapter extends BaseAdapter {
    private static final String LOG_TAG = ImageAdapter.class.getSimpleName();

    class DownloadImageAsyncTask extends AsyncTask<Void, Void, Void> {
        private final String imageUrl;
        public DownloadImageAsyncTask(String imageUrl) {
            this.imageUrl = imageUrl;
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(
                        (InputStream) new URL(imageUrl).getContent());
                imageCache.put(imageUrl, bitmap);
            } catch (IOException e) {
                Log.e("DownloadImageAsyncTask", "Error reading bitmap" + e);
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            downloadingImageUrls.remove(imageUrl);
            notifyDataSetChanged();
        }
    }

    private Context mContext;
    private final LayoutInflater layoutInflater;
    private List<String> imageUrls = new ArrayList<String>();
    private List<String> names = new ArrayList<String>();
    private LruCache<String, Bitmap> imageCache = new LruCache<String, Bitmap>(100);
    private Set<String> downloadingImageUrls = new HashSet<String>();

    public ImageAdapter(Context c) {
        mContext = c;
        layoutInflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return imageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.d(LOG_TAG, "Setting VIEW ---------");
        LinearLayout layout;
        ImageView imageView;
        TextView textView;
        //if (convertView == null) { // create a new view if no recycled view is available
            layout = (LinearLayout) layoutInflater.inflate(
                    R.layout.grid_view_item, parent, false /* attachToRoot */);
            imageView = (ImageView) layout.findViewById(R.id.image);
            textView = (TextView) layout.findViewById(R.id.name);
        //} else {
          //  imageView = (ImageView) convertView;
            //imageView.setImageBitmap(null);
            //textView = (TextView) convertView.findViewById(R.id.name);
        //}
        if (position >= imageUrls.size()) { // imageUrls not yet downloaded!
            return imageView;
        }
        String imageUrl = imageUrls.get(position);

        Bitmap bitmap = imageCache.get(imageUrl);
        if (bitmap != null) {
            imageView.setImageDrawable(new RoundedAvatarDrawable(bitmap));
            // Use file name as content description.
            imageView.setContentDescription(Uri.parse(imageUrl).getLastPathSegment());
            Log.d(LOG_TAG, names.get(position));
            textView.setText(names.get(position));
        } else {
            if (!downloadingImageUrls.contains(imageUrl)) {
                downloadingImageUrls.add(imageUrl);
                new DownloadImageAsyncTask(imageUrl).execute();
            }
        }
        return layout;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        imageCache = new LruCache<String, Bitmap>(100);
        downloadingImageUrls = new HashSet<String>();
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
