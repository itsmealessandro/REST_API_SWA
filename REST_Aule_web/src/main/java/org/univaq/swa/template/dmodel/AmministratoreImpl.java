package org.univaq.swa.template.dmodel;

import data.domain.Amministratore;
import framework.data.DataItemImpl;

public class AmministratoreImpl extends DataItemImpl<Integer> implements Amministratore{

    
    private String username;
    
    private String password;
    
    
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

}
