package com.cisco.mediasense.serlvet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

import org.apache.log4j.Logger;

import com.cisco.beans.CallSessionIdList;
import com.cisco.beans.GetSessionsRequest;
import com.cisco.mediasense.callgrouping.CallLegGrouper;
import com.cisco.mediasense.callgrouping.exceptions.GroupingException;
import com.cisco.mediasense_jsc.MediasenseConnector;

public class GetSessions extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(GetSessions.class);

	// Are we using the DEV_API_USER account instead of real API user?
	private static final boolean DEV_API_USER = false;

	/**
	 * Constructor of the object.
	 */
	public GetSessions() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doProcess(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doProcess(request, response);
	}

	public void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		if (!"PASS".equals(session.getAttribute("LOGIN_RESULT"))) {
			response.sendRedirect("/index.jsp");

		}

		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		// String status_command="getSessionsByDeviceRef";
		String status_command = "getSessions";
		String jsonstring = "";
		String id;
		String page = request.getParameter("page");
		String limit = request.getParameter("rows");
		GetSessionsRequest gc = null;
		try {
			gc = buildRequest(request, page, limit);
		} catch (Exception e) {
			logger.error("An error occured building the GetSessionsRequest.");
			jsonstring = "{code:'NOT_FOUND',message:'no results found'}";
			System.out.println("no results found");
			JSONObject jsonobj = (JSONObject) JSONSerializer.toJSON(jsonstring);
			response.getWriter().write(jsonobj.toString());
			return;
		}
		// String json_string=gc.generateDefaultUrl();
		String json_string = gc.generateUrl();
		logger.info("URL:" + json_string);
		System.out.println("request:" + json_string);
		boolean bean_value = true;
		MediasenseConnector mc = new MediasenseConnector();
		String responseStr = mc.selectMethod(status_command, bean_value,
				json_string);
		logger.info("response:" + responseStr);
		JSONObject json = (JSONObject) JSONSerializer.toJSON(responseStr);
		String code = json.optString("responseCode", "");
		System.out.println("response code:"
				+ json.optString("responseCode", ""));
		System.out.println("response Message:"
				+ json.optString("responseMessage", ""));
		if (code.equals("2000")) {
			jsonstring = buildResponse(page, limit, json);
		} else {
			jsonstring = "{code:'NOT_FOUND',message:'no results found'}";
			System.out.println("no results found");
		}
		JSONObject jsonobj = (JSONObject) JSONSerializer.toJSON(jsonstring);

		JSONArray arr = new JSONArray();

		response.getWriter().write(jsonobj.toString());
	}

	public GetSessionsRequest buildRequest(HttpServletRequest request,
			String page, String limit) throws Exception {
		String sortColumn = "";
		try {
			// jqgrid 4
			sortColumn = request.getParameter("sidx").split(" ")[0];
		} catch (Exception e) {
			// jqgrd 3.x
			sortColumn = request.getParameter("sidx");

		}

		// The sort order. Either asc or desc.
		String sortOrd = request.getParameter("sord");

		String searchField = request.getParameter("searchField");
		String searchOperationOrig = request.getParameter("searchOper");

		String searchOperation = "";
		if (searchField != null && !"".equals(searchField)) {
			if (searchField.equals("Participants"))
				searchField = "deviceRef";
			else if (searchField.equals("Start Date"))
				searchField = "sessionStartDate";
			else if (searchField.equals("Status"))
				searchField = "sessionState";
			else if (searchField.equals("xrefid"))
				searchField = "xRefCi";
			else if (searchField.equals("duration"))
				searchField = "sessionDuration";
			else if (searchField.equals("ccid"))
				searchField = "ccid";

			else
				searchField = "deviceRef";

		} else {
			searchField = "deviceRef";
		}

		if (searchOperationOrig != null && !"".equals(searchOperationOrig)) {
			if (searchOperationOrig.equals("eq"))
				searchOperation = "equals";
			else if (searchOperationOrig.equals("cn"))
				searchOperation = "contains";
			else if (searchOperationOrig.equals("lt")
					&& searchField.equals("sessionStartDate"))
				searchOperation = "between";
			else if (searchOperationOrig.equals("lt"))
				searchOperation = "lessThan";

			else if (searchOperationOrig.equals("gt")
					&& searchField.equals("sessionStartDate"))
				searchOperation = "between";
			else if (searchOperationOrig.equals("gt"))
				searchOperation = "greaterThan";

			else
				searchOperation = "equals";

		} else {
			searchOperation = "equals";
		}

		try {
			sortOrd = sortOrd.toUpperCase();
		} catch (Exception e) {
			// Null pointer is okay. there is no sorting specified by client.
			// will use defualt.
			sortOrd = "DESC";
		}

		try {
			if (sortColumn.equals("Start Date")) {
				sortColumn = "sessionStartDate";
			} else if (sortColumn.equals("CCID")) {
				sortColumn = "ccid";
			} else if (sortColumn.equals("Duration")) {
				sortColumn = "sessionDuration";
			} else if (sortColumn.equals("Status")) {
				sortColumn = "sessionState";
			} else {
				sortColumn = "sessionStartDate";
			}
		} catch (Exception e) {
			// Null pointer is okay. there is no sorting specified by client.
			// will use defualt.
		}

		int pageInt = 1;
		try {
			pageInt = Integer.parseInt(page);
		} catch (Exception e) {
			logger.error("Could not parse as int page: " + page
					+ ". Using default of " + pageInt);

		}

		int limitInt = 10;
		try {
			limitInt = Integer.parseInt(limit);
		} catch (Exception e) {
			logger.error("Could not parse as int limit: " + limit
					+ ". Using default of " + limitInt);

		}
		Integer offsetInt = pageInt * limitInt;
		String offset = offsetInt.toString();

		// String limit = "10";

		// GetAllActiveSessionsByDeviceRequest gc=new
		// GetAllActiveSessionsByDeviceRequest();
		GetSessionsRequest gc = new GetSessionsRequest();
		gc.setByFieldName(sortColumn);
		gc.setOrder(sortOrd);
		String value = request.getParameter("searchString");
		// If no value.
		if (value == null || "".equals(value)) {
			// gc.setFieldOperator("greaterThan");
			gc.setFieldOperator("between");
			gc.setFieldName("sessionStartDate");
			// Get all sessions for the last week.
			gc.setFieldValues(new Long(System.currentTimeMillis()
					- (7 * 24 * 60 * 60 * 1000)).toString()
					+ "," + new Long(System.currentTimeMillis()).toString());
			gc.setFieldValues(new Long(0).toString()
					+ ","
					+ new Long(System.currentTimeMillis()
							+ (7 * 24 * 60 * 60 * 1000)).toString());
		} else {
			try {
				value = URLDecoder.decode(value, "UTF-8");
			} catch (Exception e) {
				logger.error("Could not decode value: " + value
						+ " with UTF-8.");

			}
			gc.setFieldOperator(searchOperation);
			gc.setFieldName(searchField);

			if (searchField.equals("sessionStartDate")) {
				if (searchOperationOrig.equals("gt")) {
					value = " " + MediasenseConnector.formatDateToMS(value)
							+ ", " + System.currentTimeMillis() + "";
				} else {
					value = " 0, " + MediasenseConnector.formatDateToMS(value)
							+ " ";
				}

			} else {
				value = "\"" + value + "\"";
			}

			gc.setFieldValues(value);
		}
		// gc.setValue(value);
		gc.setOffset(offset);
		gc.setLimit(limit);
		return gc;
	}

	public String buildResponse(String page, String limit, JSONObject json) {
		String jsonstring;
		String id;
		CallLegGrouper clgTool = new CallLegGrouper();
		ArrayList<CallSessionIdList> clgToolResults = new ArrayList<CallSessionIdList>();
		try {
			clgToolResults = clgTool.groupCallLegs(json);
		} catch (GroupingException e1) {
			// TODO Auto-generated catch block
			logger.error("Could not group call legs.", e1);
			logger.error("Ungroupable json was: \n " + json.toString());
		}

		// TEST DATA.
		// String data = "";
		//
		// Scanner scan = new Scanner(new
		// FileInputStream("C:/Mediasense/testGroupCallLegs_RoundRobinProblemCall.txt"));
		//
		// while (scan.hasNext()) {
		// data += scan.next();
		// }
		//
		// json = (JSONObject) JSONSerializer.toJSON(data);
		//
		// clgTool = new CallLegGrouper();
		// try {
		// clgToolResults = clgTool.groupCallLegs(json);
		// } catch (GroupingException e) {
		// //System.out.println("Everything will fail because analysis is not implemented.");
		// logger.error("Could not group call legs.", e);
		// logger.error("Ungroupable json was: \n " + json.toString());
		// }
		//
		// scan.close();

		// END TEST DATA

		int pageInt = 1;
		try {
			pageInt = Integer.parseInt(page);
		} catch (Exception e) {
			logger.error("Could not parse as int page: " + page
					+ ". Using default of " + pageInt);

		}

		int limitInt = 10;
		try {
			limitInt = Integer.parseInt(limit);
		} catch (Exception e) {
			logger.error("Could not parse as int limit: " + limit
					+ ". Using default of " + limitInt);

		}

		// System.out.println("response Body:"+json.optString("responseBody",
		// ""));
		JSONObject json1 = (JSONObject) JSONSerializer.toJSON(json.optString(
				"responseBody", ""));

		// System.out.println("session:"+json1.optJSONArray("sessions"));
		JSONArray jsonarray = json1.optJSONArray("sessions");
		JSONArray tracks = new JSONArray();
		String deviceRef = "";
		String groupId = "";
		String xRefID = "";
		String sessionid = "";
		String duration = "";
		String time = "";
		String ccid = "";
		String urlrstp = "";
		// GetAllActiveSessionsByDeviceRefUI gasrui;
		// List<GetAllActiveSessionsByDeviceRefUI> list=new
		// ArrayList<GetAllActiveSessionsByDeviceRefUI>();
		JSONArray participants = new JSONArray();
		jsonstring = "{\"total\":\"" + ((jsonarray.size() / limitInt) + 1)
				+ "\",\"page\":\"" + page + "\",\"records\":" + limit
				+ ",  \"rows\": [";
		int i = 0;

		int endPoint = (((pageInt - 1) * limitInt) + (limitInt));

		if (endPoint > jsonarray.size())
			endPoint = jsonarray.size();

		int startPoint = ((pageInt - 1) * limitInt);

		int x = 0;

		// Insert the logged in user, or DEV_API_USER, into the RTSP URI for
		// basic authentication.
		String username = (String) this.getServletContext().getAttribute(
				"USERNAME");
		String password = (String) this.getServletContext().getAttribute(
				"PASSWORD");

		if (DEV_API_USER) {
			username = "apiuser";
			password = "cisco";
		}

		for (int j = 0; j < jsonarray.size(); j++)
		// for(int j=startPoint;j<endPoint;j++)
		{
			id = "";
			String tags = "";
			String sessionstate = ((JSONObject) JSONSerializer.toJSON(jsonarray
					.get(j))).optString("sessionState");
			try {
				ccid = ((JSONObject) JSONSerializer.toJSON(jsonarray.get(j)))
						.optString("ccid");
			} catch (Exception e) {
				// not worried if we can't get ccid. sometimes it is not
				// included.
			}

			if (sessionstate.equals("ACTIVE")) {
				time = "";
			} else {
				duration = ((JSONObject) JSONSerializer
						.toJSON(jsonarray.get(j))).optString("sessionDuration");
				try {
					time = MediasenseConnector.formatIntoHHMMSS(duration);
				} catch (NumberFormatException e) {
					time = "";
				}
			}
			long sessionstartdate = Long.parseLong(((JSONObject) JSONSerializer
					.toJSON(jsonarray.get(j))).optString("sessionStartDate"));
			Date date = new Date(sessionstartdate);

			urlrstp = (((JSONObject) JSONSerializer
					.toJSON(((JSONObject) JSONSerializer.toJSON(jsonarray
							.get(j))).optString("urls"))).optString("rtspUrl"));

			urlrstp = urlrstp.replace("rtsp://", "rtsp://" + username + ":"
					+ password + "@");

			// System.out.println(urlrstp);
			tracks = ((JSONObject) JSONSerializer.toJSON(jsonarray.get(j)))
					.optJSONArray("tracks");
			// System.out.println(tracks);

			sessionid = ((JSONObject) JSONSerializer.toJSON(jsonarray.get(j)))
					.optString("sessionId");

			try {
				groupId = clgTool.getGroupId(clgToolResults, sessionid);
			} catch (GroupingException ex) {
				logger.error("Session id " + sessionid
						+ " was not found in grouping analysis results.");
				groupId = "Ungroupable";
			}

			for (int k = 0; k < tracks.size(); k++) {
				participants = ((JSONObject) JSONSerializer.toJSON(tracks
						.get(k))).optJSONArray("participants");
				// System.out.println(participants);

				for (int h = 0; h < participants.size(); h++) {

					deviceRef = ((JSONObject) JSONSerializer
							.toJSON(participants.get(h)))
							.optString("deviceRef");
					xRefID = ((JSONObject) JSONSerializer.toJSON(participants
							.get(h))).optString("xRefCi");

					// System.out.println(deviceRef);
				}
				if (deviceRef.contains("Unknown")) {
					// deviceRef=deviceRef.replace("Unknown-", " ");
					continue;
				}

				else {
					if (i >= startPoint && i < endPoint) {
						x++;
						jsonstring += "{\"id\":\"" + (x) + "\",";
						// jsonstring+="{\"id\":\""+(i+1)+"\",";
						jsonstring += "\"cell\":[\"" + ccid + "\",\"" + groupId
								+ "\",\"" + sessionid + "\",\"" + urlrstp
								+ "\",\"" + date + "\",\"" + deviceRef
								+ "\",\"" + xRefID + "\",\"" + time + "\",\""
								+ tags + "\",\"" + sessionstate + "\",\""
								+ "Listen!\"]},";
						// jsonstring+="\"cell\":[\""+ccid + "\",\""+groupId +
						// "\",\""+sessionid + "\",\" " +
						// "\",\""+date+"\",\""+deviceRef+"\",\""+xRefID+"\",\""+time+"\",\""+tags+"\",\""+sessionstate+"\",\""+"Listen!\"]},";

					}
				}

			}

			// This is needed for CLOSED_ERROR calls.
			if (tracks.size() == 0) {
				x++;
				jsonstring += "{\"id\":\"" + (x + 1) + "\",";
				jsonstring += "\"cell\":[\"" + ccid + "\",\"" + groupId
						+ "\",\"" + sessionid + "\",\"" + urlrstp + "\",\""
						+ date + "\",\"" + deviceRef + "\",\"" + xRefID
						+ "\",\"" + time + "\",\"" + tags + "\",\""
						+ sessionstate + "\",\"" + "Listen!\"]},";

			}
			if (j != (jsonarray.size() - 1)) {
				// jsonstring+=",";
			}
			i++;
		}
		jsonstring += "]}";
		return jsonstring;
	}

}
