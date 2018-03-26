package example.model.http;

public class Response {

	public boolean success;
	public int errorCode;
	public String errMsg;
	public String result;

	public static class ErrCode {
		public static final int ONE = 1;
		public static final int TWO = 2;
	}

	public static class ErrMsg {
		public static final String ONE = "������ʽ����";
		public static final String TWO = "���ݿ����ʧ��";
	}
}
