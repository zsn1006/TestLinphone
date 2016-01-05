package org.linphone.newphone.bean;

public class CallRecords {

	private int id;
	private String account;
	private String startTime;
	private int timeLength; //秒
	private int callType; // 0 没有类型，1视频， 2 音频
	private int incoming; // 1 呼入，0 呼出
	private Contact contact;

	public CallRecords() {
	}

	public CallRecords(int id, String account, String startTime, int timeLength, int callType,
			int incoming) {
		this.id = id;
		this.account = account;
		this.startTime = startTime;
		this.timeLength = timeLength;
		this.callType = callType;
		this.incoming = incoming;
	}

	public Contact getContact() {
		return contact;
	}

	public void setContact(Contact contact) {
		this.contact = contact;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public int getTimeLength() {
		return timeLength;
	}

	public void setTimeLength(int timeLength) {
		this.timeLength = timeLength;
	}

	public int getCallType() {
		return callType;
	}

	public void setCallType(int callType) {
		this.callType = callType;
	}

	public int getIncoming() {
		return incoming;
	}

	public void setIncoming(int incoming) {
		this.incoming = incoming;
	}
}
