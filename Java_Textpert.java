import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.*;

public class Java_Textpert {

    static int sentences = 0;
 
    static int words = 0;

    static int frame = 0;

    // Funcion de guardado de historial
   

    // Función para contar oraciones
    public static int snts(int sentences, int words, int subjects, JTextField textInput) {
        String text = textInput.getText();
        String[] oraciones = text.split("[.!?]+");
        int count = 0;
        for (String oracion : oraciones) {
            if (!oracion.trim().isEmpty() && words != 0 && subjects != 0) {
                count++;
            }
        }
        return count;
    }


    // Función para contar palabras
    public static int wrds(int words, JTextField textInput) {
        String text = textInput.getText();
        String[] oraciones = text.split(" ");
        int count = 0;
        for (String oracion : oraciones) {
            if (!oracion.trim().isEmpty()) {
                count++;
            }
        }
        return count;
    }

    // Función para contar sujetos
    public static int subjects(JTextField textInput) {
        String text = textInput.getText();
        if (text == null || text.trim().isEmpty()) return 0;

        String lower = text;

        

        Pattern pronPattern = Pattern.compile("\\b(?:I|you|he|she|we|they|it|I'm|I’m|you're|you’re|he's|he’s|she's|she’s|it's|it’s|we're|we’re|they're|they’re|I've|I’ve|you've|you’ve|we've|we’ve|they've|they’ve|I'd|I’d|you'd|you’d|he'd|he’d|she'd|she’d|we'd|we’d|they'd|they’d)\\b",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

            
    
        Pattern detNounPattern = Pattern.compile("\\b(?:the|a|an|some|any|my|your|his|her|its|our|their|this|that|these|those)\\s+([A-Za-zÁÉÍÓÚáéíóúÑñ]+)",
            Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);

        List<int[]> spans = new ArrayList<>();

        Matcher mPron = pronPattern.matcher(lower);
        while (mPron.find()) {
            spans.add(new int[]{mPron.start(), mPron.end()});
        }

        Matcher mDet = detNounPattern.matcher(text);
        while (mDet.find()) {
            spans.add(new int[]{mDet.start(), mDet.end()});
        }

        if (spans.isEmpty()) return 0;

  
        spans.sort(Comparator.comparingInt(a -> a[0]));
        int mergedCount = 0;
        int curEnd = spans.get(0)[1];
        for (int i = 1; i < spans.size(); i++) {
            int s = spans.get(i)[0];
            int e = spans.get(i)[1];
            if (s <= curEnd) {
                curEnd = Math.max(curEnd, e);
            } else {
                mergedCount++;
                curEnd = e;
            }
        }
        mergedCount++; 

        return mergedCount;
    }

    // Main
    public static void main(String[] args) {


        // Configuración de la ventana

        JFrame ventana = new JFrame("Textpert Assistant");
        ventana.setSize(600, 600);
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.getContentPane().setBackground(new Color(41, 41, 41));
        Image icon = Toolkit.getDefaultToolkit().getImage("icons.png");
        ventana.setIconImage(icon);
        ventana.setLayout(null);

        // GUI

        JLabel titulo = new JLabel("Textpert Assistant");
        titulo.setFont(new Font("Eras Demi ITC", Font.BOLD, 30));
        titulo.setForeground(Color.WHITE);
        titulo.setBounds(150, 20, 400, 50);

        JLabel instrucciones = new JLabel("Introduce your text below");
        instrucciones.setFont(new Font("Eras Demi ITC", Font.BOLD, 20));
        instrucciones.setForeground(Color.WHITE);
        instrucciones.setBounds(65, 135, 400, 50);

        JLabel historiales = new JLabel("Hola platanos");
        historiales.setFont(new Font("Eras Demi ITC", Font.BOLD, 20));
        historiales.setForeground(Color.WHITE);
        historiales.setBounds(50, 100, 475, 50);

        JLabel resultad1 = new JLabel(" ");
        resultad1.setFont(new Font("Eras Demi ITC", Font.BOLD, 20));
        resultad1.setForeground(Color.WHITE);
        resultad1.setBounds(50, 450, 475, 50);
        resultad1.setVisible(false);

        JLabel resultad2 = new JLabel(" ");
        resultad2.setFont(new Font("Eras Demi ITC", Font.BOLD, 20));
        resultad2.setForeground(Color.WHITE);
        resultad2.setBounds(50, 500, 475, 50);
        resultad2.setVisible(false);

        JLabel resultad3 = new JLabel(" ");
        resultad3.setFont(new Font("Eras Demi ITC", Font.BOLD, 20));
        resultad3.setForeground(Color.WHITE);
        resultad3.setBounds(325, 450, 475, 50);
        resultad3.setVisible(false);


        JTextField textInput = new JTextField();
        textInput.setFont(new Font("Eras Demi ITC", Font.BOLD, 17));
        textInput.setForeground(Color.BLACK);
        textInput.setBounds(65, 180, 450, 50);
        textInput.setDocument(new LimiteDeCaracteres(10000));

        ImageIcon gifIcon = new ImageIcon("Loadings.gif");
        JLabel gifLabel = new JLabel(gifIcon);
        gifLabel.setBounds(250, 320, 100, 100);
        gifLabel.setVisible(false);

        JButton checksInput = new JButton("Check Text") {
            @Override
            protected void paintComponent(Graphics g) {
                if (!isOpaque() && getBackground().getAlpha() < 255) {
                    super.paintComponent(g);
                    return;
                }
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
                g2.dispose();
            }
        };

        checksInput.setFont(new Font("Eras Demi ITC", Font.BOLD, 25));
        checksInput.setForeground(Color.BLACK);
        checksInput.setBackground(Color.WHITE);
        checksInput.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        checksInput.setBounds(60, 260, 170, 40);
        checksInput.setContentAreaFilled(false);
        checksInput.setOpaque(false);
        checksInput.setFocusPainted(false);

       
        JPanel rectangulo_1 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setStroke(new BasicStroke(3));
                g2d.setColor(Color.WHITE);
                g2d.drawRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
            }
        };

