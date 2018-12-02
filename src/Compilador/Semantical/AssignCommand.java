package Compilador.Semantical;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedro
 */
public class AssignCommand extends Command {
 
  private List<Variable> vars;
  private Value<?> value;
 
  public AssignCommand(Value<?> value, int line) {
    super(line);
    this.vars = new ArrayList<Variable>();
    this.value = value;
  }
 
  public void addVariable(Variable v) {
    this.vars.add(v);
  }
 
  @Override
  public void execute() {
        Value<?> value = (this.value instanceof Variable) ? ((Variable) this.value).value() : this.value;
        //System.out.println(value.value());
        Value<?> newValue = null;
        if (value instanceof IntValue) {
            IntValue iv = (IntValue) value;
//            System.out.println(iv.value());
            newValue = new ConstIntValue((Integer)iv.value(), -1);
        } else if (value instanceof StringValue) {
            StringValue sv = (StringValue) value;
            //System.out.println(sv.value());
            newValue = new ConstStringValue(sv.value(), -1);
        } else if (value instanceof FloatValue) {
            FloatValue fv = (FloatValue) value;
            //System.out.println(fv.value());
            newValue = new ConstFloatValue((Float)fv.value(), -1);
        } else if (value instanceof DualNumberExp) {
            try{
                DualNumberExp dn = (DualNumberExp) value;
           
                if(dn.value() instanceof Integer){
//                    System.out.println("int");
                    newValue = new ConstIntValue((int)dn.value(), -1);
                } else {
//                    System.out.println("Float");
                    newValue = new ConstFloatValue((float)dn.value(), -1);
                } 
            } catch(Exception e) {
                System.out.println("Entrada Invalida em  " + super.getLine());
                System.out.println("Erro: " + e);
                System.exit(0);
            }            
        } else {
            System.err.println("[ASSIGN NOT SUPPORTED FOR THIS TYPE] LINE - " + super.getLine());
            System.exit(0);
        }

        for (Variable v : this.vars) {
            try{
                v.setValue(newValue);
            } catch (Exception e) {
                System.out.println("Entrada Invalida em  " + super.getLine());
                System.out.println("Erro: " + e);
                System.exit(0);
            }
        }
    }
}
