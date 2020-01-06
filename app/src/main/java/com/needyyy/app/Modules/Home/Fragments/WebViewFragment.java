package com.needyyy.app.Modules.Home.Fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.needyyy.app.Modules.Profile.fragments.SeeAllPhotoFragment;
import com.needyyy.app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WebViewFragment extends Fragment {
    private WebView webView;
    String url;
    public WebViewFragment() {
    }

    public static Fragment newInstance(String url) {
        WebViewFragment webViewFragment=new WebViewFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",url);
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getArguments()!=null) {
            url=getArguments().getString("url");
        }
        View v=inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = (WebView) v.findViewById(R.id.web_View);
        if(url.contains("https://")) {
            webView.loadUrl(url);
        }
        else {
            webView.loadUrl("https://" + url);
        }

//        if(getArguments()!=null)
//        {
//            url=getArguments().getString("url");
//       }
//       webView.loadUrl(url);

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
        return v;
    }

}
