package com.cisco.mediasense.callgrouping;

import java.util.ArrayList;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cisco.beans.CallSessionIdList;
import com.cisco.mediasense.callgrouping.exceptions.GroupingException;

/**
 * This class takes data from Mediasense search and groups the call legs.
 * 
 * @author Kevin Walker
 * 
 */
public class CallLegGrouper {

	/**
	 * Searches the list of the parent for this session.
	 * 
	 * @param session
	 *            json object of one session.
	 * @param sessions
	 *            the json array of all the sessions.
	 * @return returns the sessionid that is parent of this session.
	 * @throws GroupingException
	 *             no parent was found for this session.
	 */
	public String findParent(JSONObject session, JSONArray sessions)
			throws GroupingException {

		String child = getSessionId(session);

		return findParent(child, sessions);
		// throw new NotImplementedException();
	}

	public String findParent(String childSessionId, JSONArray sessions)
			throws GroupingException {

		// ArrayList<String> parents = findNonChildSessionIds(sessions);

		String parentResult = null;
		for (String parent : getAllSessionIds(sessions)) {
			// for (String parent : parents) {
			if (isSessionParent(sessions, parent, childSessionId)) {
				parentResult = parent;
			}
		}

		// RECURSIVE ALGORITHM. COMPILE FOR TESTS FOR THIS BASTARD.
		if (parentResult != null) {
			// try to find the parents parent.
			return findParent(parentResult, sessions);
		} else {
			// if result is null, I am the highest parent, so return myself.
			return childSessionId;
		}

	}

	/**
	 * Get the session duration.
	 * 
	 * @param session
	 * @return
	 */
	public long getSessionDuration(JSONObject session) {
		// TODO Auto-generated method stub
		return session.getLong("sessionDuration");
	}

	/**
	 * Get the session start time.
	 * 
	 * @param session
	 * @return
	 */
	public long getSessionStartTime(JSONObject session) {
		// TODO Auto-generated method stub
		return session.getLong("sessionStartDate");
	}

	public long getSessionStartTime(JSONArray sessions, String sessionId) {
		JSONObject session = (JSONObject) sessions.get(0);

		long result = getSessionStartTime(session);

		return result;
	}

	/**
	 * Get a list of all participants (deviceRef) on all tracks of a session.
	 * 
	 * @param session
	 * @return
	 */
	public ArrayList<String> getParticipantList(JSONObject session) {
		// TODO Auto-generated method stub
		ArrayList<String> results = new ArrayList<String>();
		JSONArray tracks = session.getJSONArray("tracks");
		for (Object trackObj : tracks) {
			JSONObject track = (JSONObject) trackObj;
			JSONArray parts = track.getJSONArray("participants");
			for (Object partObj : parts) {
				JSONObject part = (JSONObject) partObj;
				results.add(part.getString("deviceRef"));
			}
		}

		return results;

	}

	/**
	 * Tests if the parent child relationship exists between these two sessions
	 * in the session array given.
	 * 
	 * @param sessions
	 *            the full list of sessions.
	 * @param parentSessionId
	 *            the sessionid of the suspected parent
	 * @param childSessionId
	 *            the sessionid of the suspected child
	 * @return whether parent is parent of child.
	 */
	public boolean isSessionParent(JSONArray sessions, String parentSessionId,
			String childSessionId) {

		JSONObject parentSession = null;
		JSONObject childSession = null;

		for (Object sessionObj : sessions) {
			JSONObject session = (JSONObject) sessionObj;

			if (getSessionId(session).equals(parentSessionId)) {
				parentSession = session;
			} else if (getSessionId(session).equals(childSessionId)) {
				childSession = session;
			}
		}

		if (parentSession == null || childSession == null) {
			return false;
		}

		long parentSessionStartTime = 0;
		long parentSessionDuration = 0;
		long parentSessionEndTime = 0;

		try {
			parentSessionStartTime = parentSession.getLong("sessionStartDate");
			parentSessionDuration = parentSession.getLong("sessionDuration");

		} catch (Exception e) {
			if (!parentSession.getString("sessionState").equals("CLOSED_ERROR")
					&& !parentSession.getString("sessionState")
							.equals("ACTIVE")) {
				System.out
						.println("Error reading session times. This is normal for sessions that end in error, but not in this one.");
				System.out.println(parentSession);
				e.printStackTrace();
			}
			// System.out.println(parentSession.getString("sessionStartDate"));
			// System.out.println(parentSession.getString("sessionDuration"));
			// This may fail if the session ended with status error.
		}
		parentSessionEndTime = parentSessionStartTime + parentSessionDuration;
		long childSessionStartTime = childSession.getLong("sessionStartDate");
		// long childSessionDuration = childSession.getLong("sessionDuration");
		// long childSessionEndTime = childSessionStartTime +
		// childSessionDuration;

		if ((parentSessionStartTime <= childSessionStartTime)
				&& (parentSessionEndTime >= childSessionStartTime)
				&& (doSessionShareParticipants(parentSession, childSession))) {
			// if innerSessionEndsBefore parent session ends.
			return true;
		}

		return false;

	}

