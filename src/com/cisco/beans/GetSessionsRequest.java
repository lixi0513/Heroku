package com.cisco.beans;

public class GetSessionsRequest {
	private String fieldName;
	private String fieldOperator;
	private String fieldValues;
	private String fieldConnector;
	private String paramConnector;
	private String offset;
	private String limit;
	private String byFieldName;
	private String order;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldOperator() {
		return fieldOperator;
	}

	public void setFieldOperator(String fieldOperator) {
		this.fieldOperator = fieldOperator;
	}

	public String getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(String fieldValues) {
		this.fieldValues = fieldValues;
	}

	public String getFieldConnector() {
		return fieldConnector;
	}

	public void setFieldConnector(String fieldConnector) {
		this.fieldConnector = fieldConnector;
	}

	public String getParamConnector() {
		return paramConnector;
	}

	public void setParamConnector(String paramConnector) {
		this.paramConnector = paramConnector;
	}

	public String getOffset() {
		return offset;
	}

	public void setOffset(String offset) {
		this.offset = offset;
	}

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public String getByFieldName() {
		return byFieldName;
	}

	public void setByFieldName(String byFieldName) {
		this.byFieldName = byFieldName;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String generateUrl() {
		String string = "";
		if (getFieldName() != null && getFieldName().length() > 0) {
			string = "{\"requestParameters\":[";
			String fieldvalue[] = getFieldValues().toString().replace("\"", "")
					.split(",");
			String values = "";
			if (getFieldName().equals("sessionStartDate")) {
				values = "{\"fieldName\":\"" + getFieldName()
						+ "\",\"fieldConditions\":[{\"fieldOperator\":\""
						+ getFieldOperator() + "\",\"fieldValues\":["
						+ getFieldValues() + "]}]}";
			} else {
				for (int i = 0; i < fieldvalue.length; i++) {
					values += "{\"fieldName\":\"" + getFieldName()
							+ "\",\"fieldConditions\":[{\"fieldOperator\":\""
							+ getFieldOperator() + "\",\"fieldValues\":["
							+ fieldvalue[i] + "]}]";
					if (i + 1 < fieldvalue.length) {
						values += ",\"paramConnector\" : \"AND\"},";
					} else {
						// values+="}";

						// values+="],";
						values += ",\"paramConnector\": \"AND\"";
						values += "},";
						values += "{";
						values += "\"fieldName\" : \"sessionStartDate\",";
						values += "\"fieldConditions\": [";
						values += "{";
						values += "\"fieldOperator\" : \"between\",";
						values += "\"fieldValues\" : [";
						values += "0,";
						values += "" + System.currentTimeMillis() + "";

						values += "]}";

					}
				}
			}
			string += values + "],";
		} else {
			string = "{\"requestParameters\":[{\"fieldName\":\"mediaType\",\"fieldConditions\":[{\"fieldOperator\":\"equals\",\"fieldValues\":[\"audio\"]}]}],";
		}
		// These are not actually used. We need the full result set in order to
		// tell client how many pages there are.
		// string += "\"pageParameters\": { \"offset\": \"" +getOffset()+
		// "\", \"limit\": \"" +getLimit()+ "\"},";
		if (this.getByFieldName() != null && this.getOrder() != null
				&& this.getByFieldName().length() > 0
				&& this.getOrder().length() > 0) {
			string += "\"sortParameters\": [{ \"byFieldName\": \""
					+ this.getByFieldName() + "\", \"order\": \"" + getOrder()
					+ "\"}]";
		}
		string += "}]}";
		return string;
	}

}
