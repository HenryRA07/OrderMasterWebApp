package edu.unl.cc.ordermaster.domain;

import java.math.BigDecimal;

public class Transferencia extends MetodoPago{
    private String banco;
    private String numeroComprobante;
    private Pedido pedido;

    public Transferencia(BigDecimal cantidad, String banco, String numeroComprobante,Pedido pedido){
        super(cantidad, pedido);
        setBanco(banco);
        setNumeroComprobante(numeroComprobante);
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        if(banco == null || banco.trim().isEmpty()){
            throw new IllegalArgumentException("El banco no puede estar vacio");
        }
        this.banco = banco;
    }

    public String getNumeroComprobante() {
        return numeroComprobante;
    }

    public void setNumeroComprobante(String numeroComprobante) {
        if(numeroComprobante == null || numeroComprobante.trim().isEmpty()){
            throw new IllegalArgumentException("El número de comprobante no puede estar vacio");
        }
        this.numeroComprobante = numeroComprobante;
    }

    @Override
    public String toString() {
        return "Transferencia [" + super.toString() +
                "banco='" + banco + '\'' +
                ", numeroComprobante='" + numeroComprobante + '\'' +
                ']';
    }

}
