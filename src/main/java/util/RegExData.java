package util;

import java.awt.Toolkit;
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class RegExData extends DocumentFilter {
	private static final String DATE_REGEX = "^(\\d{0,2})(/)?(\\d{0,2})(/)?(\\d{0,4})?$";
    private final Pattern pattern = Pattern.compile(DATE_REGEX);

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.insert(offset, string);
        if(pattern.matcher(sb.toString()).matches())
            super.insertString(fb, offset, string, attr);
        else
            Toolkit.getDefaultToolkit().beep();
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        StringBuilder sb = new StringBuilder(fb.getDocument().getText(0, fb.getDocument().getLength()));
        sb.replace(offset, offset + length, text);
        if(pattern.matcher(sb.toString()).matches())
            super.replace(fb, offset, length, text, attrs);
        else
            Toolkit.getDefaultToolkit().beep();
    }
}
