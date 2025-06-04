package util;

import javax.swing.text.*;

public class NumericDocumentFilter extends DocumentFilter {
	@Override
    public void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        replace(fb, offset, 0, text, attr); 
	}

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        String currentText = doc.getText(0, doc.getLength());
        String before = currentText.substring(0, offset);
        String after = currentText.substring(offset + length);
        String newText = before + text + after;

        // expressão regular para permitir apenas números com uma só vírgula ou nenhuma
        if(newText.matches("[0-9]*,?[0-9]*")) {
            
            // se o input começa com vírgula, adiciona um zero antes
            if (newText.startsWith(",")) {
                newText = "0" + newText;
            }

            // limpa e substitui o texto pelo corrigido
            fb.replace(0, doc.getLength(), newText, attrs);
        }
    }
}
