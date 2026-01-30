/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.view;

import edu.unl.cc.ordermaster.domain.common.GenderType;
import edu.unl.cc.ordermaster.domain.common.IdentificationType;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;


import java.io.Serial;
import java.util.Arrays;
import java.util.List;

@Named("factoryListApp")
@ApplicationScoped
public class FactoryListApplication implements java.io.Serializable{

    @Serial
    private static final long serialVersionUID = 1L;

    private List<GenderType> genderOptions;
    private List<IdentificationType> identificationTypeOptions;

    @PostConstruct
    public void init() {
        genderOptions = Arrays.asList(GenderType.values());
        identificationTypeOptions = Arrays.asList(IdentificationType.values());
    }

    public List<GenderType> getGenderOptions() {
        return genderOptions;
    }

    public List<IdentificationType> getIdentificationTypeOptions() {
        return identificationTypeOptions;
    }
}

