package Compilador.Semantical;

/**
 *
 * @author pedro
 */
public class IfCommand extends Command{
    private Value<?> expr;
    private Command then;
    private Command elses;
    
    public IfCommand(Value<?> expr, Command then, int line) {
        super(line);
        this.expr = expr;
        this.then = then;
    }
    
    public IfCommand(Value<?> expr, Command then, Command elses, int line) {
        super(line);
        this.expr = expr;
        this.then = then;
        this.elses = elses;
    }
    
    @Override
    public void execute(){
        BoolValue expr = ((BoolValue) this.expr);
        if(expr.value())
            this.then.execute();
        else if(this.elses != null)
            this.elses.execute();
    }
}
