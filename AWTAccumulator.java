import java.awt.*;
import java.awt.event.*;

public class AWTAccumulator extends Frame{
    private TextField tfInput;
    private TextField tfOutput;
    private int sum = 0;

    public AWTAccumulator(){
        setLayout(new GridLayout(2,2));
        add(new Label ("Enter an Integer: "));
        tfInput = new TextField(10);
        add(tfInput);
        tfInput.addActionListener(new TFInputListener());

        add(new Label("The accumulated sum is: "));
        tfOutput = new TextField(10);
        tfOutput.setEditable(false);
        add(tfOutput);

        setTitle("AWT Accumulator");
        setSize(350,120);
        setVisible(true);
        addWindowListener(new MyWindowListener());
    }

    public static void main(String[] args){
        new AWTAccumulator();
    }
    
    private class TFInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent evt){
            int numberIn = Integer.parseInt(tfInput.getText());
            sum += numberIn;
            tfInput.setText("");
            tfOutput.setText(sum+"");
        }
    }

   
    private class MyWindowListener implements WindowListener {
        @Override
        public void windowClosing(WindowEvent evt){
            System.exit(0);
        }
        @Override public void windowOpened(WindowEvent evt){}
        @Override public void windowClosed(WindowEvent evt){}
        @Override public void windowIconified(WindowEvent evt){}
        @Override public void windowDeiconified(WindowEvent evt){}
        @Override public void windowActivated(WindowEvent evt){}
        @Override public void windowDeactivated(WindowEvent evt){}
    }
}
