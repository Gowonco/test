package model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseStCm<M extends BaseStCm<M>> extends Model<M> implements IBean {

	public M setID(String ID) {
		set("ID", ID);
		return (M)this;
	}

	public String getID() {
		return getStr("ID");
	}

	public M setSTCD(String STCD) {
		set("STCD", STCD);
		return (M)this;
	}

	public String getSTCD() {
		return getStr("STCD");
	}

	public M setBSNM(String BSNM) {
		set("BSNM", BSNM);
		return (M)this;
	}

	public String getBSNM() {
		return getStr("BSNM");
	}

}