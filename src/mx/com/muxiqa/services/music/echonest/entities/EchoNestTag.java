/**
 * 
 */
package mx.com.muxiqa.services.music.echonest.entities;

import java.io.Serializable;

/**
 * @author gerardomartinezgil
 *
 */
public final class EchoNestTag implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String tag;
	private String frecuency;
	private String weigth;
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getFrecuency() {
		return frecuency;
	}
	public void setFrecuency(String frecuency) {
		this.frecuency = frecuency;
	}
	public String getWeigth() {
		return weigth;
	}
	public void setWeigth(String weigth) {
		this.weigth = weigth;
	}
	
}