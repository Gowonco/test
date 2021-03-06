package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseCfBb<M extends BaseCfBb<M>> extends Model<M> implements IBean {

	public M setNO(java.lang.String NO) {
		set("NO", NO);
		return (M)this;
	}
	
	public java.lang.String getNO() {
		return getStr("NO");
	}

	public M setDBCD(java.lang.String DBCD) {
		set("DBCD", DBCD);
		return (M)this;
	}
	
	public java.lang.String getDBCD() {
		return getStr("DBCD");
	}

	public M setIL(java.lang.String IL) {
		set("IL", IL);
		return (M)this;
	}
	
	public java.lang.String getIL() {
		return getStr("IL");
	}

	public M setW(java.math.BigDecimal W) {
		set("W", W);
		return (M)this;
	}
	
	public java.math.BigDecimal getW() {
		return get("W");
	}

	public M setFL(java.lang.Integer FL) {
		set("FL", FL);
		return (M)this;
	}
	
	public java.lang.Integer getFL() {
		return getInt("FL");
	}

}
