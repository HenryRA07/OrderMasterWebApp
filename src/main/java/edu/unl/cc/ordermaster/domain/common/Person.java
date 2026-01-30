/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.domain.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class Person extends Organization{

    @NotNull @NotEmpty
    private String firstName;

    @NotNull @NotEmpty
    private String lastName;

    private GenderType gender;

    public Person() {
        gender = GenderType.FEMALE;
        setCreationDate(LocalDate.now());
    }

    public Person(Long id,
                  @NotNull @NotEmpty String firstName,
                  @NotNull @NotEmpty String lastName,
                  GenderType gender,
                  LocalDate creationDate,
                  IdentificationType identificationType,
                  String identificationNumber, String email
    ) {
        super(id, null, creationDate, identificationType, identificationNumber, email);
        //validateObligatoryField(firstName);
        //validateObligatoryField(lastName);
        setFirstName(firstName);
        setLastName(lastName);
        setName(getFullName());
        this.gender = gender;
    }

    @Deprecated
    private void validateObligatoryField(String text){
        if (text == null || text.isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    private String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(@NotNull @NotEmpty String firstName) {
        this.firstName = firstName.trim().toUpperCase();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(@NotNull @NotEmpty String lastName) {
        this.lastName = lastName.trim().toUpperCase();
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Person{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", gender=").append(gender);
        sb.append(", ").append(super.toString());
        sb.append('}');
        return sb.toString();
    }
}

