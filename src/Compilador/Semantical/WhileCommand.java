package Compilador.Semantical;

/**
 *
 * @author pedro
 */
public class WhileCommand extends Command{
    private Value<?> expr;
    private Command cmd;
    
    public WhileCommand (Value<?> expr, Command cmd, int line){
        super(line);
        this.expr = expr;
        this.cmd = cmd;
    }

    @Override
    public void execute() {
        BoolValue expr = ((BoolValue) this.expr);
        while(expr.value())
            cmd.execute();
    }
}