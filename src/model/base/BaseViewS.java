package model.base;

import com.jfinal.plugin.activerecord.IBean;
import com.jfinal.plugin.activerecord.Model;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseViewS<M extends BaseViewS<M>> extends Model<M> implements IBean {

	public M setCODE(Integer CODE) {
		set("CODE", CODE);
		return (M)this;
	}

	public Integer getCODE() {
		return getInt("CODE");
	}

	public M setCODEN(String CODEN) {
		set("CODEN", CODEN);
		return (M)this;
	}

	public String getCODEN() {
		return getStr("CODEN");
	}

	public M setVALUE(String VALUE) {
		set("VALUE", VALUE);
		return (M)this;
	}

	public String getVALUE() {
		return getStr("VALUE");
	}

	public M setFRONTCOLORCODE(String FRONTCOLORCODE) {
		set("FRONTCOLORCODE", FRONTCOLORCODE);
		return (M)this;
	}

	public String getFRONTCOLORCODE() {
		return getStr("FRONTCOLORCODE");
	}

	public M setBACKCOLORCODE(String BACKCOLORCODE) {
		set("BACKCOLORCODE", BACKCOLORCODE);
		return (M)this;
	}

	public String getBACKCOLORCODE() {
		return getStr("BACKCOLORCODE");
	}

	public M setFONT(String FONT) {
		set("FONT", FONT);
		return (M)this;
	}

	public String getFONT() {
		return getStr("FONT");
	}

	public M setDEFAULT(Integer DEFAULT) {
		set("DEFAULT", DEFAULT);
		return (M)this;
	}

	public Integer getDEFAULT() {
		return getInt("DEFAULT");
	}

}
