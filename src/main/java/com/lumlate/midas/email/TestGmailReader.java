package com.lumlate.midas.email;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.SortedSet;
import java.util.Map.Entry;
 
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
 
import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;

import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpParameters;
import oauth.signpost.signature.HmacSha1MessageSigner;
import oauth.signpost.signature.OAuthMessageSigner;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.commonshttp.HttpRequestAdapter;

public class TestGmailReader {
 
    private static final String TAG = "OAuthHelper";
 
    private OAuthConsumer mConsumer;
    private OAuthProvider mProvider;
 
    private String mCallbackUrl;
 
    public TestGmailReader(String consumerKey, String consumerSecret,
            String scope, String callbackUrl, String appname)
    throws UnsupportedEncodingException {
        String reqUrl;
        if (appname == null)
            reqUrl = OAuth.addQueryParameters(
                    "https://www.google.com/accounts/OAuthGetRequestToken",
                    "scope", scope);
        else
            reqUrl = OAuth.addQueryParameters(
                    "https://www.google.com/accounts/OAuthGetRequestToken",
                    "scope", scope, "xoauth_displayname", appname);
 
        mConsumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
 
        mProvider = new CommonsHttpOAuthProvider(reqUrl,
                "https://www.google.com/accounts/OAuthGetAccessToken",
                "https://www.google.com/accounts/OAuthAuthorizeToken?hd=default");
        mProvider.setOAuth10a(true);
 
        mCallbackUrl = (callbackUrl == null ? OAuth.OUT_OF_BAND : callbackUrl);
    }
 
    public String getRequestToken()
    throws OAuthMessageSignerException, OAuthNotAuthorizedException,
    OAuthExpectationFailedException, OAuthCommunicationException {
        String authUrl =
                mProvider.retrieveRequestToken(mConsumer, mCallbackUrl);
        return authUrl;
    }
 
    public String[] getAccessToken(String verifier)
    throws OAuthMessageSignerException, OAuthNotAuthorizedException,
    OAuthExpectationFailedException, OAuthCommunicationException {
        mProvider.retrieveAccessToken(mConsumer, verifier);
        return new String[] {
                mConsumer.getToken(), mConsumer.getTokenSecret()
        };
    }
 
    public String[] getToken() {
        return new String[] {
                mConsumer.getToken(), mConsumer.getTokenSecret()
        };
    }
 
    public void setToken(String token, String secret) {
        mConsumer.setTokenWithSecret(token, secret);
    }
 
    public String getUrlContent(String url)
    throws OAuthMessageSignerException, OAuthExpectationFailedException,
    OAuthCommunicationException, IOException {
        HttpGet request = new HttpGet(url);
 
        // sign the request
        mConsumer.sign(request);
 
        // send the request
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse response = httpClient.execute(request);
 
        // get content
        BufferedReader in = new BufferedReader(
                new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null)
            sb.append(line + NL);
        in.close();
 
        return sb.toString();
    }
 
    public String buildXOAuth(String email) {
        String url =
            String.format("https://mail.google.com/mail/b/%s/smtp/", email);
        HttpRequestAdapter request = new HttpRequestAdapter(new HttpGet(url));
 
        // Sign the request, the consumer will add any missing parameters
        try {
            mConsumer.sign(request);
        } catch (OAuthMessageSignerException e) {
        	e.printStackTrace();
            return null;
        } catch (OAuthExpectationFailedException e) {
        	e.printStackTrace();
            return null;
        } catch (OAuthCommunicationException e) {
        	e.printStackTrace();
            return null;
        }
        HttpParameters params = mConsumer.getRequestParameters();
 
        // Since signpost doesn't put the signature into params,
        // we've got to create it again.
        OAuthMessageSigner signer = new HmacSha1MessageSigner();
        signer.setConsumerSecret(mConsumer.getConsumerSecret());
        signer.setTokenSecret(mConsumer.getTokenSecret());
        String signature;
        try {
            signature = signer.sign(request, params);
        } catch (OAuthMessageSignerException e) {
        	e.printStackTrace();
            return null;
        }
        params.put(OAuth.OAUTH_SIGNATURE, OAuth.percentEncode(signature));
 
        StringBuilder sb = new StringBuilder();
        sb.append("GET ");
        sb.append(url);
        sb.append(" ");
        int i = 0;
        for (Entry<String, SortedSet<String>> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().first();
 
            int size = entry.getValue().size();
            if (size != 1)
                System.out.println("warning: " + key + " has " + size + " values");
 
            if (i++ != 0)
                sb.append(",");
            sb.append(key);
            sb.append("=\"");
            sb.append(value);
            sb.append("\"");
        }
        System.out.println("xoauth encoding " + sb);
 
        Base64 base64 = new Base64();
        try {
            byte[] buf = base64.encode(sb.toString().getBytes("utf-8"));
            return new String(buf, "utf-8");
        } catch (UnsupportedEncodingException e) {
        	System.out.println("invalid string " + sb);
        }
 
        return null;
    }
    
    public static void main(String args[]){
    	String consumerKey="deallr.com";
    	String consumerSecret="f_yk4d2GkQljJ38JQrcRJBPr";
    	String scope="https://mail.google.com/";
    	String email="sharmavipul@gmail.com";
    	String uid="4";
    	String callbackUrl="http://dev.deallr.com/account/upgradeEmailToken/"+ uid + "/gmail/" + email;
    	String appname="deallr";
    	
    	try {
			TestGmailReader reader=new TestGmailReader(consumerKey, consumerSecret, scope, callbackUrl, appname);
			String url=reader.buildXOAuth(email);
			System.out.println(url);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
}