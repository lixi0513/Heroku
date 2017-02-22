package com.cisco.mediasense_jsc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Scanner;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.log4j.Logger;

import com.cisco.beans.LoginRequestBean;
import com.cisco.beans.RequestParameterBean;
import com.cisco.mediasense.utils.WebInfGetter;
import com.cisco.mediasense_core.XMLRequestor;

/**
 * 
 * @author SpinSci This class is responsible to send the request to https
 *         mediasense server. The constructor calls the XML file that contains
 *         the URL pattern and the API calls for mediasense.
 */
public class MediasenseConnector {
	private static Logger logger = Logger.getLogger(MediasenseConnector.class
			.getName());
	private static String XML_FILE_PATH;
	private static String CERTSTORE_PATH;
	private static String CERTSTORE_PASS;
	private static XMLRequestor XMLR;
	public static HttpClient client;
	private static String JSESSIONID;

	public MediasenseConnector() {
		Properties props = new Properties();
		try {
			System.out.println(new File(WebInfGetter.getWebInfPath()
					+ "/Mediasense.properties").getAbsolutePath());
			props.load(new FileInputStream(new File(WebInfGetter
					.getWebInfPath() + "/Mediasense.properties")));
			XML_FILE_PATH = props.getProperty("XMLPATH");
			CERTSTORE_PATH = props.getProperty("CERTSTOREPATH");
			CERTSTORE_PASS = props.getProperty("CERTSTOREPASS");
			logger.info("XML File Path: " + XML_FILE_PATH);
		} catch (FileNotFoundException e) {
			logger.info("Exception loading xml file" + e.getMessage());
		} catch (IOException e) {
			logger.info("Exception loading properties file" + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 *            This method logs into mediasense server.
	 * @return true for successful login (saves JSESSIONID of the user to global
	 *         variable JSESSIONID)
	 */
	public boolean login(String username, String password) {

		boolean value = false;
		PostMethod postMethod = null;

		// starting try block

		try {

			// load your key store as a stream and initialize a KeyStore
			// InputStream trustStream = new FileInputStream(new
			// File(CERTSTORE_PATH));
			// KeyStore trustStore =
			// KeyStore.getInstance(KeyStore.getDefaultType());

			// if your store is password protected then declare it (it can be
			// null however)
			// char[] trustPassword = CERTSTORE_PASS.toCharArray();

			// load the stream to your store
			// trustStore.load(trustStream, trustPassword);

			// initialize a trust manager factory with the trusted store
			// TrustManagerFactory trustFactory =
			// TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			// trustFactory.init(trustStore);

			// KeyManagerFactory keyFactory =
			// KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			// keyFactory.init(trustStore, trustPassword);

			// get the trust managers from the factory
			// TrustManager[] trustManagers = trustFactory.getTrustManagers();
			// KeyManager[] km = keyFactory.getKeyManagers();

			// SecureRandom random = new SecureRandom();

			// initialize an ssl context to use these managers and set as
			// default
			// SSLContext sslContext = SSLContext.getInstance("SSL");
			// SSLContext sslContext = SSLContext.getDefault();
			// sslContext.init(km, tm, random);
			// sslContext.getDefaultSSLParameters().
			// sslContext.init(km, trustManagers,random);
			// SSLContext.setDefault(sslContext);

			String status = "login";
			logger.info("connecting to mediasense");
			// to retrieve data from xml

			XMLR = new XMLRequestor();
			logger.info("checking the xml file");
			RequestParameterBean rpb = XMLR
					.read_From_XML(status, XML_FILE_PATH);
			// String requestBody =rpb.getBody();
			String url = rpb.getUrl();
			System.out.println("URL genertede:" + url);
			rpb.getMethod();
			logger.info("retreiving the url:" + url);

			// setting values to bean

			LoginRequestBean lrb = new LoginRequestBean();
			lrb.setUsername(username);
			lrb.setPassword(password);
			String requestBody = lrb.generateJSON();
			logger.info("generating json from LoginRequestBean");
			// setting header

			System.out.println("Afer setting the requestbody " + requestBody);
			logger.info("sending request body");
			// posting the request

			postMethod = new PostMethod(url);
			client = new HttpClient();
			postMethod.setParameter("username", username);
			postMethod.setParameter("password", password);
			postMethod.setParameter("Content-Type", "application/json");
			postMethod.setRequestEntity(new StringRequestEntity(requestBody));
			System.out.println("After setting the request entity");

			client.executeMethod(postMethod);

			Header locationH = postMethod.getResponseHeader("Location");

			if (postMethod.getStatusCode() == 302) {

				String location = locationH.toString().split(" ")[1];
				System.out.println("location of redirect: " + location);
				postMethod.setURI(new URI(location));
				client.executeMethod(postMethod);
			}

			logger.info("receiving response");
			System.out.println(postMethod.getStatusLine());
			System.out.println("Response as string "
					+ postMethod.getResponseBodyAsString());

			// retrieving jsession id

			Header head = postMethod.getResponseHeader("Set-Cookie");
			if (head != null) {
				String header_value = head.toString();
				System.out.println(header_value);
				logger.info("Generating JSESSIONID");
				String[] session_variables = header_value.split(";");
				JSESSIONID = ((session_variables[0].split("="))[1]);
				logger.info("JSESSION ID: " + JSESSIONID);
				System.out.println("JsessionID:  " + JSESSIONID);
				System.out.println();

				value = true;
			} else {
				value = false;
			}
		} catch (Exception e) {

			e.printStackTrace();
			logger.info("Exception to login:" + e.getMessage());
		}
		return value;
	}

	/**
	 * users input to next method
	 * 
	 * @param status_command
	 *            the api call
	 * @param beanvalue
	 *            to check if it runs in console mode or web mode()
	 * @param json_string
	 *            String generated from the requested servlet.
	 * @return result -- contains the result from the mediasense server.
	 */

	public String selectMethod(String status_command, boolean beanvalue,
			String json_string) {
		logger.info("In selectMethod");
		logger.info(status_command);
		logger.info(XML_FILE_PATH);
		String result = "";

		try {
			//
			XMLR = new XMLRequestor();
			RequestParameterBean rpb = XMLR.read_From_XML(status_command,
					XML_FILE_PATH);
			String method = rpb.getMethod();
			String url = rpb.getUrl();
			// checking method as post or get
			logger.info("Method: " + method);
			if (method.equals("POST")) {
				result = methodPOST(JSESSIONID, client, status_command, url,
						beanvalue, json_string);

			}
			if (method.equals("GET")) {
				result = methodGET(JSESSIONID, client, status_command, url,
						beanvalue, json_string);

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("Exception selecting method" + e);
		}
		return result;
	}

	/**
	 * Not for web based. This is used to dynamically call the classes using
	 * Reflection for console mode.
	 * 
	 * @param status
	 *            -- Contains the api call
	 * @return fields from the class called using reflection
	 */
	public static Field[] XML_Bean_retrieve(String status) {
		// ArrayList<String> al=new ArrayList<String>();
		Field fld[] = {};
		try {

			XMLR = new XMLRequestor();
			RequestParameterBean rpb = XMLR
					.read_From_XML(status, XML_FILE_PATH);
			rpb.getBody();

			String classpath = rpb.getClasspath();

			// retrieving the attributes on the bean

			Class<?> cl = Class.forName(classpath);
			fld = cl.getDeclaredFields();
			/*
			 * for(int i=0;i<fld.length;i++) { fld[i].getName(); }
			 */
		} catch (Exception e) {
			System.out.println(e);
		}
		return fld;

	}

	// starting of method get
	/**
	 * 
	 * @param jsessionid
	 *            -- The session ID of the user
	 * @param client
	 *            -- HttpClient object
	 * @param status
	 *            -- The api call
	 * @param url
	 *            -- The Mediasense API URL
	 * @param beanvalue
	 *            -- To check if it is web or console mode
	 * @param json_string
	 *            -- The request string generated
	 * @return -- Returns the response fom the mediasense server.
	 */

	public static String methodGET(String jsessionid, HttpClient client,
			String status, String url, boolean beanvalue, String json_string) {
		logger.info("In methodGET");
		String jsonstring = "";
		try {
			XMLR = new XMLRequestor();
			RequestParameterBean rpb = XMLR
					.read_From_XML(status, XML_FILE_PATH);
			// setting url pattern
			url = url + "?";
			String value = "";
			if (!beanvalue) {
				Scanner s = new Scanner(System.in);
				Field fld[] = XML_Bean_retrieve(status);
				for (int i = 0; i < fld.length; i++) {
					System.out.println("enter " + fld[i].getName() + " value");
					value = value + fld[i].getName() + "=" + s.next();
					if (i < (fld.length - 1)) {
						value = value + "&";
					}
				}
			} else {

				value = json_string;
				logger.info("JSON_VALUE: " + value);
			}
			/*
			 * for(int i=0;i<al.size();i++) { if(i<(al.size()-1)) {
			 * value=al.get(i)+"&"; }
			 * 
			 * }
			 */
			url = url + value;
			logger.info("URL: " + url);
			// Header header=new Header("JSESSIONID",jsessionid);
			GetMethod getMethod = new GetMethod(url);
			getMethod.setRequestHeader("JSESSIONID", jsessionid);

			// setting header

			Header str = getMethod.getRequestHeader("JSESSIONID");
			System.out.println(str.toString());
			// HttpClient client1=new HttpClient();
			int i = client.executeMethod(getMethod);
			System.out.println(i);
			InputStream inputstream = getMethod.getResponseBodyAsStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inputstream));
			String data = "";
			while ((data = br.readLine()) != null) {
				jsonstring = data;
				logger.info(data + "\r\n");

			}

		} catch (Exception e) {

			logger.info("exception at get:" + e.getMessage());

		}

		return jsonstring;
	}

	/**
	 * 
	 * @param jsessionid
	 *            -- The session ID of the user
	 * @param client
	 *            -- HttpClient object
	 * @param status
	 *            -- The api call
	 * @param url
	 *            -- The Mediasense API URL
	 * @param beanvalue
	 *            -- To check if it is web or console mode
	 * @param json_string
	 *            -- The request string generated
	 * @return -- Returns the response fom the mediasense server.
	 */
	public static String methodPOST(String jsessionid, HttpClient client,
			String status, String url, boolean beanvalue, String json_string) {
		logger.info("In methodPOST");
		String jsonstring = "";
		String data = "";
		PostMethod postmethod;
		try {
			Scanner s = new Scanner(System.in);
			XMLR = new XMLRequestor();
			String str = "";

			RequestParameterBean rpb = XMLR
					.read_From_XML(status, XML_FILE_PATH);

			rpb.getUrl();
			String classpath = rpb.getClasspath();

			// setting header

			if (status.equals("logout")) {
				postmethod = new PostMethod(url);
				postmethod.setRequestHeader("JSESSIONID", jsessionid);
				str = "";
			} else {
				postmethod = new PostMethod(url);
				postmethod.setRequestHeader("JSESSIONID", jsessionid);

				// retrieving the attributes on the bean

				Class<?> cl = Class.forName(classpath);
				cl.newInstance();
				Object obj2 = cl.newInstance();
				Field fld[] = cl.getDeclaredFields();
				String values_request[] = new String[fld.length];
				// Console Mode
				if (!beanvalue) {

					for (int i = 0; i < fld.length; i++) {
						System.out.println("enter " + fld[i].getName()
								+ " value");
						String param = s.next();
						values_request[i] = param;
						postmethod.setParameter(fld[i].getName(), param);

					}
					Method method1 = cl.getDeclaredMethod("convert",
							String[].class);
					str = method1.invoke(obj2, new Object[] { values_request })
							.toString();
					System.out.println(str);
				}
				// Web mode
				else {
					str = json_string;
					logger.info("JSON_VALUE: " + str);
				}
			}

			postmethod.setParameter("Content-Type", "application/json");
			postmethod.setParameter("JSESSIONID", jsessionid);
			postmethod.setRequestEntity(new StringRequestEntity(str));
			client.executeMethod(postmethod);
			InputStream inputstream = postmethod.getResponseBodyAsStream();

			// String jsonstr = IOUtils.toString( inputstream );
			// JSONObject json = (JSONObject) JSONSerializer.toJSON(jsonstr);

			// System.out.println("response code:"+json.optString("responseCode",
			// ""));
			// System.out.println("response Message:"+json.optString("responseMessage",
			// ""));

			/*
			 * if(status.equals("addSessionTag")) {
			 * 
			 * } else { JSONObject json1 = (JSONObject)
			 * JSONSerializer.toJSON(json.optString("responseBody", ""));
			 * System.out.println("session:"+json1.optJSONArray("sessions"));
			 * JSONArray jsonarray=json1.optJSONArray("sessions"); JSONArray
			 * tracks=new JSONArray(); JSONArray participants=new JSONArray();
			 * String deviceRef="";
			 * jsonstring="{total:'20',page:'1',records:'50',root:["; for(int
			 * j=0;j<jsonarray.size();j++) { id=""; String
			 * sessionstate=((JSONObject)
			 * JSONSerializer.toJSON(jsonarray.get(j))
			 * ).optString("sessionState"); long
			 * sessionstartdate=Long.parseLong(((JSONObject)
			 * JSONSerializer.toJSON
			 * (jsonarray.get(j))).optString("sessionStartDate")); Date date=new
			 * Date(sessionstartdate); System.out.println(date); String
			 * sessionid=((JSONObject)
			 * JSONSerializer.toJSON(jsonarray.get(j))).optString("sessionId");
			 * String duration=((JSONObject)
			 * JSONSerializer.toJSON(jsonarray.get(
			 * j))).optString("sessionDuration"); String
			 * time=formatIntoHHMMSS(duration); String urlrstp=(((JSONObject)
			 * JSONSerializer.toJSON(((JSONObject)
			 * JSONSerializer.toJSON(jsonarray
			 * .get(j))).optString("urls"))).optString("rtspUrl"));
			 * //System.out.println(urlrstp); tracks=((JSONObject)
			 * JSONSerializer.toJSON(jsonarray.get(j))).optJSONArray("tracks");
			 * // System.out.println(tracks); for(int k=0;k<tracks.size();k++) {
			 * participants=((JSONObject)
			 * JSONSerializer.toJSON(tracks.get(k))).optJSONArray
			 * ("participants"); //System.out.println(participants); for(int
			 * h=0;h<participants.size();h++) { deviceRef=((JSONObject)
			 * JSONSerializer
			 * .toJSON(participants.get(h))).optString("deviceRef");
			 * //System.out.println(deviceRef); }
			 * jsonstring+="{ID:'"+j+"',Date:'"
			 * +date+"',duration:'"+time+"',deviceid:'"
			 * +deviceRef+"',url:'"+urlrstp
			 * +"',sessionstate:'"+sessionstate+"',sessionid:'"+sessionid+"'}";
			 * if(j!=(jsonarray.size()-1)) { jsonstring+=","; } } }
			 * jsonstring+="]}"; /*JSONArray jarray =
			 * searchresults.getJSONArray("URI"); for(int i=0 ; i <
			 * jarray.size(); i++) { System.out.println("jarray [" + i +
			 * "] --------" + jarray.getString(i)); }
			 */
			/*
			 * BufferedReader br=new BufferedReader(new
			 * InputStreamReader(inputstream));
			 * 
			 * while((data=br.readLine())!=null){
			 * System.out.println(data+"\r\n"); }
			 * 
			 * }
			 */

			BufferedReader br = new BufferedReader(new InputStreamReader(
					inputstream));

			while ((data = br.readLine()) != null) {
				jsonstring = data;
				System.out.println(data + "\r\n");
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return jsonstring;
	}

	/**
	 * Format milliseconds into HH:MM:SS format.
	 * 
	 * @param millisecs
	 *            -- Time returned form the medisense server
	 * @return -- value to readble date format. HH:MM:SS
	 */
	public static String formatIntoHHMMSS(String millisecs) {
		long millisec = Long.parseLong(millisecs);
		long secsIn = millisec / 1000;
		long hours = secsIn / 3600, remainder = secsIn % 3600, minutes = remainder / 60, seconds = remainder % 60;

		return ((hours < 10 ? "0" : "") + hours + ":"
				+ (minutes < 10 ? "0" : "") + minutes + ":"
				+ (seconds < 10 ? "0" : "") + seconds);

	}

	public static String formatDateToMS(String date) {

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm");
		Date d;
		try {
			d = sdf.parse(date);
		} catch (ParseException e) {
			logger.error("Error parsing date: " + date);
			System.out.println("Error parsing date: " + date);
			d = new Date();
		}

		return new Long(d.getTime()).toString();

	}

}
