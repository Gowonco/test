package model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseRpCr<M extends BaseRpCr<M>> extends Model<M> implements IBean {

	public M setID(java.lang.String ID) {
		set("ID", ID);
		return (M)this;
	}
	
	public java.lang.String getID() {
		return getStr("ID");
	}

	public M setNO(java.lang.String NO) {
		set("NO", NO);
		return (M)this;
	}
	
	public java.lang.String getNO() {
		return getStr("NO");
	}

	public M setW(java.math.BigDecimal W) {
		set("W", W);
		return (M)this;
	}
	
	public java.math.BigDecimal getW() {
		return get("W");
	}

	public M setCW(java.math.BigDecimal CW) {
		set("CW", CW);
		return (M)this;
	}
	
	public java.math.BigDecimal getCW() {
		return get("CW");
	}

}
