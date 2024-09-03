/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.template.dmodel;

import data.domain.Dipartimento;
import framework.data.DataItemImpl;

/**
 *
 * @author user
 */
public class DipartimentoImpl extends DataItemImpl<Integer> implements Dipartimento {
    private String nome;
    private String descrizione;

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
    
}
