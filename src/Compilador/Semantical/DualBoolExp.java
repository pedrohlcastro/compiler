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
public class DualBoolExp extends BoolValue{
    private MulOp mo = null;
    private AddOp ao = null;
    private Value<?> left;
    private Value<?>  right;

    public DualBoolExp(MulOp op, Value<?>  left, Value<?>  right, int line) {
        super(line);
        this.mo = op;
        this.left = left;
        this.right = right;
    }
    public DualBoolExp(AddOp op, Value<?>  left, Value<?>  right, int line) {
        super(line);
        this.ao = op;
        this.left = left;
        this.right = right;
    }


    @Override
    public Boolean value() {
        Value<?> left = (this.left instanceof Variable) ? ((Variable) this.left).value() : this.left;
        Value<?> right = (this.right instanceof Variable) ? ((Variable) this.right).value() : this.right;
        
        if(mo != null){
            if(mo == MulOp.And){
                if(left instanceof BoolValue && right instanceof BoolValue){
                    return ((Boolean)left.value()) && ((Boolean)right.value());
                }
                
            }
        }
        else if(ao != null){
            if(ao == AddOp.Or){
                if(left instanceof BoolValue && right instanceof BoolValue){
                    return ((Boolean)left.value()) || ((Boolean)right.value());
                }
            }
        }
        return null;
    }
    
    
}
