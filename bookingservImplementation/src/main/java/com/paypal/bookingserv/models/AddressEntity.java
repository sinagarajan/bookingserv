
package com.paypal.bookingserv.models;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * 
 * Entity object to represent Address object in DB
 *
 */

@Entity
@Table(name="address")
public class AddressEntity {

	@Id
    @GeneratedValue
    private Integer id;
	
	@OneToOne(mappedBy= "address" )
    private BookingEntity booking;
	
	@Column
    private String line1;


	@Column
    private String line2;


	@Column
    private String city;

	@Column
    private String state;


	@Column
    private String country;

	@Column
    private String zipCode;



	public String getLine1() {
		return line1;
	}

	public void setLine1(String line1) {
		this.line1 = line1;
	}

	public String getLine2() {
		return line2;
	}

	public void setLine2(String line2) {
		this.line2 = line2;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

}
