package Compilador.Lexical;

/**
 *
 * @author pedro
 */
public class SymbolTableInfo {
    private TokenType t;
    private int level;
    
    public SymbolTableInfo(TokenType t, int level) {
        this.level = level;
        this.t = t;
    }
    
    public SymbolTableInfo(TokenType t) {
        this.level = 0;
        this.t = t;
    }
    
    public TokenType getTokenType(){
        return this.t;
    }
    
}