        JPanel rectangulo_2 = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(Color.darkGray);
                g2d.fillRoundRect(5, 5, getWidth() - 10, getHeight() - 10, 15, 15);
            }
        };

        rectangulo_1.setBounds(55, 170, 470, 70);
        rectangulo_1.setOpaque(false);

        rectangulo_2.setBounds(25, -10, 530, 610);
        rectangulo_2.setOpaque(false);

        // Añadiendo Todo
       
        ventana.add(resultad1);
        ventana.add(resultad2);
        ventana.add(resultad3);
        ventana.add(gifLabel);
        ventana.add(rectangulo_1);
        ventana.add(titulo);
        ventana.add(instrucciones);
        ventana.add(textInput);
        ventana.add(checksInput);

        ventana.add(rectangulo_2);

        // Logica

        textInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                 resultad1.setVisible(false);
                 resultad2.setVisible(false);
                 resultad3.setVisible(false);
            }
        });

   
        checksInput.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                checksInput.setBackground(Color.LIGHT_GRAY);
            }
            public void mouseExited(MouseEvent e) {
                checksInput.setBackground(Color.WHITE);
            }
            public void mousePressed(MouseEvent e) {
                if (!textInput.getText().trim().isEmpty()) {
                    gifLabel.setVisible(true);
                   
                    Timer timer = new Timer(2000, evt -> {
                        gifLabel.setVisible(false);

                        int w = wrds(words, textInput);
                        int subj = subjects(textInput);
                        int s = snts(sentences,w,subj, textInput);
                        
                        
                        resultad1.setText("Your text has " + s + " sentences");
                        resultad1.setVisible(true);

                        resultad2.setText("And a total of " + w + " words.");
                        resultad2.setVisible(true);

                        resultad3.setText("Estimated subjects: " + subj);
                        resultad3.setVisible(true);
                    });



                    timer.setRepeats(false); 
                    timer.start();
                }
            }
        });

       
                        
        ventana.setVisible(true);
    }


    static class LimiteDeCaracteres extends PlainDocument {
        private final int limite;

        public LimiteDeCaracteres(int limite) {
            this.limite = limite;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
            if (str == null) return;
            if ((getLength() + str.length()) <= limite) {
                super.insertString(offset, str, attr);
            }
        }
    }
}
