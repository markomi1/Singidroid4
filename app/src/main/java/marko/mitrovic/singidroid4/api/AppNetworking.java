package marko.mitrovic.singidroid4.api;

import android.content.Context;
import marko.mitrovic.singidroid4.R;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class AppNetworking{


    public static Retrofit retrofit = null;
    public static final String BASE_URL = "https://192.168.4.110:8080"; //https://192.168.4.110:8080


    public static SSLContext getSSLConfig(Context context) throws CertificateException, IOException,
            KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        // Loading CAs from an InputStream
        CertificateFactory cf = null;
        cf = CertificateFactory.getInstance("X.509");

        Certificate ca;
        // I'm using Java7. If you used Java6 close it manually with finally.
        try (InputStream cert = context.getResources().openRawResource(R.raw.client)) {
            ca = cf.generateCertificate(cert);
        }

        // Creating a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Creating a TrustManager that trusts the CAs in our KeyStore.
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Creating an SSLSocketFactory that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        return sslContext;
    }

    public static Retrofit getClient(Context context) {

        try {
            Retrofit.Builder builder = new Retrofit.Builder().baseUrl(BASE_URL);
            //Note overwrite for hostname problem.
            OkHttpClient okHttp = new OkHttpClient.Builder().hostnameVerifier(new HostnameVerifier(){
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            }).sslSocketFactory(getSSLConfig(context).getSocketFactory()).build();

            if (retrofit == null) {
                retrofit = builder.client(okHttp)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
        } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException | CertificateException | IOException ex) {
            ex.printStackTrace();
        }

        return retrofit;
    }




}
