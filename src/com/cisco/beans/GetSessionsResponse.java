package com.cisco.beans;

public class GetSessionsResponse {
	private String responseCode;
	private String responseMessage;
	// sessions
	private String sessionId;
	private String sessionStartDate;
	private String sessionDuration;
	private String sessionState;
	// tags
	private String tagName;
	private String tagType;
	private String tagCreateDate;
	private String tagOffset;
	// tracks
	private String trackNumber;
	private String trackStartDate;
	private String trackDuration;
	private String trackMediaType;
	private String downloadUrl;
	private String deviceRef;
	// participants
	private String participantStartDate;
	private String participantDuration;
	private String isConference;
	private String xRefCi;
	private String deviceId;
	// url's
	private String httpUrl;
	private String rtspUrl;

	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponseMessage() {
		return responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionStartDate() {
		return sessionStartDate;
	}

	public void setSessionStartDate(String sessionStartDate) {
		this.sessionStartDate = sessionStartDate;
	}

	public String getSessionDuration() {
		return sessionDuration;
	}

	public void setSessionDuration(String sessionDuration) {
		this.sessionDuration = sessionDuration;
	}

	public String getSessionState() {
		return sessionState;
	}

	public void setSessionState(String sessionState) {
		this.sessionState = sessionState;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getTagCreateDate() {
		return tagCreateDate;
	}

	public void setTagCreateDate(String tagCreateDate) {
		this.tagCreateDate = tagCreateDate;
	}

	public String getTagOffset() {
		return tagOffset;
	}

	public void setTagOffset(String tagOffset) {
		this.tagOffset = tagOffset;
	}

	public String getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(String trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getTrackStartDate() {
		return trackStartDate;
	}

	public void setTrackStartDate(String trackStartDate) {
		this.trackStartDate = trackStartDate;
	}

	public String getTrackDuration() {
		return trackDuration;
	}

	public void setTrackDuration(String trackDuration) {
		this.trackDuration = trackDuration;
	}

	public String getTrackMediaType() {
		return trackMediaType;
	}

	public void setTrackMediaType(String trackMediaType) {
		this.trackMediaType = trackMediaType;
	}

	public String getDownloadUrl() {
		return downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
		this.downloadUrl = downloadUrl;
	}

	public String getDeviceRef() {
		return deviceRef;
	}

	public void setDeviceRef(String deviceRef) {
		this.deviceRef = deviceRef;
	}

	public String getParticipantStartDate() {
		return participantStartDate;
	}

	public void setParticipantStartDate(String participantStartDate) {
		this.participantStartDate = participantStartDate;
	}

	public String getParticipantDuration() {
		return participantDuration;
	}

	public void setParticipantDuration(String participantDuration) {
		this.participantDuration = participantDuration;
	}

	public String getIsConference() {
		return isConference;
	}

	public void setIsConference(String isConference) {
		this.isConference = isConference;
	}

	public String getxRefCi() {
		return xRefCi;
	}

	public void setxRefCi(String xRefCi) {
		this.xRefCi = xRefCi;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getHttpUrl() {
		return httpUrl;
	}

	public void setHttpUrl(String httpUrl) {
		this.httpUrl = httpUrl;
	}

	public String getRtspUrl() {
		return rtspUrl;
	}

	public void setRtspUrl(String rtspUrl) {
		this.rtspUrl = rtspUrl;
	}
}