	/**
	 * Tests to see if two sessions share at least one participant in common.
	 * 
	 * @param parentSession
	 * @param childSession
	 * @return
	 */
	public boolean doSessionShareParticipants(JSONObject parentSession,
			JSONObject childSession) {
		// TODO Auto-generated method stub

		ArrayList<String> parentParts = getParticipantList(parentSession);
		ArrayList<String> childParts = getParticipantList(childSession);

		for (String pp : parentParts) {
			if (childParts.contains(pp))
				return true;
		}

		return false;
	}

	/**
	 * Is this session the parent of any of the sessions in the list?
	 * 
	 * @param session
	 *            json object of one session.
	 * @param list
	 *            the list of known call session IDs.
	 * @param sessions
	 *            the json array of all the sessions.
	 * @return is this session a parent?
	 */
	/*
	 * public boolean isSessionParent(JSONObject session,
	 * ArrayList<CallSessionIdList> list, JSONArray sessions) {
	 * 
	 * ArrayList<String> participants = getParticipantList(session); long
	 * sessionStartTime = getSessionStartTime(session); long sessionDuration =
	 * getSessionDuration(session);
	 * 
	 * 
	 * boolean result = areAnyMembersEncapsulatedByTimeFrame(list, sessions,
	 * sessionStartTime, sessionDuration);
	 * 
	 * 
	 * return result;
	 * 
	 * }
	 */

	// /**
	// * Are any of the members of list fully encapsulated by the time frame
	// described in starttime and duration?
	// * @param list the list of session ides to be analyzed
	// * @param sessions the entire json array of sessions.
	// * @param sessionStartTime the start time of the session in question
	// * @param sessionDuration the duration of the session in question
	// * @return
	// */
	// public boolean areAnyMembersEncapsulatedByTimeFrame(
	// ArrayList<CallSessionIdList> list, JSONArray sessions,
	// long sessionStartTime, long sessionDuration) {
	// // TODO Auto-generated method stub
	// // return false;
	//
	// for (CallSessionIdList call : list) {
	// for (String sessionid : call.sessionIds) {
	// for (Object sessionObj : sessions) {
	// JSONObject session = (JSONObject) sessionObj;
	//
	// if (session.getString("sessionId").equals(sessionid)) {
	// long innerSessionStartTime = session.getLong("sessionStartDate");
	// long innerSessionDuration = session.getLong("sessionDuration");
	//
	// if (sessionStartTime <= innerSessionStartTime) {
	// // if inner session starts later.
	// long sessionEndTime = sessionStartTime + sessionDuration;
	// long innerSessionEndTime = innerSessionStartTime + innerSessionDuration;
	// if (sessionEndTime >= innerSessionEndTime) {
	// // if innerSessionEndsBefore parent session ends.
	// return true;
	// }
	// }
	//
	// }
	//
	// }
	// }
	// }
	// return false;
	//
	// }

	/**
	 * Find all the session ids that are not children
	 * 
	 * @param sessions
	 * @return
	 */
	public ArrayList<String> findNonChildSessionIds(JSONArray sessions) {
		ArrayList<String> results = new ArrayList<String>();
		try {

			for (int x = 0; x < sessions.size(); x++) {
				String sessX = sessions.getJSONObject(x).getString("sessionId");
				boolean child = false;
				if (sessX.startsWith("411316c")) {
					System.out.println("debug line");

				}

				for (int y = 0; y < sessions.size(); y++) {
					if (y != x) {
						String sessY = sessions.getJSONObject(y).getString(
								"sessionId");

						// if y is ever x's parent, then x cannot join results.
						if (isSessionParent(sessions, sessY, sessX)) {
							child = true;

							if (sessX.startsWith("411316c")) {
								System.out.println("debug line");

							}
							System.out.println(sessX + " is a child of "
									+ sessY);

							// no need to keep looking, break out.

						}
					}
				}
				if (!child)
					results.add(sessX);
			}
		} catch (IndexOutOfBoundsException e) {
			// sessions is empty or has only one element.
			e.printStackTrace();
			if (sessions.size() > 0) {
				JSONObject session = (JSONObject) sessions.get(0);

				results.add(getSessionId(session));
			} else {
				// don't do anthing, just return empty results.
			}
		}

		return results;
	}

