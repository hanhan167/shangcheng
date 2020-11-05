package net.lab1024.smartadmin.util;



/**
 * @ClassName: BusinessException  
 * @Description: 应用服务异常
 */
public class BusinessException extends CommonRuntimeException {
	private static final long serialVersionUID = 3852715919578829470L;
	private Object data;
	public BusinessException(Integer code, String msg) {
		super(code, msg);
	}

	public BusinessException(Integer code, String msg, Object data) {
		super(code, msg);
		this.data =data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
