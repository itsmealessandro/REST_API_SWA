package org.univaq.swa.template.dmodel;

import data.domain.Attrezzatura;
import framework.data.DataItemImpl;

public class AttrezzaturaImpl extends DataItemImpl<Integer> implements Attrezzatura {
    
    private int id;
    private String nome;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    
}
