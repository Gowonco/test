package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseUser<M extends BaseUser<M>> extends Model<M> implements IBean {

	public M setUCODE(java.lang.String UCODE) {
		set("UCODE", UCODE);
		return (M)this;
	}
	
	public java.lang.String getUCODE() {
		return getStr("UCODE");
	}

	public M setUNAME(java.lang.String UNAME) {
		set("UNAME", UNAME);
		return (M)this;
	}
	
	public java.lang.String getUNAME() {
		return getStr("UNAME");
	}

	public M setUPWD(java.lang.String UPWD) {
		set("UPWD", UPWD);
		return (M)this;
	}
	
	public java.lang.String getUPWD() {
		return getStr("UPWD");
	}

	public M setROLE(java.lang.Integer ROLE) {
		set("ROLE", ROLE);
		return (M)this;
	}
	
	public java.lang.Integer getROLE() {
		return getInt("ROLE");
	}

	public M setUNIT(java.lang.String UNIT) {
		set("UNIT", UNIT);
		return (M)this;
	}
	
	public java.lang.String getUNIT() {
		return getStr("UNIT");
	}

}
