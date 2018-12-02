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
        
        Value<?> left = (this.left instanceof Variable) ? ((Variable) this.left).value() : this.left;
        Value<?> right = (this.right instanceof Variable) ? ((Variable) this.right).value() : this.right;
        
        if(this.ao != null) {
            if(ao == AddOp.Minus){
                if(left instanceof FloatValue || right instanceof FloatValue){
                    value = ((Float)left.value()) - ((Float)right.value());
                }
                else{
                    value = ((Integer)left.value()) - ((Integer)right.value());
                }
            }
            else if(ao == AddOp.Plus){
                if(left instanceof FloatValue || right instanceof FloatValue){
                    value = ((Float)left.value()) + ((Float)right.value());
                }
                else{
                    value = ((Integer)left.value()) + ((Integer)right.value());
                }
            }
        }
        else if(this.mo != null) {
            if(mo == MulOp.Div){
                if(left instanceof FloatValue || right instanceof FloatValue){
                    value = ((Float)left.value()) /- ((Float)right.value());
                }
                else{
                    value = ((Integer)left.value()) / ((Integer)right.value());
                }
            }
            else if(mo == MulOp.Mul){
                if(left instanceof FloatValue || right instanceof FloatValue){
                    value = ((Float)left.value()) * ((Float)right.value());
                }
                else{
                    value = ((Integer)left.value()) * ((Integer)right.value());
                }
            }
        }
        return value;
    }
}