	public ArrayList<String> getAllSessionIds(JSONArray sessions) {

		ArrayList<String> results = new ArrayList<String>();

		for (Object sessionObj : sessions) {
			JSONObject session = (JSONObject) sessionObj;

			results.add(getSessionId(session));

		}

		return results;

	}

	public String getSessionId(JSONObject session) {
		return session.getString("sessionId");
	}

	/**
	 * 
	 * @param data
	 *            the data from mediasense
	 */
	public ArrayList<CallSessionIdList> groupCallLegs(JSONObject data)
			throws GroupingException {

		ArrayList<CallSessionIdList> results = new ArrayList<CallSessionIdList>();

		JSONObject responseBody = (JSONObject) data.get("responseBody");

		JSONArray sessions = (JSONArray) responseBody.get("sessions");

		// Get potential parents
		ArrayList<String> potentialParents = findNonChildSessionIds(sessions);

		for (String parent : potentialParents) {
			CallSessionIdList callSessionIdList = new CallSessionIdList();
			callSessionIdList.addSessionId(parent);

			JSONArray tags = getTags(sessions, parent);

			if (tags != null) {
				for (Object tagObj : tags) {
					String tag = ((JSONObject) tagObj).getString("tagName");
					if (tag.startsWith("testGroup")) {
						callSessionIdList.setTag(tag);
					}
				}
			}
			results.add(callSessionIdList);
		}

		for (Object sessionObj : sessions) {
			JSONObject session = (JSONObject) sessionObj;

			String sessionId = getSessionId(session);

			// If not parent, find parent id
			if (!potentialParents.contains(sessionId)) {

				String parentId = findParent(session, sessions);

				for (CallSessionIdList parent : results) {
					if (parent.getSessionIds().contains(parentId)) {
						parent.addSessionId(sessionId);
					}
				}

			}

		}
		// }

		return results;

		// throw new NotImplementedException();
	}

	/**
	 * get all the session level tags for the given session id.
	 * 
	 * @param sessions
	 * @param parent
	 * @return
	 */
	public JSONArray getTags(JSONArray sessions, String sessionId) {
		// TODO Auto-generated method stub

		JSONArray tags = new JSONArray();

		try {
			JSONObject session = getSession(sessions, sessionId);
			tags = (JSONArray) session.get("tags");
		} catch (GroupingException e) {
			// Don't care, will just return empty tag set.
		}

		return tags;

	}

	/**
	 * Return a session using a given session id
	 * 
	 * @param sessions
	 * @param sessionId
	 * @throws GroupingException
	 *             if session is not found in session list.
	 * @return
	 */
	public JSONObject getSession(JSONArray sessions, String sessionId)
			throws GroupingException {
		// TODO Auto-generated method stub

		for (Object sessionObj : sessions) {
			JSONObject session = (JSONObject) sessionObj;

			if (getSessionId(session).equals(sessionId))
				return session;

		}

		throw new GroupingException();
	}

	public ArrayList<String> getParticipantList(JSONArray sessions,
			String sessionId) {
		// TODO Auto-generated method stub

		JSONObject session = (JSONObject) sessions.get(0);

		ArrayList<String> result = getParticipantList(session);

		return result;

	}

	public long getSessionDuration(JSONArray sessions, String sessionId) {
		// TODO Auto-generated method stub
		JSONObject session = (JSONObject) sessions.get(0);

		long result = getSessionDuration(session);

		return result;
	}

	/**
	 * Get the groupid (parent sesion id) for this session)
	 * 
	 * @param clgToolResults
	 * @param sessionId
	 * @return
	 */
	public String getGroupId(ArrayList<CallSessionIdList> clgToolResults,
			String sessionId) throws GroupingException {
		// TODO Auto-generated method stub

		for (CallSessionIdList idList : clgToolResults) {
			if (idList.hasSessionId(sessionId)) {
				return idList.getParentSessionId();
			}

		}
		throw new GroupingException();
	}

}
