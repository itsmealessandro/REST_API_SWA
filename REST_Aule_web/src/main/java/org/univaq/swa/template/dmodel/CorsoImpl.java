/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.univaq.swa.template.dmodel;

import data.domain.Corso;
import framework.data.DataItemImpl;

/**
 *
 * @author user
 */
public class CorsoImpl extends DataItemImpl<Integer> implements Corso {
    
    private String nome;
    private Responsabile responsabile;

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public Responsabile getResponsabile() {
        return responsabile;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setResponsabile(Responsabile responsabile) {
        this.responsabile = responsabile;
    }
    
}
