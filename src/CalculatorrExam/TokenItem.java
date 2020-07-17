package CalculatorrExam;

public class TokenItem {
    Calculater.Token token;
    String value;

    public TokenItem(Calculater.Token token, String value)
    {
        this.token = token;
        this.value = value;

    }

    public Double getValueInDouble()
    {
        return Double.parseDouble(value);
    }

    public boolean isOp()
    {
        if(token == Calculater.Token.TK_PLUS || token == Calculater.Token.TK_MINUS || token == Calculater.Token.TK_MUL || token == Calculater.Token.TK_DIV || token == Calculater.Token.TK_POW)
        {
            return true;
        }
        else
        {
            return false;
        }

    }

    public int getOpPriority() {
        if (token == Calculater.Token.TK_PLUS) {
            return 2;
        } else if (token == Calculater.Token.TK_MINUS) {
            return 2;
        } else if (token == Calculater.Token.TK_MUL) {
            return 4;
        } else if (token == Calculater.Token.TK_DIV) {
            return 4;
        }else if (token == Calculater.Token.TK_POW) {
            return 5;
        }
        return -1;
    }

    public double apply(double a,double b) {

        if (token == Calculater.Token.TK_PLUS) {
            return a + b;
        } else if (token == Calculater.Token.TK_MINUS) {
            return a - b;
        } else if (token == Calculater.Token.TK_MUL) {
            return a * b;
        } else if (token == Calculater.Token.TK_DIV) {
            return a / b;
        }else if (token == Calculater.Token.TK_POW) {
            return Math.pow(a,b);
        }
        return -1;
    }
}
