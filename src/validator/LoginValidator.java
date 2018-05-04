package validator;

import com.jfinal.core.Controller;
import com.jfinal.validate.Validator;

/**
 * Created by IntelliJ IDEA.
 * User: 涂耀辉  13-4-28 下午1:11
 */
public class LoginValidator extends Validator {

    protected void validate(Controller c){
        validateRequiredString("username","nameMsg","请输入用户名");
        validateRequiredString("password","passMsg","请输入密码");
    }
    
    protected void handleError(Controller c){
        c.forwardAction("/system/login_view");
    }
}
