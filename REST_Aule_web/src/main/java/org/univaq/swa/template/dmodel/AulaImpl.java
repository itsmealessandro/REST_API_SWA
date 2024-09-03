package org.univaq.swa.template.dmodel;

import data.domain.Attrezzatura;
import data.domain.Aula;
import data.domain.Dipartimento;
import data.domain.Responsabile;
import framework.data.DataItemImpl;

public class AulaImpl extends DataItemImpl<Integer> implements Aula {

  private String nome;
  private String luogo;
  private String edificio;
  private String piano;
  private int capienza;
  private int preseElettriche;
  private int preseRete;
  private String note; // Text
  private Attrezzatura attrezzatura;
  private Dipartimento dipartimento;
  private Responsabile responsabile;

  @Override
  public String getNome() {
    return nome;
  }

  @Override
  public void setNome(String nome) {
    this.nome = nome;
  }

  @Override
  public String getLuogo() {
    return luogo;
  }

  @Override
  public void setLuogo(String luogo) {
    this.luogo = luogo;
  }

  @Override
  public String getEdificio() {
    return edificio;
  }

  @Override
  public void setEdificio(String edificio) {
    this.edificio = edificio;
  }

  @Override
  public String getPiano() {
    return piano;
  }

  @Override
  public void setPiano(String piano) {
    this.piano = piano;
  }

  @Override
  public int getCapienza() {
    return capienza;
  }

  @Override
  public void setCapienza(int capienza) {
    this.capienza = capienza;
  }

  @Override
  public int getPreseElettriche() {
    return preseElettriche;
  }

  @Override
  public void setPreseElettriche(int presaElettrica) {
    this.preseElettriche = presaElettrica;
  }

  @Override
  public int getPreseRete() {
    return preseRete;
  }

  @Override
  public void setPreseRete(int preseRete) {
    this.preseRete = preseRete;
  }

  @Override
  public String getNote() {
    return note;
  }

  @Override
  public void setNote(String note) {
    this.note = note;
  }

  @Override
  public Attrezzatura getAttrezzatura() {
    return attrezzatura;
  }

  @Override
  public void setAttrezzatura(Attrezzatura attrezzatura) {
    this.attrezzatura = attrezzatura;
  }

  @Override
  public Dipartimento getDipartimento() {
    return dipartimento;
  }

  @Override
  public void setDipartimento(Dipartimento dipartimento) {
    this.dipartimento = dipartimento;
  }

  @Override
  public Responsabile getResponsabile() {
    return responsabile;
  }

  @Override
  public void setResponsabile(Responsabile responsabile) {
    this.responsabile = responsabile;
  }
}
