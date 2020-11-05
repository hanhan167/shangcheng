package net.lab1024.smartadmin.util;

/**  
 * @ClassName: CommonRuntimeException
 */  
public class CommonRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 154141025257886487L;
	private Integer code;	//错误码
	private String msg;	//错误描述
	
	public CommonRuntimeException(Integer code, String msg, Throwable e) {
		super(msg,e);
		this.code = code;
		this.msg = msg;
	}
	
	public CommonRuntimeException(Integer code, String msg){
		super(msg);		
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	@Override
	public String toString() {
		return "CommonRuntimeException{" +
				"code=" + code +
				", msg='" + msg + '\'' +
				'}';
	}
}
