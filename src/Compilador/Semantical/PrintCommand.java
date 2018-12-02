/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Compilador.Semantical;

import javax.swing.text.StyledEditorKit;

/**
 *
 * @author pedro
 */
public class PrintCommand extends Command{
    private Value<?> value = null;

    public PrintCommand(Value <?> value, int line) {
        super(line);
        this.value = value;
    }


    @Override
    public void execute() {
        //System.out.printf("Executando: %s\n", this.value);
        String text = "";
        Value<?> value = this.value;
        if (value instanceof Variable){
           value = ((Variable) value).value();
        }
        
        if (value instanceof StringValue){
            StringValue sv = (StringValue) value;
            text = sv.value() + "\n";
        }
        else if (value instanceof IntValue){
            IntValue iv = (IntValue) value;
            int n = iv.value();
            text = "" + n + "\n";
        }
        else if (value instanceof FloatValue){
            FloatValue fv = (FloatValue) value;
            float n = fv.value();
            text = "" + n + "\n";
            
        }
        else{
            System.err.println("[WRONG TYPE OR VALUE FOR PRINT] LINE - " + super.getLine());
            System.exit(0);
        }
        System.out.print(text);
    }
}
