/**
 * @author FrancisEngine(Francisco Chamba)
 */
package edu.unl.cc.ordermaster.business;

import edu.unl.cc.ordermaster.domain.Product;
import jakarta.ejb.Stateless;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class JBrewFacade implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Product> getAllProducts(){
        return new ArrayList<Product>();
    }

}
