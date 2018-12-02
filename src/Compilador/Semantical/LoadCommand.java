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
            }
            catch (Exception e) {
                System.out.println("Entrada Invalida!");
                System.out.println("Erro: " + e);
            }
        } else if(type.equals("float") ){
            Float input;
            try {
                input = sc.nextFloat();
            }
            catch (Exception e) {
                System.out.println("Entrada Invalida!");
                System.out.println("Erro: " + e);
            }
        } else {
            String input = "";
            try {
                input = sc.nextLine();
            }
            catch (Exception e) {
                System.out.println("Entrada Invalida!");
                System.out.println("Erro: " + e);
            }
        }
    }
    
    
}