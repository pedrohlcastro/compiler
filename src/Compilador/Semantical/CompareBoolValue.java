package Compilador.Semantical;

/**
 *
 * @author pedro
 */
public class CompareBoolValue extends BoolValue{
    private RelOp op;
    private Value<?> left;
    private Value<?> right;

    public CompareBoolValue(RelOp op, Value<?> left, Value<?> right, int line) {
        super(line);
        this.op = op;
        this.left = left;
        this.right = right;
    }

    @Override
    public Boolean value() {
        try{
            Value<?> left = (this.left instanceof Variable) ? ((Variable) this.left).value() : this.left;
            Value<?> right = (this.right instanceof Variable) ? ((Variable) this.right).value() : this.right;
//            System.out.println(left);
//            System.out.println(right);
            
            if(left instanceof ConstStringValue && right instanceof ConstStringValue){
                switch(this.op){
                    case Equal:
                        return ((String)left.value()).equals((String)right.value());
                    case NotEqual:
                        return !((String)left.value()).equals((String)right.value());
                    case LowerThan:
                        return ((String)left.value()).compareTo((String)right.value()) == -1;
                    case LowerEqual:
                        return ((String)left.value()).compareTo((String)right.value()) <= 0;
                    case GreaterThan:
                        return ((String)left.value()).compareTo((String)right.value()) == 1;
                    case GreaterEqual:
                        return ((String)left.value()).compareTo((String)right.value()) >= 0;
                }
            } else if(left instanceof ConstIntValue && right instanceof ConstIntValue){
                switch(this.op){
                    case Equal:
                        return (Integer)left.value() == (Integer)right.value();
                    case NotEqual:
                        return (Integer)left.value() != (Integer)right.value();
                    case LowerThan:
                        return (Integer)left.value() < (Integer)right.value();
                    case LowerEqual:
                        return (Integer)left.value() <= (Integer)right.value();
                    case GreaterThan:
                        return (Integer)left.value() > (Integer)right.value();
                    case GreaterEqual:
                        return (Integer)left.value() >= (Integer)right.value();
                }
            } else if(left instanceof ConstFloatValue && right instanceof ConstFloatValue){
                switch(this.op){
                    case Equal:
                        return (Float)left.value() == (Float)right.value();
                    case NotEqual:
                        return (Float)left.value() != (Float)right.value();
                    case LowerThan:
                        return (Float)left.value() < (Float)right.value();
                    case LowerEqual:
                        return (Float)left.value() <= (Float)right.value();
                    case GreaterThan:
                        return (Float)left.value() > (Float)right.value();
                    case GreaterEqual:
                        return (Float)left.value() >= (Float)right.value();
                }
            } else {
                System.out.println("Erro na linha " + super.getLine());
                System.out.println("Comparação não suportada");
                System.exit(0);
            }
            
        } catch(Exception e){
            System.out.println("Erro na linha " + super.getLine());
            System.out.println("Comparação com valores nulos");
            System.exit(0);
        }
        return null;
    }
    
    
}