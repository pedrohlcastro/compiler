package Compilador.Lexical;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author pedro
 */
public class SymbolTable {
    public static Map<String,SymbolTableInfo> st;

    public SymbolTable() {
        SymbolTable.st = new HashMap<>();

        // symbols
        SymbolTable.st.put(",", new SymbolTableInfo(TokenType.COMMA));  //
        SymbolTable.st.put(";", new SymbolTableInfo(TokenType.DOT_COMMA));  // ;
        SymbolTable.st.put("(", new SymbolTableInfo(TokenType.PAR_OPEN));  // (
        SymbolTable.st.put(")", new SymbolTableInfo(TokenType.PAR_CLOSE));  // );
        SymbolTable.st.put("{", new SymbolTableInfo(TokenType.CBRA_OPEN));  // {
        SymbolTable.st.put("}", new SymbolTableInfo(TokenType.CBRA_CLOSE));  // }

        // keywords
       SymbolTable.st.put("print", new SymbolTableInfo(TokenType.PRINT)); // print
       SymbolTable.st.put("if", new SymbolTableInfo(TokenType.IF)); // if
       SymbolTable.st.put("else", new SymbolTableInfo(TokenType.ELSE)); // else
       SymbolTable.st.put("while", new SymbolTableInfo(TokenType.WHILE)); // while
       SymbolTable.st.put("start", new SymbolTableInfo(TokenType.START)); // start
       SymbolTable.st.put("exit", new SymbolTableInfo(TokenType.EXIT)); // exit
       SymbolTable.st.put("int", new SymbolTableInfo(TokenType.INT)); // int
       SymbolTable.st.put("float", new SymbolTableInfo(TokenType.FLOAT)); // float
       SymbolTable.st.put("string", new SymbolTableInfo(TokenType.STRING)); // string
       SymbolTable.st.put("do", new SymbolTableInfo(TokenType.DO)); // do
       SymbolTable.st.put("end", new SymbolTableInfo(TokenType.END)); // end
       SymbolTable.st.put("scan", new SymbolTableInfo(TokenType.SCAN)); // scan
       SymbolTable.st.put("then", new SymbolTableInfo(TokenType.THEN)); // scan

        // operators
       SymbolTable.st.put("and", new SymbolTableInfo(TokenType.AND)); // and
       SymbolTable.st.put("or", new SymbolTableInfo(TokenType.OR)); // or
       SymbolTable.st.put("==", new SymbolTableInfo(TokenType.EQUAL_COMP)); // ==
       SymbolTable.st.put("=", new SymbolTableInfo(TokenType.EQUAL)); // =
       SymbolTable.st.put("!=", new SymbolTableInfo(TokenType.DIFF)); // !=
       SymbolTable.st.put("<", new SymbolTableInfo(TokenType.LOWER)); // <
       SymbolTable.st.put(">", new SymbolTableInfo(TokenType.HIGHER)); // >
       SymbolTable.st.put("<=", new SymbolTableInfo(TokenType.LOWER_EQ)); // <=
       SymbolTable.st.put(">=", new SymbolTableInfo(TokenType.HIGHER_EQ)); // >=
       SymbolTable.st.put("+", new SymbolTableInfo(TokenType.PLUS)); // +
       SymbolTable.st.put("-", new SymbolTableInfo(TokenType.MINUS)); // -
       SymbolTable.st.put("*", new SymbolTableInfo(TokenType.MUL)); // *
       SymbolTable.st.put("/", new SymbolTableInfo(TokenType.DIV)); // /
    }

    public boolean contains(String token) {
        return SymbolTable.st.containsKey(token);
    }

    public SymbolTableInfo find(String token) {
        return this.contains(token) ?
            SymbolTable.st.get(token) : new SymbolTableInfo(TokenType.INVALID_TOKEN);
    }
    
    public TokenType createVar(String token, int level, TokenType type){
        SymbolTable.st.put(token, new SymbolTableInfo(TokenType.VAR, level));
        return TokenType.VAR;
//        JEITO CERTO PARTE 2
//        if(null != type)
//            switch (type) {
//                case INT:
//                    SymbolTable.st.put(token, new SymbolTableInfo(TokenType.VAR_INT, level));
//                    return TokenType.VAR_INT;
//                case FLOAT:
//                    SymbolTable.st.put(token, new SymbolTableInfo(TokenType.VAR_FLT, level));
//                    return TokenType.VAR_FLT;
//                case STRING:
//                    SymbolTable.st.put(token, new SymbolTableInfo(TokenType.VAR_STR, level));
//                    return TokenType.VAR_STR;
//                default:
//                    return TokenType.INVALID_TOKEN;
//        }
//        return TokenType.INVALID_TOKEN;
    }
    
    public void printSymbolTable(){
        System.out.println("");
        System.out.println("");
        System.out.println("*********************TABELA DE SIMBOLOS**********************");
        System.out.println("");
        SymbolTable.st.forEach((key, value) -> {
            System.out.println('[' + key + ']' + " <--> " + value);
        });
    }
}
