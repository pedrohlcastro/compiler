package Compilador.Semantical;

import java.util.Scanner;

/**
 *
 * @author pedro
 */
public class LoadCommand extends Command {
    private Variable v;
    public LoadCommand(int line, Variable v) {
        super(line);
        this.v = v;
    }



    @Override
    public void execute() {
        Scanner sc = new Scanner(System.in);
        
        String type = v.getType();
        if (type.equals("int")){
            int input;
            try {
                input = sc.nextInt();
                sc.nextLine();
                v.setValue(new ConstIntValue(input, this.getLine()));
            }
            catch (Exception e) {
                System.out.println("Entrada Invalida!");
                System.out.println("Erro: " + e);
                System.exit(0);
            }
        } else if(type.equals("float") ){
            Float input;
            try {
                input = sc.nextFloat();
                sc.nextLine();
                v.setValue(new ConstFloatValue(input, this.getLine()));
            }
            catch (Exception e) {
                System.out.println("Entrada Invalida!");
                System.out.println("Erro: " + e);
                System.exit(0);
            }
        } else {
            String input = "";
            try {
                input = sc.nextLine();
                v.setValue(new ConstStringValue(input, this.getLine()));
            }
            catch (Exception e) {
                System.out.println("Entrada Invalida em  " + super.getLine());
                System.out.println("Erro: " + e);
                System.exit(0);
            }
            
        }
        
    }
    
    
}