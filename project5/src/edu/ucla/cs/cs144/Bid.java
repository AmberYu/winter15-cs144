package edu.ucla.cs.cs144;

public class Bid {

	private String bidder;
	private String bidder_rating;
	private String location;
	private String country;
	private String time;
	private String amount;
    
	public String getBidder() {
		return bidder;
	}
	public void setBidder(String bidder) {
		this.bidder = bidder;
	}
	public String getBidder_rating() {
		return bidder_rating;
	}
	public void setBidder_rating(String bidder_rating) {
		this.bidder_rating = bidder_rating;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
}
