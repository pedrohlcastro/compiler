package Compilador.Semantical;

/**
 *
 * @author pedro
 */
public class DualNumberExp extends Value{
    private MulOp mo = null;
    private AddOp ao = null;
    private Value<?> left;
    private Value<?> right;

    public DualNumberExp(MulOp op, Value<?> left, Value<?> right, int line) {
        super(line);
        this.mo = op;
        this.left = left;
        this.right = right;
    }
    
    public DualNumberExp(AddOp op, Value<?> left, Value<?> right, int line) {
        super(line);
        this.ao = op;
        this.left = left;
        this.right = right;
    }
    
    @Override
    public Number value() {
        Number value = 0;
        Number nLeft = null, nRigth = null;
//        System.out.println("VALUEE");
        
        Value<?> left = (this.left instanceof Variable) ? ((Variable) this.left).value() : this.left;
        Value<?> right = (this.right instanceof Variable) ? ((Variable) this.right).value() : this.right;
        
        if(left instanceof FloatValue){
            nLeft = ((Float)left.value());
        } else if(left instanceof IntValue){ 
            nLeft = ((Integer)left.value());
        } else {
            nLeft = (Number) left.value();
        }
        if(right instanceof FloatValue){
            nRigth = ((Float)right.value());
        } else if(right instanceof IntValue){ 
            nRigth = ((Integer)right.value());
        } else {
            nRigth = (Number) left.value();
//            System.out.println(right.value());
        }
//        System.out.println(nLeft + " . " + nRigth);
        if(this.ao != null) {
            if(ao == AddOp.Minus){
                if(nLeft instanceof Float || nRigth instanceof Float){
                     value = nLeft.floatValue() -nRigth.floatValue() ;
                }
                else{
                    value = nLeft.intValue() - nRigth.intValue();
                }
            }
            else if(ao == AddOp.Plus){
                if(nLeft instanceof Float || nRigth instanceof Float){
                     value = nLeft.floatValue() + nRigth.floatValue() ;
                }
                else{
                    value = nLeft.intValue() + nRigth.intValue();
                }
            }
        }
        else if(this.mo != null) {
            if(mo == MulOp.Div){
                if(nLeft instanceof Float || nRigth instanceof Float){
                     value = nLeft.floatValue() /nRigth.floatValue() ;
                }
                else{
                    value = nLeft.intValue() /nRigth.intValue();
                }
            }
            else if(mo == MulOp.Mul){
                if(nLeft instanceof Float || nRigth instanceof Float){
                     value = nLeft.floatValue() * nRigth.floatValue() ;
                }
                else{
                    value = nLeft.intValue() * nRigth.intValue();
                }
            }
        }
        return (Number) value;
    }
}