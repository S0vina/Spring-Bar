package com.spring.bar.springBar.dto;

public class AberturaContaDTO {

    private int numeroMesa;
    private int numPessoas;
    private boolean habilitarCouvert;

    public AberturaContaDTO() {}

    public AberturaContaDTO(int numeroMesa, int numPessoas, boolean habilitarCouvert) {
        this.numeroMesa = numeroMesa;
        this.numPessoas = numPessoas;
        this.habilitarCouvert = habilitarCouvert;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public int getNumPessoas() {
        return numPessoas;
    }

    public void setNumPessoas(int numPessoas) {
        this.numPessoas = numPessoas;
    }

    public boolean isHabilitarCouvert() {
        return habilitarCouvert;
    }

    public void setHabilitarCouvert(boolean habilitarCouvert) {
        this.habilitarCouvert = habilitarCouvert;
    }
}