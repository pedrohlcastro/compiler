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

    public void setValue(Value<?> value){
        this.value = value;
    }
    
    public Value<?> value(){        
        return this.value;
    }
}
