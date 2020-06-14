package marko.mitrovic.singidroid4.fragments;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.*;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import marko.mitrovic.singidroid4.R;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Predmet extends Fragment{
    public WebView mWebView;
    private View view;
    private SharedPreferences studentPerfs;
    //NOTE Global IP used only for webview
    private final String GLOBAL_IP = "192.168.4.110:8080"; //135.181.26.76:8080
    private final String TAG = "PredmetClass";
    public Boolean backStatus = true;
    public int modalNumber = 0;
    private SharedViewModel viewModel;

    public Predmet() {
        // Required empty public constructor
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        studentPerfs = this.getActivity().getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);

        boolean skipped = studentPerfs.getBoolean("SkippedInit", true);
        if (!skipped) {
            String faculty = studentPerfs.getString("FacultyChoice", "");
            String year = studentPerfs.getString("YearChoice", "");
            String courseId = studentPerfs.getString("CourseChoice", "120");
            view = inflater.inflate(R.layout.fragment_predmet_webview, container, false);
            WebView.setWebContentsDebuggingEnabled(true);
            mWebView = (WebView) view.findViewById(R.id.predmet_webview);
            mWebView.setWebViewClient(new WebViewClient(){
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
                        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
            mWebView.loadUrl("https://" + GLOBAL_IP + "/predmeti?id=" + courseId);
            WebSettings webSettings = mWebView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            mWebView.addJavascriptInterface(new JSIntefrace(){
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


        } else {
            view = inflater.inflate(R.layout.fragment_predmet, container, false);
        }


        return view;
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
