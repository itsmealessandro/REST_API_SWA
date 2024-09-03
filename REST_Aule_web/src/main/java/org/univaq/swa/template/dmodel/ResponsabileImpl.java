package org.univaq.swa.template.dmodel;

import data.domain.Responsabile;
import framework.data.DataItemImpl;

public class ResponsabileImpl extends DataItemImpl<Integer> implements Responsabile {

    private String nome;
    private String cognome;
    private String email;

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
    
}
