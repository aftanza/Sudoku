import java.awt.*;
import java.awt.event.*;

public class MouseEventDemo extends Frame {
    private TextField tfMouseX;
    private TextField tfMouseY;
    private TextField tfMousePositionX;
    private TextField tfMousePositionY;
    private TextField tfInput;
    private TextArea taDisplay;

    public MouseEventDemo(){
        setLayout(new FlowLayout());

        add(new Label ("X-Click: "));
        tfMouseX = new TextField(10);
        tfMouseX.setEditable(false);
        add(tfMouseX);

        add(new Label ("Y-Click: "));
        tfMouseY = new TextField(10);
        tfMouseY.setEditable(false);
        add(tfMouseY);

        add(new Label ("X-Position: "));
        tfMousePositionX = new TextField(10);
        tfMousePositionX.setEditable(false);
        add(tfMousePositionX);

        add(new Label ("Y-Position: "));
        tfMousePositionY = new TextField(10);
        tfMousePositionY.setEditable(false);
        add(tfMousePositionY);

        add(new Label ("Enter Text: "));
        tfInput = new TextField(10);
        add(tfInput);
        taDisplay = new TextArea(5,40);
        add(taDisplay);
        tfInput.addKeyListener(new MyKeyListener());

        setTitle("MouseEvent Demo");
        setSize(370,350);
        setVisible(true);

        addMouseListener(new MyMouseListener());
        addMouseMotionListener(new MyMouseListener());
        addWindowListener(new MyWindowListener());

    }

    public static void main(String[] args){
        new MouseEventDemo();
    }

    private class MyMouseListener implements MouseListener,MouseMotionListener{
        @Override
        public void mouseClicked(MouseEvent evt){
            tfMouseX.setText(evt.getX()+"");
            tfMouseY.setText(evt.getY()+"");
        }
        @Override public void mousePressed(MouseEvent evt){}
        @Override public void mouseReleased(MouseEvent evt){}
        @Override public void mouseEntered(MouseEvent evt){}
        @Override public void mouseExited(MouseEvent evt){}

        @Override
        public void mouseMoved(MouseEvent evt){
            tfMousePositionX.setText(evt.getX()+"");
            tfMousePositionY.setText(evt.getY()+"");
        }

        @Override public void mouseDragged(MouseEvent evt){}
    }

    private class MyKeyListener implements KeyListener{
        @Override
        public void keyTyped(KeyEvent evt){
            taDisplay.append("You have typed "+evt.getKeyChar()+"\n");
        }
        public void keyPressed(KeyEvent evt){}
        public void keyReleased(KeyEvent evt){}
    }

    private class MyWindowListener implements WindowListener {
        // Called back upon clicking close-window button
        @Override
        public void windowClosing(WindowEvent evt) {
           System.exit(0);  // Terminate the program
        }
  
        // Not Used, BUT need to provide an empty body to compile.
        @Override public void windowOpened(WindowEvent evt) { }
        @Override public void windowClosed(WindowEvent evt) { }
        // For Debugging
        @Override public void windowIconified(WindowEvent evt) { System.out.println("Window Iconified"); }
        @Override public void windowDeiconified(WindowEvent evt) { System.out.println("Window Deiconified"); }
        @Override public void windowActivated(WindowEvent evt) { System.out.println("Window Activated"); }
        @Override public void windowDeactivated(WindowEvent evt) { System.out.println("Window Deactivated"); }
     }
}