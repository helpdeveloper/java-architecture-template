package br.com.helpdev.sample.adapters.output.db.jpa.entities;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "address")
public class AddressEntity {

   @Id
   private Long id;

   @Column(name = "user_id")
   private Long userId;

   private String country;

   private String state;

   private String city;

   @Column(name = "zip_code")
   private String zipCode;

   @Column(name = "street_address")
   private String streetAddress;

   public AddressEntity(Long id, Long userId, String country, String state, String city, String zipCode, String streetAddress) {
      this.id = id;
      this.userId = userId;
      this.country = country;
      this.state = state;
      this.city = city;
      this.zipCode = zipCode;
      this.streetAddress = streetAddress;
   }

   public AddressEntity() {
      this(null, null, null, null, null, null, null);
   }

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public String getCountry() {
      return country;
   }

   public void setCountry(String country) {
      this.country = country;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getCity() {
      return city;
   }

   public void setCity(String city) {
      this.city = city;
   }

   public String getZipCode() {
      return zipCode;
   }

   public void setZipCode(String zipCode) {
      this.zipCode = zipCode;
   }

   public String getStreetAddress() {
      return streetAddress;
   }

   public void setStreetAddress(String streetAddress) {
      this.streetAddress = streetAddress;
   }

   @Override
   public String toString() {
      return "AddressEntity{" + "id=" + id + ", userId=" + userId + ", country='" + country + '\'' + ", state='" + state + '\'' + ", city='" + city
            + '\'' + ", zipCode='" + zipCode + '\'' + ", streetAddress='" + streetAddress + '\'' + '}';
   }
}
