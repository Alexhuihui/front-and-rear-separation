package top.alexmmd.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author 汪永晖
 */
@Data
@NoArgsConstructor
@ToString
public class RespEntity {

    private int code;
    private String msg;
    private Object data;
    private String token;
    private int sign;

    public RespEntity(RespCode respCode){
        this.code=respCode.getCode();
        this.msg=respCode.getMsg();
    }

    public RespEntity(RespCode respCode,String token){
        this(respCode);
        this.token=token;
    }

    public RespEntity(int sign,Object userData){
        this.sign=sign;
        this.data=userData;
    }
}
