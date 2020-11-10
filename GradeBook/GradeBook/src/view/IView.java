/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

/**
 * IView provides a generic interface for the display of browsable records
 *
 * @author PC
 */
public interface IView<T> {

    void displayRecord(T r);

    void displayMessage(String m);

    void setBrowsing(boolean b);

    void displayError(String e);

    void displayGrade(String v);

    void displayTextArea(String t);

    void notifySize(T r);
}
