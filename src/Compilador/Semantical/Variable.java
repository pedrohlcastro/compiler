package Compilador.Semantical;

/**
 *
 * @author pedro
 */
public class Variable extends Value<Value<?>> {
    private String name;
    private Value<?> value;
    private String type;

    public Variable(String name, String type) {
        super(-1);
        this.name = name;
        this.value = null;
        this.type = type;
    }

    public String getName() {
        return name;
    }
    
    public String getType(){
        return this.type;
    }

    public void setValue(Value<?> value) throws Exception{
        if(this.type == "float"){
            if(value instanceof ConstFloatValue){
                this.value = value;
            } else if(value instanceof ConstIntValue){
                ConstIntValue cv = ((ConstIntValue) value);
                this.value = new ConstFloatValue(cv.value().floatValue(), value.getLine());
            } else {
                throw new Exception("Assign Value Must be a FLOAT");
            }
        } else if(this.type == "int"){
            if(value instanceof ConstIntValue){
                this.value = value;
            }  else if(value instanceof ConstFloatValue){
                ConstFloatValue cv = ((ConstFloatValue) value);
                this.value = new ConstIntValue(cv.value().intValue(), value.getLine());
            } else {
                throw new Exception("Assign Value Must be a INT");
            }
        } else if(this.type == "string"){
            if(value instanceof ConstStringValue){
                this.value = value;
            } else {
                throw new Exception("Assign Must be a STRING");
            }
        }
    }
    
    public Value<?> value(){        
        return this.value;
    }
}
