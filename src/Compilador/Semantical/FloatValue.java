/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador.Semantical;

/**
 *
 * @author pedro
 */
public abstract class FloatValue extends Value<Float>{

    public FloatValue(int line) {
        super(line);
    }
    
    @Override
    public abstract Float value();
}