package Compilador.Semantical;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedro
 */
public class CommandBlock extends Command {
    private List<Command> commands;
    
    public CommandBlock (){
        super(-1);
        this.commands = new ArrayList<Command>();
    }
    
    public void addCommand (Command c){
        this.commands.add(c);
    }
    
    @Override
    public void execute() {
        for (Command c: commands){
//            System.out.println(c);
            c.execute();
        }
    }
}
