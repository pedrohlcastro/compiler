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
public class ConstFloatValue extends FloatValue {
    private float value;

    public ConstFloatValue(float value, int line) {
        super(line);
        this.value = value;
    }

    @Override
    public Float value() {
        return this.value;
    } 
}
