package br.com.helpdev.sample.adapters.output.db.jpa.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity(name = "user")
public class UserEntity {

   @Id
   private Long id;

   private String uuid;

   private String name;

   private String email;

   @Column(name = "birth_date")
   private LocalDate birthDate;

   @OneToOne
   @JoinColumn(name = "id", referencedColumnName = "user_id", insertable = false, updatable = false)
   private AddressEntity address;

   public UserEntity(Long id, String uuid, String name, String email, LocalDate birthDate, AddressEntity address) {
      this.id = id;
      this.uuid = uuid;
      this.name = name;
      this.email = email;
      this.birthDate = birthDate;
      this.address = address;
   }

   public UserEntity() {
      this(null, null, null, null, null, null);
   }

   public void setBirthDate(LocalDate birthDate) {
      this.birthDate = birthDate;
   }

   public AddressEntity getAddress() {
      return address;
   }

   public void setAddress(AddressEntity address) {
      this.address = address;
   }

   public Long getId() {
      return id;
   }

   public String getUuid() {
      return uuid;
   }

   public String getName() {
      return name;
   }

   public String getEmail() {
      return email;
   }

   public LocalDate getBirthDate() {
      return birthDate;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public void setUuid(String uuid) {
      this.uuid = uuid;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   @Override
   public String toString() {
      return "UserEntity{" + "id=" + id + ", uuid='" + uuid + '\'' + ", name='" + name + '\'' + ", email='" + email + '\'' + ", birthDate="
            + birthDate + ", address=" + address + '}';
   }
}