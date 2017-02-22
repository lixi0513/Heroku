package com.cisco.beans;

import java.util.ArrayList;

public class CallSessionIdList {

	public ArrayList<String> sessionIds = new ArrayList<String>();

	private String parentSessionId;

	private String tag;

	public ArrayList<String> getSessionIds() {
		return sessionIds;
	}

	public void setCallLegs(ArrayList<String> ids) {
		this.sessionIds = ids;
		try {
			this.parentSessionId = ids.get(0);
		} catch (Exception e) {
			// don't worry if this array is empty.
		}
	}

	public void addSessionId(String id) {
		this.sessionIds.add(id);
		if (this.sessionIds.size() == 1) {
			this.parentSessionId = id;
		}
	}

	public boolean listMatches(ArrayList<String> sessions) {
		return (sessionIds.containsAll(sessions) && sessionIds.size() == sessions
				.size());
	}

	public boolean isTag(String string) {
		// TODO Auto-generated method stub
		return string.equals(getTag());
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag;
	}

	public String getParentSessionId() {
		return parentSessionId;
	}

	public boolean hasSessionId(String sessionId) {
		// TODO Auto-generated method stub
		for (String id : sessionIds) {
			if (id.equals(sessionId))
				return true;
		}

		return false;
	}

}
