package io.investigation.response;

public class BaseResponse {
	private Integer code = 20000;
	private String msg;
    private Object data;
    
    public BaseResponse() {
		super();
	}

    public BaseResponse(String msg, Object data) {
		super();
		this.msg = msg;
		this.data = data;
	}

	public BaseResponse(Integer code, String msg, Object data) {
		super();
		this.code = code;
		this.msg = msg;
		this.data = data;
	}

	public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
