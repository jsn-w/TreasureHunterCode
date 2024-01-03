import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class OutputWindow {
    private StyledDocument doc;
    private Style style;
    private JTextPane textPane;

    public OutputWindow() {
        JFrame frame = new JFrame("Treasure Hunter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLocation(300, 300);
        textPane = new JTextPane();
        textPane.setEditable(false);
        doc = textPane.getStyledDocument();
        style = doc.addStyle("my style", null);
        StyleConstants.setFontSize(style, 16);
        frame.add(textPane);
        frame.getContentPane().setBackground(Color.black);
        frame.setVisible(true);
    }

    public void addTextToWindow(String text, Color color) {
        StyleConstants.setForeground(style, color);
        try {
            doc.insertString(doc.getLength(), text, style);
        } catch (Exception ignored) { }
    }

    public void clear() {
        textPane.setText("");
    }
}
