package marko.mitrovic.singidroid4.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import marko.mitrovic.singidroid4.R;
import marko.mitrovic.singidroid4.api.AppNetworking;
import marko.mitrovic.singidroid4.repo.SharedViewModel;
import marko.mitrovic.singidroid4.util.JSIntefrace;

import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Predmet extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    public WebView mWebView;
    private View view;
    private SharedPreferences studentPerfs;
    //NOTE Global IP used only for webview
    private final String GLOBAL_IP = AppNetworking.BASE_URL;
    private final String TAG = "PredmetClass";
    public Boolean backStatus = true;
    public int modalNumber = 0;
    private SharedViewModel viewModel;
    SwipeRefreshLayout swipeLayout;
    private PredmetSettingsDialog predmetSettings;
    private String courseId = "";

    public Predmet() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        studentPerfs = this.getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class); //get repo
        boolean skipped = studentPerfs.getBoolean("SkippedInit", true);
        view = inflater.inflate(R.layout.fragment_predmet_webview, container, false);
        if (!skipped) {

            courseId = studentPerfs.getString("CourseChoice", "120");

            loadPage(courseId);


        } else {
            predmetSettings = new PredmetSettingsDialog();
            predmetSettings.show(getActivity().getSupportFragmentManager(), "predmetSettings");
            //view = inflater.inflate(R.layout.predmeti_settings_dialog, container, false);
        }

        viewModel.getPredmetDialog().observe(getViewLifecycleOwner(), new Observer<String>(){
            @Override
            public void onChanged(String s) {
                loadPage(s);
            }
        });


        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        swipeLayout = view.findViewById(R.id.swiperefresh_predmet); //Get the refresh ID
        swipeLayout.setOnRefreshListener(this); //Set refresh listener

        int swiperColor = Color.parseColor(studentPerfs.getString("Color", "#A8011D"));// Sets it at first run
        swipeLayout.setColorSchemeColors(swiperColor); //Not gonna change the color of it immediately but it'll have a nice animation doing so

    }

    @Override
    public void onRefresh() {
        if (mWebView == null) {
            Log.d(TAG, "mWebView is null, called from onRefresh in Predmet");
            return;
        }
        mWebView.reload(); //Reload the WebView
    }

    public void loadPage(String courseId) {
        WebView.setWebContentsDebuggingEnabled(true); //NOTE DISABLE LATER
        mWebView = view.findViewById(R.id.predmet_webview);
        mWebView.setWebViewClient(new WebViewClient(){

            public void onPageFinished(WebView view, String url) {
                swipeLayout.setRefreshing(false); //Triggered once the page has been loaded
            }

            @Override
            public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
                try {
                    //NOTE SSL CERTIFICATE VERIFICATION

                    //Get the X509 trust manager from your ssl certificate
                    X509TrustManager trustManager = verifyCertificate(getContext());

                    //Get the certificate from error object
                    Bundle bundle = SslCertificate.saveState(error.getCertificate());
                    X509Certificate x509Certificate;
                    byte[] bytes = bundle.getByteArray("x509-certificate");
                    if (bytes == null) {
                        x509Certificate = null;
                    } else {
                        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
                        Certificate cert = certFactory.generateCertificate(new ByteArrayInputStream(bytes));
                        x509Certificate = (X509Certificate) cert;
                    }
                    X509Certificate[] x509Certificates = new X509Certificate[1];
                    x509Certificates[0] = x509Certificate;

                    // check weather the certificate is trusted
                    trustManager.checkServerTrusted(x509Certificates, "ECDH_RSA");

                    //NOTE Stole this code below from https://stackoverflow.com/a/38389335/13154944

                    Log.e(TAG, "Sertifikat sa: " + error.getUrl() + " je bezbedan.");
                    handler.proceed();
                } catch (Exception e) {
                    Log.e(TAG, "Neuspeli pristup: " + error.getUrl() + ". Greška: " + error.getPrimaryError());
                    final AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    String message = "SSL Certificate error.";
                    switch (error.getPrimaryError()) {
                        case SslError.SSL_UNTRUSTED:
                            message = "Sertifikat nije bezbedan.";
                            break;
                        case SslError.SSL_EXPIRED:
                            message = "Sertifikat je istekao.";
                            break;
                        case SslError.SSL_IDMISMATCH:
                            message = "ID Sertifikata  se ne poklapa.";
                            break;
                        case SslError.SSL_NOTYETVALID:
                            message = "Sertifikat jos nije validan.";
                            break;
                    }
                    message += " Da li želite da nastavite?";

                    builder.setTitle("SSL Sertifikat greška");
                    builder.setMessage(message);
                    builder.setPositiveButton("nastavi", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.proceed();
                        }
                    });
                    builder.setNegativeButton("odustani", new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            handler.cancel();
                        }
                    });
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        viewModel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);
        mWebView.loadUrl(GLOBAL_IP + "/predmeti/" + courseId);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDomStorageEnabled(true);
        mWebView.addJavascriptInterface(new JSIntefrace(){ //JS Interface
            @Override
            @JavascriptInterface
            public void backPressed(boolean echo) {
                backStatus = echo;
            }

            @Override
            @JavascriptInterface
            public void modalNumber(int number) {
                modalNumber = number;
            }
        }, "JSInterface");
    }


    public void closeModal() {
        mWebView.loadUrl("javascript:hideTopMostModal()");
    }


    public X509TrustManager verifyCertificate(Context context) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        // Load CAs from an InputStream
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

        InputStream inputStream = context.getResources().openRawResource(R.raw.client); //(.crt)
        Certificate certificate = certificateFactory.generateCertificate(inputStream);
        inputStream.close();

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", certificate);

        // Create a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
        trustManagerFactory.init(keyStore);

        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        return (X509TrustManager) trustManagers[0];

    }


}
