/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.domain.common;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Objects;

public abstract class Organization implements java.io.Serializable{

    private Long id;

    @NotNull @NotEmpty
    private String name;

    @NotNull
    private LocalDate creationDate;

    @NotNull
    private IdentificationType identificationType;

    @NotNull @NotEmpty
    private String identificationNumber;

    @NotNull @NotEmpty
    @Email(message = "Formato de email incorrecto")
    private String email;

    public Organization() {
        setIdentificationType(IdentificationType.DNI);
        setCreationDate(LocalDate.now());
    }

    public Organization(Long id,
                        String name,
                        @NotNull LocalDate creationDate,
                        @NotNull IdentificationType identificationType,
                        @NotNull @NotEmpty String identificationNumber,
                        @NotNull @NotEmpty String email) {
        this.id = id;
        this.setName(name);
        this.setCreationDate(creationDate);
        this.setIdentificationType(identificationType);
        this.setIdentificationNumber(identificationNumber);
        this.setEmail(email);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    protected void setName(@NotNull @NotEmpty String name){
        this.name = name.trim();
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public IdentificationType getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(@NotNull IdentificationType identificationType) {
        this.identificationType = identificationType;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(@NotNull @NotEmpty String identificationNumber) {
        this.identificationNumber = identificationNumber.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(@NotNull @NotEmpty String email) {
        this.email = email.trim();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Organization that = (Organization) o;
        return Objects.equals(getId(), that.getId()) && Objects.equals(getName(), that.getName()) && Objects.equals(getCreationDate(), that.getCreationDate()) && getIdentificationType() == that.getIdentificationType() && Objects.equals(getIdentificationNumber(), that.getIdentificationNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getCreationDate(), getIdentificationType(), getIdentificationNumber());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Organization{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", creationDate=").append(creationDate);
        sb.append(", identificationType=").append(identificationType);
        sb.append(", identificationNumber='").append(identificationNumber).append('\'');
        sb.append(", email='").append(email).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

