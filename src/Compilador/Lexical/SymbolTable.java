package Compilador.Lexical;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pedro
 */
public class SymbolTable {
    private Map<String,SymbolTableInfo> st;

    public SymbolTable() {
        this.st = new HashMap<>();

        // symbols
        this.st.put(",", new SymbolTableInfo(TokenType.COMMA));  //
        this.st.put(";", new SymbolTableInfo(TokenType.DOT_COMMA));  // ;
        this.st.put("(", new SymbolTableInfo(TokenType.PAR_OPEN));  // (
        this.st.put(")", new SymbolTableInfo(TokenType.PAR_CLOSE));  // );
        this.st.put("{", new SymbolTableInfo(TokenType.CBRA_OPEN));  // {
        this.st.put("}", new SymbolTableInfo(TokenType.CBRA_CLOSE));  // }

        // keywords
        this.st.put("print", new SymbolTableInfo(TokenType.PRINT)); // print
        this.st.put("if", new SymbolTableInfo(TokenType.IF)); // if
        this.st.put("else", new SymbolTableInfo(TokenType.ELSE)); // else
        this.st.put("while", new SymbolTableInfo(TokenType.WHILE)); // while
        this.st.put("start", new SymbolTableInfo(TokenType.START)); // start
        this.st.put("exit", new SymbolTableInfo(TokenType.EXIT)); // exit
        this.st.put("int", new SymbolTableInfo(TokenType.INT)); // int
        this.st.put("float", new SymbolTableInfo(TokenType.FLOAT)); // float
        this.st.put("string", new SymbolTableInfo(TokenType.STRING)); // string
        this.st.put("do", new SymbolTableInfo(TokenType.DO)); // do
        this.st.put("end", new SymbolTableInfo(TokenType.END)); // end
        this.st.put("scan", new SymbolTableInfo(TokenType.SCAN)); // scan
        this.st.put("then", new SymbolTableInfo(TokenType.THEN)); // scan

        // operators
        this.st.put("and", new SymbolTableInfo(TokenType.AND)); // and
        this.st.put("or", new SymbolTableInfo(TokenType.OR)); // or
        this.st.put("==", new SymbolTableInfo(TokenType.EQUAL_COMP)); // ==
        this.st.put("=", new SymbolTableInfo(TokenType.EQUAL)); // =
        this.st.put("!=", new SymbolTableInfo(TokenType.DIFF)); // !=
        this.st.put("<", new SymbolTableInfo(TokenType.LOWER)); // <
        this.st.put(">", new SymbolTableInfo(TokenType.HIGHER)); // >
        this.st.put("<=", new SymbolTableInfo(TokenType.LOWER_EQ)); // <=
        this.st.put(">=", new SymbolTableInfo(TokenType.HIGHER_EQ)); // >=
        this.st.put("+", new SymbolTableInfo(TokenType.PLUS)); // +
        this.st.put("-", new SymbolTableInfo(TokenType.MINUS)); // -
        this.st.put("*", new SymbolTableInfo(TokenType.MUL)); // *
        this.st.put("/", new SymbolTableInfo(TokenType.DIV)); // /
    }

    public boolean contains(String token) {
        return this.st.containsKey(token);
    }

    public SymbolTableInfo find(String token) {
        return this.contains(token) ?
            this.st.get(token) : new SymbolTableInfo(TokenType.INVALID_TOKEN);
    }
}
