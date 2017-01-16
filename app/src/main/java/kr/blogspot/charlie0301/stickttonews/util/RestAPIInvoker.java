package kr.blogspot.charlie0301.stickttonews.util;

import android.util.Log;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.spi.service.ServiceFinder;

import org.json.JSONObject;

public class RestAPIInvoker {

	private static final String LOG_TAG = "RestAPIInvoker";
	public enum HTTP_METHOD { GET, POST, PUT, DELETE }

	private static final boolean isNeedToPrintResult = false;
	
	public RestAPIInvoker()
	{
	}
	
	public static JSONObject invokeRESTAPI(HTTP_METHOD method, String fullURL, String params){

		Client client = null;
		WebResource webResource = null;
		JSONObject object = null;

		try{
			ServiceFinder.setIteratorProvider(new AndroidServiceIteratorProvider<Object>());

			Log.d(LOG_TAG, "Invoke REST API, " + method.toString() + ", URL = " + fullURL);
			client = SSLClientHelper.createClient();
			webResource = client.resource(fullURL);
			ClientResponse response;

			WebResource.Builder wb = webResource.type("application/x-www-form-urlencoded");
			
			switch(method){

			case GET :
				response = wb.get(ClientResponse.class);
				break;


			case POST :				
				response = wb.post(ClientResponse.class, params);				
				break;

			case PUT:
				response = wb.put(ClientResponse.class, params);
				break;
				
			case DELETE :
				response = wb.delete(ClientResponse.class);
				break;
				
			default :
				throw new Exception("Not supported HTTP Method");
			}

			String output = response.getEntity(String.class);
			
			if(RestAPIInvoker.isNeedToPrintResult){
				Log.d(LOG_TAG, "result -------------------------------------------------");
				Log.d(LOG_TAG, output.toString());
				Log.d(LOG_TAG, "result -------------------------------------------------");	
			}

			if (response.getStatus() != 200 &&
					response.getStatus() != 201) {

				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}

			object = new JSONObject(output);

			webResource = null;
			client.destroy();
			client = null;

		} catch (Exception e) {

			e.printStackTrace();
			object = null;
			
		} finally {

			webResource = null;
			if(null != client){
				client.destroy();	
			}
			client = null;
		}

		return object;
	}
}
