package http;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * ClassName: HttpsClientUtil.java
 * @Description: 
 * @author Terry
 * @date Jul 14, 2016 10:17:40 AM
 * @version 1.0
 */
public class HttpsClientUtil {

	private static final String METHOD_POST = "POST";
	private static final String METHOD_GET = "GET";
	private static final String DEFAULT_CHARSET = "utf-8";

	private static final Logger logger = LogManager.getLogger(HttpsClientUtil.class);

	private static final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();


	//使用客户端线程池
	static {
		try {

			SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy(){

				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();

			httpClientBuilder.setSSLContext(sslContext);

			HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

			SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
			Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.getSocketFactory())
					.register("https", sslSocketFactory)
					.build();

			PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
			connMgr.setMaxTotal(200);
			connMgr.setDefaultMaxPerRoute(100);
			httpClientBuilder.setConnectionManager( connMgr);
		}catch (Exception e){
			logger.error(e.getMessage(),e);
		}

	}

	//通过证书来发送
	public static String postByCert(String url,String params,String cert,String certPwd,String contentType){

		InputStream inputStream = null;
		try {
			//设置证书类型
			KeyStore keyStore  = KeyStore.getInstance("PKCS12");

			//获取证书文件流
			inputStream = HttpsClientUtil.class.getClassLoader().getResourceAsStream(cert);

			//加载证书
			keyStore.load(inputStream, certPwd.toCharArray());

			//创建SSL上下文对象
			SSLContext sslContext = new SSLContextBuilder().loadKeyMaterial(keyStore,certPwd.toCharArray()).build();

			SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sslContext);

			CloseableHttpClient httpclient = HttpClients.custom()
					.setSSLSocketFactory(ssf)
					.build();

			//创建POST请求对象
			HttpPost httpPost = new HttpPost(url);

			//创建POST参数
			HttpEntity httpEntity = new StringEntity(params);

			//将参数加入到POST请求对象中
			httpPost.setEntity(httpEntity);

			//发送HTTPS的POST请求
			CloseableHttpResponse response = httpclient.execute(httpPost);

			//从返回的流中读取内容，并返回
			return IOUtils.toString(response.getEntity().getContent(),"UTF-8");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} finally {
			if(inputStream!=null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	//简单的发送
	public static String postByTrust(String url,String params,String contentType){

		try {

			//创建SSL上下文对象
			CloseableHttpClient httpclient = createTrustHttpsClient();

			//创建POST请求对象
			HttpPost httpPost = new HttpPost(url);

			//创建POST参数
			HttpEntity httpEntity = new StringEntity(params);

			//将参数加入到POST请求对象中
			httpPost.setEntity(httpEntity);

			//发送HTTPS的POST请求
			CloseableHttpResponse response = httpclient.execute(httpPost);

			//从返回的流中读取内容，并返回
			return IOUtils.toString(response.getEntity().getContent(),"UTF-8");
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (KeyManagementException e) {
			e.printStackTrace();
		}
		return null;
	}

	//创建单个https客户端对象
	public static CloseableHttpClient createTrustHttpsClient() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException {
		SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(new TrustStrategy(){

            @Override
            public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                return true;
            }
        }).build();

		SSLConnectionSocketFactory ssf = new SSLConnectionSocketFactory(sslContext);

		return HttpClients.custom().setSSLSocketFactory(ssf).build();
	}


	//通过线程池获取https客户端对象
	public static CloseableHttpClient createTrustHttpsClientByPool(){
		return httpClientBuilder.build();
	}




}
