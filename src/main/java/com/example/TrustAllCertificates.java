package com.example;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TrustAllCertificates {

    public static void main(String[] args) {
        try {
            // Crea un contesto SSL che non controlla certificati
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            // Installa un HostnameVerifier che non fa nulla
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            // Ora puoi connetterti a un URL HTTPS ignorando i problemi di certificato
            URL url = new URL("https://your-secure-url.com");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String inputLine;
            while ((inputLine = reader.readLine()) != null) {
                System.out.println(inputLine);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
