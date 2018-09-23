package Compilador;
import Compilador.Lexical.*;
/**
 *
 * @author pedro
 */
public class Compilador {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java main [File to Compile]");
            return;
        }

        try {
            LexicalAnalysis l = new LexicalAnalysis(args[0]);
            Lexeme lex;
            while (checkType((lex = l.nextToken()).type)) {
                System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
            }

            switch (lex.type) {
                case INVALID_TOKEN:
                    System.out.printf("%02d: Lexema inválido [%s]\n", l.line(), lex.token);
                    break;
                case UNEXPECTED_EOF:
                    System.out.printf("%02d: Fim de arquivo inesperado\n", l.line());
                    break;
                default:
                    System.out.printf("(\"%s\", %s)\n", lex.token, lex.type);
                    break;
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            //e.printStackTrace();
        }
    }

    private static boolean checkType(TokenType type) {
        return !(type == TokenType.END_OF_FILE ||
                 type == TokenType.INVALID_TOKEN ||
                 type == TokenType.UNEXPECTED_EOF);
    }
    
}
