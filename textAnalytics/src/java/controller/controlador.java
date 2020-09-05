/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dao.DetectLanguage;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import model.idiomaModel;

/**
 *
 * @author DESKTOP-659TNOO
 */
@Named(value = "controlador")
@SessionScoped
public class controlador implements Serializable {

    idiomaModel selected = new idiomaModel();
    DetectLanguage lenguaje = new DetectLanguage();
    public void enviar(){
    lenguaje.consultar(selected);
    }

    public idiomaModel getSelected() {
        return selected;
    }

    public void setSelected(idiomaModel selected) {
        this.selected = selected;
    }

    public DetectLanguage getLenguaje() {
        return lenguaje;
    }

    public void setLenguaje(DetectLanguage lenguaje) {
        this.lenguaje = lenguaje;
    }

}
