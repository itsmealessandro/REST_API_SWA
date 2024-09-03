package org.univaq.swa.template.dmodel;


import java.sql.Time;
import java.sql.Date;

public class Evento  {

  private Integer IDMaster;
  private String nome;
  private Time oraInizio;
  private Time oraFine;
  private String descrizione;
  private Ricorrenza ricorrenza;
  private TipologiaEvento tipologiaEvento;
  private Date data;
  private Date dataFineRicorrenza;
  private Aula aula;
  private Responsabile responsabile;
  private Corso corso;
  private boolean isRicorrente; // Aggiunto campo isRicorrente

    public Integer getIDMaster() {
        return IDMaster;
    }

    public void setIDMaster(Integer IDMaster) {
        this.IDMaster = IDMaster;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Time getOraInizio() {
        return oraInizio;
    }

    public void setOraInizio(Time oraInizio) {
        this.oraInizio = oraInizio;
    }

    public Time getOraFine() {
        return oraFine;
    }

    public void setOraFine(Time oraFine) {
        this.oraFine = oraFine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Ricorrenza getRicorrenza() {
        return ricorrenza;
    }

    public void setRicorrenza(Ricorrenza ricorrenza) {
        this.ricorrenza = ricorrenza;
    }

    public TipologiaEvento getTipologiaEvento() {
        return tipologiaEvento;
    }

    public void setTipologiaEvento(TipologiaEvento tipologiaEvento) {
        this.tipologiaEvento = tipologiaEvento;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataFineRicorrenza() {
        return dataFineRicorrenza;
    }

    public void setDataFineRicorrenza(Date dataFineRicorrenza) {
        this.dataFineRicorrenza = dataFineRicorrenza;
    }

    public Aula getAula() {
        return aula;
    }

    public void setAula(Aula aula) {
        this.aula = aula;
    }

    public Responsabile getResponsabile() {
        return responsabile;
    }

    public void setResponsabile(Responsabile responsabile) {
        this.responsabile = responsabile;
    }

    public Corso getCorso() {
        return corso;
    }

    public void setCorso(Corso corso) {
        this.corso = corso;
    }

    public boolean isIsRicorrente() {
        return isRicorrente;
    }

    public void setIsRicorrente(boolean isRicorrente) {
        this.isRicorrente = isRicorrente;
    }
  
  

}
