package org.univaq.swa.template.dmodel;



public class Aula{
    
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getPiano() {
        return piano;
    }

    public void setPiano(String piano) {
        this.piano = piano;
    }

    public int getCapienza() {
        return capienza;
    }

    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    public int getPreseElettriche() {
        return preseElettriche;
    }

    public void setPreseElettriche(int preseElettriche) {
        this.preseElettriche = preseElettriche;
    }

    public int getPreseRete() {
        return preseRete;
    }

    public void setPreseRete(int preseRete) {
        this.preseRete = preseRete;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Attrezzatura getAttrezzatura() {
        return attrezzatura;
    }

    public void setAttrezzatura(Attrezzatura attrezzatura) {
        this.attrezzatura = attrezzatura;
    }

    public Dipartimento getDipartimento() {
        return dipartimento;
    }

    public void setDipartimento(Dipartimento dipartimento) {
        this.dipartimento = dipartimento;
    }

    public Responsabile getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Responsabile responsabile) {
        this.responsabile = responsabile;
    }

  }
