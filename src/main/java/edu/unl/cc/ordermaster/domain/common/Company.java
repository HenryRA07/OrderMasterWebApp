/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.domain.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class Company extends Organization{

    @NotNull
    private CompanyType type;

    public Company() {
        super();
        this.type = CompanyType.PRIVATE;
    }

    public Company(Long id,
                   @NotNull @NotEmpty String name,
                   @NotNull LocalDate creationDate,
                   @NotNull IdentificationType identificationType,
                   @NotNull @NotEmpty String identificationNumber,
                   @NotNull @NotEmpty String email,
                   @NotNull CompanyType type) {
        super(id, name, creationDate, identificationType, identificationNumber, email);
        this.type = type;
    }
}

