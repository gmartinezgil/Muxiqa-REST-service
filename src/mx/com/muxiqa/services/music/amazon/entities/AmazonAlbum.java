/**
 * 
 */
package mx.com.muxiqa.services.music.amazon.entities;

import java.io.Serializable;

/**
 * @author gerardomartinezgil
 * 
 */
public final class AmazonAlbum implements Serializable {
	private static final long serialVersionUID = 1L;
	private String detailPage;
	private String smallImage;
	private String artist;
	private String label;
	private String price;
	private String releaseDate;
	private String upc;
	private String lowestPrice;
	private String averageRating;
	private String totalReviews;

	public String getDetailPage() {
		return detailPage;
	}

	public void setDetailPage(String detailPage) {
		this.detailPage = detailPage;
	}

	public String getSmallImage() {
		return smallImage;
	}

	public void setSmallImage(String smallImage) {
		this.smallImage = smallImage;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public String getLowestPrice() {
		return lowestPrice;
	}

	public void setLowestPrice(String lowestPrice) {
		this.lowestPrice = lowestPrice;
	}

	public String getAverageRating() {
		return averageRating;
	}

	public void setAverageRating(String averageRating) {
		this.averageRating = averageRating;
	}

	public String getTotalReviews() {
		return totalReviews;
	}

	public void setTotalReviews(String totalReviews) {
		this.totalReviews = totalReviews;
	}

}