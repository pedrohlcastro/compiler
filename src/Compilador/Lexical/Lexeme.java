package Compilador.Lexical;

/**
 *
 * @author pedro
 */
public class Lexeme {
    public String token;
    public TokenType type;

    public Lexeme(String token, TokenType type) {
        this.token = token;
        this.type = type;
    }

    @Override
    public String toString() {
        return "Lexeme{" + "token=" + token + ", type=" + type + '}';
    }
    
}
